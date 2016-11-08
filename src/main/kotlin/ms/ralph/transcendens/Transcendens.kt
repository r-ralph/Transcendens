package ms.ralph.transcendens

import ms.ralph.transcendens.Transcendens.ConvertMethod.MCWORLD_TO_PC
import ms.ralph.transcendens.method.AbstractMethod
import ms.ralph.transcendens.method.MethodMcWorldToPc
import java.io.File
import kotlin.system.exitProcess

object Transcendens {

    @JvmStatic fun main(args: Array<String>) {
        // Check arguments
        if (args.size < 3) {
            println("Missing arguments!")
            printUsage()
            exitProcess(1)
        }
        // Check source and destination
        val source = File(args[1])
        val destination = args[2]
        val params = args.copyOfRange(3, args.size)
        if (!source.exists()) {
            println("Source file is not exist!")
            exitProcess(1)
        }
        val method: AbstractMethod = when (args[0]) {
            MCWORLD_TO_PC.commandName -> MethodMcWorldToPc(source, destination, params)
            else -> {
                println("Unknown method: ${args[0]}")
                printUsage()
                exitProcess(1)
            }
        }
        println("Start process.")
        val start = System.currentTimeMillis()
        method.start()
        val end = System.currentTimeMillis()
        println("Finished.")
        println("Elapsed time: ${end - start} ms")
    }

    private fun printUsage() {
        println("java -jar transcendens.jar <method> <source> <dest> [optional params...]")
        println("method: ${ConvertMethod.values().map { it.commandName }.joinToString { it } }")
    }

    enum class ConvertMethod(val commandName: String) {
        MCWORLD_TO_PC("mcworld2pc"),
        //PC_TO_MCWORLD("pc2mcworld"),
    }
}
