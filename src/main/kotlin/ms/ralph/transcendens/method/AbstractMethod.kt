package ms.ralph.transcendens.method

import java.io.File

abstract class AbstractMethod(internal val source: File,
                              internal val destination: String,
                              internal val params: Array<String>) {
    abstract fun start()
}
