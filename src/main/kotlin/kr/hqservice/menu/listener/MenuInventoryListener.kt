package kr.hqservice.menu.listener

import kr.hqservice.menu.inventory.MenuInventoryHolder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MenuInventoryListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        event.inventory.holder?.let {
            if (it is MenuInventoryHolder) {
                it.onInventoryClick(event)
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        event.inventory.holder?.let {
            if (it is MenuInventoryHolder) {
                it.onInventoryClose(event)
            }
        }
    }
}