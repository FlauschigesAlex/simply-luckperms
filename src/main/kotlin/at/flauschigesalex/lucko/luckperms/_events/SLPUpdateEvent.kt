package at.flauschigesalex.lucko.luckperms._events

import at.flauschigesalex.lucko.luckperms.LuckPermsAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

@EventInternal
abstract class SLPUpdateEvent internal constructor(player: Player) : PlayerEvent(player, Bukkit.isPrimaryThread().not())

/**
 * Display field targeted by a Simply LuckPerms update attempt.
 */
@Suppress("unused")
enum class UpdateField(
    private val invocation: () -> Unit
) {
    
    /**
     * Player list order weight.
     */
    PLAYER_LIST_ORDER({
        LuckPermsAPI.attemptUpdatePlayerListOrder()
    }),

    /**
     * Player list display name.
     */
    PLAYER_LIST_NAME({
        LuckPermsAPI.attemptUpdatePlayerListNames()
    }),

    /**
     * Bukkit display name.
     */
    DISPLAY_NAME({
        LuckPermsAPI.attemptUpdateDisplayNames()
    }),

    /**
     * Scoreboard team.
     */
    SCOREBOARD_TEAM({
        LuckPermsAPI.attemptUpdateTeams()
    }),

    /**
     * Locator bar waypoint color.
     */
    LOCATOR_BAR_WAYPOINT({
        LuckPermsAPI.attemptUpdateWaypoints()
    }),
    ;
    
    internal operator fun invoke() = invocation()
}

@RequiresOptIn("Abstract or unused event class.")
internal annotation class EventInternal