package com.astrainteractive.astrasync.modules

import com.astrainteractive.astrasync.utils.PluginTranslation
import ru.astrainteractive.astralibs.di.IReloadable

object TranslationProvider : IReloadable<PluginTranslation>() {
    override fun initializer(): PluginTranslation = PluginTranslation()
}

