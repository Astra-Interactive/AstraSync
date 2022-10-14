package com.astrainteractive.astrasync.api.messaging


import com.astrainteractive.astrasync.api.messaging.models.BungeeMessage
import com.astrainteractive.astrasync.api.messaging.models.BungeeMessageData
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import ru.astrainteractive.astralibs.Logger

abstract class BungeeMessageService : PluginMessageListener {
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != "BungeeCord") return
        val bungeeResponse = BungeeDecoder.decode(message)
        Logger.log("onPluginMessageReceived: ${BungeeDecoder.decode(message)}", consolePrint = true)
        when (bungeeResponse.action) {
            BungeeMessage.GET_SERVERS.value -> {
                onGetServers(message, bungeeResponse)
//                val servers = bungeeResponse.message.joinToString("").split(", ").map { it.trim() }
//                println("Servers: $servers")
//                BungeeMessageListener.onGetServersReceived(BungeeMessage.GetServers(servers))
            }

            BungeeMessage.PLAYER_LIST.value -> {
                onGetPlayerList(message, bungeeResponse)
//                val server = bungeeResponse.message[0]
//                val players = bungeeResponse.message.getOrNull(1)?.split(", ") ?: emptyList()
//                BungeeMessageListener.onPlayerListReceived(BungeeMessage.PlayerList(mapOf(server to players)))
            }

            BungeeMessage.GET_SERVER.value -> {
                onGetServer(message, bungeeResponse)
//                bungeeResponse.message.firstOrNull()
//                    ?.let { BungeeMessageListener.onGetServerReceived(BungeeMessage.GetServer(it)) }
            }
        }
    }

    abstract fun onGetServers(message: ByteArray, decoded: BungeeMessageData)

    abstract fun onGetPlayerList(message: ByteArray, decoded: BungeeMessageData)

    abstract fun onGetServer(message: ByteArray, decoded: BungeeMessageData)

}



