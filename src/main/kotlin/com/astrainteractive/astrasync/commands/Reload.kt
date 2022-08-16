package com.astrainteractive.astrasync.commands

import CommandManager
import com.astrainteractive.astralibs.commands.AstraDSLCommand
import com.astrainteractive.astrasync.AstraSync
import com.astrainteractive.astrasync.utils.Translation

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






