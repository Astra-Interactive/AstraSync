package com.astrainteractive.astrasync.commands

import CommandManager
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.commands.AstraDSLCommand
import com.astrainteractive.astrasync.AstraSync
import com.astrainteractive.astrasync.events.BungeeUtil
import com.astrainteractive.astrasync.events.EventController
import com.astrainteractive.astrasync.utils.Translation
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Reload command handler
 */

/**
 * This function called only when atempreload being called
 *
 * Here you should also check for permission
 */
fun CommandManager.reload() = AstraDSLCommand.command("atempreload") {
    sender.sendMessage(Translation.reload)
    AstraSync.instance.reloadPlugin()
    sender.sendMessage(Translation.reloadComplete)
}


fun CommandManager.syncServer() = AstraDSLCommand.command("syncserver") {
    sender.sendMessage(Translation.pleaseWait)
    sender.sendMessage(Translation.inventoryLossWarning)
    val player = sender as? Player
    player ?: run {
        return@command
    }
    val server = getArgumentOrNull(0) ?: run {
        return@command
    }

    EventController.savePlayer(player) {
        BungeeUtil.sendBungeeMessage( "BungeeCord", "Connect", server,player)
    }
}






