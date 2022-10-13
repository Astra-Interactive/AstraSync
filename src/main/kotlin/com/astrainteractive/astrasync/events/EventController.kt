package com.astrainteractive.astrasync.events

import com.astrainteractive.astraclans.domain.datasource.SQLDataSource
import com.astrainteractive.astraclans.domain.exception.DomainException
import com.astrainteractive.astrasync.api.LocalPlayerDataSource
import com.astrainteractive.astrasync.utils.Locker
import com.astrainteractive.astrasync.utils.fromDTO
import com.astrainteractive.astrasync.utils.toDTO
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.utils.uuid
import java.util.*


object EventController {
    val locker = Locker<UUID>()
    private val sqlDataSource = SQLDataSource
    private val localDataSource = LocalPlayerDataSource

    val writerDispatcher = Dispatchers.IO.limitedParallelism(1)

    private inline fun <reified T> withLock(uuid: UUID, crossinline block: suspend CoroutineScope.() -> T) =
        PluginScope.launch {
            if (locker.isLocked(uuid)) throw DomainException.PlayerLockedException
            locker.lock(uuid)
            val result = block.invoke(this)
            locker.unlock(uuid)
            result
        }

    fun loadPlayer(player: Player) = withLock(player.uniqueId) {
        localDataSource.savePlayer(player, LocalPlayerDataSource.TYPE.ENTER)
        val playerDTO = sqlDataSource.select(player.uuid) ?: throw DomainException.PlayerDataNotExists
        playerDTO.fromDTO()
    }

    fun savePlayer(player: Player, type: LocalPlayerDataSource.TYPE = LocalPlayerDataSource.TYPE.EXIT) =
        withLock(player.uniqueId) {
            localDataSource.savePlayer(player, type)
            val playerDTO = player.toDTO()
            sqlDataSource.update(playerDTO)
        }

    fun saveAllPlayers() = PluginScope.launch {
        Bukkit.getOnlinePlayers().map {
            async {
                savePlayer(it, LocalPlayerDataSource.TYPE.SAVE_ALL)
            }
        }.awaitAll()
    }
}