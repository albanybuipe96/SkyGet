package com.aitech.model

import com.aitech.model.EntryTable.entryDate
import com.aitech.model.EntryTable.entryName
import com.aitech.model.EntryTable.entryPrice
import com.aitech.model.EntryTable.id
import com.aitech.util.toLocalDate2
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime
import tornadofx.ItemViewModel
import tornadofx.getValue
import java.math.BigDecimal
import java.time.LocalDate

fun ResultRow.toEntry() = Entry(
    this[id],
    this[entryDate].toLocalDate2(),
    this[entryName],
    this[entryPrice].toDouble()
)

object EntryTable : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val entryDate: Column<DateTime> = date("entry_date")
    val entryName: Column<String> = varchar("entry_name", length = 50)
    val entryPrice: Column<BigDecimal> = decimal("entry_price", precision = 9, scale = 2)
}

class Entry(id: Int, date: LocalDate, name: String, price: Double) {
    val idProperty = SimpleIntegerProperty(id)
    private val id by idProperty

    val dateProperty = SimpleObjectProperty(date)
    private val date by dateProperty

    val nameProperty = SimpleStringProperty(name)
    private val name by nameProperty

    val priceProperty = SimpleDoubleProperty(price)
    private val price by priceProperty

    override fun toString(): String {
        return "ID: $id, NAME: $name, PRICE: $price, DATE: $date"
    }
}

class EntryModel : ItemViewModel<Entry>() {

    val id: SimpleIntegerProperty = bind { item?.idProperty }
    val name: SimpleStringProperty = bind { item?.nameProperty }
    val date: SimpleObjectProperty<LocalDate> = bind { item?.dateProperty }
    val price: SimpleDoubleProperty = bind { item?.priceProperty }

}