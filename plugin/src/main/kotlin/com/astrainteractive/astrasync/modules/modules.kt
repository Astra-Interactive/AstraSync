package com.astrainteractive.astrasync.modules

import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astraclans.domain.datasource.RemoteDataSource
import com.astrainteractive.astrasync.api.LocalPlayerDataSource
import com.astrainteractive.astrasync.utils.Locker
import com.astrainteractive.astrasync.utils.PluginTranslation
import ru.astrainteractive.astralibs.EmpireSerializer
import ru.astrainteractive.astralibs.di.getValue
import ru.astrainteractive.astralibs.di.module
import ru.astrainteractive.astralibs.di.reloadable
import java.util.*

val ConfigProvider = reloadable {
    EmpireSerializer.toClass< PluginConfig>(Files.configFile)!!
}
val TranslationProvider = reloadable {
    PluginTranslation()
}
val RemoteDataSourceModule = module {
    val database by DatabaseModule
    RemoteDataSource()
}
val LocalDataSourceModule = module {
    LocalPlayerDataSource()
}
val uuidLockerModule = module {
    Locker<UUID>()
}