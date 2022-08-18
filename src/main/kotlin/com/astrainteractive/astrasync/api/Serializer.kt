package com.astrainteractive.astrasync.api

import com.astrainteractive.astralibs.utils.catching
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

object Serializer {
    fun <T> toByteArray(obj: T): ByteArray {
        val io = ByteArrayOutputStream()
        val os = BukkitObjectOutputStream(io)
        os.writeObject(obj)
        os.flush()
        return io.toByteArray()
    }

    fun <T> fromByteArray(byteArray: ByteArray): T? = catching() {
        val _in = ByteArrayInputStream(byteArray)
        val _is = BukkitObjectInputStream(_in)
        return _is.readObject() as T
    }

    fun <T> toBase64(obj: T): String {
        val encoder = Base64.getEncoder()
        return encoder.encodeToString(toByteArray(obj))
    }

    fun <T> fromBase64(string: String): T? = catching() {
        fromByteArray<T>(Base64.getDecoder().decode(string))
    }
}