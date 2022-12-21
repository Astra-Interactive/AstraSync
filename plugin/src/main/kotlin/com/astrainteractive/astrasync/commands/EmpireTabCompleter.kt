package com.astrainteractive.astrasync.commands


import CommandManager
import com.astrainteractive.astrasync.modules.ConfigProvider
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.di.getValue
import ru.astrainteractive.astralibs.utils.registerTabCompleter
import ru.astrainteractive.astralibs.utils.withEntry


fun CommandManager.tabCompleter() = AstraLibs.registerTabCompleter("syncserver") { sender, args ->
    val config by ConfigProvider
    if (args.isEmpty())
        return@registerTabCompleter config.serverIDList.withEntry(args.last())
    if (args.size == 1)
        return@registerTabCompleter config.serverIDList.withEntry(args.last())
    return@registerTabCompleter listOf<String>()
}