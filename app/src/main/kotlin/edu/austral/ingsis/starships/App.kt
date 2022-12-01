package edu.austral.ingsis.starships

import edu.austral.ingsis.starships.ui.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableMap
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import starships.GameState
import starships.entities.ship.ShipController
import starships.entities.BaseEntity
import starships.movement.Mover
import persistence.Constants
import persistence.gamestate.GameStateSaver
import persistence.WindowConfigurator
import java.awt.Button
import java.awt.Label
import java.io.FileReader
import java.util.ListResourceBundle

fun main() {
    launch(Starships::class.java)
}
private var gameState = GameState()
class Starships() : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    private val facade = ElementsViewFacade(imageResolver)
    private val keyTracker = KeyTracker()
    private val gameSaver = GameStateSaver()
    var gameScene = Scene(StackPane())
    var startScene = Scene(StackPane())
    companion object Paused {
        var paused = false
    }

    override fun start(primaryStage: Stage) {
        cleanFacade()
        val gameInitializer = GameInitializer(gameSaver)
        startScene = generateStartScene(primaryStage, gameInitializer)
        primaryStage.scene = startScene
        val entityInSceneManager = EntityInSceneManager(facade)
        addEventListeners(entityInSceneManager, primaryStage, gameInitializer)
        gameScene = Scene(facade.view)
        keyTracker.scene = gameScene
        setUpPrimaryStage(primaryStage, startScene)
        startApplicationComponents(primaryStage)
    }

    private fun cleanFacade() {
        facade.showCollider.value = true
        facade.showGrid.value = false
    }

    fun generateStartScene(primaryStage: Stage, gameInitializer: GameInitializer): Scene {
        val gameTitle = javafx.scene.control.Label("Starships")
        val newGameButton = createNewGameButton(gameInitializer, primaryStage)
        val loadGameButton = createLoadGameButton(gameInitializer, primaryStage)
        val layout = VBox()
        layout.children.addAll(gameTitle, newGameButton, loadGameButton)
        return Scene(layout)
    }

    private fun createNewGameButton(gameInitializer: GameInitializer, primaryStage: Stage): javafx.scene.control.Button {
        val newGameButton = javafx.scene.control.Button("New Game")
        newGameButton.onAction = EventHandler {
            startGameScene(gameInitializer, primaryStage, GameInitializer.GameStart.NEW)
        }
        return newGameButton
    }

    private fun createLoadGameButton(gameInitializer: GameInitializer, primaryStage: Stage): javafx.scene.control.Button {
        val loadGameButton = javafx.scene.control.Button("Load Game")
        loadGameButton.onAction = EventHandler {
            startGameScene(gameInitializer, primaryStage, GameInitializer.GameStart.LOAD)
        }
        return loadGameButton
    }

    private fun startGameScene(gameInitializer: GameInitializer, primaryStage: Stage, gameStart: GameInitializer.GameStart) {
        gameState = gameInitializer.selectGameStart(gameStart)
        insertCoreEntitiesIntoUI()
        primaryStage.scene = gameScene
    }

    private fun startApplicationComponents(primaryStage: Stage) {
        facade.start()
        keyTracker.start()
        primaryStage.show()
    }

    private fun setUpPrimaryStage(primaryStage: Stage, scene: Scene) {
        val windowConfigurator = WindowConfigurator.getInstance()
        primaryStage.scene = scene
        primaryStage.height = (windowConfigurator.getProperty("height").get() as Long).toDouble()
        primaryStage.width = (windowConfigurator.getProperty("width").get() as Long).toDouble()
    }

    private fun addEventListeners(entityInSceneManager: EntityInSceneManager, primaryStage: Stage, gameInitializer: GameInitializer) {
        val gameFinishedListener = GameFinishedListener(primaryStage, startScene, gameInitializer)
        facade.timeListenable.addEventListener(TimeListener(facade.elements, entityInSceneManager, Paused, gameFinishedListener))
        facade.collisionsListenable.addEventListener(CollisionListener())
        facade.outOfBoundsListenable.addEventListener(OutOfBoundsListener())
        facade.reachBoundsListenable.addEventListener(ReachBoundsListener())
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(gameSaver))
    }

    fun insertCoreEntitiesIntoUI() {
        gameState.ships.forEach{
            facade.elements[it.id] = it.adapt()
        }
        gameState.movingEntities.forEach{
            facade.elements[it.id] = it.adapt()
        }
    }

    override fun stop() {
        facade.stop()
        keyTracker.stop()
    }
}

class EntityInSceneManager(private val facade: ElementsViewFacade){

    fun insert(entity: ElementModel){
        facade.elements[entity.id] = entity
    }

    fun removeById(entityId: String){
        facade.elements.remove(entityId)
    }

}

