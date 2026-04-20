@file:Suppress("UnstableApiUsage")

package at.flauschigesalex.lucko.luckperms

import at.flauschigesalex.lib.minecraft.paper.base.internal.PaperListener
import at.flauschigesalex.lucko.SimpleLuckoConfig
import at.flauschigesalex.lucko.utils.MiniMessage
import at.flauschigesalex.lucko.utils.delayTick
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

@Suppress("unused")
private class LuckPermsListener : PaperListener() {
    
    @EventHandler
    private fun onPlayerLogin(event: PlayerJoinEvent) {
        LuckPermsAPI.attemptUpdateEverything()
        
        delayTick(SimpleLuckoConfig.internalNodeAwaitDelay) {
            LuckPermsAPI.attemptUpdateEverything()
        }
    }
    
    @EventHandler
    private fun onPlayerChat(event: AsyncChatEvent) {
        if (SimpleLuckoConfig.useChatDisplay.not()) return
        
        val player = event.player
        val user = player.luckPermsUser ?: return

        val textComponent = event.originalMessage() as? TextComponent ?: return
        val text = textComponent.content()
        val meta = user.meta
        
        val prefix = meta.chatPrefix
        val suffix = meta.chatSuffix
        val chatColor = meta.chatColor
        
        val username = meta.chatNameColor.let { if (it.contains("%s")) it else "$it%s" }.format(player.name)
        val message = chatColor.let { if (it.contains("%s")) it else "$it%s" }.format(text)

        val full = SimpleLuckoConfig.chatDisplay
            .replace("%prefix%", prefix)
            .replace("%suffix%", suffix)
            .replace("%weight%", meta.weight.toString())
            .replace("%username%", username)
            .replace("%message%", message)
            .replace("%team-color%", "<color:${meta.teamColor.asHexString()}>")
        
        val component = MiniMessage.deserialize(full)
        event.renderer { player, playerName, message, audience -> 
            return@renderer component
        }
    }
}