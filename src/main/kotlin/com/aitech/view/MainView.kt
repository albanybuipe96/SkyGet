package com.aitech.view

import com.aitech.controller.ItemController
import com.aitech.model.EntryModel
import javafx.geometry.Pos
import tornadofx.*

class MainView : View("SkyGet") {

    private val model = EntryModel()
    private val controller = find(ItemController::class)
    private var table: TableViewEditModel<EntryModel> by singleAssign()

    override val root = borderpane {
        center = vbox(spacing = 10.0) {
            form {
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
                } // date field
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
                } // name field
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
                } // price field
                fieldset {
                    hbox(spacing = 10.0) {
                        button("Add") {
                            enableWhen(model.valid)
                            action {
                                model.commit()
                                addEntry()
                                println("adding entry to db")
                                model.rollback()
                            }
                        } // add button
                        button("Delete") {
                            action {
                                when (val selected = table.tableView.selectedItem) {
                                    null -> return@action
                                    else -> {
                                        controller.delete(selected)
                                        // TODO("recalculate total")
                                    }
                                }
                            }
                        } // delete button
                        button("Reset") {
                            enableWhen(model.dirty)
                            action {
                                // TODO("reset form")
                            }
                        } // reset button
                    }
                }
            } // entry form
            tableview<EntryModel> {
                items = controller.items
                table = editModel
                column("Id", EntryModel::id)
                column("Added", EntryModel::date).makeEditable()
                column("Entry", EntryModel::name).makeEditable()
                column("Cost", EntryModel::price).makeEditable()

                onEditCommit {
                    // TODO("update db")
                    controller.update(it)
                    // TODO("recalculate total cost")
                    // TODO("update chart")
                }
            }
        } // center [of border pane]

        right = vbox(spacing = 10.0) {
            alignment = Pos.CENTER
            piechart("Budget Summary") {
                data = controller.pieItems
            }
        }
    }.apply {
        style {
            padding = box(20.px)
        }
    }

    private fun addEntry() {
        controller.add(model.date.value, model.name.value, model.price.value.toDouble())
        // TODO("recalculate total")
    }
}