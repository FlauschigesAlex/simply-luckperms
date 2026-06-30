@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
/**
 * Called when the player list name of a player is updated.
 * @param richPlayerListName The rich player list name of the player.
 * @see [net.kyori.adventure.text.minimessage.MiniMessage]
 */
class SLPPlayerListNameUpdateEvent internal constructor(
    player: Player,
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