class TimeListener(private val elements: ObservableMap<String, ElementModel>,
                   private val inserter: EntityInSceneManager,
                   private var paused: Starships.Paused,
                   private val gameFinishedListener: GameFinishedListener

                   ) : EventListener<TimePassed> {
    private val startingShips = (WindowConfigurator.getInstance().getProperty("players").get() as Long).toInt()
    private val gameFinishedEmitter = createGameFinishedEmitter(gameFinishedListener)

    private fun createGameFinishedEmitter(gameFinishedListener: GameFinishedListener): ListenableEmitter<GameEnding> {
        val gameEndingEmitter = ListenableEmitter<GameEnding>()
        gameEndingEmitter.addEventListener(gameFinishedListener)
        return gameEndingEmitter
    }




    override fun handle(event: TimePassed) {
        if (Starships.paused) return
        val newShipList = ArrayList<ShipController>()
        val newMoverList = ArrayList<Mover<BaseEntity>>()
        if(startingShips > 1) checkMultiplayerVictory()
        else checkGameOver()
        updateShips(newShipList)
        removeIdsInScene()
        spawnAsteroid(newMoverList)
        updateMovingEntities(newMoverList)

        gameState = GameState(newMoverList, newShipList, gameState.removedIds, gameState.scores)
    }

    private fun checkGameOver() {
        if(gameState.ships.isEmpty() && areDestroyedShips() && !gameFinishedListener.called)
            gameFinishedEmitter.emit(GameEnding("DESTRUCTION", "0"))
    }

    private fun areDestroyedShips(): Boolean {
        gameState.removedIds.forEach {
            if (it.startsWith("Ship")) return true
        }
        return false

    }

    private fun checkMultiplayerVictory() {
        if(gameState.ships.size == 1 && !gameFinishedListener.called){
            //println(gameState.ships[0].id + " won!")
            gameFinishedEmitter.emit(GameEnding("WIN", gameState.ships[0].id.drop(5)))
        }
    }

    private fun updateMovingEntities(newMoverList: ArrayList<Mover<BaseEntity>>) {
        gameState.movingEntities.forEach {
            val updatedMover = it.updatePosition()
            val updatedMoverElementModel = updatedMover.adapt()

            if (elements.containsKey(it.id)) {
                elements[it.id]?.x?.set(updatedMoverElementModel.x.value)
                elements[it.id]?.y?.set(updatedMoverElementModel.y.value)
                if(it.id.startsWith("Asteroid"))
                    elements[it.id]?.rotationInDegrees?.set(elements[it.id]?.rotationInDegrees!!.value + 1)
                    elements[it.id]?.height?.set(elements[it.id]?.height!!.value)
                    elements[it.id]?.width?.set(elements[it.id]?.width!!.value)
            } else {
                inserter.insert(updatedMoverElementModel)
                //elements[updatedMoverElementModel.id] = updatedMoverElementModel
            }
            newMoverList.add(updatedMover)
        }
    }

    private fun spawnAsteroid(newMoverList: ArrayList<Mover<BaseEntity>>) {
        if (Math.random() <= Constants.ASTEROID_SPAWN_RATE) {
            val asteroidMover = gameState.spawnAsteroid()
            newMoverList.add(asteroidMover as Mover<BaseEntity>)
        }
    }

    private fun removeIdsInScene() {
        gameState.removedIds.forEach {
            if (elements.containsKey(it)) {
                inserter.removeById(it)
            }
        }
    }

    private fun updateShips(newShipList: ArrayList<ShipController>) {
        gameState.ships.forEach {
            val updatedShip = it.update()
            val updatedShipElementModel = updatedShip.adapt()
            elements[it.id]?.x?.set(updatedShipElementModel.x.value)
            elements[it.id]?.y?.set(updatedShipElementModel.y.value)
            elements[it.id]?.rotationInDegrees?.set(updatedShipElementModel.rotationInDegrees.value)
            newShipList.add(updatedShip)
        }
    }
}

class CollisionListener() : EventListener<Collision> {
    override fun handle(event: Collision) {
        gameState = gameState.handleCollision(event.element1Id, event.element2Id)
    }

}

class KeyPressedListener(val gameSaver: GameStateSaver): EventListener<KeyPressed> {

    var keyBindingMap = insertBindings()

    fun insertBindings(): Map<String, Map<String, KeyCode>>{
        val mapToReturn = HashMap<String, Map<String, KeyCode>>()
        val it: Iterator<*> = getKeybindingsMapIterator()
        var id = 1
        while (it.hasNext()) {
            val binding = it.next() as JSONObject
            val bindingMap = loadMapWithBindings(binding)
            mapToReturn["Ship-"+id] = bindingMap
            id++

        }
        return mapToReturn
    }

