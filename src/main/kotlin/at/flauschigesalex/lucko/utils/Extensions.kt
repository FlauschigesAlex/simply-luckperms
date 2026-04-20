@file:Suppress("unused")

package at.flauschigesalex.lucko.utils

import at.flauschigesalex.lucko.SimpleLuckoPlugin
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Scoreboard
import net.kyori.adventure.text.minimessage.MiniMessage as MM

internal val MiniMessage = MM.miniMessage()

internal fun delayTick(ticks: Long = 0, block: () -> Unit) = Bukkit.getScheduler().runTaskLater(SimpleLuckoPlugin.instance, block, ticks)

val Scoreboard.isMainScoreboard: Boolean
    get() = this == Bukkit.getScoreboardManager().mainScoreboard
val Scoreboard.isPrivateScoreboard: Boolean
    get() = this.isMainScoreboard.not()