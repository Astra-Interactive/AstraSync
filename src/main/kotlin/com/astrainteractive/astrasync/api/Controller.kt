package com.astrainteractive.astrasync.api

import com.astrainteractive.astralibs.utils.catching
import com.astrainteractive.astrasync.api.entities.*
import com.astrainteractive.astrasync.utils.EmpireConfig
import org.bukkit.entity.Player
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction


object Controller {
    private val Player.databaseName: String
        get() = name.uppercase()

    fun getPlayerInfo(player: Player): DomainPlayer? = catching(true) {
        transaction {
            val fullPlayer = FullPlayer.find(Players.minecraftName eq player.databaseName).firstOrNull()
            fullPlayer?.toDomain(player)
        }
    }

    fun <T> estimate(msg: String, block: () -> T): T {
        val started = System.currentTimeMillis()
        val result = block()
        val time = System.currentTimeMillis() - started
        println(msg + "${time / 1000.0}")
        return result
    }

    suspend fun saveFullPlayer(player: Player, clear: Boolean = false) = catching(true) {
        transaction {
            savePlayer(player, clear).value
        }
    }

    private fun savePlayer(player: Player, clear: Boolean = false): EntityID<Int> {
        val block: DBPlayer.() -> Unit = {
            this.minecraftName = player.databaseName
            this.experience = player.totalExperience
            this.lastServerName = EmpireConfig.serverID
            this.health = player.health
            this.foodLevel = player.foodLevel
            this.items = Serializer.toBase64(if (clear) emptyList() else player.inventory.contents.toList())
            this.enderChestItems = Serializer.toBase64(if (clear) emptyList() else player.enderChest.contents.toList())
            this.effects = Serializer.toBase64(if (clear) emptyList() else player.activePotionEffects.filterNotNull())
        }
        return DBPlayer.find(Players.minecraftName eq player.databaseName).firstOrNull()?.apply(block)?.id
            ?: DBPlayer.new(block).id
    }
}
