package com.aitech.util

import org.joda.time.DateTime
import java.time.*

fun DateTime.toLocalDate2(): LocalDate = LocalDate.of(this.year, this.monthOfYear, this.dayOfMonth)

fun LocalDate.toDate(): DateTime {
    return DateTime(this.year, this.monthValue, this.dayOfMonth, 0, 0, 0)
}
