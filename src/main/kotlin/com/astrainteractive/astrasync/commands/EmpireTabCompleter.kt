package com.astrainteractive.astrasync.commands


import CommandManager
import com.astrainteractive.astrasync.modules.ConfigProvider
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.utils.registerTabCompleter
import ru.astrainteractive.astralibs.utils.withEntry


fun CommandManager.tabCompleter() = AstraLibs.registerTabCompleter("syncserver") { sender, args ->
    // TODO
    val serverList = ConfigProvider.value
    if (args.isEmpty())
        return@registerTabCompleter serverList.serverIDList.withEntry(args.last())
    if (args.size == 1)
        return@registerTabCompleter serverList.serverIDList.withEntry(args.last())
    return@registerTabCompleter listOf<String>()
}



