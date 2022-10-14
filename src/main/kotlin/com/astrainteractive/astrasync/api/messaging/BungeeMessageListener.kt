package com.astrainteractive.astrasync.api.messaging


import com.astrainteractive.astrasync.api.messaging.models.BungeeResponse
import com.astrainteractive.astrasync.utils.PluginTranslation
import com.astrainteractive.astrasync.utils.providers.TranslationProvider

object BungeeMessageListener {
    val translation: PluginTranslation
        get() = TranslationProvider.value

    fun onGetServersReceived(it: BungeeResponse.GetServers) {
        BungeeController.rememberServers(it.servers)
        it.servers.forEach {
//            BungeeController.requestServerPlayers(it)
        }
    }

    fun onPlayerListReceived(it: BungeeResponse.PlayerList) {
        it.serverAndPlayers.forEach { (server, players) ->
            BungeeController.rememberPlayers(server, players)
        }
    }

    fun onGetServerReceived(it: BungeeResponse.GetServer) {
        BungeeController.rememberServer(it.server)
    }
}