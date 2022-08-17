package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.utils.catching
import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams
import github.scarsz.discordsrv.DiscordSRV
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import org.bukkit.plugin.messaging.PluginMessageRecipient
import org.jetbrains.kotlin.com.google.common.io.ByteArrayDataOutput
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

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
            sendBungeeMessage("BungeeCord", "Message $it", message, sender)
        }
        sendCrossServerMessage("MyChannel", message)
    }

    fun sendCrossServerMessage(message: String, channel: String = "BungeeCord") {
        val out = createByteOutputArray("Forward ALL $SHARED_SERVER_MESSAGE")
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
            channel,
            out.toByteArray()
        )
    }


    fun createByteOutputArray(action: String, message: String? = null): com.google.common.io.ByteArrayDataOutput {
        return ByteStreams.newDataOutput().apply {
            action.split(" ").forEach(::writeUTF)
            message?.let(::writeUTF)
        }
    }

    fun sendBungeeMessage(
        channel: String = "BungeeCord",
        action: String,
        message: String? = null,
        sender: PluginMessageRecipient? = Bukkit.getServer(),
    ) {
        val sender: PluginMessageRecipient = sender ?: Bukkit.getServer()
        val out = createByteOutputArray(action, message)
        sender.sendPluginMessage(AstraLibs.instance, channel, out.toByteArray())
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
        val bungeeMessage = decodeBungeeMessage(message)
        Logger.log("onPluginMessageReceived: ${decodeBungeeMessage(message)}", consolePrint = true)
        if (channel != "BungeeCord") return
        when (bungeeMessage.action) {
            "GetServers" -> {
                rememberServers(bungeeMessage.message)
                bungeeMessage.message.joinToString("").split(", ").forEach {
                    sendBungeeMessage("BungeeCord", "PlayerList", it.trim(), player)
                }
            }
            "PlayerList" -> {
                val server = bungeeMessage.message[0]
                val players = bungeeMessage.message.getOrNull(1)?.split(", ") ?: emptyList()
                rememberPlayers(server, players)
            }
            "GetServer" -> rememberServer(bungeeMessage.message.firstOrNull())
            SHARED_SERVER_MESSAGE -> {
                (Bukkit.getPluginManager().getPlugin("DiscordSRV") as DiscordSRV?)?.let {
                    it.mainTextChannel.sendMessage(bungeeMessage.message.joinToString(" "))
                }
            }
        }
    }

    const val SHARED_SERVER_MESSAGE = "ESMP_CHANNEL"
}