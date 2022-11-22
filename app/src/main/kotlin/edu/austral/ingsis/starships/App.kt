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
import starships.ShipController
import starships.entities.BaseEntity
import starships.movement.Mover
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
        insertCoreEntitiesIntoUI()

//        facade.elements["asteroid-1"] =
//            ElementModel("asteroid-1", 0.0, 0.0, 30.0, 40.0, 0.0, Elliptical, null)
//        facade.elements["asteroid-2"] =
//            ElementModel("asteroid-2", 100.0, 100.0, 30.0, 20.0, 90.0, Rectangular, null)
//        facade.elements["asteroid-3"] =
//            ElementModel("asteroid-3", 200.0, 200.0, 20.0, 30.0, 180.0, Elliptical, null)


//        val starship = ElementModel("starship", 300.0, 300.0, 40.0, 40.0, 270.0, Triangular, STARSHIP_IMAGE_REF)

        facade.timeListenable.addEventListener(TimeListener(facade.elements))
        facade.collisionsListenable.addEventListener(CollisionListener())
        val ship = gameEngine.ships[0].adapt()
        val ships = facade.elements.filter { (key, _) -> key.startsWith("Ship") }
//        val keyPressedListener = KeyPressedListener(ships)
//        keyPressedListener.insertBindings()
//        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(ships, facade.elements))
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(gameEngine.ships[0]))

        val scene = Scene(facade.view)
        keyTracker.scene = scene

        primaryStage.scene = scene
        primaryStage.height = 800.0
        primaryStage.width = 800.0

        facade.start()
        keyTracker.start()
        primaryStage.show()
    }

    private fun insertCoreEntitiesIntoUI() {
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

class TimeListener(private val elements: Map<String, ElementModel>) : EventListener<TimePassed> {
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
            elements[it.id]?.x?.set(updatedMoverElementModel.x.value)
            elements[it.id]?.y?.set(updatedMoverElementModel.y.value)
            elements[it.id]?.rotationInDegrees?.set(updatedMoverElementModel.rotationInDegrees.value)
            newMoverList.add(updatedMover)
        }
        gameEngine = GameEngine(newMoverList, newShipList, gameEngine.scores)
//        elements.forEach {
//            val (key, element) = it
//            when(key) {
//                "starship" -> {element= gameEngine.ships[0].update().adapt()}
//                "asteroid-1" -> {
//                    element.x.set(element.x.value + 0.25)
//                    element.y.set(element.y.value + 0.25)
//                }
//                else -> {
//                    element.x.set(element.x.value - 0.25)
//                    element.y.set(element.y.value - 0.25)
//                }
//            }

        //element.rotationInDegrees.set(element.rotationInDegrees.value + 1)
    //}
    }
}

class CollisionListener() : EventListener<Collision> {
    override fun handle(event: Collision) {
        println("${event.element1Id} ${event.element2Id}")
    }

}

class KeyPressedListener(private val ship: ShipController): EventListener<KeyPressed> {

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
//        when(event.key) {
//            KeyCode.UP -> starship.y.set(starship.y.value - 5 )
//            KeyCode.DOWN -> starship.y.set(starship.y.value + 5 )
//            KeyCode.LEFT -> starship.x.set(starship.x.value - 5 )
//            KeyCode.RIGHT -> starship.x.set(starship.x.value + 5 )
//            else -> {}
//        }

        val pressedKey = event.key


        // estoy teniendo un problema con las referencias, no se esta actualizando el cambio
        // verlo con la rotacion


//        for ((shipId, keyCodeList) in keyBindingMap.entries.iterator()){
////            val (shipId, keyCodeList) = it
//            val starship = ships[shipId]!!
//            when(pressedKey){
//                keyCodeList["accelerate"] -> {
//                    var foundShip: ShipController? = null
//                    for (ship in gameEngine.ships.iterator()){
//                        if(ship.id.equals(shipId)) foundShip = ship
//                    }
//                    val acceleratedShipElementModel = foundShip!!.accelerate(3.0).update().adapt()
//                    starship.y.set(acceleratedShipElementModel.y.value + 5) //no esta actualizando el valor
//                    print(starship.y.value)
//                }
//                keyCodeList["brake"] -> ships[shipId]?.y?.set(ships[shipId]?.y?.value!! - 5 )
//                keyCodeList["rotate_clockwise"] -> {
//                    print("before = " + starship.rotationInDegrees.value)
//                    var foundShip: ShipController? = null
//                    for (ship in gameEngine.ships.iterator()){
//                        if(ship.id.equals(shipId)) foundShip = ship
//                    }
//                    val newRotatedShipController = foundShip!!.rotate(100).update()
//                    val acceleratedShipElementModel = newRotatedShipController.adapt()
//                    starship.rotationInDegrees.set(acceleratedShipElementModel.rotationInDegrees.value) //no esta actualizando el valor
//                    print("after = " + starship.rotationInDegrees.value)
//                }
//                keyCodeList["rotate_counterclockwise"] -> ships[shipId]?.x?.set(ships[shipId]?.x?.value!! - 5 )
////                keyCodeList["accelerate"] -> print("accelerating!$starship")
////                keyCodeList["brake"] -> print("braking!")
////                keyCodeList["rotate_clockwise"] -> print("rotating clockwise")
////                keyCodeList["rotate_counterclockwise"] -> print("rotating counterclock")
////                keyCodeList["shoot"] -> print("shooting! - shipId = "+ shipId)
//                else -> {}
//            }
//
//
//        }

        for ((shipId, keyCodeList) in keyBindingMap.entries.iterator()) {
//            val (shipId, keyCodeList) = it
            if(shipId.equals(ship.id)){
//                when (pressedKey) {
//                    keyCodeList["accelerate"] -> {
//                        println("before = " + facadeElements[shipId]?.y?.value)
//                        val updatedShipController = ship.accelerate(300.0).update()
//                        val acceleratedShipElementModel = updatedShipController.update().update().adapt()
//                        facadeElements[shipId]?.y?.set(acceleratedShipElementModel.y.value) //no esta actualizando el valor
//                        println("After = " + facadeElements[shipId]?.y?.value)
//                    }
//                    else -> {}
//                }

                when(pressedKey){
                    keyCodeList["accelerate"] -> {
                        gameEngine = gameEngine.accelerateShip(shipId, 0.5)
                    }
                    keyCodeList["brake"] -> {
                        gameEngine = gameEngine.accelerateShip(shipId, -0.4)
                    }
                    keyCodeList["rotate_clockwise"] -> {
                        gameEngine = gameEngine.rotateShip(shipId, 20)
                    }
                    keyCodeList["rotate_counterclockwise"] -> {
                        gameEngine = gameEngine.rotateShip(shipId, -20)
                    }
                    keyCodeList["shoot"] -> {
                        println("shooting!")
                    }
                    else -> {}
                }
            }

        }



//        when(event.key) {
//            KeyCode.UP -> gameEngine
//            KeyCode.DOWN -> starship.y.set(starship.y.value + 5 )
//            KeyCode.LEFT -> starship.x.set(starship.x.value - 5 )
//            KeyCode.RIGHT -> starship.x.set(starship.x.value + 5 )
//            else -> {}
//        }
    }

}