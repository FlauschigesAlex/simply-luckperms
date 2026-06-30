@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
/**
 * Called when the player list order of a player is updated.
 * @param playerListOrderWeight The player list order weight of the player.
 */
class SLPPlayerListOrderUpdateEvent internal constructor(
    player: Player,
    var playerListOrderWeight: Int,
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

