package edu.austral.ingsis.starships

import edu.austral.ingsis.starships.ui.*
import game.GameState
import game.LiveGame
import game.actions.GameStateAction
import game.actions.GameStateActionMapper
import javafx.application.Application
import javafx.application.Application.launch
import javafx.collections.ObservableMap
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import persistence.Constants
import persistence.WindowConfigurator
import persistence.gamestate.GameStateSaver
import starships.entities.BaseEntity
import starships.entities.ship.ShipController
import starships.movement.Mover
import java.io.File
import java.io.FileReader


fun main() {
    launch(Starships::class.java)
}
private var gameState: GameState = LiveGame()
class Starships() : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    private val facade = ElementsViewFacade(imageResolver)
    private val keyTracker = KeyTracker()
    private val gameSaver = GameStateSaver()
    var gameScene = Scene(StackPane())
    var startScene = Scene(StackPane())
    val song = initMusic()

    private fun initMusic(): MediaPlayer {
        val song = javafx.scene.media.Media(File(Constants.SONG_FILE_PATH).toURI().toString())
        return MediaPlayer(song)
    }


    override fun start(primaryStage: Stage) {
        cleanFacade()
        val gameInitializer = GameInitializer(gameSaver)
        startScene = generateStartScene(primaryStage, gameInitializer)
        primaryStage.scene = startScene
        val entityInSceneManager = EntityInSceneManager(facade)

        val (generalPane, lifeLabels, scoreLabels) = buildGeneralPane()
        addEventListeners(entityInSceneManager, primaryStage, lifeLabels, scoreLabels)
        gameScene = Scene(generalPane, 950.0, 800.0)

        //gameScene = Scene(facade.view)
        addCssToFacade()
        keyTracker.scene = gameScene

        startMusic()

        setUpPrimaryStage(primaryStage, startScene)
        startApplicationComponents(primaryStage)
    }

    private fun startMusic() {
        song.volume = Constants.MUSIC_VOLUME
        song.play()
    }

    private fun addCssToFacade() {
        facade.view.id = "facade"
        gameScene.stylesheets.add(this::class.java.classLoader.getResource("gameStyles.css")?.toString())
    }

    private fun buildGeneralPane(): Triple<StackPane, List<Label>, List<Label>> {
        val generalPane = StackPane()

        val (livesLayout, lifeLabels) = buildLivesLayout()
        val (scoreLayout, scoreLabels) = buildScoresLayout()

        generalPane.children.addAll(facade.view, livesLayout, scoreLayout)
        return Triple(generalPane, lifeLabels, scoreLabels)

    }

    private fun buildLivesLayout(): Pair<VBox, List<Label>> {
        return buildVerticalPlayerDataLabelLayout(Pos.TOP_LEFT, "HEALTH")
    }
    private fun buildScoresLayout(): Pair<VBox, List<Label>> {
        return buildVerticalPlayerDataLabelLayout(Pos.TOP_RIGHT, "SCORES")
    }

    private fun buildVerticalPlayerDataLabelLayout(position: Pos, layoutTitle: String): Pair<VBox, List<Label>> {
        val verticalLayout = VBox()
        verticalLayout.alignment = position
        val playerQuantity = getPlayerQuantity()
        val labelList = generateLabelList(layoutTitle, playerQuantity)
        verticalLayout.children.addAll(labelList)

        return verticalLayout to labelList
    }

    private fun generateLabelList(layoutTitle: String, playerQuantity: Int): ArrayList<Label> {
        val labelList = ArrayList<Label>()
        labelList.add(generateLayoutTitle(layoutTitle))
        repeat(playerQuantity) { index ->
            labelList.add(generateInitialPlayerLabel(index))
        }
        return labelList
    }

    private fun generateInitialPlayerLabel(index: Int): Label {
        val label = Label("Player " + (index+1) + ": ")
        label.textFill = Color.WHITE
        label.font = Font("Arial", 15.0)
        return label

    }

    private fun generateLayoutTitle(title: String): Label {
        val layoutTitle = Label(title)
        layoutTitle.textFill = Color.WHITE
        layoutTitle.font = Font("Arial", 25.0)
        return layoutTitle
    }

    private fun getPlayerQuantity() : Int = (WindowConfigurator.getInstance().getProperty("players").get() as Long).toInt()

    private fun cleanFacade() {
        facade.showCollider.value = false
        facade.showGrid.value = false
    }

    fun generateStartScene(primaryStage: Stage, gameInitializer: GameInitializer): Scene {
        val gameTitle = Label("Starships")
        primaryStage.title = "Starships"
        val newGameButton = createGameStartButton(gameInitializer, primaryStage, GameInitializer.GameStart.NEW, "New Game")
        val loadGameButton = createGameStartButton(gameInitializer, primaryStage, GameInitializer.GameStart.LOAD, "Load Game")
        val layout = VBox()
        layout.children.addAll(gameTitle, newGameButton, loadGameButton)
        return Scene(layout)
    }


    private fun createGameStartButton(gameInitializer: GameInitializer, primaryStage: Stage, gameStart: GameInitializer.GameStart, message: String) : javafx.scene.control.Button{
        val button = javafx.scene.control.Button(message)
        button.onAction = EventHandler {
            startGameScene(gameInitializer, primaryStage, gameStart)
        }
        return button
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

    private fun addEventListeners(entityInSceneManager: EntityInSceneManager, primaryStage: Stage, lifeLabels: List<Label>, scoreLabels: List<Label>) {
        val gameFinishedListener = GameFinishedListener(primaryStage)
        val outOfBoundsListener = OutOfBoundsListener()
        facade.timeListenable.addEventListener(TimeListener(facade.elements, entityInSceneManager, gameFinishedListener, lifeLabels, scoreLabels))
        facade.collisionsListenable.addEventListener(CollisionListener())
        facade.outOfBoundsListenable.addEventListener(outOfBoundsListener)
        facade.reachBoundsListenable.addEventListener(ReachBoundsListener())
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener())
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
                   private val gameFinishedListener: GameFinishedListener,
                   private var lifeLabels: List<Label>,
                   private var scoreLabels: List<Label>

) : EventListener<TimePassed> {
    private val startingShips = (WindowConfigurator.getInstance().getProperty("players").get() as Long).toInt()
    private val gameFinishedEmitter = createGameFinishedEmitter(gameFinishedListener)

    private fun createGameFinishedEmitter(gameFinishedListener: GameFinishedListener): ListenableEmitter<GameEnding> {
        val gameEndingEmitter = ListenableEmitter<GameEnding>()
        gameEndingEmitter.addEventListener(gameFinishedListener)

        return gameEndingEmitter
    }


    override fun handle(event: TimePassed) {
        val newShipList = ArrayList<ShipController>()
        val newMoverList = ArrayList<Mover<BaseEntity>>()
        if(startingShips > 1) checkMultiplayerVictory()
        else checkGameOver()
        updateGameState(newShipList, newMoverList)

        gameState = gameState.getCopyWith(newMoverList, newShipList, gameState.removedIds, gameState.scores)
    }

    private fun updateGameState(newShipList: ArrayList<ShipController>, newMoverList: ArrayList<Mover<BaseEntity>>) {
        updateShips(newShipList)
        removeIdsInScene()
        spawnAsteroid(newMoverList)
        updateMovingEntities(newMoverList)
        updateLabels()
    }

    private fun updateLabels(){
        updateScoreLabels()
        updateHealthLabels()
    }

    private fun updateHealthLabels() {
        gameState.ships.forEach {
            val shipNumber = it.id.drop(5).toInt()
            lifeLabels.get(shipNumber).text = "Player ${(shipNumber)}: " + it.health
        }
    }

    private fun updateScoreLabels() {
        gameState.scores.forEach {
            val shipNumber = it.key.drop(5).toInt()
            scoreLabels.get(shipNumber).text = "Player ${(shipNumber)}: " + it.value
        }
    }

    private fun checkGameOver() {
        if(gameState.ships.isEmpty() && areDestroyedShips() && !gameFinishedListener.called)
            gameFinishedEmitter.emit(GameEnding("SINGLEPLAYER", "0"))
    }

    private fun areDestroyedShips(): Boolean {
        gameState.removedIds.forEach {
            if (it.startsWith("Ship")) return true
        }
        return false

    }

    private fun checkMultiplayerVictory() {
        if(gameState.ships.size == 1 && !gameFinishedListener.called){
            gameFinishedEmitter.emit(GameEnding("MULTIPLAYER", gameState.ships[0].id.drop(5)))
        }
    }


    private fun updateMovingEntities(newMoverList: ArrayList<Mover<BaseEntity>>) {
        gameState.movingEntities.forEach {
            val updatedMover = it.updatePosition()
            val updatedMoverElementModel = updatedMover.adapt()

            if (elements.containsKey(it.id)) {
                updateMovingEntitiesInFacadeValues(it, updatedMoverElementModel)
            } else {
                inserter.insert(updatedMoverElementModel)
            }
            newMoverList.add(updatedMover)
        }
    }

    private fun updateMovingEntitiesInFacadeValues(it: Mover<BaseEntity>, updatedMoverElementModel: ElementModel) {
        elements[it.id]?.x?.set(updatedMoverElementModel.x.value)
        elements[it.id]?.y?.set(updatedMoverElementModel.y.value)
        if (it.id.startsWith("Asteroid"))
            elements[it.id]?.rotationInDegrees?.set(elements[it.id]?.rotationInDegrees!!.value + 1)
        elements[it.id]?.height?.set(updatedMoverElementModel.height.value)
        elements[it.id]?.width?.set(updatedMoverElementModel.width.value)
    }

    private fun spawnAsteroid(newMoverList: ArrayList<Mover<BaseEntity>>){
        val shouldSpawnAsteroid = evaluateAsteroidSpawnLogic()
        spawnAsteroid(newMoverList, shouldSpawnAsteroid)
    }

    private fun evaluateAsteroidSpawnLogic(): Boolean {
        return Math.random() <= Constants.ASTEROID_SPAWN_RATE
    }

    private fun spawnAsteroid(newMoverList: ArrayList<Mover<BaseEntity>>, shouldSpawnAsteroid: Boolean) {
        if (shouldSpawnAsteroid) {
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
            updateElementInFacadeValues(it, updatedShipElementModel)
            newShipList.add(updatedShip)
        }
    }

    private fun updateElementInFacadeValues(it: ShipController, updatedShipElementModel: ElementModel) {
        elements[it.id]?.x?.set(updatedShipElementModel.x.value)
        elements[it.id]?.y?.set(updatedShipElementModel.y.value)
        elements[it.id]?.rotationInDegrees?.set(updatedShipElementModel.rotationInDegrees.value)
    }
}

