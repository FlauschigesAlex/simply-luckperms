@file:OptIn(EventInternal::class)

package at.flauschigesalex.lucko.luckperms._events

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

@Suppress("unused")
class SLPScoreboardTeamUpdateEvent internal constructor(
    player: Player,
    var usePrefix: Boolean,
    var useSuffix: Boolean,
    var color: NamedTextColor,
    var richPrefix: String,
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

