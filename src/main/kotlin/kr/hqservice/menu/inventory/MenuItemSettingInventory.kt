package kr.hqservice.menu.inventory

import kr.hqservice.menu.HQCustomMenu.Companion.prefix
import kr.hqservice.menu.data.Menu
import kr.hqservice.menu.extension.async
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class MenuItemSettingInventory(
    private val menu: Menu
) : MenuInventoryHolder("${menu.name} 메뉴 : 아이템 설정", menu.getRow()) {

    private val contents = menu.getContents()

    override fun init(inventory: Inventory) {
        contents.forEach { (slot, menuItemStack) ->
            inventory.setItem(slot, menuItemStack.item)
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        menu.setContents(event.inventory.contents)
        async { menu.save() }
        player.sendMessage("$prefix 아이템이 설정되었습니다.")
    }
}