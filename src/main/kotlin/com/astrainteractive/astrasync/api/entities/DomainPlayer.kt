package com.astrainteractive.astrasync.api.entities

import com.astrainteractive.astrasync.api.Serializer
import com.astrainteractive.astrasync.utils.EmpireConfig
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class DomainPlayer(
    val player: Player,
    val experience: Int,
    val lastServerName: String,
    val foodLevel: Int,
    val health: Double,
    val items: List<ItemStack?>,
    val enderChestItems: List<ItemStack?>,
    val potionEffect: List<org.bukkit.potion.PotionEffect>,
){
    companion object{
        fun fromPlayer(player: Player) = DomainPlayer(
            player = player,
            experience = player.totalExperience,
            lastServerName = EmpireConfig.serverID,
            foodLevel =  player.foodLevel,
            health = player.health,
            items = player.inventory.contents?.toList()?: emptyList(),
            enderChestItems = player.enderChest.contents?.toList()?: emptyList(),
            potionEffect = player.activePotionEffects.toList()
        )
    }
}

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