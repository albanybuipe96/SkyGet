package com.aitech.style

import tornadofx.Stylesheet
import tornadofx.cssclass

class Styles : Stylesheet() {

    companion object {
        val heading by cssclass()
    }

    init {
        label and heading {

        }
    }

}