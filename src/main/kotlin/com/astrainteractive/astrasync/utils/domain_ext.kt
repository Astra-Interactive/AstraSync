package com.astrainteractive.astrasync.utils

import com.astrainteractive.astraclans.domain.dto.PlayerDTO
import com.astrainteractive.astraclans.domain.exception.DomainException
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import ru.astrainteractive.astralibs.utils.uuid
import java.util.*


fun Player.toDTO(): PlayerDTO {
    return PlayerDTO(
        minecraftUUID = uuid,
        totalExperience = totalExperience,
        health = health,
        foodLevel = foodLevel,
        lastServerName = "",
        items = Serializer.encodeList(inventory.contents.filterNotNull()),
        enderChestItems = Serializer.encodeList(enderChest.contents.filterNotNull()),
        effects = Serializer.encodeList(activePotionEffects.filterNotNull()),
    )
}

fun PlayerDTO.fromDTO(): Player {
    val player = Bukkit.getPlayer(UUID.fromString(this.minecraftUUID)) ?: throw DomainException.PlayerNotFoundException
    player.totalExperience = totalExperience
    player.health = health
    player.foodLevel = foodLevel
    player.inventory.contents = Serializer.decodeList<ItemStack>(items).toTypedArray()
    player.enderChest.contents = Serializer.decodeList<ItemStack>(enderChestItems).toTypedArray()
    player.activePotionEffects.map { player.removePotionEffect(it.type) }
    Serializer.decodeList<PotionEffect>(effects).map(player::addPotionEffect)
    return player
}