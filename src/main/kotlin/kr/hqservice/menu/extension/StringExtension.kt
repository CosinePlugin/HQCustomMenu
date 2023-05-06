package kr.hqservice.menu.extension

import org.bukkit.ChatColor

internal fun String.applyColor(): String = ChatColor.translateAlternateColorCodes('&', this)

internal fun String.isInt(): Boolean {
    return try {
        this.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}