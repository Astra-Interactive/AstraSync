package com.astrainteractive.astrasync.api.messaging

sealed class BungeeMessage(val action: String) {
    enum class Messages(val value: String) {
        GET_SERVERS("GetServers"),
        PLAYER_LIST("PlayerList"),
        GET_SERVER("GetServer"),
        CONNECT("Connect"),
        SEND_DISCORD_MESSAGE("SendDiscordMessage"),
        BUNGEE_CHANNEL("BungeeCord"),
    }

    data class GetServers(val servers: List<String>) : BungeeMessage(Messages.GET_SERVERS.value)
    data class PlayerList(val serverAndPlayers: Map<String, List<String>>) : BungeeMessage(Messages.PLAYER_LIST.value)
    data class GetServer(val server: String) : BungeeMessage(Messages.GET_SERVER.value)
    data class SendDiscordMessage(val playerUUID: String, val message: String) :
        BungeeMessage(Messages.SEND_DISCORD_MESSAGE.value) {

        fun encode(): String {
            return listOf(playerUUID,message).toString()
        }

        companion object {
            fun decode(it: String): SendDiscordMessage? {
                val start = it.indexOfFirst { it=='[' }
                val end = it.indexOfLast { it==']' }
                val list = it.slice(start+1 until end)
                println("Received: $list")
                val newList = list.split(", ")
                return SendDiscordMessage(newList[0],newList[1])
            }
        }
    }

}