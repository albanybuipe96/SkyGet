package com.aitech.app

import com.aitech.style.Styles
import com.aitech.view.SkySpace
import javafx.stage.Stage
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus
import tornadofx.reloadViewsOnFocus

class MainApp : App(SkySpace::class, Styles::class) {
    init {
        reloadViewsOnFocus()
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        with(stage) {
            width = 1200.0
            height = 800.0
        }
        super.start(stage)
    }
}