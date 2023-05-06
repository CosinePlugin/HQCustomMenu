package kr.hqservice.menu.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.hqservice.menu.enums.PermissionType
import kr.hqservice.menu.extension.executeCommandAsOp
import kr.hqservice.menu.extension.sync
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.coroutines.CoroutineContext

data class MenuItemStack(
    var item: ItemStack,
    val sound: MenuSound?,
    val commands: MutableList<String> = mutableListOf()
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    /*
    user:/give %player% stone 1
    op:/give %player% stone 1
    wait 1 tick
    console:/give %player% stone 1
    */
    fun runCommand(player: Player) {
        if (commands.isEmpty()) return

        val server = player.server

        launch {
            getCommands(player).forEach {
                if (it.contains("wait")) {
                    delay(it.getWaitTime())
                } else {
                    val data = it.getCommand()

                    val permission = data.first
                    val command = data.second

                    when (permission) {
                        PermissionType.CONSOLE -> sync { server.dispatchCommand(server.consoleSender, command) }

                        PermissionType.OP -> sync { player.executeCommandAsOp(command) }

                        PermissionType.USER -> sync { player.performCommand(command) }
                    }
                }
            }
        }
    }

    private fun getCommands(player: Player): List<String> {
        return commands.map { it.replace("%player%", player.name) }
    }

    private fun String.getWaitTime(): Long {
        return split(" ")[1].toLong() * 50
    }

    private fun String.getCommand(): Pair<PermissionType, String> {
        val split = split(":", limit = 2)
        val permission = PermissionType.valueOf(split[0].uppercase())
        val command = split[1]
        return permission to command
    }
}
