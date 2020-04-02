package com.aitech.view

import com.aitech.config.Config
import com.aitech.controller.ItemController
import com.aitech.model.EntryTable
import com.aitech.util.createTable
import com.aitech.util.execute
import com.aitech.util.log
import com.aitech.util.toDate
import javafx.scene.control.TabPane
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime
import tornadofx.Workspace
import java.math.BigDecimal
import java.time.LocalDate

class SkySpace : Workspace("SkyGet", NavigationMode.Tabs) {

    init {
        // 1. Database configs
        log()
        Database.connect(url = Config.url, driver = Config.driver)
        createTable()

        // 2. Controller configs
        ItemController()

        // 3. Doc app views
        dock<MainView>()

        tabContainer.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
    }

}