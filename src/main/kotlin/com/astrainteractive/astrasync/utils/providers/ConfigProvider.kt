package com.astrainteractive.astrasync.utils.providers

import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astrasync.utils.Files
import ru.astrainteractive.astralibs.EmpireSerializer

object ConfigProvider : IReloadable<PluginConfig>() {
    override fun initializer(): PluginConfig {
        return EmpireSerializer.toClass(Files.configFile)!!
    }
}