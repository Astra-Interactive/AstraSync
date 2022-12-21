package com.astrainteractive.astrasync.api.messaging.models

data class BungeeMessageData(
    val action: String,
    val message: List<String>,
)