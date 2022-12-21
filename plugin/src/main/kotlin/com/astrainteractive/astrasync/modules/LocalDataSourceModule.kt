package com.astrainteractive.astrasync.modules

import com.astrainteractive.astrasync.api.ILocalPlayerDataSource
import com.astrainteractive.astrasync.api.LocalPlayerDataSource
import ru.astrainteractive.astralibs.di.IModule

object LocalDataSourceModule : IModule<ILocalPlayerDataSource>() {
    override fun initializer(): ILocalPlayerDataSource {
        return LocalPlayerDataSource()
    }
}