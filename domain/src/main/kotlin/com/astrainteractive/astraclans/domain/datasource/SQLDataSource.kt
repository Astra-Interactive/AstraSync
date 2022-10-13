package com.astrainteractive.astraclans.domain.datasource

import com.astrainteractive.astraclans.domain.dto.PlayerDTO
import com.astrainteractive.astraclans.domain.dto.mapping.PlayerDTOMapper
import com.astrainteractive.astraclans.domain.entities.PlayerDAO
import com.astrainteractive.astraclans.domain.entities.PlayerDSL
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object SQLDataSource {
    fun update(playerDTO: PlayerDTO) = transaction {
        delete(playerDTO.minecraftUUID)
        insert(playerDTO)
    }

    private fun insert(playerDTO: PlayerDTO): PlayerDTO {
        return PlayerDAO.new(PlayerDTOMapper.toExposed(playerDTO)).let(PlayerDTOMapper::toDTO)
    }

    fun select(uuid: String): PlayerDTO? = transaction {
        PlayerDAO.find(PlayerDSL.minecraftUUID eq uuid).firstOrNull()?.let(PlayerDTOMapper::toDTO)
    }


    private fun delete(uuid: String): Int {
        return PlayerDSL.deleteWhere { PlayerDSL.minecraftUUID eq uuid }
    }
}