package com.astrainteractive.astrasync.api.messaging.models

sealed class BungeeResponse(val action: String) {
    data class GetServers(val servers: List<String>) : BungeeResponse(BungeeMessage.GET_SERVERS.value)
    data class PlayerList(val serverAndPlayers: Map<String, List<String>>) : BungeeResponse(BungeeMessage.PLAYER_LIST.value)
    data class GetServer(val server: String) : BungeeResponse(BungeeMessage.GET_SERVER.value)

}