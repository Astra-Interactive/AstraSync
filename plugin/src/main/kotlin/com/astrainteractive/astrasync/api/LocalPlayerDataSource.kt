package com.astrainteractive.astrasync.api

import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.file_manager.FileManager

class LocalPlayerDataSource : ILocalPlayerDataSource {


    override fun savePlayer(player: Player, type: ILocalPlayerDataSource.TYPE) {
        val name = "temp/${player.name}/${type.name}_${System.currentTimeMillis()}.yml"
        val fileManager = FileManager(name)
        val config = fileManager.fileConfiguration
        config.set("player.items", player.inventory.contents)
        config.set("player.enderchest", player.enderChest.contents)
        fileManager.save()
    }
}