package com.astrainteractive.astrasync.utils.providers

import com.astrainteractive.astrasync.utils.PluginTranslation

object TranslationProvider : IReloadable<PluginTranslation>() {
    override fun initializer(): PluginTranslation = PluginTranslation()
}

