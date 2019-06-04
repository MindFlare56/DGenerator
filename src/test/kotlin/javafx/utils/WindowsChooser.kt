package javafx.utils

import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class WindowsChooser(var stage: Stage? = null, var title: String = "Select file", windowsChooser: (windowsChooser: WindowsChooser) -> Unit) {

    private lateinit var fileChooser: FileChooser
    private lateinit var directoryChooser: DirectoryChooser
    internal var file: File? = null
    internal var directory: File? = null
    internal var types: Array<String> = arrayOf()

    init {
        windowsChooser(this)
    }

    internal fun fileSelectDialog(stage: Stage? = null, title: String? = null, run: () -> Unit) {
        initialise(stage, title)
        file = createFileChooser()
        check(!hasBeenCancelled()) { "No file selected" }
        check(checkMimeType()) { "Invalid file type" }
        run()
    }

    internal fun directorySelectDialog(stage: Stage? = null, title: String? = null, run: () -> Unit) {
        directory = createDirectoryChooser()
        initialise(stage, title)
        run()
    }

    private fun hasBeenCancelled(): Boolean {
        if (file == null) {
            return true
        }
        return false
    }

    private fun checkMimeType(): Boolean {
        if (types.isNotEmpty()) {
            types.forEach { type ->
                if (file?.extension == type) {
                    return true
                }
            }
        }
        Toast.makeText(stage!!, "Invalid file type!")
        return false
    }

    private fun createDirectoryChooser(): File? {
        directoryChooser = DirectoryChooser()
        directoryChooser.title = title
        return directoryChooser.showDialog(stage)
    }

    private fun createFileChooser(): File? {
        fileChooser = FileChooser()
        fileChooser.title = this.title
        return fileChooser.showOpenDialog(stage)
    }

    private fun initialise(stage: Stage?, title: String?) {
        if (stage != null) {
            this.stage = stage
        }
        if (title != null) {
            this.title = title
        }
    }
}