@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
/**
 * Called when a scoreboard team is updated for a player.
 * @param color The display color of the team.
 * @see [MiniMessage](https://docs.advntr.dev/minimessage/index.html)
 * @see NamedTextColor
 */
class SLPScoreboardTeamUpdateEvent internal constructor(
    player: Player,
    /**
     * Whether the scoreboard team prefix should be applied.
     */
    var usePrefix: Boolean,

    /**
     * Whether the scoreboard team suffix should be applied.
     */
    var useSuffix: Boolean,

    /**
     * Team color that will be applied.
     */
    var color: NamedTextColor,

    /**
     * Rich [MiniMessage](https://docs.advntr.dev/minimessage/index.html) team prefix that will be applied.
     */
    var richPrefix: String,

    /**
     * Rich [MiniMessage](https://docs.advntr.dev/minimessage/index.html) team suffix that will be applied.
     */
    var richSuffix: String,
) : SLPUpdateEvent(player) {

    companion object {
        private val handlers = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }
    
    override fun getHandlers(): HandlerList = getHandlerList()
}

