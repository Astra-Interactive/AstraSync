package com.astrainteractive.astrasync.api

import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astrasync.api.entities.*
import com.astrainteractive.astrasync.utils.EmpireConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.vendors.MariaDBDialect
import org.jetbrains.exposed.sql.vendors.MysqlDialect

class AstraDatabase {
    val host: String
        get() = EmpireConfig.mysql.host
    val port: String
        get() = EmpireConfig.mysql.port
    val login: String
        get() = EmpireConfig.mysql.login
    val password: String
        get() = EmpireConfig.mysql.password
    val name: String
        get() = EmpireConfig.mysql.name
    val driver: String
        get() = EmpireConfig.mysql.driver

    val db = Database.connect(
        "jdbc:mysql://$host:$port/$name",
        driver = driver,
        user = login, password = password
    )
    init {
        Logger.log("jdbc:mysql://$host:$port/$name")
        Logger.log("password: $password login: $login driver: $driver")
        Database.registerDialect("mariadb") { MariaDBDialect() }
        Database.registerDialect("mysql") { MysqlDialect() }
        transaction {
            val tables = buildList {
                add(Players)
            }
            tables.forEach(SchemaUtils::create)
            tables.forEach(SchemaUtils::createMissingTablesAndColumns)
        }
    }
}