package com.astrainteractive.astrasync.api.messaging

import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astrasync.utils.Translation
import github.scarsz.discordsrv.DiscordSRV
import github.scarsz.discordsrv.dependencies.jda.api.entities.User
import github.scarsz.discordsrv.util.WebhookUtil
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object BungeeMessageListener {
    fun onGetServersReceived(it: BungeeMessage.GetServers) {
        BungeeController.rememberServers(it.servers)
        it.servers.forEach {
            BungeeController.requestServerPlayers(it)
        }
    }

    fun onPlayerListReceived(it: BungeeMessage.PlayerList) {
        it.serverAndPlayers.forEach { (server, players) ->
            BungeeController.rememberPlayers(server, players)
        }
    }

    fun onGetServerReceived(it: BungeeMessage.GetServer) {
        BungeeController.rememberServer(it.server)
    }

    fun onMinecraftMessage(player: Player, _message: String, discord: Boolean = false) {
        val message = Translation.messageFormat.replace("%message%", _message).replace("%player%", player.name)
        BungeeController.sendMessageToAllPlayers(message, player)
        if (discord)
            BungeeController.sendDiscordMessage(player, _message)
    }

    fun onSendDiscordMessageRequest(it: BungeeMessage.SendDiscordMessage) {
        val discordSRV = (Bukkit.getPluginManager().getPlugin("DiscordSRV") as? DiscordSRV?)
        AsyncHelper.launch {
            discordSRV?.mainTextChannel?.sendMessage(it.message)?.queue()
            val offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(it.playerUUID))
            discordSRV?.let { discordSRV ->
                WebhookUtil.deliverMessage(
                    discordSRV.mainTextChannel,
                    offlinePlayer,
                    offlinePlayer.name ?: "Who?",
                    it.message,
                    null
                )
            }

        }
    }

    fun onDiscordMessage(author: User, contentRaw: String) {
        BungeeController.sendMessageToAllPlayers(
            Translation.fromDiscordMessageFormat.replace("%player%", author.name)
                .replace("%message%", contentRaw),
        )
    }
}