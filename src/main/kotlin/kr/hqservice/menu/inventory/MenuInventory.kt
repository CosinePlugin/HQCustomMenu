package kr.hqservice.menu.inventory

import kr.hqservice.menu.data.Menu
import kr.hqservice.menu.extension.executeCommandAsOp
import kr.hqservice.menu.extension.later
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class MenuInventory(
    private val menu: Menu
) : MenuInventoryHolder(menu.getCustomName(), menu.getRow(), true) {

    private val contents = menu.getContents()

    override fun init(inventory: Inventory) {
        contents.forEach { (slot, menuItemStack) ->
            inventory.setItem(slot, menuItemStack.item)
        }
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return

        val slot = event.rawSlot
        if (slot > menu.getRow() * 9) return

        val player = event.whoClicked as Player
        val menuItemStack = contents[slot] ?: return

        menuItemStack.sound?.playSound(player)
        later { menuItemStack.runCommand(player) }
    }

    override fun onClose(event: InventoryCloseEvent) {
        val close = menu.getClose()
        if (close != "") {
            val player = event.player as Player
            later { player.executeCommandAsOp("메뉴 열기 $close") }
        }
    }
}