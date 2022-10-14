package com.astrainteractive.astrasync.api.messaging


import com.astrainteractive.astrasync.api.messaging.models.BungeeMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageRecipient
import ru.astrainteractive.astralibs.AstraLibs
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

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

    fun rememberServers(list: List<String>?) {
        servers.addAll(list ?: emptyList())
    }

    fun rememberServer(server: String?) {
        currentServer = server
    }

    fun rememberPlayers(server: String, players: List<String>) {
        serversAndPlayers[server] = mutableSetOf<String>().apply { addAll(players) }
    }

    fun sendCrossServerMessage(customChannel: String, message: String) {
        val out = BungeeDecoder.createByteOutputArray("Forward ALL $customChannel")
        val msgbytes = ByteArrayOutputStream()
        val msgout = DataOutputStream(msgbytes)
        try {
            msgout.writeUTF(message)
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        out.writeShort(msgbytes.toByteArray().size)
        out.write(msgbytes.toByteArray())
        (Bukkit.getOnlinePlayers().firstOrNull() ?: Bukkit.getServer() as PluginMessageRecipient).sendPluginMessage(
            AstraLibs.instance,
            BungeeMessage.BUNGEE_CHANNEL.value,
            out.toByteArray()
        )
    }
}