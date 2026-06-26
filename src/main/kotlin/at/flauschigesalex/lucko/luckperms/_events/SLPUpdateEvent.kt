package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

@EventInternal
abstract class SLPUpdateEvent internal constructor(player: Player) : PlayerEvent(player, Bukkit.isPrimaryThread().not())

@Suppress("unused")
enum class UpdateField {
    PLAYER_LIST_ORDER,
    PLAYER_LIST_NAME,
    DISPLAY_NAME,
    SCOREBOARD_TEAM,
    LOCATOR_BAR_WAYPOINT,
}

@RequiresOptIn("Abstract or unused event class.")
internal annotation class EventInternal