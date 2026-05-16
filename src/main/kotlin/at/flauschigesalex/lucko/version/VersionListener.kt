package at.flauschigesalex.lucko.version

import at.flauschigesalex.lib.minecraft.paper.base.internal.PaperListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

@Suppress("unused")
private class VersionListener : PaperListener() {
    
    @EventHandler
    private fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (player.isOp.not()) return
        
        val newerVersionResult = VersionChecker.newerVersion ?: return
        val diff = newerVersionResult.getOrNull() ?: return
        
        player.sendNewerVersionMessage(diff)
    }
}