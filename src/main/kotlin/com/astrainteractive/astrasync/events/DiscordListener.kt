package com.astrainteractive.astrasync.events

import com.astrainteractive.astralibs.events.EventListener
import com.astrainteractive.astralibs.events.EventManager
import com.astrainteractive.astralibs.utils.BukkitConstant
import com.astrainteractive.astralibs.utils.catching
import com.astrainteractive.astrasync.utils.Translation
import github.scarsz.discordsrv.DiscordSRV
import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent
import org.bukkit.Bukkit

class DiscordListener {


    @Subscribe
    fun onDiscordMessage(e: MessageReceivedEvent) {
        BungeeUtil.broadcast(
            Translation.fromDiscordMessageFormat.replace("%player%", e.author.name)
                .replace("%message%", e.message.contentRaw),
            Bukkit.getOnlinePlayers().firstOrNull()
        )
    }

    fun onEnable() {
        catching {
            DiscordSRV.api.subscribe(this)
        }
    }

    fun onDisable() {
        catching {
            DiscordSRV.api.unsubscribe(this)
        }
    }
}