    private fun loadMapWithBindings(binding: JSONObject) = mapOf(
            "accelerate" to KeyCode.valueOf(binding.get("accelerate") as String),
            "brake" to KeyCode.valueOf(binding.get("brake") as String),
            "rotate_clockwise" to KeyCode.valueOf(binding.get("rotate_clockwise") as String),
            "rotate_counterclockwise" to KeyCode.valueOf(binding.get("rotate_counterclockwise") as String),
            "shoot" to KeyCode.valueOf(binding.get("shoot") as String),
            "change_weapon" to KeyCode.valueOf(binding.get("change_weapon") as String),
            "pause" to KeyCode.valueOf(binding.get("pause") as String),
            "save" to KeyCode.valueOf(binding.get("save") as String)
    )

    private fun getKeybindingsMapIterator(): Iterator<*> {
        val obj = JSONParser().parse(FileReader(Constants.KEYBINDINGS_FILE_PATH))
        val keyBindings = obj as JSONArray
        val it: Iterator<*> = keyBindings.iterator()
        return it
    }

    override fun handle(event: KeyPressed) {
        val pressedKey = event.key
        for ((shipId, keyCodeMap) in keyBindingMap.entries.iterator()) {
            handlePressedKeyAction(pressedKey, keyCodeMap, shipId)
        }
    }

    private fun handlePressedKeyAction(pressedKey: KeyCode, keyCodeMap: Map<String, KeyCode>, shipId: String) {
        when (pressedKey) {
            keyCodeMap["accelerate"] -> gameState = gameState.accelerateShip(shipId, Constants.SHIP_ACCELERATION_COEFFICIENT)
            keyCodeMap["brake"] -> gameState = gameState.stopShip(shipId)
            keyCodeMap["rotate_clockwise"] -> gameState = gameState.rotateShip(shipId, Constants.SHIP_ROTATION_DEGREES)
            keyCodeMap["rotate_counterclockwise"] -> gameState = gameState.rotateShip(shipId, -Constants.SHIP_ROTATION_DEGREES)
            keyCodeMap["shoot"] -> gameState = gameState.shoot(shipId)
            keyCodeMap["change_weapon"] -> gameState = gameState.changeWeapon(shipId)
            keyCodeMap["pause"] -> Starships.paused = !Starships.paused
            keyCodeMap["save"] -> gameSaver.saveGameState(gameState)
            else -> {}
        }
    }

}

data class GameEnding(val endingType: String, val winnerId: String)



class GameFinishedListener(val primaryStage: Stage, val startScene: Scene, val gameInitializer: GameInitializer): EventListener<GameEnding> {
    var called = false
    private fun createGameOverScene(): Scene {
        val gameOverLabel = javafx.scene.control.Label("Game Over")
//        val playAgainButton = javafx.scene.control.Button("Play again")
//        playAgainButton.onAction = EventHandler {
//            gameState = gameInitializer.selectGameStart(GameInitializer.GameStart.NEW)
//            primaryStage.scene = startScene
//        }

        val scoresLabel = javafx.scene.control.Label(generateScoresString())
        val layout = VBox()
        layout.children.addAll(gameOverLabel, scoresLabel)
        return Scene(layout)
    }

    private fun generateScoresString(): String {
        var scoresString = ""
        gameState.scores.forEach {
            val (shipId, score) = it
            val playerNumber = shipId.drop(5)
            scoresString = "$scoresString\nPlayer $playerNumber - $score points"
        }
        return scoresString
    }

    override fun handle(event: GameEnding) {
        called = true
        if(event.endingType.equals("DESTRUCTION")){
            val gameOverScene = createGameOverScene()
            primaryStage.scene = gameOverScene
        } else if(event.endingType.equals("WIN")){
            val winningScene = createWinningScene(event.winnerId)
            primaryStage.scene = winningScene
        }
    }

    private fun createWinningScene(winnerId: String): Scene {
        val gameOverLabel = javafx.scene.control.Label("Player $winnerId wins!")
//        val playAgainButton = javafx.scene.control.Button("Play again")
//        playAgainButton.onAction = EventHandler {
//            primaryStage.scene = startScene
//        }
        val scoresLabel = javafx.scene.control.Label(generateScoresString())
        val layout = VBox()
        layout.children.addAll(gameOverLabel, scoresLabel)
        return Scene(layout)
    }

}


class OutOfBoundsListener() : EventListener<OutOfBounds> {
    override fun handle(event: OutOfBounds) {
        gameState = gameState.handleOutOfBounds(event.id)
    }
}

class ReachBoundsListener() : EventListener<ReachBounds> {
    override fun handle(event: ReachBounds) {
        gameState = gameState.handleReachBounds(event.id)
    }
}

class GameInitializer(val gameSaver: GameStateSaver){
    enum class GameStart {
        NEW, LOAD
    }

    fun selectGameStart(selectedStartMode: GameStart): GameState{
        return when(selectedStartMode){
            GameStart.NEW -> startNewGame()
            GameStart.LOAD -> loadGame()
        }
    }

    private fun startNewGame(): GameState {
        return gameSaver.startNewGame()
    }

    private fun loadGame(): GameState {
        return gameSaver.loadGameState()
    }

}