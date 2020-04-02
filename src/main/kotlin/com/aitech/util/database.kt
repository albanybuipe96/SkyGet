package com.aitech.util

import com.aitech.model.EntryTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

private var LOG_TO_CONSOLE: Boolean = false

fun transaction() : Transaction = TransactionManager.currentOrNew(Connection.TRANSACTION_SERIALIZABLE).apply {
        if (LOG_TO_CONSOLE) addLogger(StdOutSqlLogger)
    }

fun log() {
    LOG_TO_CONSOLE = true
}

fun createTable() {
    with(transaction()) {
        SchemaUtils.create(EntryTable)
    }
}

fun <T> execute(command: () -> T) : T {
    with(transaction()) {
        return command().apply {
            commit()
            close()
        }
    }
}