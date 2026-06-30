@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
/**
 * Called when the display name of a player is updated.
 * @param richDisplayName The rich display name of the player.
 * @see [MiniMessage](https://docs.advntr.dev/minimessage/index.html)
 */
class SLPDisplayNameUpdateEvent internal constructor(
    player: Player,
    /**
     * Rich [MiniMessage](https://docs.advntr.dev/minimessage/index.html) display name that will be applied.
     */
    var richDisplayName: String,
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

