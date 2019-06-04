package javafx.utils

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration

class Toast {

    companion object {
        fun makeText(
            stage: Stage,
            message: String,
            color: Color = Color.RED,
            displayTime: Int = 500,
            fadeInDelay: Int = 500,
            fadeOutDelay: Int = 500,
            size: Double = 15.0,
            opacity: Double = 5.0
        ) {
            //todo refactor this code
            val toastStage = Stage()
            toastStage.initOwner(stage)
            toastStage.isResizable = false
            toastStage.initStyle(StageStyle.TRANSPARENT)

            val text = Text(message)
            text.font = Font.font("Verdana", size)
            text.fill = color

            val root = StackPane(text)
            root.style = "-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 50px;"
            root.opacity = opacity

            val scene = Scene(root)
            scene.fill = Color.TRANSPARENT
            toastStage.scene = scene
            toastStage.show()

            val fadeInTimeline = Timeline()
            val fadeInKey1 =
                KeyFrame(Duration.millis(fadeInDelay.toDouble()), KeyValue(toastStage.scene.root.opacityProperty(), 1))
            fadeInTimeline.keyFrames.add(fadeInKey1)
            fadeInTimeline.setOnFinished {
                Thread {
                    try {
                        Thread.sleep(displayTime.toLong())
                    } catch (e: InterruptedException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }

                    val fadeOutTimeline = Timeline()
                    val fadeOutKey1 =
                        KeyFrame(
                            Duration.millis(fadeOutDelay.toDouble()),
                            KeyValue(toastStage.scene.root.opacityProperty(), 0)
                        )
                    fadeOutTimeline.keyFrames.add(fadeOutKey1)
                    fadeOutTimeline.setOnFinished { toastStage.close() }
                    fadeOutTimeline.play()
                }.start()
            }
            fadeInTimeline.play()
        }
    }
}
