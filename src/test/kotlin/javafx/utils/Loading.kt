package javafx.utils

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.Scene
import javafx.scene.control.ProgressBar
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration

class Loading {

    companion object {
        fun makeLoadingModal(
            currentStage: Stage,
            displayTime: Int = 500,
            fadeInDelay: Int = 500,
            fadeOutDelay: Int = 500,
            opacity: Double = 5.0
        ) {
            //todo refactor this code
            val toastStage = Stage()
            toastStage.initOwner(currentStage)
            toastStage.isResizable = false
            toastStage.initStyle(StageStyle.TRANSPARENT)
            val progressBar = ProgressBar()

            val root = StackPane(progressBar)
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