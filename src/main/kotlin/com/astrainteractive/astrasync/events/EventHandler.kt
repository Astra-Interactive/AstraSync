package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.events.DSLEvent
import com.astrainteractive.astrasync.api.Repository
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID


class EventHandler {
    val unInitializedPlayer = HashSet<UUID>()
    val onPlayerJoin = DSLEvent.event(PlayerJoinEvent::class.java) { e ->
        unInitializedPlayer.add(e.player.uniqueId)
        val playerDomain = Repository.getPlayerInfo(e.player) ?: return@event
        e.player.inventory.contents = playerDomain.items.toTypedArray()
        e.player.totalExperience = playerDomain.experience
        e.player.health = playerDomain.health
        e.player.foodLevel = playerDomain.foodLevel
        playerDomain.potionEffect.forEach {
            e.player.addPotionEffect(it)
        }
        unInitializedPlayer.remove(e.player.uniqueId)
    }

    val onPlayerLeave = DSLEvent.event(PlayerQuitEvent::class.java) { e ->
        AsyncHelper.launch {
            Repository.savePlayerInfo(e.player)
        }
        unInitializedPlayer.remove(e.player.uniqueId)
    }
    val onMove = DSLEvent.event(PlayerMoveEvent::class.java) { e ->
        if (unInitializedPlayer.contains(e.player.uniqueId)) e.isCancelled = true
    }

    val onDamage = DSLEvent.event(EntityDamageEvent::class.java) { e ->
        val player = (e.entity as? Player) ?: return@event
        if (unInitializedPlayer.contains(player.uniqueId)) e.isCancelled = true
    }
}
