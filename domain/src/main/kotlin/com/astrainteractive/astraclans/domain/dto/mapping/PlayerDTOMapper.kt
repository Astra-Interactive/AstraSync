package com.astrainteractive.astraclans.domain.dto.mapping

import com.astrainteractive.astraclans.domain.dto.PlayerDTO
import com.astrainteractive.astraclans.domain.entities.PlayerDAO

interface IPlayerDTOMapper : Mapper<PlayerDAO, PlayerDTO>, ExposedMapper<PlayerDAO, PlayerDTO>
object PlayerDTOMapper : IPlayerDTOMapper {
    override fun toDTO(it: PlayerDAO): PlayerDTO {
        return PlayerDTO(
            minecraftUUID = it.minecraftUUID,
            totalExperience = it.experience,
            health = it.health,
            foodLevel = it.foodLevel,
            lastServerName = it.lastServerName,
            items = it.items,
            enderChestItems = it.enderChestItems,
            effects = it.effects
        )
    }

    override fun toExposed(it: PlayerDTO): PlayerDAO.() -> Unit  = {
        this.minecraftUUID = it.minecraftUUID
        this.experience = it.totalExperience
        this.health = it.health
        this.foodLevel = it.foodLevel
        this.lastServerName = it.lastServerName
        this.items = it.items
        this.enderChestItems = it.enderChestItems
        this.effects = it.effects
    }
}