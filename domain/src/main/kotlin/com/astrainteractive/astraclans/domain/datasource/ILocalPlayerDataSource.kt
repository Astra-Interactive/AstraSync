package com.astrainteractive.astraclans.domain.datasource

import java.io.File

interface ILocalPlayerDataSource<PLAYER, ITEM_STACK> {
    enum class TYPE {
        EXIT, ENTER, SAVE_ALL, DEATH
    }

    fun savePlayer(player: PLAYER, type: TYPE)
    fun loadPlayerSaves(player: PLAYER): List<File>

    fun readPlayerInventorySave(player: PLAYER, file: File): List<ITEM_STACK>
    fun readPlayerEnderChestSave(player: PLAYER, file: File): List<ITEM_STACK>
}
