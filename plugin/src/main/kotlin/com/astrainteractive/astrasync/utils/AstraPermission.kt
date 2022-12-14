package com.astrainteractive.astrasync.utils


sealed class AstraPermission(override val value: String) : IPermission {
    object Reload : AstraPermission("astra_template.reload")
    object Damage : AstraPermission("astra_template.damage")
    object History : AstraPermission("astra_template.history")
}

