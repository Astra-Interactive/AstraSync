package com.astrainteractive.astrasync.utils

import com.astrainteractive.astralibs.EmpireSerializer
import com.astrainteractive.astralibs.Logger

val EmpireConfig: _EmpireConfig
    get() = _EmpireConfig.instance

/**
 * Example config file with 3 types of initialization
 */
@kotlinx.serialization.Serializable
data class _EmpireConfig(
    val mysql: MySqlConfig,
    val serverID: String,
) {
    @kotlinx.serialization.Serializable
    data class MySqlConfig(
        val host: String,
        val port: String,
        val login: String,
        val password: String,
        val name: String,
        val driver: String = "com.mysql.cj.jdbc.Driver",
    )

    companion object {
        lateinit var instance: _EmpireConfig

        /**
         * This is a new version using kotlinx.serialization
         * It will send console message when one of paramter in config file is wrong
         */
        fun kotlinxSerializaion(): _EmpireConfig {
            val config =
                EmpireSerializer.toClass<_EmpireConfig>(Files.configFile) ?: throw Exception("Wrong config.yml")
            instance = config
            Logger.log("$config", "EmpireConfig")
            return config
        }
    }
}
