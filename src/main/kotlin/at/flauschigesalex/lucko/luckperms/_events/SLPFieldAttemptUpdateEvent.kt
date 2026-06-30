@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList

@Suppress("unused")
/**
 * Called when the plugin attempts to update a field.
 * @param field The field that is being attempted to update.
 * @see UpdateField
 */
class SLPFieldAttemptUpdateEvent internal constructor(
    player: Player,
    val field: UpdateField
) : SLPUpdateEvent(player), Cancellable {

    companion object {
        private val handlers = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }
    
    private var isCancelled: Boolean = false
    override fun isCancelled(): Boolean = isCancelled
    override fun setCancelled(cancel: Boolean) {
        this.isCancelled = cancel
    }

    override fun getHandlers(): HandlerList = getHandlerList()
}

