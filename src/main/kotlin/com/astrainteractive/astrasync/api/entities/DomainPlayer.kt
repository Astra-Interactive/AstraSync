package com.astrainteractive.astrasync.api.entities

import com.astrainteractive.astrasync.api.Serializer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class DomainPlayer(
    val player: Player,
    val experience: Int,
    val lastServerName: String,
    val foodLevel:Int,
    val health:Double,
    val items: List<ItemStack>,
    val potionEffect: List<org.bukkit.potion.PotionEffect>,
)

fun FullPlayer.toDomain(player: Player): DomainPlayer {
    return DomainPlayer(
        player = player,
        experience = experience,
        lastServerName = lastServerName,
        foodLevel = foodLevel,
        health = health,
        items = items.mapNotNull { Serializer.deserializeItem(it.item.bytes) },
        potionEffect = potionEffect.mapNotNull { Serializer.deserializeItem(it.potionEffect.bytes) }
    )
}