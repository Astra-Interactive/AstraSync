package com.astrainteractive.astrasync.api.messaging

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

object BungeeMessageService : PluginMessageListener {
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != "BungeeCord") return
        val bungeeResponse = BungeeResponse.decode(message)
        Logger.log("onPluginMessageReceived: ${BungeeResponse.decode(message)}", consolePrint = true)
        when (bungeeResponse.action) {
            BungeeMessage.Messages.GET_SERVERS.value -> {
                val servers = bungeeResponse.message.joinToString("").split(", ").map { it.trim() }
                println("Servers: $servers")
                BungeeMessageListener.onGetServersReceived(BungeeMessage.GetServers(servers))
            }
            BungeeMessage.Messages.PLAYER_LIST.value -> {
                val server = bungeeResponse.message[0]
                val players = bungeeResponse.message.getOrNull(1)?.split(", ") ?: emptyList()
                BungeeMessageListener.onPlayerListReceived(BungeeMessage.PlayerList(mapOf(server to players)))
            }
            BungeeMessage.Messages.GET_SERVER.value -> bungeeResponse.message.firstOrNull()
                ?.let { BungeeMessageListener.onGetServerReceived(BungeeMessage.GetServer(it)) }

            BungeeMessage.Messages.SEND_DISCORD_MESSAGE.value -> {
                BungeeMessage.SendDiscordMessage.decode(bungeeResponse.message.joinToString(" "))
                    ?.let(BungeeMessageListener::onSendDiscordMessageRequest)
            }
        }
    }

    fun remember() {
        Bukkit.getServer().messenger.registerIncomingPluginChannel(
            AstraLibs.instance,
            BungeeMessage.Messages.BUNGEE_CHANNEL.value,
            BungeeMessageService
        )
    }

    fun forget() {
        Bukkit.getServer().messenger.unregisterIncomingPluginChannel(
            AstraLibs.instance,
            BungeeMessage.Messages.BUNGEE_CHANNEL.value,
            BungeeMessageService
        )
    }

}



