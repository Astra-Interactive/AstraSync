package com.astrainteractive.astrasync.commands

import CommandManager
import com.astrainteractive.astrasync.AstraSync
import com.astrainteractive.astrasync.api.messaging.BungeeController
import com.astrainteractive.astrasync.events.EventController
import com.astrainteractive.astrasync.modules.TranslationProvider
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.di.getValue
import ru.astrainteractive.astralibs.utils.registerCommand

/**
 * Reload command handler
 */

/**
 * This function called only when atempreload being called
 *
 * Here you should also check for permission
 */
fun CommandManager.reload() = AstraLibs.registerCommand("atempreload") { sender, args ->
    val translation by TranslationProvider
    sender.sendMessage(translation.reload)
    AstraSync.instance.reloadPlugin()
    sender.sendMessage(translation.reloadComplete)
}


fun CommandManager.syncServer() = AstraLibs.registerCommand("syncserver") {sender, args ->
    val translation by TranslationProvider
    sender.sendMessage(translation.pleaseWait)
    sender.sendMessage(translation.inventoryLossWarning)
    val player = sender as? Player
    player ?: run {
        sender.sendMessage(translation.onlyPlayerCommand)
        return@registerCommand
    }
    val server = args.getOrNull(0) ?: run {
        sender.sendMessage(translation.inputServerName)
        return@registerCommand
    }
    EventController.savePlayer(player).invokeOnCompletion {
        BungeeController.connectPlayerToServer(server, player)
    }
}






