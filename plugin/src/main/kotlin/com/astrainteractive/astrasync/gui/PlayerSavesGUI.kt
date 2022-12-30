package com.astrainteractive.astrasync.gui

import com.astrainteractive.astrasync.modules.LocalDataSourceModule
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import ru.astrainteractive.astralibs.di.getValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PlayerSavesGUI(player: Player, val wantedPlayer: Player) : SharedPaginatedMenu(player) {
    val localDataSource by LocalDataSourceModule

    val files = localDataSource.loadPlayerSaves(wantedPlayer).sortedByDescending { it.timestamp }
    override val maxItemsAmount: Int
        get() = files.size
    override var menuTitle: String = wantedPlayer.name

    override fun onInventoryClicked(e: InventoryClickEvent) {
        super.onInventoryClicked(e)
        if (e.whoClicked == playerMenuUtility.player) e.isCancelled = true
        if (e.slot <= maxItemsPerPage) {
            val index = getIndex(maxItemsPerPage, page, e.slot)
            val item = files.getOrNull(index) ?: return
            playerMenuUtility.player.inventory.contents =
                if (e.isLeftClick) localDataSource.readPlayerInventorySave(wantedPlayer, item).toTypedArray()
                else if (e.isRightClick) localDataSource.readPlayerEnderChestSave(wantedPlayer, item).toTypedArray()
                else return

        }
    }

    private val File.timestamp: Long
        get() {
            val fileName = nameWithoutExtension
            return fileName.split("_").lastOrNull()?.toLongOrNull() ?: 0L
        }

    private fun render() {
        inventory.clear()
        setManageButtons()
        for (i in 0 until maxItemsPerPage) {
            val index = getIndex(maxItemsPerPage, page, i)
            val item = files.getOrNull(index) ?: continue

            button(Material.DIAMOND, i) {
                val time = SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date(item.timestamp))
                setDisplayName("${ChatColor.WHITE}${item.nameWithoutExtension}")
                lore = listOf(
                    "${ChatColor.GRAY}Created: $time"
                )
            }.setInventoryButton()
        }

    }

    override fun onCreated() = render()
    override fun onInventoryClose(it: InventoryCloseEvent) = Unit
    override fun onPageChanged() = render()
}