package com.astrainteractive.astrasync.api

import com.astrainteractive.astrasync.api.entities.*
import com.astrainteractive.astrasync.utils.EmpireConfig
import org.bukkit.entity.Player
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction

val Player.databaseName: String
    get() = name.uppercase()

object Repository {

    val Player.daoID: EntityID<Int>?
        get() = transaction {
            DBPlayer.find(Players.minecraftName eq databaseName).firstOrNull()?.id
        }

    fun getPlayerInfo(player: Player): DomainPlayer? {
        return transaction {
            val fullPlayer = FullPlayer.find(Players.minecraftName eq player.databaseName).firstOrNull()
            fullPlayer?.toDomain(player)
        }
    }

    suspend fun savePlayerInfo(player: Player) {
        val playerID = savePlayer(player).value
        transaction { InventoryItems.deleteWhere { InventoryItems.playerID eq playerID } }
        saveInventory(player, playerID)
        saveEffects(player, playerID)
    }

    private fun savePlayer(player: Player) = transaction {
        val playerID = player.daoID
        val block: DBPlayer.() -> Unit = {
            this.minecraftName = player.databaseName
            this.experience = player.totalExperience
            this.lastServerName = EmpireConfig.serverID
            this.health = player.health
            this.foodLevel = player.foodLevel
        }
        playerID?.let(DBPlayer::findById)?.apply(block)?.id ?: DBPlayer.new(block).id
    }

    private fun saveInventory(player: Player, playerID: Int) = transaction {
        InventoryItems.batchInsert(player.inventory.contents?.toList() ?: emptyList()) {
            this[InventoryItems.playerID] = playerID
            this[InventoryItems.item] = ExposedBlob(Serializer.serializeItem(it))
        }
    }

    private fun saveEffects(player: Player, playerID: Int) = transaction {
        PotionEffects.batchInsert(player.activePotionEffects.filterNotNull()) {
            this[InventoryItems.playerID] = playerID
            this[PotionEffects.potionEffect] = ExposedBlob(Serializer.serializeItem(it))
        }
    }
}

