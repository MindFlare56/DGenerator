package mindf.utils

import java.io.File

class Bat {

    companion object {

        internal fun createFileCommand(fileName: String): String {
            return "cmd /c echo 2>$fileName"
        }

        internal fun createFile(fileName: String): Process {
            return execute(createFileCommand(fileName))!!
        }

        internal fun createFile(name: String, content: String = ""): File {
            val file = File(name)
            file.writeText(content)
            return file
        }

        internal fun execute(command: String): Process? {
            return Runtime.getRuntime().exec(command)
        }
    }
}