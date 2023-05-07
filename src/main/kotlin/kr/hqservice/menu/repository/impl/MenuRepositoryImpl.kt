package kr.hqservice.menu.repository.impl

import kr.hqservice.menu.data.Menu
import kr.hqservice.menu.extension.async
import kr.hqservice.menu.repository.MenuRepository
import org.bukkit.plugin.Plugin
import java.io.File

class MenuRepositoryImpl(
    plugin: Plugin
) : MenuRepository<String, Menu> {

    private val menuFolder = File(plugin.dataFolder, "Menu")

    private val menus = mutableMapOf<String, Menu>()

    fun loadAll() {
        menuFolder.listFiles()?.forEach {
            val name = it.name.run { substring(0, length - 4) }.replace("menu-", "")
            menus[name] = Menu(menuFolder, name)
        }
    }

    fun saveAll() {
        menus.values.forEach { it.save() }
    }

    override fun reload() {
        menus.values.forEach(Menu::reload)
    }

    override fun reload(name: String) {
        menus[name]?.reload()
    }

    override fun createMenu(name: String) {
        if (isCreated(name)) return
        menus[name] = Menu(menuFolder, name, 3).apply { async { save() } }
    }

    override fun deleteMenu(name: String) {
        menus.remove(name)
        async { File(menuFolder, "menu-$name.yml").delete() }
    }

    override fun isCreated(name: String): Boolean {
        return menus.containsKey(name)
    }

    override fun getMenu(name: String): Menu? {
        return menus[name]
    }

    override fun getMenus(): List<String> {
        return menus.keys.toList()
    }

    override fun getMenuMap(): Map<String, Menu> {
        return menus
    }
}