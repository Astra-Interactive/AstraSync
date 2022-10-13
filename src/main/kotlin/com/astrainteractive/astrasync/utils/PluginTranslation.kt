package com.astrainteractive.astrasync.utils

import ru.astrainteractive.astralibs.file_manager.FileManager
import ru.astrainteractive.astralibs.utils.HEX
import ru.astrainteractive.astralibs.utils.getHEXString



/**
 * All translation stored here
 */
class PluginTranslation {
    /**
     * This is a default translation file. Don't forget to create translation.yml in resources of the plugin
     */
    private val _translationFile: FileManager = FileManager("translations.yml")
    private val translation = _translationFile.fileConfiguration

    private fun getHEXString(path: String) = translation.getHEXString(path)

    /**
     * This function will write non-existing translation into config file
     * So you don't need to create your config file by yourself
     * Just run plugin with this function and translation file will be generated automatically
     */
    private fun getHEXString(path: String, default: String): String {
        val msg = translation.getHEXString(path) ?: default.HEX()
        if (!translation.contains(path)) {
            translation.set(path, default)
            _translationFile.save()
        }
        return msg
    }

    //General
    val prefix: String = getHEXString("general.prefix", "#18dbd1[EmpireItems]")
    val reload: String = getHEXString("general.reload", "#dbbb18Перезагрузка плагина")
    val reloadComplete: String =
        getHEXString("general.reload_complete", "#42f596Перезагрузка успешно завершена")
    val noPermission: String =
        getHEXString("general.no_permission", "#db2c18У вас нет прав!")

    val pleaseWait: String =
        getHEXString("general.please_wait", "#db2c18Пожалуйста, подождите")
    val inventoryLossWarning: String =
        getHEXString("general.inventory_loss_warn", "#db2c18Не отключайтесь! Это может привести к потери инвентаря!!")
    val errorOccurredInSaving: String =
        getHEXString("general.error_in_saving", "#db2c18Произошла ошибка при сохранении инвентаря")
    val errorOccurredInLoading: String =
        getHEXString("general.error_in_loading", "#db2c18Произошла ошибка при загрузке инвентаря")
    val onJoinFormat: String =
        getHEXString("general.on_join_format", "&7[&#0ecf41+&7] %player%")
    val onLeaveFormat: String =
        getHEXString("general.on_join_format", "&7[&#cf0e0e-&7] %player%")
    val messageFormat: String =
        getHEXString("general.message_format", "#0ecf41%player%: &7%message%")
    val fromDiscordMessageFormat: String =
        getHEXString("general.message_from_discord_format", "#0ecf41%player%: &7%message%")


}