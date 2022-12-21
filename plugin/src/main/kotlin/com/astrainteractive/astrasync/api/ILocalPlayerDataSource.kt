package com.astrainteractive.astrasync.api

import org.bukkit.entity.Player

interface ILocalPlayerDataSource {
    enum class TYPE {
        EXIT, ENTER, SAVE_ALL, DEATH
    }

    fun savePlayer(player: Player, type: TYPE)
}