package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.events.DSLEvent
import com.astrainteractive.astrasync.api.messaging.BungeeController
import com.astrainteractive.astrasync.api.messaging.BungeeMessageListener
import com.astrainteractive.astrasync.utils.Translation
import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*
import org.bukkit.event.world.WorldSaveEvent

class EventHandler {

    val onPlayerJoin = DSLEvent.event(PlayerJoinEvent::class.java) { e ->
        EventController.loadPlayer(e.player)
        BungeeController.requestServersUpdate()
        AsyncHelper.launch {
            BungeeMessageListener.onMinecraftMessage(e.player, Translation.onJoinFormat)
        }
    }
    val worldSaveEvent = DSLEvent.event(WorldSaveEvent::class.java) { e ->
        EventController.saveAllPlayers()
    }

    val onPlayerLeave = DSLEvent.event(PlayerQuitEvent::class.java) { e ->
        EventController.savePlayer(e.player)
        BungeeController.requestServersUpdate()
        AsyncHelper.launch { EventController.savePlayerToTemp(e.player, false) }
        AsyncHelper.launch {
            BungeeMessageListener.onMinecraftMessage(e.player, Translation.onLeaveFormat)
        }
    }
    val onMove = DSLEvent.event(PlayerMoveEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.player))
            e.isCancelled = true
    }

    val onDamage = DSLEvent.event(EntityDamageEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.entity as? Player))
            e.isCancelled = true
    }
    val inventoryOpenEvent = DSLEvent.event(InventoryOpenEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.player as? Player))
            e.isCancelled = true
    }
    val dropItemEvent = DSLEvent.event(PlayerDropItemEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.player))
            e.isCancelled = true
    }
    val pickUpItemEvent = DSLEvent.event(EntityPickupItemEvent::class.java) { e ->
        if (EventController.isPlayerLocked((e.entity as? Player)))
            e.isCancelled = true
    }
    val onPlayerInteract = DSLEvent.event(PlayerInteractEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.player))
            e.isCancelled = true
    }
    val onBlockPlace = DSLEvent.event(BlockPlaceEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.player))
            e.isCancelled = true
    }
    val onBlockBreak = DSLEvent.event(BlockBreakEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.player))
            e.isCancelled = true
    }
    val onInventoryClick = DSLEvent.event(InventoryClickEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.whoClicked as? Player))
            e.isCancelled = true
    }
    val onPlayerDeath = DSLEvent.event(PlayerDeathEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.player)) {
            e.isCancelled = true
            e.drops.clear()
        } else EventController.savePlayer(e.player, true)
    }


    val messageEvent = DSLEvent.event(PlayerChatEvent::class.java) { e ->
        AsyncHelper.launch {
            BungeeController.requestServersUpdate()
            if (BungeeController.currentServer == null)
                BungeeController.requestServerUpdate()
            BungeeMessageListener.onMinecraftMessage(e.player, e.message, true)
        }
    }


    init {
        BungeeController.requestServersUpdate()
        BungeeController.requestServerUpdate()
    }
}