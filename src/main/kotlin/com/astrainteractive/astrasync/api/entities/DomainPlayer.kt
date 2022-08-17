package com.astrainteractive.astrasync.api.entities

import com.astrainteractive.astrasync.api.Serializer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class DomainPlayer(
    val player: Player,
    val experience: Int,
    val lastServerName: String,
    val foodLevel: Int,
    val health: Double,
    val items: List<ItemStack>,
    val enderChestItems: List<ItemStack>,
    val potionEffect: List<org.bukkit.potion.PotionEffect>,
)

fun FullPlayer.toDomain(player: Player): DomainPlayer {
    return DomainPlayer(
        player = player,
        experience = experience,
        lastServerName = lastServerName,
        foodLevel = foodLevel,
        health = health,
        items = Serializer.fromBase64(items) ?: emptyList(),
        enderChestItems = Serializer.fromBase64(enderChestItems) ?: emptyList(),
        potionEffect = Serializer.fromBase64(potionEffects) ?: emptyList()
    )
}