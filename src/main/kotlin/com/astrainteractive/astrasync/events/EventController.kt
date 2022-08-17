package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astrasync.api.Controller
import com.astrainteractive.astrasync.utils.Translation
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashSet

object EventController {
    private val lockedPlayers = HashSet<UUID>()

    @Synchronized
    fun lockPlayer(player: Player) = lockedPlayers.add(player.uniqueId)

    @Synchronized
    fun unlockPlayer(player: Player) = lockedPlayers.remove(player.uniqueId)

    @Synchronized
    fun isPlayerLocked(player: Player?) = lockedPlayers.contains(player?.uniqueId)
    fun loadPlayer(player: Player) = AsyncHelper.launch {
        if (isPlayerLocked(player)) return@launch
        lockPlayer(player)
        val playerDomain = Controller.getPlayerInfo(player) ?: run {
            player.kickPlayer(Translation.errorOccurredInLoading)
            unlockPlayer(player)
            return@launch
        }
        AsyncHelper.callSyncMethod {
            player.inventory.contents = playerDomain.items.toTypedArray()
            player.totalExperience = playerDomain.experience
            player.health = playerDomain.health
            player.foodLevel = playerDomain.foodLevel
            player.enderChest.contents = playerDomain.enderChestItems.toTypedArray()
            playerDomain.potionEffect.forEach {
                player.addPotionEffect(it)
            }
        }?.get()
        unlockPlayer(player)
    }

    fun savePlayer(player: Player, clear: Boolean = false, onSaved: () -> Unit = {}) = AsyncHelper.launch {
        if (isPlayerLocked(player)) return@launch
        lockPlayer(player)
        Controller.saveFullPlayer(player, clear)?.let {
            AsyncHelper.callSyncMethod(onSaved)
        }?:run{
            player.sendMessage(Translation.errorOccurredInSaving)
        }
        unlockPlayer(player)
    }

    fun clearPlayer(player: Player) = AsyncHelper.launch {
        if (isPlayerLocked(player)) return@launch
        lockPlayer(player)
        Controller.saveFullPlayer(player)
        unlockPlayer(player)
    }

    fun saveAllPlayers() = AsyncHelper.launch {
        Bukkit.getOnlinePlayers().map {
            async {
                savePlayer(it)
            }
        }.awaitAll()
    }

}