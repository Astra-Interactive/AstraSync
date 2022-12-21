package com.astrainteractive.astrasync

import CommandManager
import com.astrainteractive.astrasync.api.messaging.BungeeController
import com.astrainteractive.astrasync.api.messaging.models.BungeeMessage
import com.astrainteractive.astrasync.events.EventController
import com.astrainteractive.astrasync.events.EventHandler
import com.astrainteractive.astrasync.modules.ConfigProvider
import com.astrainteractive.astrasync.modules.DatabaseModule
import com.astrainteractive.astrasync.modules.Files
import com.astrainteractive.astrasync.modules.TranslationProvider
import kotlinx.coroutines.runBlocking
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.Logger
import ru.astrainteractive.astralibs.events.GlobalEventManager

/**
 * Initial class for your plugin
 */
class AstraSync : JavaPlugin() {
    companion object {
        lateinit var instance: AstraSync
    }

    init {
        instance = this
    }


    /**
     * This method called when server starts or PlugMan load plugin.
     */
    override fun onEnable() {
        AstraLibs.rememberPlugin(this)
        Logger.prefix = "AstraSync"
        reloadPlugin()
        DatabaseModule.value
        EventHandler()
        CommandManager()
        BungeeController.registerChannel(BungeeMessage.BUNGEE_CHANNEL)
    }

    /**
     * This method called when server is shutting down or when PlugMan disable plugin.
     */
    override fun onDisable() {
        runBlocking { EventController.saveAllPlayers() }
        HandlerList.unregisterAll(this)
        GlobalEventManager.onDisable()
    }

    /**
     * As it says, function for plugin reload
     */
    fun reloadPlugin() {
        Files.configFile.reload()
        TranslationProvider.reload()
        ConfigProvider.reload()
    }

}


