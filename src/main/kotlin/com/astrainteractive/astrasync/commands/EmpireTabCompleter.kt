package com.astrainteractive.astrasync.commands


import CommandManager
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.utils.registerTabCompleter
import com.astrainteractive.astralibs.utils.withEntry
import com.astrainteractive.astrasync.api.messaging.BungeeController


fun CommandManager.tabCompleter() = AstraLibs.registerTabCompleter("syncserver") { sender, args ->
    if (args.isEmpty())
        return@registerTabCompleter BungeeController.servers.toList().withEntry(args.last())
    if (args.size == 1)
        return@registerTabCompleter BungeeController.servers.toList().withEntry(args.last())
    return@registerTabCompleter listOf<String>()
}



