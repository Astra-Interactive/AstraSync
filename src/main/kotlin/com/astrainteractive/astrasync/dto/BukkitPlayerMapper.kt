package com.astrainteractive.astrasync.dto

import com.astrainteractive.astraclans.domain.dto.PlayerDTO
import com.astrainteractive.astraclans.domain.exception.DomainException
import com.astrainteractive.astrasync.utils.Serializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import ru.astrainteractive.astralibs.domain.mapping.IMapper
import ru.astrainteractive.astralibs.utils.uuid
import java.util.*

interface IBukkitPlayerMapper : IMapper<Player, PlayerDTO>

object BukkitPlayerMapper:IBukkitPlayerMapper{
    override fun toDTO(it: Player): PlayerDTO = PlayerDTO(
        minecraftUUID = it.uuid,
        totalExperience = it.totalExperience,
        health = it.health,
        foodLevel = it.foodLevel,
        lastServerName = "",
        items = Serializer.encodeList(it.inventory.contents.filterNotNull()),
        enderChestItems = Serializer.encodeList(it.enderChest.contents.filterNotNull()),
        effects = Serializer.encodeList(it.activePotionEffects.filterNotNull()),
    )

    override fun fromDTO(it: PlayerDTO): Player {
        val player = Bukkit.getPlayer(UUID.fromString(it.minecraftUUID)) ?: throw DomainException.PlayerNotFoundException
        player.totalExperience = it.totalExperience
        player.health = it.health
        player.foodLevel = it.foodLevel
        player.inventory.contents = Serializer.decodeList<ItemStack>(it.items).toTypedArray()
        player.enderChest.contents = Serializer.decodeList<ItemStack>(it.enderChestItems).toTypedArray()
        player.activePotionEffects.map { player.removePotionEffect(it.type) }
        Serializer.decodeList<PotionEffect>(it.effects).map(player::addPotionEffect)
        return player
    }

}