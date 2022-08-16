package com.astrainteractive.astrasync.api.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

/**
 * Инвентарь
 */
object InventoryItems : IntIdTable() {
    val playerID = reference("player_id", Players)
    val item: Column<ExposedBlob> = blob("item_stack")
}

class InventoryItem(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, InventoryItem>(InventoryItems)
    var playerID by InventoryItems.playerID
    var item by InventoryItems.item
}

/**
 * Эффекты
 */
object PotionEffects : IntIdTable() {
    val playerID = reference("player_id", Players)
    val potionEffect: Column<ExposedBlob> = blob("potion_effect")
}

class PotionEffect(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, PotionEffect>(PotionEffects)

    var playerID by PotionEffects.playerID
    var potionEffect by PotionEffects.potionEffect
}



