package com.astrainteractive.astrasync.events

import com.astrainteractive.astrasync.api.LocalPlayerDataSource
import kotlinx.coroutines.runBlocking
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
import ru.astrainteractive.astralibs.events.DSLEvent

class EventHandler {
    private fun isPlayerLocked(player: Player?) = runBlocking {
        EventController.locker.isLocked(player?.uniqueId)
    }

    val onPlayerJoin = DSLEvent.event(PlayerJoinEvent::class.java) { e ->
        EventController.loadPlayer(e.player)
    }


    val worldSaveEvent = DSLEvent.event(WorldSaveEvent::class.java) { e ->
        EventController.saveAllPlayers()
    }

    val onPlayerLeave = DSLEvent.event(PlayerQuitEvent::class.java) { e ->
        EventController.savePlayer(e.player)
    }
    val onMove = DSLEvent.event(PlayerMoveEvent::class.java) { e ->
        if (isPlayerLocked(e.player))
            e.isCancelled = true
    }

    val onDamage = DSLEvent.event(EntityDamageEvent::class.java) { e ->
        if (isPlayerLocked(e.entity as? Player))
            e.isCancelled = true
    }
    val inventoryOpenEvent = DSLEvent.event(InventoryOpenEvent::class.java) { e ->
        if (isPlayerLocked(e.player as? Player))
            e.isCancelled = true
    }
    val dropItemEvent = DSLEvent.event(PlayerDropItemEvent::class.java) { e ->
        if (isPlayerLocked(e.player))
            e.isCancelled = true
    }
    val pickUpItemEvent = DSLEvent.event(EntityPickupItemEvent::class.java) { e ->
        if (isPlayerLocked((e.entity as? Player)))
            e.isCancelled = true
    }
    val onPlayerInteract = DSLEvent.event(PlayerInteractEvent::class.java) { e ->
        if (isPlayerLocked(e.player))
            e.isCancelled = true
    }
    val onBlockPlace = DSLEvent.event(BlockPlaceEvent::class.java) { e ->
        if (isPlayerLocked(e.player))
            e.isCancelled = true
    }
    val onBlockBreak = DSLEvent.event(BlockBreakEvent::class.java) { e ->
        if (isPlayerLocked(e.player))
            e.isCancelled = true
    }
    val onInventoryClick = DSLEvent.event(InventoryClickEvent::class.java) { e ->
        if (isPlayerLocked(e.whoClicked as? Player))
            e.isCancelled = true
    }
    val onPlayerDeath = DSLEvent.event(PlayerDeathEvent::class.java) { e ->
        if (isPlayerLocked(e.player)) {
            e.isCancelled = true
            e.drops.clear()
        } else EventController.savePlayer(e.player, LocalPlayerDataSource.TYPE.DEATH)
    }

}