package com.astrainteractive.astrasync.api.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FullPlayer(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, FullPlayer>(Players)

    var playerID by Players.id
    var minecraftName by Players.minecraftName
    var experience by Players.experience
    var lastServerName by Players.lastServerName
    var foodLevel by Players.foodLevel
    var health by Players.health

    var items by Players.items
    var potionEffects by Players.effects
    var enderChestItems by Players.enderChestItems
}