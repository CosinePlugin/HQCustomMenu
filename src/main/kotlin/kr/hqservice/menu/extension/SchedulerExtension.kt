package kr.hqservice.menu.extension

import kr.hqservice.menu.HQCustomMenu.Companion.plugin
import org.bukkit.scheduler.BukkitTask

internal fun sync(block: () -> Unit) {
    plugin.server.scheduler.runTask(plugin, Runnable(block))
}

internal fun async(block: () -> Unit) {
    plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable(block))
}

internal fun later(delay: Int = 1, async: Boolean = false, block: () -> Unit = {}): BukkitTask {
    return if (async) {
        plugin.server.scheduler.runTaskLaterAsynchronously(plugin, Runnable(block), delay.toLong())
    } else {
        plugin.server.scheduler.runTaskLater(plugin, Runnable(block), delay.toLong())
    }
}