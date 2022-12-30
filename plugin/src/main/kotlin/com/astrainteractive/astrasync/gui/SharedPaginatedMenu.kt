package com.astrainteractive.astrasync.gui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import ru.astrainteractive.astralibs.menu.AstraMenuSize
import ru.astrainteractive.astralibs.menu.IInventoryButton
import ru.astrainteractive.astralibs.menu.IPlayerHolder
import ru.astrainteractive.astralibs.menu.PaginatedMenu

abstract class SharedPaginatedMenu(player: Player) : PaginatedMenu() {
    fun getIndex(maxItemsPerPage: Int, page: Int, i: Int) = maxItemsPerPage * page + i

    fun button(
        material: Material,
        index: Int,
        builder: ItemMeta.() -> Unit
    ): IInventoryButton {
        return object : IInventoryButton {
            override val index: Int = index
            override val item: ItemStack = ItemStack(material).apply {
                editMeta(builder)
            }
            override val onClick: (e: InventoryClickEvent) -> Unit = {

            }
        }
    }
    override val maxItemsPerPage: Int = 45 - 9
    override val backPageButton: IInventoryButton = button(Material.PAPER, 40) {
        setDisplayName("Back")
    }
    override val nextPageButton: IInventoryButton = button(Material.PAPER, 41) {
        setDisplayName("Next")
    }
    override val prevPageButton: IInventoryButton = button(Material.PAPER, 39) {
        setDisplayName("Prev")
    }
    override val menuSize: AstraMenuSize = AstraMenuSize.L
    override var page: Int = 0
    override val playerMenuUtility: IPlayerHolder = object : IPlayerHolder {
        override val player: Player = player
    }
}