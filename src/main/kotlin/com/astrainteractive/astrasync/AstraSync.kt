package com.astrainteractive.astrasync

import CommandManager
import com.astrainteractive.astrasync.events.EventController
import com.astrainteractive.astrasync.events.EventHandler
import com.astrainteractive.astrasync.utils.Files
import com.astrainteractive.astrasync.utils.providers.DatabaseModule
import com.astrainteractive.astrasync.utils.providers.TranslationProvider
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
     * Class for handling all of your events
     */
    private lateinit var eventHandler: EventHandler

    /**
     * Command manager for your commands.
     *
     * You can create multiple managers.
     */
    private lateinit var commandManager: CommandManager


    /**
     * This method called when server starts or PlugMan load plugin.
     */
    override fun onEnable() {
        AstraLibs.rememberPlugin(this)
        Logger.prefix = "AstraSync"
        eventHandler = EventHandler()
        commandManager = CommandManager()
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
        DatabaseModule.value
    }

}


