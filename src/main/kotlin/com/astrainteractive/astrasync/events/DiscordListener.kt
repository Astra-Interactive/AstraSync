package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.events.EventListener
import com.astrainteractive.astralibs.events.EventManager
import com.astrainteractive.astralibs.utils.catching
import com.astrainteractive.astrasync.api.messaging.BungeeMessageListener
import github.scarsz.discordsrv.DiscordSRV
import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent
import org.bukkit.Bukkit

class DiscordListener:EventListener {


    @Subscribe
    fun onDiscordMessage(e: DiscordGuildMessageReceivedEvent) {
        if (e.channel!=(Bukkit.getPluginManager().getPlugin("DiscordSRV") as DiscordSRV).mainTextChannel) return
        BungeeMessageListener.onDiscordMessage(e.author,e.message.contentRaw)
    }

    override fun onEnable(manager: EventManager): EventListener {
        catching {
            DiscordSRV.api.subscribe(this)
        }
        return super.onEnable(manager)
    }

    override fun onDisable() {
        catching {
            DiscordSRV.api.unsubscribe(this)
        }
    }
}