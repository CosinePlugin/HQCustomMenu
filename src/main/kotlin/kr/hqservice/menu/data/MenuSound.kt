package kr.hqservice.menu.data

import org.bukkit.entity.Player

data class MenuSound(
    val sound: String,
    val volume: Float = 1f,
    val pitch: Float = 1f
) {

    fun playSound(player: Player) {
        player.playSound(player.location, sound, volume, pitch)
    }

    override fun toString(): String {
        return "$sound, $volume, $pitch"
    }

    companion object {
        fun String?.toMenuSound(): MenuSound? {
            if (this == null) return null
            val split = split(", ")
            if (split.size != 3) return null
            return MenuSound(split[0], split[1].toFloat(), split[2].toFloat())
        }
    }
}