class CollisionListener() : EventListener<Collision> {
    override fun handle(event: Collision) {
        gameState = gameState.handleCollision(event.element1Id, event.element2Id)
    }
}

class KeyPressedListener(): EventListener<KeyPressed> {

    var keyBindingMap = insertBindings()

    fun insertBindings(): Map<KeyCode, GameStateAction>{
        val mapToReturn = HashMap<KeyCode, GameStateAction>()
        val it: Iterator<*> = getKeybindingsMapIterator()
        var id = 1
        while (it.hasNext()) {
            val binding = it.next() as JSONObject
            loadMapWithBindings(binding, ("Ship-"+id), mapToReturn)
            id++
        }
        return mapToReturn
    }

    private fun loadMapWithBindings(binding: JSONObject, shipId: String, bindingsMap: MutableMap<KeyCode, GameStateAction>) {
        val existingActions = binding.keys as Set<String>
        existingActions.forEach {
            val correspondingAction = GameStateActionMapper.getShipActionForDescription(shipId, it)
            bindingsMap.put(KeyCode.valueOf(binding.get(it) as String), correspondingAction)
        }
    }


    private fun getKeybindingsMapIterator(): Iterator<*> {
        val obj = JSONParser().parse(FileReader(Constants.KEYBINDINGS_FILE_PATH))
        val keyBindings = obj as JSONArray
        val it: Iterator<*> = keyBindings.iterator()
        return it
    }

    override fun handle(event: KeyPressed) {
        if(keyBindingMap.containsKey(event.key))
            gameState = keyBindingMap.get(event.key)!!.applyAction(gameState)
    }

}

data class GameEnding(val endingType: String, val winnerId: String)



class GameFinishedListener(private val primaryStage: Stage): EventListener<GameEnding> {
    var called = false
    private fun createGameOverScene(): Scene {
        val gameOverLabel = Label("Game Over")
        val scoresLabel = Label(generateScoresString())
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
        if(event.endingType.equals("SINGLEPLAYER")){
            val gameOverScene = createGameOverScene()
            primaryStage.scene = gameOverScene
        } else if(event.endingType.equals("MULTIPLAYER")){
            val winningScene = createWinningScene(event.winnerId)
            primaryStage.scene = winningScene
        }
    }

    private fun createWinningScene(winnerId: String): Scene {
        val gameOverLabel = Label("Player $winnerId wins!")
        val scoresLabel = Label(generateScoresString())
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

    fun selectGameStart(selectedStartMode: GameStart): GameState {
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