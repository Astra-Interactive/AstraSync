package com.astrainteractive.astrasync.utils

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


sealed class AstraPermission(val value: String) {
    object Reload : AstraPermission("astra_template.reload")
    object Damage : AstraPermission("astra_template.damage")
    fun hasPermission(player: CommandSender) = player.hasPermission(value)


    fun permissionSize(player: Player) = player.effectivePermissions
        .firstOrNull { it.permission.startsWith(value) }
        ?.permission
        ?.replace("$value.", "")
        ?.toIntOrNull()
}