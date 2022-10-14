package com.astrainteractive.astraclans.domain.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

/**
 * Игрок
 */
object PlayerDSL : IntIdTable() {
    val minecraftUUID: Column<String> = varchar("minecraft_name",128).uniqueIndex()
    val experience: Column<Int> = integer("experience")
    val health: Column<Double> = double("health")
    val foodLevel: Column<Int> = integer("foodLevel")
    val lastServerName: Column<String> = varchar("last_server_name", 32)
    val items: Column<String> = text("items")
    val enderChestItems: Column<String> = text("ender_chest")
    val effects: Column<String> = text("effects")
}

class PlayerDAO(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, PlayerDAO>(PlayerDSL)

    var minecraftUUID by PlayerDSL.minecraftUUID
    var experience by PlayerDSL.experience
    var health by PlayerDSL.health
    var foodLevel by PlayerDSL.foodLevel
    var lastServerName by PlayerDSL.lastServerName
    var items by PlayerDSL.items
    var enderChestItems by PlayerDSL.enderChestItems
    var effects by PlayerDSL.effects
}

