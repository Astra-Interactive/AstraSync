package com.astrainteractive.astrasync.events

import com.astrainteractive.astraclans.domain.exception.DomainException
import com.astrainteractive.astrasync.api.ILocalPlayerDataSource
import com.astrainteractive.astrasync.api.messaging.BungeeController
import com.astrainteractive.astrasync.dto.BukkitPlayerMapper
import com.astrainteractive.astrasync.modules.LocalDataSourceModule
import com.astrainteractive.astrasync.modules.RemoteDataSourceModule
import com.astrainteractive.astrasync.modules.uuidLockerModule
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.async.BukkitMain
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.di.getValue
import ru.astrainteractive.astralibs.utils.uuid
import java.util.*


object EventController {
    private val locker by uuidLockerModule
    private val sqlDataSource by RemoteDataSourceModule
    private val localDataSource by LocalDataSourceModule
    fun isPlayerLocked(player: Player?) = runBlocking {
        locker.isLocked(player?.uniqueId)
    }

    private inline fun <reified T> withLock(uuid: UUID, crossinline block: suspend CoroutineScope.() -> T) =
        PluginScope.launch(Dispatchers.IO) {
            if (locker.isLocked(uuid)) throw DomainException.PlayerLockedException
            locker.lock(uuid)
            val result = block.invoke(this)
            locker.unlock(uuid)
            result
        }

    fun loadPlayer(player: Player) = withLock(player.uniqueId) {
        withContext(Dispatchers.IO) { localDataSource.savePlayer(player, ILocalPlayerDataSource.TYPE.ENTER) }
        val playerDTO = sqlDataSource.select(player.uuid) ?: return@withLock
        withContext(Dispatchers.BukkitMain) { BukkitPlayerMapper.fromDTO(playerDTO) }
    }

    fun savePlayer(player: Player, type: ILocalPlayerDataSource.TYPE = ILocalPlayerDataSource.TYPE.EXIT) =
        withLock(player.uniqueId) {
            localDataSource.savePlayer(player, type)
            val playerDTO = BukkitPlayerMapper.toDTO(player)
            sqlDataSource.update(playerDTO)
        }

    fun saveAllPlayers() = PluginScope.launch(Dispatchers.IO) {
        Bukkit.getOnlinePlayers().map {
            async {
                savePlayer(it, ILocalPlayerDataSource.TYPE.SAVE_ALL)
            }
        }.awaitAll()
    }

    fun changeServer(player: Player, server: String) =
        withLock(player.uniqueId) {
            localDataSource.savePlayer(player, ILocalPlayerDataSource.TYPE.EXIT)
            val playerDTO = BukkitPlayerMapper.toDTO(player)
            sqlDataSource.update(playerDTO)
            BungeeController.connectPlayerToServer(server, player)
        }
}