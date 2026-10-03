package at.flauschigesalex.lucko.luckperms.companion

import at.flauschigesalex.lib.minecraft.paper.base.internal.PaperListener
import at.flauschigesalex.lucko.luckperms._events.SLPScoreboardTeamUpdateEvent
import at.flauschigesalex.lucko.utils.delayTick
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

@Suppress("unused")
private class CompanionListener : PaperListener() {
    
    @EventHandler(priority = EventPriority.MONITOR)
    private fun onJoin(event: SLPScoreboardTeamUpdateEvent) {
        val player = event.player
        val companion = player.Companion
        
        companion.updateOrSpawn(player)
    }
    
    @EventHandler
    private fun onSneak(event: PlayerToggleSneakEvent) {
        val player = event.player
        val companion = player.Companion

        delayTick {
            companion.updateOrSpawn(player)
        }
    }
    
    @EventHandler
    private fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val companion = player.Companion

        companion.remove()
    }
}