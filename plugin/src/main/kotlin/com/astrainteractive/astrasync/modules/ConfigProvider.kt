package com.astrainteractive.astrasync.modules

import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astrasync.utils.Files
import ru.astrainteractive.astralibs.EmpireSerializer
import ru.astrainteractive.astralibs.di.IReloadable

object ConfigProvider : IReloadable<PluginConfig>() {
    override fun initializer(): PluginConfig {
        return EmpireSerializer.toClass(Files.configFile)!!
    }
}