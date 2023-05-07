package kr.hqservice.menu.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kr.hqservice.menu.data.MenuSound.Companion.toMenuSound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File
import kotlin.coroutines.CoroutineContext

class Menu(
    folder: File,
    val name: String,
    private var row: Int = 3
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private val file = File(folder, "menu-$name.yml")
    private val config = YamlConfiguration.loadConfiguration(file)

    private val contents = mutableMapOf<Int, MenuItemStack>()

    private var customName = name
    private var close = ""

    init {
        load()
    }

    fun load() {
        config.getConfigurationSection("menu")?.let { menuSection ->
            customName = menuSection.getString("name") ?: name
            row = menuSection.getInt("row")
            close = menuSection.getString("close") ?: ""

            menuSection.getConfigurationSection("contents")?.let { contentSection ->
                contentSection.getKeys(false).forEach { slot ->
                    contentSection.getConfigurationSection(slot)?.let { slotSection ->
                        val item = slotSection.getItemStack("item")
                        val sound = slotSection.getString("sound").toMenuSound()
                        val commands = slotSection.getStringList("commands")
                        contents[slot.toInt()] = MenuItemStack(item, sound, commands)
                    }
                }
            }
        }
    }

    fun save() {
        config.set("menu", null)
        config.set("menu.name", customName)
        config.set("menu.row", row)
        config.set("menu.close", close)
        contents.forEach { (slot, menuItemStack) ->
            config.set("menu.contents.$slot.item", menuItemStack.item)
            config.set("menu.contents.$slot.sound", menuItemStack.sound?.toString())
            config.set("menu.contents.$slot.commands", menuItemStack.commands)
        }
        config.save(file)
    }

    fun reload() {
        config.load(file)
        contents.clear()
        load()
    }

    fun getContents(): Map<Int, MenuItemStack> {
        return contents
    }

    fun setContents(items: Array<ItemStack?>) {
        contents.clear()
        items.forEachIndexed { index, item ->
            if (item != null) {
                val menuItemStack = contents[index]
                    ?: MenuItemStack(item, null, mutableListOf()).apply { this@Menu.contents[index] = this }
                menuItemStack.item = item
            }
        }
    }

    fun getCustomName(): String {
        return customName
    }

    fun getRow(): Int {
        return row
    }

    fun getClose(): String {
        return close
    }
}