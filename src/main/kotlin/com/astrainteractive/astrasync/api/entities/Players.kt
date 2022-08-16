package com.astrainteractive.astrasync.api.entities

import com.astrainteractive.astrasync.api.Serializer
import com.astrainteractive.astrasync.api.databaseName
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

/**
 * Игрок
 */
object Players : IntIdTable() {
    val minecraftName:Column<String> = varchar("minecraft_name",32).uniqueIndex()
    val experience: Column<Int> = integer("experience")
    val health: Column<Double> = double("health")
    val foodLevel: Column<Int> = integer("foodLevel")
    val lastServerName: Column<String> = varchar("last_server_name", 32)
}

class DBPlayer(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, DBPlayer>(Players)
    var minecraftName by Players.minecraftName
    var experience by Players.experience
    var health by Players.health
    var foodLevel by Players.foodLevel
    var lastServerName by Players.lastServerName
}

