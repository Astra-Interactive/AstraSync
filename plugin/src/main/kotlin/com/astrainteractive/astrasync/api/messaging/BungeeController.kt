package com.astrainteractive.astrasync.api.messaging


import com.astrainteractive.astrasync.api.messaging.models.BungeeMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.AstraLibs

object BungeeController {
    var serversAndPlayers: HashMap<String, Set<String>> = HashMap()
        private set
    var servers: HashSet<String> = HashSet()
    var currentServer: String? = null
        private set

    fun registerChannel(channel:BungeeMessage){
        Bukkit.getServer().messenger.registerOutgoingPluginChannel(AstraLibs.instance, channel.value)
    }

    fun connectPlayerToServer(server: String, player: Player) {
        BungeeDecoder.sendBungeeMessage(
            BungeeMessage.CONNECT,
            server,
            player
        )
    }
}