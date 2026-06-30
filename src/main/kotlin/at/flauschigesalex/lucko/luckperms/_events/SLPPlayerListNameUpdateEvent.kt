@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
/**
 * Called when the player list name of a player is updated.
 * @param richPlayerListName The rich player list name of the player.
 * @see [MiniMessage](https://docs.advntr.dev/minimessage/index.html)
 */
class SLPPlayerListNameUpdateEvent internal constructor(
    player: Player,
    /**
     * Rich [MiniMessage](https://docs.advntr.dev/minimessage/index.html) player list name that will be applied.
     */
    var richPlayerListName: String,
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

