package com.astrainteractive.astrasync.utils

import ru.astrainteractive.astralibs.file_manager.FileManager
import ru.astrainteractive.astralibs.utils.BaseTranslation


/**
 * All translation stored here
 */
class PluginTranslation : BaseTranslation() {
    /**
     * This is a default translation file. Don't forget to create translation.yml in resources of the plugin
     */
    override val translationFile: FileManager = FileManager("translations.yml")


    //General
    val prefix: String = translationValue("general.prefix", "#18dbd1[EmpireItems]")
    val reload: String = translationValue("general.reload", "#dbbb18Перезагрузка плагина")
    val reloadComplete: String =
        translationValue("general.reload_complete", "#42f596Перезагрузка успешно завершена")
    val noPermission: String =
        translationValue("general.no_permission", "#db2c18У вас нет прав!")

    val pleaseWait: String =
        translationValue("general.please_wait", "#db2c18Пожалуйста, подождите")
    val inventoryLossWarning: String =
        translationValue("general.inventory_loss_warn","#db2c18Не отключайтесь! Это может привести к потери инвентаря!!")
    val errorOccurredInSaving: String =
        translationValue("general.error_in_saving", "#db2c18Произошла ошибка при сохранении инвентаря")
    val errorOccurredInLoading: String =
        translationValue("general.error_in_loading", "#db2c18Произошла ошибка при загрузке инвентаря")
    val onJoinFormat: String =
        translationValue("general.on_join_format", "&7[&#0ecf41+&7] %player%")
    val onLeaveFormat: String =
        translationValue("general.on_join_format", "&7[&#cf0e0e-&7] %player%")
    val messageFormat: String =
        translationValue("general.message_format", "#0ecf41%player%: &7%message%")
    val fromDiscordMessageFormat: String =
        translationValue("general.message_from_discord_format", "#0ecf41%player%: &7%message%")


}