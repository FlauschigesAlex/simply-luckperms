@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
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

