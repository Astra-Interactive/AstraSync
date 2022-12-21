package com.astrainteractive.astraclans.domain.dto

class PlayerDTO(
    val minecraftUUID: String,
    val totalExperience: Int,
    val health: Double,
    val foodLevel: Int,
    val lastServerName: String,
    val items: String,
    val enderChestItems: String,
    val effects: String
)