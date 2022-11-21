package edu.austral.ingsis.starships

import edu.austral.ingsis.starships.ui.*
import edu.austral.ingsis.starships.ui.ElementColliderType.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import starships.GameEngine
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
        val ships = facade.elements.filter { (key, _) -> key.startsWith("Ship") }
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(ships))

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
        gameEngine.ships.forEach {
            val updatedShipElementModel = it.update().adapt()
            elements[it.id]?.x?.set(updatedShipElementModel.x.value)
            elements[it.id]?.y?.set(updatedShipElementModel.y.value)
            elements[it.id]?.rotationInDegrees?.set(updatedShipElementModel.rotationInDegrees.value)
        }
        gameEngine.movingEntities.forEach {
            val updatedMover = it.updatePosition().adapt()
            elements[it.id]?.x?.set(updatedMover.x.value)
            elements[it.id]?.y?.set(updatedMover.y.value)
            elements[it.id]?.rotationInDegrees?.set(updatedMover.rotationInDegrees.value)
        }

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

class KeyPressedListener(private val ships: Map<String, ElementModel>): EventListener<KeyPressed> {

    val keyBindingMap = HashMap<String, List<KeyCode>>()

    fun insertBindings(): Map<String, List<KeyCode>>{
        val mapToReturn = HashMap<String, List<KeyCode>>()
        val obj = JSONParser().parse(FileReader(System.getProperty("user.dir") +
                "/app/src/main/java/starships/persistence/keybindings.json"))
        val keyBindings = obj as JSONArray
        val it: Iterator<*> = keyBindings.iterator()
        var id = 1
        while (it.hasNext()) {
            val binding = it.next() as JSONObject
            val bindingList = listOf(
                    KeyCode.valueOf(binding.get("accelerate") as String),
                    KeyCode.valueOf(binding.get("brake") as String),
                    KeyCode.valueOf(binding.get("rotate_clockwise") as String),
                    KeyCode.valueOf(binding.get("rotate_counterclockwise") as String),
                    KeyCode.valueOf(binding.get("shoot") as String)

            )

            id++
            mapToReturn["Ship-"+id] = bindingList

        }
        return mapToReturn
    }

    override fun handle(event: KeyPressed) {
        when(event.key) {
            KeyCode.UP -> starship.y.set(starship.y.value - 5 )
            KeyCode.DOWN -> starship.y.set(starship.y.value + 5 )
            KeyCode.LEFT -> starship.x.set(starship.x.value - 5 )
            KeyCode.RIGHT -> starship.x.set(starship.x.value + 5 )
            else -> {}
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