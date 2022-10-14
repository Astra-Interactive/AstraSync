package com.astrainteractive.astrasync.api.messaging

import com.astrainteractive.astrasync.api.messaging.models.BungeeMessage
import com.astrainteractive.astrasync.api.messaging.models.BungeeMessageData
import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.plugin.messaging.PluginMessageRecipient
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.utils.catching

object BungeeDecoder {

    val anyPlayerMessageRecipient: PluginMessageRecipient?
        get() = Bukkit.getOnlinePlayers().firstOrNull()

    val serverMessageRecipient: PluginMessageRecipient
        get() = Bukkit.getServer()

    val defaultMessageRecipient: PluginMessageRecipient
        get() = anyPlayerMessageRecipient ?: serverMessageRecipient

    fun decode(message: ByteArray): BungeeMessageData {
        val byteArrayInput: ByteArrayDataInput = ByteStreams.newDataInput(message)
        val action = byteArrayInput.readUTF()
        val response: List<String> = buildList<String> {

            var line = catching() { byteArrayInput.readUTF() }
            while (line != null) {
                add(line)
                line = catching() { byteArrayInput.readUTF() }
            }
        }
        return BungeeMessageData(
            action, response
        )
    }

    fun createByteOutputArray(action: String, message: String? = null): ByteArrayDataOutput {
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