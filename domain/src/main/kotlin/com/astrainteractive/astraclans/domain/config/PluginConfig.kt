package com.astrainteractive.astraclans.domain.config


/**
 * Example config file with 3 types of initialization
 */
@kotlinx.serialization.Serializable
data class PluginConfig(
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

}
