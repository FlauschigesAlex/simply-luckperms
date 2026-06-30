package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

@EventInternal
abstract class SLPUpdateEvent internal constructor(player: Player) : PlayerEvent(player, Bukkit.isPrimaryThread().not())

/**
 * Display field targeted by a Simply LuckPerms update attempt.
 */
@Suppress("unused")
enum class UpdateField {
    /**
     * Player list order weight.
     */
    PLAYER_LIST_ORDER,

    /**
     * Player list display name.
     */
    PLAYER_LIST_NAME,

    /**
     * Bukkit display name.
     */
    DISPLAY_NAME,

    /**
     * Scoreboard team.
     */
    SCOREBOARD_TEAM,

    /**
     * Locator bar waypoint color.
     */
    LOCATOR_BAR_WAYPOINT,
}

@RequiresOptIn("Abstract or unused event class.")
internal annotation class EventInternal