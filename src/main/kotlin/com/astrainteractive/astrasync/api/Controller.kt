package com.astrainteractive.astrasync.api

import com.astrainteractive.astrasync.api.entities.*
import com.astrainteractive.astrasync.utils.EmpireConfig
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction


object Controller {
    private val Player.databaseName: String
        get() = name.uppercase()

    fun getPlayerInfo(player: Player): DomainPlayer? {
        return transaction {
            val fullPlayer = FullPlayer.find(Players.minecraftName eq player.databaseName).firstOrNull()
            fullPlayer?.toDomain(player)
        }
    }

    suspend fun saveFullPlayer(player: Player) {
        val playerID = savePlayer(player).value
        transaction {
            InventoryItems.deleteWhere { InventoryItems.playerID eq playerID }
            EnderChestInventoryItems.deleteWhere { EnderChestInventoryItems.playerID eq playerID }
        }
        transaction {
            saveInventory(player, playerID)
            saveEnderInventory(player, playerID)
            saveEffects(player, playerID)
        }
    }

    private fun savePlayer(player: Player) = transaction {
        val playerID = DBPlayer.find(Players.minecraftName eq player.databaseName).firstOrNull()?.id
        val block: DBPlayer.() -> Unit = {
            this.minecraftName = player.databaseName
            this.experience = player.totalExperience
            this.lastServerName = EmpireConfig.serverID
            this.health = player.health
            this.foodLevel = player.foodLevel
        }
        playerID?.let(DBPlayer::findById)?.apply(block)?.id ?: DBPlayer.new(block).id
    }

    private fun saveInventory(player: Player, playerID: Int) {
        InventoryItems.batchInsert(player.inventory.contents?.toList() ?: emptyList()) {
            this[InventoryItems.playerID] = playerID
            this[InventoryItems.item] = ExposedBlob(Serializer.serializeItem(it))
        }
    }

    private fun saveEnderInventory(player: Player, playerID: Int) {
        InventoryItems.batchInsert(player.enderChest.contents?.toList() ?: emptyList()) {
            this[InventoryItems.playerID] = playerID
            this[InventoryItems.item] = ExposedBlob(Serializer.serializeItem(it))
        }
    }

    private fun saveEffects(player: Player, playerID: Int) {
        PotionEffects.batchInsert(player.activePotionEffects.filterNotNull()) {
            this[InventoryItems.playerID] = playerID
            this[PotionEffects.potionEffect] = ExposedBlob(Serializer.serializeItem(it))
        }
    }
}

