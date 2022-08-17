package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.events.DSLEvent
import com.astrainteractive.astralibs.utils.catching
import com.astrainteractive.astrasync.utils.Translation
import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams
import kotlinx.coroutines.launch
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
import org.bukkit.plugin.messaging.PluginMessageListener

class EventHandler {

    val onPlayerJoin = DSLEvent.event(PlayerJoinEvent::class.java) { e ->
        EventController.loadPlayer(e.player)
        BungeeUtil.requestServersUpdate()
        AsyncHelper.launch { BungeeUtil.broadcast(Translation.onJoinFormat.replace("%player%",e.player.name)) }
    }
    val worldSaveEvent = DSLEvent.event(WorldSaveEvent::class.java) { e ->
        EventController.saveAllPlayers()
    }

    val onPlayerLeave = DSLEvent.event(PlayerQuitEvent::class.java) { e ->
        EventController.savePlayer(e.player)
        BungeeUtil.requestServersUpdate()
        AsyncHelper.launch { EventController.savePlayerToTemp(e.player,false) }
        AsyncHelper.launch { BungeeUtil.broadcast(Translation.onLeaveFormat.replace("%player%",e.player.name)) }
    }
    val onMove = DSLEvent.event(PlayerMoveEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked(e.player)
    }

    val onDamage = DSLEvent.event(EntityDamageEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked(e.entity as? Player)
    }
    val inventoryOpenEvent = DSLEvent.event(InventoryOpenEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked(e.player as? Player)
    }
    val dropItemEvent = DSLEvent.event(PlayerDropItemEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked(e.player)
    }
    val pickUpItemEvent = DSLEvent.event(EntityPickupItemEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked((e.entity as? Player))
    }
    val onPlayerInteract = DSLEvent.event(PlayerInteractEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked(e.player)
    }
    val onBlockPlace = DSLEvent.event(BlockPlaceEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked(e.player)
    }
    val onBlockBreak = DSLEvent.event(BlockBreakEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked(e.player)
    }
    val onInventoryClick = DSLEvent.event(InventoryClickEvent::class.java) { e ->
        e.isCancelled = EventController.isPlayerLocked(e.whoClicked as? Player)
    }
    val onPlayerDeath = DSLEvent.event(PlayerDeathEvent::class.java) { e ->
        if (EventController.isPlayerLocked(e.player)) {
            e.isCancelled = true
            e.drops.clear()
        } else EventController.savePlayer(e.player, true)
    }


    val messageEvent = DSLEvent.event(PlayerChatEvent::class.java) { e ->
        AsyncHelper.launch {
            BungeeUtil.requestServersUpdate()
            if (BungeeUtil.currentServer == null)
                BungeeUtil.requestServerUpdate()
            BungeeUtil.broadcast(Translation.messageFormat.replace("%message%",e.message).replace("%player%",e.player.name),e.player)
        }
    }


    init {
        BungeeUtil.requestServersUpdate()
        BungeeUtil.requestServerUpdate()
    }
}