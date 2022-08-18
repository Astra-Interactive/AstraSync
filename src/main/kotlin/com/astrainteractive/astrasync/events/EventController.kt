package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.FileManager
import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.utils.catching
import com.astrainteractive.astrasync.api.Controller
import com.astrainteractive.astrasync.api.Controller.databaseName
import com.astrainteractive.astrasync.api.entities.DomainPlayer
import com.astrainteractive.astrasync.utils.Translation
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object EventController {
    private val lockedPlayers = HashSet<UUID>()

    @Synchronized
    fun lockPlayer(player: Player) = lockedPlayers.add(player.uniqueId)

    @Synchronized
    fun unlockPlayer(player: Player) = lockedPlayers.remove(player.uniqueId)

    @Synchronized
    fun isPlayerLocked(player: Player?) = lockedPlayers.contains(player?.uniqueId)
    fun loadPlayer(player: Player) = AsyncHelper.launch {
        AsyncHelper.launch { savePlayerToTemp(player, false) }
        if (isPlayerLocked(player)) return@launch
        lockPlayer(player)
        val playerDomain = Controller.getPlayerInfo(player) ?: run {
            AsyncHelper.callSyncMethod {
                player.kickPlayer(Translation.errorOccurredInLoading)
                unlockPlayer(player)
            }
            return@launch
        }
        AsyncHelper.callSyncMethod {
            catching(true) { player.inventory.contents = playerDomain.items.toTypedArray() }
            catching { player.totalExperience = playerDomain.experience }
            catching { player.health = playerDomain.health }
            catching { player.foodLevel = playerDomain.foodLevel }
            catching(true) { player.enderChest.contents = playerDomain.enderChestItems.toTypedArray() }
            catching(true) {
                playerDomain.potionEffect.forEach {
                    player.addPotionEffect(it)
                }
            }
        }?.get()
        unlockPlayer(player)
    }

    fun savePlayer(player: Player, clear: Boolean = false, onSaved: () -> Unit = {}) = AsyncHelper.launch {
        AsyncHelper.launch { savePlayerToTemp(player,true) }
        if (isPlayerLocked(player)) return@launch
        lockPlayer(player)
        Controller.saveFullPlayer(player, clear)?.let {
            AsyncHelper.callSyncMethod(onSaved)
        } ?: run {
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

    fun savePlayerToTemp(player: Player, onLogin: Boolean) {
        val prefix = if (onLogin) "login" else "exit"
        val name = "temp/${player.databaseName}/${prefix}_${System.currentTimeMillis()}.yml"
        val fileManager = FileManager(name)
        val config = fileManager.getConfig()
        DomainPlayer.fromPlayer(player).also {
            config.set("player.items", it.items)
            config.set("player.enderchest", it.enderChestItems)
        }
        fileManager.saveConfig()
    }

}