package javafx.views.builders

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import mindf.ddata.generator.DGenerator
import org.controlsfx.glyphfont.FontAwesome
import tornadofx.*
import tornadofx.controlsfx.toGlyph
import javafx.utils.Toast
import javafx.utils.Tools.Companion.wait
import javafx.utils.WindowsChooser
import java.awt.Desktop
import java.io.File

class MainBuilder : View() {

    override val root = BorderPane()
    private lateinit var packageText: TextField
    private lateinit var script: File
    private lateinit var directory: File
    private lateinit var packagePath: String

    init {
        with(root) {
            title = "DData generator"
            style {
                backgroundColor += c("#F2F2F2")
            }

            paddingTop = 2
            paddingBottom = 2
            paddingLeft = 16
            top {
                hbox(4) {
                    paddingBottom = 8
                    alignment = Pos.CENTER_RIGHT
                    button(graphic = FontAwesome.Glyph.MINUS.toGlyph()) {
                        action {
                           primaryStage.isIconified = true
                        }
                    }
                    button(graphic = FontAwesome.Glyph.CLOSE.toGlyph()) {
                        action {
                            close()
                        }
                    }
                }
            }
            center {
                vbox {
                    style {
                        borderColor += box(all = c("#444"))
                    }
                    paddingAll = 16
                    form {
                        fieldset {
                            //field("Package path")//todo add this on need
                            field("Select the creation directory") {
                                button("Directory") {
                                    action {
                                        WindowsChooser(primaryStage) {
                                            it.directorySelectDialog {
                                                directory = it.directory!!
                                            }
                                        }
                                    }
                                }
                            }
                            field("Select the sql script") {
                                button("Script") {
                                    action {
                                        WindowsChooser(primaryStage) {
                                            it.types = arrayOf("txt", "sql")
                                            it.fileSelectDialog {
                                                script = it.file!!
                                            }
                                        }
                                    }
                                }
                            }
                            field("Optional package name") {
                                packageText = textfield {
                                    promptText = "package"
                                }
                            }
                            field("Execute generation") {
                                button("run") {
                                    action {
                                        if (!::directory.isInitialized) {
                                            Toast.makeText(primaryStage, "No directory selected!")
                                        } else {
                                            if (!::script.isInitialized) {
                                                Toast.makeText(primaryStage, "No script selected!")
                                            } else {
                                                DGenerator(script, directory, packageText.text) {
                                                    wait(
                                                        { Platform.runLater { Toast.makeText(primaryStage, "Data are generated!", Color.GREEN) } }
                                                        , 2000
                                                    ) {
                                                        Platform.runLater {
                                                            close()
                                                            if (Desktop.isDesktopSupported()) {
                                                                Desktop.getDesktop().open(File(directory.path + "\\generateDData"))
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            bottom {
                paddingAll = 8
            }
        }
    }
}
