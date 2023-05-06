package kr.hqservice.menu.extension

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

internal fun Player.sendMessages(vararg message: String?) {
    message.filterNotNull().forEach { sendMessage(it) }
}

internal fun CommandSender.sendMessages(vararg message: String?) {
    message.filterNotNull().forEach { sendMessage(it) }
}

internal fun Player.executeCommandAsOp(command: String): Boolean {
    val op = isOp
    isOp = true
    try {
        performCommand(command)
    } finally {
        if (!op) isOp = false
    }
    return true
}