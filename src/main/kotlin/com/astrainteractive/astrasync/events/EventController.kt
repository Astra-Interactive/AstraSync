package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astrasync.api.Controller
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashSet

object EventController {
    private val unInitializedPlayer = HashSet<UUID>()

    @Synchronized
    fun rememberInitializedPlayer(player: Player) = unInitializedPlayer.add(player.uniqueId)

    @Synchronized
    fun setPlayerInitialized(player: Player) = unInitializedPlayer.remove(player.uniqueId)

    @Synchronized
    fun isPlayerInitialized(player: Player?) = unInitializedPlayer.contains(player?.uniqueId)
    fun loadPlayer(player: Player) {
        rememberInitializedPlayer(player)
        val playerDomain = Controller.getPlayerInfo(player) ?: return
        player.inventory.contents = playerDomain.items.toTypedArray()
        player.totalExperience = playerDomain.experience
        player.health = playerDomain.health
        player.foodLevel = playerDomain.foodLevel
        playerDomain.potionEffect.forEach {
            player.addPotionEffect(it)
        }
        setPlayerInitialized(player)
    }

    fun onPlayerLeave(player: Player) {
        if (!isPlayerInitialized(player)) return
        AsyncHelper.launch {
            Controller.saveFullPlayer(player)
            setPlayerInitialized(player)
        }
    }

    suspend fun saveAllPlayers() = coroutineScope {
        Bukkit.getOnlinePlayers().filter { isPlayerInitialized(it) }.map {
            async {
                Controller.saveFullPlayer(it)
            }
        }.awaitAll()
    }

}