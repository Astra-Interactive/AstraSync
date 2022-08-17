package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.utils.catching
import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

object BungeeUtil : PluginMessageListener {
    var serversAndPlayers: HashMap<String, Set<String>> = HashMap()
        private set
    var servers: HashSet<String> = HashSet()
    var currentServer: String? = null
        private set

    fun requestServersUpdate() {
        BungeeUtil.sendBungeeMessage("BungeeCord", "GetServers")

    }

    fun requestServerUpdate() {
        BungeeUtil.sendBungeeMessage("BungeeCord", "GetServer")
    }

    fun rememberServers(list: List<String>?) {

        servers.addAll(list?.joinToString("")?.split(", ") ?: emptyList())
    }

    fun rememberServer(server: String?) {
        this.currentServer = server
    }

    fun rememberPlayers(server: String, players: List<String>) {
        serversAndPlayers[server] = mutableSetOf<String>().apply { addAll(players) }
    }

    fun broadcast(message: String, sender: Player? = null) {
        val players = serversAndPlayers.filter { it.key != currentServer }.flatMap { it.value }.toSet()
        players.forEach {
            Logger.warn("Broadcasting $message to $it")
            sender?.let { p -> sendBungeeMessage(p, "BungeeCord", "Message $it", message) }
                ?: sendBungeeMessage("BungeeCord", "Message $it", message)
        }
    }

    fun sendBungeeMessage(sender: Player, channel: String, action: String, message: String? = null) {
        val out = ByteStreams.newDataOutput().apply {
            action.split(" ").forEach(::writeUTF)
            message?.let(::writeUTF)
        }
        sender.sendPluginMessage(AstraLibs.instance, channel, out.toByteArray())
    }

    fun sendBungeeMessage(channel: String, action: String, message: String? = null) {
        val out = ByteStreams.newDataOutput().apply {
            action.split(" ").forEach(::writeUTF)
            message?.let(::writeUTF)
        }
        Bukkit.getServer().sendPluginMessage(AstraLibs.instance, channel, out.toByteArray())
    }

    data class BungeeMessage(
        val action: String,
        val message: List<String>,
    )

    fun decodeBungeeMessage(message: ByteArray): BungeeMessage {
        val byteArrayInput: ByteArrayDataInput = ByteStreams.newDataInput(message)
        val action = byteArrayInput.readUTF()
        val response: List<String> = buildList<String> {
            var line = catching { byteArrayInput.readUTF() }
            while (line != null) {
                add(line)
                line = catching { byteArrayInput.readUTF() }
            }
        }
        return BungeeMessage(
            action, response
        )
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != "BungeeCord") return
        val bungeeMessage = decodeBungeeMessage(message)
        Logger.log("onPluginMessageReceived: ${decodeBungeeMessage(message)}", consolePrint = true)
        when (bungeeMessage.action) {
            "GetServers" -> {
                rememberServers(bungeeMessage.message)
                bungeeMessage.message.joinToString("").split(", ").forEach {
                    sendBungeeMessage(player, "BungeeCord", "PlayerList", it.trim())
                }
            }
            "PlayerList" -> {
                val server = bungeeMessage.message[0]
                val players = bungeeMessage.message.getOrNull(1)?.split(", ") ?: emptyList()
                rememberPlayers(server, players)
            }
            "GetServer" -> rememberServer(bungeeMessage.message.firstOrNull())
        }
    }
}