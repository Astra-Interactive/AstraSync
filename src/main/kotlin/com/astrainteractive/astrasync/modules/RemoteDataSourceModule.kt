package com.astrainteractive.astrasync.modules

import com.astrainteractive.astraclans.domain.datasource.IRemoteDataSource
import com.astrainteractive.astraclans.domain.datasource.RemoteDataSource
import ru.astrainteractive.astralibs.di.IModule

object RemoteDataSourceModule : IModule<IRemoteDataSource>() {
    override fun initializer(): IRemoteDataSource {
        return RemoteDataSource()
    }
}