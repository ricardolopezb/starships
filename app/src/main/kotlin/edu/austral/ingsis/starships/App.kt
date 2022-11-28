package edu.austral.ingsis.starships

import edu.austral.ingsis.starships.ui.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.collections.ObservableMap
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import starships.GameState
import starships.entities.ship.ShipController
import starships.entities.BaseEntity
import starships.movement.Mover
import persistence.Constants
import persistence.WindowConfigurator
import starships.utils.RandomNumberGenerator
import java.io.FileReader

fun main() {
    launch(Starships::class.java)
}
private var gameState = GameState()
class Starships() : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    private val facade = ElementsViewFacade(imageResolver)
    private val keyTracker = KeyTracker()


    companion object {
        val STARSHIP_IMAGE_REF = ImageRef("starship", 70.0, 70.0)
    }

    override fun start(primaryStage: Stage) {
        gameState = GameState().initialize()
        val windowConfigurator = WindowConfigurator()
        insertCoreEntitiesIntoUI()
        val entityInSceneManager = EntityInSceneManager(facade)
//        val bullet =  ElementModel(
//                "lmao",
//                350.0,
//                350.0,
//                10.0,
//                10.0,
//                180.0, ElementColliderType.Rectangular,
//                ImageRef("bullet", 20.0, 20.0)
//        )
//        facade.elements["lmao"] = bullet
//        facade.elements["asteroid-1"] =
//            ElementModel("asteroid-1", 0.0, 0.0, 30.0, 40.0, 0.0, Elliptical, null)
//        facade.elements["asteroid-2"] =
//            ElementModel("asteroid-2", 100.0, 100.0, 30.0, 20.0, 90.0, Rectangular, null)
//        facade.elements["asteroid-3"] =
//            ElementModel("asteroid-3", 200.0, 200.0, 20.0, 30.0, 180.0, Elliptical, null)


//        val starship = ElementModel("starship", 300.0, 300.0, 40.0, 40.0, 270.0, Triangular, STARSHIP_IMAGE_REF)

        addEventListeners(entityInSceneManager)

        //        val ship = gameEngine.ships[0].adapt()
//        val ships = facade.elements.filter { (key, _) -> key.startsWith("Ship") }
////        val keyPressedListener = KeyPressedListener(ships)
//        keyPressedListener.insertBindings()
//        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(ships, facade.elements))

        val scene = Scene(facade.view)
        keyTracker.scene = scene

        setUpPrimaryStage(primaryStage, scene, windowConfigurator)

        facade.start()
        keyTracker.start()
        primaryStage.show()
    }

    private fun setUpPrimaryStage(primaryStage: Stage, scene: Scene, windowConfigurator: WindowConfigurator) {
        primaryStage.scene = scene
        primaryStage.height = (windowConfigurator.getProperty("height").get() as Long).toDouble()
        primaryStage.width = (windowConfigurator.getProperty("width").get() as Long).toDouble()
    }

    private fun addEventListeners(entityInSceneManager: EntityInSceneManager) {
        facade.timeListenable.addEventListener(TimeListener(facade.elements, entityInSceneManager))
        facade.collisionsListenable.addEventListener(CollisionListener())
        facade.outOfBoundsListenable.addEventListener(OutOfBoundsListener())
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
        println("Inserting " + entity.id)
        facade.elements[entity.id] = entity
        println(facade.elements)
    }

    fun removeById(entityId: String){
        facade.elements.remove(entityId)
    }

}

class TimeListener(private val elements: ObservableMap<String, ElementModel>,
                   private val inserter: EntityInSceneManager) : EventListener<TimePassed> {
    override fun handle(event: TimePassed) {
        val newShipList = ArrayList<ShipController>()
        val newMoverList = ArrayList<Mover<BaseEntity>>()

        updateShips(newShipList)
        removeIdsInScene()
        spawnAsteroid(newMoverList)
        updateMovingEntities(newMoverList)


        gameState = GameState(newMoverList, newShipList, gameState.removedIds, gameState.scores)

        //element.rotationInDegrees.set(element.rotationInDegrees.value + 1)
    }

    private fun updateMovingEntities(newMoverList: ArrayList<Mover<BaseEntity>>) {
        gameState.movingEntities.forEach {
            val updatedMover = it.updatePosition()
            val updatedMoverElementModel = updatedMover.adapt()

            if (elements.containsKey(it.id)) {
                elements[it.id]?.x?.set(updatedMoverElementModel.x.value)
                elements[it.id]?.y?.set(updatedMoverElementModel.y.value)
                elements[it.id]?.rotationInDegrees?.set(updatedMoverElementModel.rotationInDegrees.value)
            } else {
                //inserter.insert(updatedMoverElementModel)
                elements[updatedMoverElementModel.id] = updatedMoverElementModel
            }
            newMoverList.add(updatedMover)
        }
    }

    private fun spawnAsteroid(newMoverList: ArrayList<Mover<BaseEntity>>) {
        if (Math.random() <= Constants.ASTEROID_SPAWN_RATE) {
            //prevMillis = currentMillis
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

class KeyPressedListener(): EventListener<KeyPressed> {

    var keyBindingMap = insertBindings()

    fun insertBindings(): Map<String, Map<String, KeyCode>>{
        val mapToReturn = HashMap<String, Map<String, KeyCode>>()
        val it: Iterator<*> = getKeybindingsMapIterator()
        var id = 1
        while (it.hasNext()) {
            val binding = it.next() as JSONObject
            val bindingMap = mapOf(
                    "accelerate" to KeyCode.valueOf(binding.get("accelerate") as String),
                    "brake" to KeyCode.valueOf(binding.get("brake") as String),
                    "rotate_clockwise" to KeyCode.valueOf(binding.get("rotate_clockwise") as String),
                    "rotate_counterclockwise" to KeyCode.valueOf(binding.get("rotate_counterclockwise") as String),
                    "shoot" to KeyCode.valueOf(binding.get("shoot") as String),
                    "change_weapon" to KeyCode.valueOf(binding.get("change_weapon") as String)
            )

            mapToReturn["Ship-"+id] = bindingMap
            id++

        }
        return mapToReturn
    }

    private fun getKeybindingsMapIterator(): Iterator<*> {
        val obj = JSONParser().parse(FileReader(Constants.KEYBINDINGS_FILE_PATH))
        val keyBindings = obj as JSONArray
        val it: Iterator<*> = keyBindings.iterator()
        return it
    }

    override fun handle(event: KeyPressed) {
        val pressedKey = event.key
        for ((shipId, keyCodeMap) in keyBindingMap.entries.iterator()) {

            when(pressedKey){
                keyCodeMap["accelerate"] -> gameState = gameState.accelerateShip(shipId, Constants.SHIP_ACCELERATION_COEFFICIENT)
                keyCodeMap["brake"] -> gameState = gameState.stopShip(shipId)
                keyCodeMap["rotate_clockwise"] -> gameState = gameState.rotateShip(shipId, Constants.SHIP_ROTATION_DEGREES)
                keyCodeMap["rotate_counterclockwise"] -> gameState = gameState.rotateShip(shipId, -Constants.SHIP_ROTATION_DEGREES)
                keyCodeMap["shoot"] -> gameState = gameState.shoot(shipId)
                keyCodeMap["change_weapon"] -> gameState = gameState.changeWeapon(shipId)
                else -> {}
            }
        }
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