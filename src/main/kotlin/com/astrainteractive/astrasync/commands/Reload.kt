package com.astrainteractive.astrasync.commands

import CommandManager
import com.astrainteractive.astrasync.AstraSync
import com.astrainteractive.astrasync.utils.providers.TranslationProvider
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.commands.AstraDSLCommand

/**
 * Reload command handler
 */

/**
 * This function called only when atempreload being called
 *
 * Here you should also check for permission
 */
fun CommandManager.reload() = AstraDSLCommand.command("atempreload") {
    sender.sendMessage(TranslationProvider.value.reload)
    AstraSync.instance.reloadPlugin()
    sender.sendMessage(TranslationProvider.value.reloadComplete)
}


fun CommandManager.syncServer() = AstraDSLCommand.command("syncserver") {
    sender.sendMessage(TranslationProvider.value.pleaseWait)
    sender.sendMessage(TranslationProvider.value.inventoryLossWarning)
    val player = sender as? Player
    player ?: run {
        return@command
    }
    val server = getArgumentOrNull(0) ?: run {
        return@command
    }
    //TODO
//    EventController.savePlayer(player) {
//        BungeeController.connectPlayerToServer(server, player)
//    }
}






