@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
/**
 * Called when the display name of a player is updated.
 * @param richDisplayName The rich display name of the player.
 * @see [net.kyori.adventure.text.minimessage.MiniMessage]
 */
class SLPDisplayNameUpdateEvent internal constructor(
    player: Player,
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

