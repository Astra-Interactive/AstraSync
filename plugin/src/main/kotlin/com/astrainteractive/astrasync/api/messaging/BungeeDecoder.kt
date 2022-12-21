package com.astrainteractive.astrasync.api.messaging

import com.astrainteractive.astrasync.api.messaging.models.BungeeMessage
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.plugin.messaging.PluginMessageRecipient
import ru.astrainteractive.astralibs.AstraLibs

object BungeeDecoder {

    private val anyPlayerMessageRecipient: PluginMessageRecipient?
        get() = Bukkit.getOnlinePlayers().firstOrNull()

    private val serverMessageRecipient: PluginMessageRecipient
        get() = Bukkit.getServer()

    private val defaultMessageRecipient: PluginMessageRecipient
        get() = anyPlayerMessageRecipient ?: serverMessageRecipient

    private fun createByteOutputArray(action: String, message: String? = null): ByteArrayDataOutput {
        return ByteStreams.newDataOutput().apply {
            action.split(" ").forEach(::writeUTF)
            message?.let(::writeUTF)
            println("Sending: ${action.split(" ")} ${message ?: ""}")
        }
    }

    fun sendBungeeMessage(
        action: BungeeMessage,
        message: String? = null,
        sender: PluginMessageRecipient = defaultMessageRecipient,
    ) {
        val out = BungeeDecoder.createByteOutputArray(action.value, message)
        sender.sendPluginMessage(AstraLibs.instance, BungeeMessage.BUNGEE_CHANNEL.value, out.toByteArray())
    }


}