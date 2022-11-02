package com.astrainteractive.astraclans.domain.datasource

import com.astrainteractive.astraclans.domain.dto.PlayerDTO

interface IRemoteDataSource {
    fun update(playerDTO: PlayerDTO): PlayerDTO
    fun insert(playerDTO: PlayerDTO): PlayerDTO
    fun select(uuid: String): PlayerDTO?
    fun delete(uuid: String): Int
}