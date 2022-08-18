package com.astrainteractive.astrasync.api.messaging

import com.astrainteractive.astralibs.utils.catching
import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams

data class BungeeResponse(
    val action: String,
    val message: List<String>,
) {
    companion object {
        fun decode(message: ByteArray): BungeeResponse {
            val byteArrayInput: ByteArrayDataInput = ByteStreams.newDataInput(message)
            val action = byteArrayInput.readUTF()
            val response: List<String> = buildList<String> {

                var line = catching() { byteArrayInput.readUTF() }
                while (line != null) {
                    add(line)
                    line = catching() { byteArrayInput.readUTF() }
                }
            }
            return BungeeResponse(
                action, response
            )
        }
    }
}