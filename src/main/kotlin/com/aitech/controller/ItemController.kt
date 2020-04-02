package com.aitech.controller

import com.aitech.model.Entry
import com.aitech.model.EntryModel
import com.aitech.model.EntryTable
import com.aitech.model.EntryTable.id
import com.aitech.model.toEntry
import com.aitech.util.execute
import com.aitech.util.toDate
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.chart.PieChart
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update
import tornadofx.Controller
import tornadofx.asObservable
import tornadofx.singleAssign
import java.math.BigDecimal
import java.time.LocalDate

class ItemController : Controller() {

    //    val items: ObservableList<EntryModel> = mutableListOf<EntryModel>().asObservable()
    private val itemsFromDb: ObservableList<EntryModel> = execute {
        EntryTable.selectAll().map {
            EntryModel().apply {
                item = it.toEntry()
            }
        }.asObservable()
    }

    var items: ObservableList<EntryModel> by singleAssign()
    val pieItems: ObservableList<PieChart.Data> = FXCollections.observableArrayList<PieChart.Data>()

    init {
        items = itemsFromDb
        items.forEach {
            pieItems.add(PieChart.Data(it.name.value, it.price.value.toDouble()))
        }
    }

    fun add(date: LocalDate, name: String, price: Double): Entry {
        val entry: InsertStatement<Number> = execute {
            EntryTable.insert {
                it[entryDate] = LocalDate.now().toDate()
                it[entryName] = name
                it[entryPrice] = BigDecimal(5.0)
            }
        }
        itemsFromDb.add(EntryModel().apply {
            item = Entry(entry[EntryTable.id], date, name, price)
        })
        pieItems.add(PieChart.Data(name, price))
        return Entry(entry[id], date, name, price)
    }

    fun update(model: EntryModel): Int {
        return execute {
            EntryTable.update({ id eq model.id.value.toInt() }) {
                it[entryDate] = model.date.value.toDate()
                it[entryName] = model.name.value
                it[entryPrice] = BigDecimal.valueOf(model.price.value.toDouble())
            }
        }
    }

    fun delete(entry: EntryModel) {
        execute {
            EntryTable.deleteWhere {
                id eq entry.id.value.toInt()
            }
        }
        itemsFromDb.remove(entry)
        removeFromChart(entry)
    }

    private fun removeFromChart(entry: EntryModel) {
        var index = 0
        pieItems.forEachIndexed {idx, data ->
            if (data.name == entry.name.value && idx != -1) {
                index = idx
            }
        }
        pieItems.removeAt(index)
    }

    fun updateChart(entry: EntryModel) {
        val id = entry.id
        var index = 0
        items.forEachIndexed { idx, data ->
            if (id == data.id) {
                index = idx
                pieItems[index].name = data.name.value
                pieItems[index].pieValue = data.price.value.toDouble()
            }
        }
    }

}