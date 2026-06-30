@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
/**
 * Called when the waypoint of a player is updated in the [locator bar](https://minecraft.wiki/w/Locator_Bar).
 * Requires Minecraft version 1.21.11 (or higher).
 * @param color The new color of the waypoint.
 */
class SLPWaypointUpdateEvent internal constructor(
    player: Player,
    var color: Color,
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

