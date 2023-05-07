package kr.hqservice.menu.command

import kr.hqservice.menu.HQCustomMenu
import kr.hqservice.menu.HQCustomMenu.Companion.prefix
import kr.hqservice.menu.data.Menu
import kr.hqservice.menu.extension.later
import kr.hqservice.menu.extension.sendMessages
import kr.hqservice.menu.inventory.MenuInventory
import kr.hqservice.menu.inventory.MenuItemSettingInventory
import kr.hqservice.menu.repository.MenuRepository
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class MenuAdminCommand(
    plugin: HQCustomMenu
) : CommandExecutor, TabCompleter {

    private companion object {
        val menuTabList = listOf("제거", "아이템설정", "열기", "리로드")
        val commandTabList = mutableListOf("생성", "목록").apply { addAll(menuTabList) }
    }

    private val menuRepository: MenuRepository<String, Menu> = plugin.menuRepository

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        if (args.size <= 1) {
            return commandTabList
        }
        if (args.size == 2 && menuTabList.contains(args[0])) {
            return menuRepository.getMenus()
        }
        return emptyList()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            printHelp(sender)
            return true
        }
        checker(sender, args)
        return true
    }

    private fun printHelp(sender: CommandSender) {
        sender.sendMessages(
            "$prefix 메뉴 명령어 도움말",
            "",
            "$prefix /메뉴 생성 [이름] : 메뉴를 생성합니다.",
            "$prefix /메뉴 제거 [이름] : 메뉴를 제거합니다.",
            "$prefix /메뉴 아이템설정 [이름] : 메뉴의 아이템을 설정합니다.",
            "$prefix /메뉴 열기 [이름] : 메뉴를 오픈합니다.",
            "$prefix /메뉴 리로드 [이름] : 메뉴를 리로드합니다.",
            "$prefix /메뉴 목록 : 메뉴의 목록을 확인합니다.",
            "§7[ 리로드에 이름을 미입력 시 모든 메뉴를 리로드합니다. ]"
        )
    }

    private fun checker(sender: CommandSender, args: Array<out String>) {
        when (args[0]) {
            "생성" -> {
                if (sender !is Player) {
                    sender.sendMessage("$prefix 콘솔에서 실행할 수 없는 명령어입니다.")
                    return
                }
                createMenu(sender, args)
            }

            "제거" -> {
                if (sender !is Player) {
                    sender.sendMessage("$prefix 콘솔에서 실행할 수 없는 명령어입니다.")
                    return
                }
                deleteMenu(sender, args)
            }

            "아이템설정" -> {
                if (sender !is Player) {
                    sender.sendMessage("$prefix 콘솔에서 실행할 수 없는 명령어입니다.")
                    return
                }
                settingMenuItem(sender, args)
            }

            "열기" -> {
                if (sender !is Player) {
                    sender.sendMessage("$prefix 콘솔에서 실행할 수 없는 명령어입니다.")
                    return
                }
                openMenu(sender, args)
            }

            "리로드" -> reloadMenu(sender, args)

            "목록" -> showMenuList(sender)
        }
    }

    private fun createMenu(player: Player, args: Array<out String>) {
        if (args.size == 1) {
            player.sendMessage("$prefix 이름을 입력해주세요.")
            return
        }
        val name = args[1]
        if (menuRepository.isCreated(name)) {
            player.sendMessage("$prefix 이미 존재하는 메뉴입니다.")
            return
        }
        menuRepository.createMenu(name)
        player.sendMessage("$prefix $name 메뉴가 생성되었습니다.")
    }

    private fun isMenuCreated(player: Player, args: Array<out String>, block: (String) -> Unit) {
        if (args.size == 1) {
            player.sendMessage("$prefix 이름을 입력해주세요.")
            return
        }
        val name = args[1]
        if (!menuRepository.isCreated(name)) {
            player.sendMessage("$prefix 존재하지 않는 메뉴입니다.")
            return
        }
        block(name)
    }

    private fun deleteMenu(player: Player, args: Array<out String>) {
        isMenuCreated(player, args) { name ->
            menuRepository.deleteMenu(name)
            player.sendMessage("$prefix $name 메뉴가 제거되었습니다.")
        }
    }

    private fun settingMenuItem(player: Player, args: Array<out String>) {
        isMenuCreated(player, args) { name ->
            val menu = menuRepository.getMenu(name) ?: run {
                player.sendMessage("$prefix 메뉴 데이터를 불러오지 못했습니다.")
                return@isMenuCreated
            }
            later { MenuItemSettingInventory(menu).openInventory(player) }
        }
    }

    private fun openMenu(player: Player, args: Array<out String>) {
        isMenuCreated(player, args) { name ->
            val menu = menuRepository.getMenu(name) ?: run {
                player.sendMessage("$prefix 메뉴 데이터를 불러오지 못했습니다.")
                return@isMenuCreated
            }
            later { MenuInventory(menu).openInventory(player) }
        }
    }

    private fun reloadMenu(sender: CommandSender, args: Array<out String>) {
        if (args.size == 1) {
            menuRepository.reload()
            sender.sendMessage("$prefix 모든 메뉴가 리로드되었습니다.")
            return
        }
        val name = args[1]
        if (!menuRepository.isCreated(name)) {
            sender.sendMessage("$prefix 존재하지 않는 메뉴입니다.")
            return
        }
        menuRepository.reload(name)
        sender.sendMessage("$prefix $name 메뉴가 리로드되었습니다.")
    }

    private fun showMenuList(sender: CommandSender) {
        val menus = menuRepository.getMenus()
        if (menus.isEmpty()) {
            sender.sendMessage("$prefix 생성된 메뉴가 없습니다.")
            return
        }
        sender.sendMessages("$prefix 메뉴 목록", "")
        if (sender is Player) {
            menus.forEachIndexed { index, name ->
                val component = TextComponent("${index + 1}. $name 메뉴 ").apply {
                    val buttonBuilder: (String, String, String) -> Unit = { title, command, description ->
                        val editorButton = TextComponent(title)
                        editorButton.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
                        editorButton.hoverEvent =
                            HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent(description)))
                        addExtra(editorButton)
                    }
                    buttonBuilder("§a[ 열기 ]", "/메뉴 열기 $name", "§f클릭 시 메뉴를 오픈합니다.")
                }
                sender.spigot().sendMessage(component)
            }
        } else {
            menus.forEachIndexed { index, name ->
                sender.sendMessage("${index + 1}. $name 메뉴")
            }
        }
    }
}