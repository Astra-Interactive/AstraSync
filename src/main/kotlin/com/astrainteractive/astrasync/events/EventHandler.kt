package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.events.DSLEvent
import com.astrainteractive.astrasync.api.Controller
import kotlinx.coroutines.*
import org.bukkit.Bukkit
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
import java.util.UUID

class EventHandler {

    val onPlayerJoin = DSLEvent.event(PlayerJoinEvent::class.java) { e ->
        EventController.loadPlayer(e.player)
    }
    val worldSaveEvent = DSLEvent.event(WorldSaveEvent::class.java) { e ->
        GlobalScope.launch { EventController.saveAllPlayers() }
    }

    val onPlayerLeave = DSLEvent.event(PlayerQuitEvent::class.java) { e ->
        EventController.onPlayerLeave(e.player)
    }
    val onMove = DSLEvent.event(PlayerMoveEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized(e.player)
    }

    val onDamage = DSLEvent.event(EntityDamageEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized(e.entity as? Player)
    }
    val inventoryOpenEvent = DSLEvent.event(InventoryOpenEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized(e.player as? Player)
    }
    val dropItemEvent = DSLEvent.event(PlayerDropItemEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized(e.player)
    }
    val pickUpItemEvent = DSLEvent.event(EntityPickupItemEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized((e.entity as? Player))
    }
    val onPlayerInteract = DSLEvent.event(PlayerInteractEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized(e.player)
    }
    val onBlockPlace = DSLEvent.event(BlockPlaceEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized(e.player)
    }
    val onBlockBreak = DSLEvent.event(BlockBreakEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized(e.player)
    }
    val onInventoryClick = DSLEvent.event(InventoryClickEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerInitialized(e.whoClicked as? Player)
    }
    val onPlayerDeath = DSLEvent.event(PlayerDeathEvent::class.java) { e ->
        if (!EventController.isPlayerInitialized(e.player)) {
            e.isCancelled = true
            e.drops.clear()
        }
    }
}
