package com.astrainteractive.astrasync.api.messaging

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.utils.uuid
import com.google.common.io.ByteStreams
import github.scarsz.discordsrv.DiscordSRV
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageRecipient
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

object BungeeController {
    var serversAndPlayers: HashMap<String, Set<String>> = HashMap()
        private set
    var servers: HashSet<String> = HashSet()
    var currentServer: String? = null
        private set

    fun requestServersUpdate() {
        sendBungeeMessage(BungeeMessage.Messages.GET_SERVERS.value)
    }

    fun requestServerUpdate() {
        sendBungeeMessage(BungeeMessage.Messages.GET_SERVER.value)
    }

    fun requestServerPlayers(server: String) {
        sendBungeeMessage(
            BungeeMessage.Messages.PLAYER_LIST.value,
            server.trim(),
            getPluginMessageRecipient
        )
    }

    fun connectPlayerToServer(server: String, player: Player) {
        sendBungeeMessage(
            BungeeMessage.Messages.CONNECT.value,
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

    fun sendMessageToAllPlayers(message: String, sender: PluginMessageRecipient? = null) {
        val players = serversAndPlayers.filter { it.key != currentServer }.flatMap { it.value }.toSet()
        players.forEach {
            Logger.warn("Broadcasting $message to $it")
            sendBungeeMessage("Message $it", message, sender)
        }
    }

    fun sendDiscordMessage(player: Player, message: String) {
        ((Bukkit.getPluginManager().getPlugin("DiscordSRV")) as DiscordSRV?) ?: run {
            sendCrossServerMessage(
                BungeeMessage.Messages.SEND_DISCORD_MESSAGE.value,
                BungeeMessage.SendDiscordMessage(player.uuid, message).encode()
            )
        }
    }

    fun sendCrossServerMessage(customChannel: String, message: String) {
        val out = createByteOutputArray("Forward ALL $customChannel")
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
            BungeeMessage.Messages.BUNGEE_CHANNEL.value,
            out.toByteArray()
        )
    }


    fun createByteOutputArray(action: String, message: String? = null): com.google.common.io.ByteArrayDataOutput {
        return ByteStreams.newDataOutput().apply {
            action.split(" ").forEach(::writeUTF)
            message?.let(::writeUTF)
            println("Sending: ${action.split(" ")} ${message ?: ""}")
        }
    }

    fun sendBungeeMessage(
        action: String,
        message: String? = null,
        sender: PluginMessageRecipient? = Bukkit.getServer(),
    ) {
        val sender: PluginMessageRecipient = sender ?: Bukkit.getServer()
        val out = createByteOutputArray(action, message)
        sender.sendPluginMessage(AstraLibs.instance, BungeeMessage.Messages.BUNGEE_CHANNEL.value, out.toByteArray())
    }


    val getPluginMessageRecipient: PluginMessageRecipient
        get() {
            val player = (Bukkit.getOnlinePlayers().firstOrNull() as? PluginMessageRecipient?)
            return player ?: Bukkit.getServer()
        }


}