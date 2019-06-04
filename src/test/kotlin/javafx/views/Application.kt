package javafx.views

import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus
import javafx.views.builders.MainBuilder
import javafx.views.styles.Style

class Application : App(MainBuilder::class, Style::class) {

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.TRANSPARENT)
        stage.isResizable = false
        //Thread.setDefaultUncaughtExceptionHandler(BaseExceptionHandler())
        super.start(stage)
    }

    init {
        reloadStylesheetsOnFocus()
    }
}
