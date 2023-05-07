package kr.hqservice.menu

import kr.hqservice.menu.command.MenuAdminCommand
import kr.hqservice.menu.listener.MenuInventoryListener
import kr.hqservice.menu.repository.impl.MenuRepositoryImpl
import kr.ms.core.bstats.Metrics
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class HQCustomMenu : JavaPlugin() {

    companion object {
        internal lateinit var plugin: Plugin
            private set

        internal const val prefix = "§6[ 메뉴 ]§f"
    }

    lateinit var menuRepository: MenuRepositoryImpl
        private set

    override fun onLoad() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }

        plugin = this
    }

    override fun onEnable() {
        if (server.pluginManager.getPlugin("MS-Core") == null) {
            logger.warning("MS-Core 플러그인을 찾을 수 없어, 플러그인이 비활성화됩니다.")
            server.pluginManager.disablePlugin(this)
            return
        }
        Metrics(this, 18264)

        menuRepository = MenuRepositoryImpl(this)
        menuRepository.loadAll()

        server.pluginManager.registerEvents(MenuInventoryListener(), this)

        getCommand("메뉴")?.setExecutor(MenuAdminCommand(this))
    }

    override fun onDisable() {
        menuRepository.saveAll()
    }
}