package com.astrainteractive.astrasync.utils.providers

import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astraclans.domain.entities.PlayerDSL
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.vendors.MariaDBDialect
import org.jetbrains.exposed.sql.vendors.MysqlDialect
import ru.astrainteractive.astralibs.di.IModule

object DatabaseModule : IModule<Database>() {
    private val config: PluginConfig
        get() = ConfigProvider.value

    private val host: String
        get() = config.mysql.host
    private val port: String
        get() = config.mysql.port
    private val login: String
        get() = config.mysql.login
    private val password: String
        get() = config.mysql.password
    private val name: String
        get() = config.mysql.name
    private val driver: String
        get() = config.mysql.driver

    override fun initializer(): Database {
        val db = Database.connect(
            "jdbc:mysql://$host:$port/$name",
            driver = driver,
            user = login, password = password
        )
        println("jdbc:mysql://$host:$port/$name")
        println("password: $password login: $login driver: $driver")

        Database.registerDialect("mariadb") { MariaDBDialect() }
        Database.registerDialect("mysql") { MysqlDialect() }

        transaction {
            val tables = buildList {
                add(PlayerDSL)
            }
            tables.forEach(SchemaUtils::create)
            tables.forEach(SchemaUtils::createMissingTablesAndColumns)
        }
        return db
    }

}