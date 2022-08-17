package com.astrainteractive.astrasync

import CommandManager
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.events.GlobalEventManager
import com.astrainteractive.astrasync.api.AstraDatabase
import com.astrainteractive.astrasync.api.Controller
import com.astrainteractive.astrasync.events.EventController
import com.astrainteractive.astrasync.events.EventHandler
import com.astrainteractive.astrasync.utils.PluginTranslation
import com.astrainteractive.astrasync.utils._EmpireConfig
import com.astrainteractive.astrasync.utils._Files
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

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

    private val database by lazy {
        AstraDatabase()
    }

    /**
     * This method called when server starts or PlugMan load plugin.
     */
    override fun onEnable() {
        AstraLibs.rememberPlugin(this)
        Logger.prefix = "AstraSync"
        PluginTranslation()
        _Files()
        _EmpireConfig.kotlinxSerializaion()
        database.toString()
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
        onDisable()
        onEnable()
    }

}


