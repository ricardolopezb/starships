package edu.austral.ingsis.starships

import edu.austral.ingsis.starships.ui.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import starships.GameEngine
import starships.entities.ship.ShipController
import starships.entities.BaseEntity
import starships.movement.Mover
import starships.persistence.WindowConfigurator
import java.io.FileReader

fun main() {
    launch(Starships::class.java)
}
private var gameEngine = GameEngine()
class Starships() : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    private val facade = ElementsViewFacade(imageResolver)
    private val keyTracker = KeyTracker()


    companion object {
        val STARSHIP_IMAGE_REF = ImageRef("starship", 70.0, 70.0)
    }

    override fun start(primaryStage: Stage) {
        gameEngine = GameEngine().initialize()
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

        facade.timeListenable.addEventListener(TimeListener(facade.elements, entityInSceneManager))
        facade.collisionsListenable.addEventListener(CollisionListener())
        facade.outOfBoundsListenable.addEventListener(OutOfBoundsListener())
        facade.reachBoundsListenable.addEventListener(ReachBoundsListener())
//        val ship = gameEngine.ships[0].adapt()
//        val ships = facade.elements.filter { (key, _) -> key.startsWith("Ship") }
////        val keyPressedListener = KeyPressedListener(ships)
//        keyPressedListener.insertBindings()
//        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(ships, facade.elements))
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(gameEngine.ships, entityInSceneManager))

        val scene = Scene(facade.view)
        keyTracker.scene = scene

        primaryStage.scene = scene
        primaryStage.height = (windowConfigurator.getProperty("height") as Long).toDouble()
        primaryStage.width = (windowConfigurator.getProperty("width") as Long).toDouble()

        facade.start()
        keyTracker.start()
        primaryStage.show()
    }

    fun insertCoreEntitiesIntoUI() {
        gameEngine.ships.forEach{
            facade.elements[it.id] = it.adapt()
        }
        gameEngine.movingEntities.forEach{
            facade.elements[it.id] = it.adapt()
        }
    }

    override fun stop() {
        facade.stop()
        keyTracker.stop()
    }
}

class EntityInSceneManager(private val facade: ElementsViewFacade){

    fun insert(entities: List<ElementModel>) {
//        for (entity in entities.iterator()){
//            facade.elements[entity.id] = entity
//        }
        println("shooting!")
    }

    fun insert(entity: ElementModel){
        println("Inserting " + entity.id)
        facade.elements[entity.id] = entity
    }

    fun removeById(entityId: String){
        facade.elements.remove(entityId)
    }

}

class TimeListener(private val elements: Map<String, ElementModel>,
                   private val inserter: EntityInSceneManager) : EventListener<TimePassed> {
    var prevMillis: Long = 0
    override fun handle(event: TimePassed) {
        val newShipList = ArrayList<ShipController>()
        val newMoverList = ArrayList<Mover<BaseEntity>>()
        gameEngine.ships.forEach {
            val updatedShip = it.update()
            val updatedShipElementModel = updatedShip.adapt()
            elements[it.id]?.x?.set(updatedShipElementModel.x.value)
            elements[it.id]?.y?.set(updatedShipElementModel.y.value)
            elements[it.id]?.rotationInDegrees?.set(updatedShipElementModel.rotationInDegrees.value)
            newShipList.add(updatedShip)
        }
        gameEngine.movingEntities.forEach {
            val updatedMover = it.updatePosition()
            val updatedMoverElementModel = updatedMover.adapt()

            if(elements[it.id] != null) {
                elements[it.id]?.x?.set(updatedMoverElementModel.x.value)
                elements[it.id]?.y?.set(updatedMoverElementModel.y.value)
                elements[it.id]?.rotationInDegrees?.set(updatedMoverElementModel.rotationInDegrees.value)
            } else {
                inserter.insert(updatedMoverElementModel)
            }
            newMoverList.add(updatedMover)
        }
        gameEngine.removedIds.forEach{
            if(elements.containsKey(it)){
                inserter.removeById(it)
            }
        }
        gameEngine = GameEngine(newMoverList, newShipList, gameEngine.removedIds, gameEngine.scores)

        if(System.currentTimeMillis() - prevMillis >= 5000){
            prevMillis = System.currentTimeMillis()
            gameEngine = gameEngine.spawnAsteroid()
        }

        //element.rotationInDegrees.set(element.rotationInDegrees.value + 1)
    }
}

class CollisionListener() : EventListener<Collision> {
    override fun handle(event: Collision) {
        gameEngine = gameEngine.handleCollision(event.element1Id, event.element2Id)
    }

}

class KeyPressedListener(private val ships: List<ShipController>, private val entityInSceneManager: EntityInSceneManager): EventListener<KeyPressed> {

    var keyBindingMap = insertBindings()

    fun insertBindings(): Map<String, Map<String, KeyCode>>{
        val mapToReturn = HashMap<String, Map<String, KeyCode>>()
        val obj = JSONParser().parse(FileReader(System.getProperty("user.dir") +
                "/app/src/main/java/starships/persistence/keybindings.json"))
        val keyBindings = obj as JSONArray
        val it: Iterator<*> = keyBindings.iterator()
        var id = 1
        while (it.hasNext()) {
            val binding = it.next() as JSONObject
            val bindingMap = mapOf(
                    "accelerate" to KeyCode.valueOf(binding.get("accelerate") as String),
                    "brake" to KeyCode.valueOf(binding.get("brake") as String),
                    "rotate_clockwise" to KeyCode.valueOf(binding.get("rotate_clockwise") as String),
                    "rotate_counterclockwise" to KeyCode.valueOf(binding.get("rotate_counterclockwise") as String),
                    "shoot" to KeyCode.valueOf(binding.get("shoot") as String)

            )

            mapToReturn["Ship-"+id] = bindingMap
            id++

        }
        return mapToReturn
    }

    override fun handle(event: KeyPressed) {
        val pressedKey = event.key

        ships.forEach {
            for ((shipId, keyCodeMap) in keyBindingMap.entries.iterator()) {
                if(shipId.equals(it.id)){
                    when(pressedKey){
                        keyCodeMap["accelerate"] -> {
                            gameEngine = gameEngine.accelerateShip(shipId, 0.5)
                        }
                        keyCodeMap["brake"] -> {
                            gameEngine = gameEngine.accelerateShip(shipId, -0.4)
                        }
                        keyCodeMap["rotate_clockwise"] -> {
                            gameEngine = gameEngine.rotateShip(shipId, 20)
                        }
                        keyCodeMap["rotate_counterclockwise"] -> {
                            gameEngine = gameEngine.rotateShip(shipId, -20)
                        }
                        keyCodeMap["shoot"] -> {
                            gameEngine = gameEngine.shoot(shipId)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

}

class OutOfBoundsListener() : EventListener<OutOfBounds> {
    override fun handle(event: OutOfBounds) {
        println("Element has gone out of bounds: ${event.id}")
    }
}

class ReachBoundsListener() : EventListener<ReachBounds> {
    override fun handle(event: ReachBounds) {
        println("Element has reach bounds: ${event.id}")
    }
}