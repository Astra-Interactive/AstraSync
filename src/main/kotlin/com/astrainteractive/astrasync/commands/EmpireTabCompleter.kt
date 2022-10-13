package com.astrainteractive.astrasync.commands


import CommandManager
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.utils.registerTabCompleter


fun CommandManager.tabCompleter() = AstraLibs.registerTabCompleter("syncserver") { sender, args ->
    // TODO
//    if (args.isEmpty())
//        return@registerTabCompleter BungeeController.servers.toList().withEntry(args.last())
//    if (args.size == 1)
//        return@registerTabCompleter BungeeController.servers.toList().withEntry(args.last())
    return@registerTabCompleter listOf<String>()
}



