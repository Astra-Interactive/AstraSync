package com.astrainteractive.astrasync.api

import com.astrainteractive.astralibs.utils.catching
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object Serializer {
    fun <T> serializeItem(obj: T): ByteArray {
        val io = ByteArrayOutputStream()
        val os = BukkitObjectOutputStream(io)
        os.writeObject(obj)
        os.flush()
        return io.toByteArray()
    }

    fun <T> deserializeItem(byteArray: ByteArray): T? = catching {
        val _in = ByteArrayInputStream(byteArray)
        val _is = BukkitObjectInputStream(_in)
        return _is.readObject() as T
    }
}