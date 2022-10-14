package com.astrainteractive.astrasync.api.messaging.models

enum class BungeeMessage(val value: String) {
    GET_SERVERS("GetServers"),
    PLAYER_LIST("PlayerList"),
    GET_SERVER("GetServer"),
    MESSAGE("Message"),
    CONNECT("Connect"),
    BUNGEE_CHANNEL("BungeeCord"),
}