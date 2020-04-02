package com.aitech.view

import com.aitech.model.EntryModel
import tornadofx.*

class EntryForm() : View("My View") {

    private val model: EntryModel by inject()

    override val root = form {
            fieldset {
                field("Date") {
                    maxWidth = 220.0
                    datepicker(model.date) {
                        required()
                        validator {
                            when {
                                it?.dayOfMonth.toString().isEmpty()
                                        || it?.dayOfMonth.toString().isNullOrEmpty()
                                        || it?.dayOfYear.toString().isEmpty() -> error("Date cannot be blank")
                                else -> null
                            }
                        }
                    }
                }
            }
            fieldset {
                field("Name") {
                    maxWidth = 220.0
                    textfield(model.name) {
                        required()
                        validator {
                            when {
                                it.isNullOrEmpty() -> error("Name cannot be empty")
                                else -> null
                            }
                        }
                    }
                }
            }
            fieldset {
                field("Price") {
                    maxWidth = 220.0
                    textfield(model.price) {
                        required()
                        filterInput {
                            it.controlNewText.isDouble() || it.controlNewText.isInt()
                        }
                        validator {
                            when (it) {
                                null -> error("Price cannot be empty")
                                else -> null
                            }
                        }
                    }
                }
            }
        }
}
