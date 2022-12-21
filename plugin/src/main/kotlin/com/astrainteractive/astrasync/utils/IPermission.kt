package com.astrainteractive.astrasync.utils

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface IPermission {
    val value: String
    fun hasPermission(player: CommandSender) = player.hasPermission(value)


    fun permissionSize(player: Player) = player.effectivePermissions
        .firstOrNull { it.permission.startsWith(value) }
        ?.permission
        ?.replace("$value.", "")
        ?.toIntOrNull()
}