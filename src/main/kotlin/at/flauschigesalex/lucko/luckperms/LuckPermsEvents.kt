package at.flauschigesalex.lucko.luckperms

import at.flauschigesalex.lucko.SimpleLuckoConfig
import at.flauschigesalex.lucko.SimpleLuckoPlugin
import at.flauschigesalex.lucko.utils.delayTick
import at.flauschigesalex.lucko.utils.scheduleSync
import net.luckperms.api.event.group.GroupDataRecalculateEvent
import net.luckperms.api.event.node.NodeAddEvent
import net.luckperms.api.event.node.NodeRemoveEvent
import net.luckperms.api.event.player.PlayerLoginProcessEvent
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import net.luckperms.api.node.types.*
import org.bukkit.Bukkit

object LuckPermsEvents {
    init {
        val plugin = SimpleLuckoPlugin.instance
        
        LuckPerms.eventBus.subscribe(plugin, PlayerLoginProcessEvent::class.java) { event ->
            scheduleSync {
                val user = event.user ?: return@scheduleSync
                val player = Bukkit.getPlayer(user.uniqueId) ?: return@scheduleSync
                LuckPermsAPI.attemptUpdateEverything(player, user)
            }
        }
        LuckPerms.eventBus.subscribe(plugin, GroupDataRecalculateEvent::class.java) { event ->
            scheduleSync { Bukkit.getOnlinePlayers()
                .filter { it.hasGroup(event.group) }
                .userPaired()
                .forEach { (player, user) ->
                    LuckPermsAPI.attemptUpdateEverything(player, user)
                }
            }
        }
        
        val affectedNodes = setOf<Class<out Node>>(
            PrefixNode::class.java,
            SuffixNode::class.java,
            WeightNode::class.java,
            InheritanceNode::class.java,
            DisplayNameNode::class.java,
            ChatMetaNode::class.java,
            MetaNode::class.java,
        )
        
        LuckPerms.eventBus.subscribe(plugin, NodeAddEvent::class.java) { event -> 
            val node = event.node
            val target = event.target
            
            if (affectedNodes.none { it.isInstance(node) })
                return@subscribe

            delayTick(SimpleLuckoConfig.internalNodeAwaitDelay) {
                if (target is User) LuckPermsAPI.updateByUser(target)
                else if (target is Group) LuckPermsAPI.updateByGroup(target)
            }
        }
        LuckPerms.eventBus.subscribe(plugin, NodeRemoveEvent::class.java) { event ->
            val node = event.node
            val target = event.target

            if (affectedNodes.none { it.isInstance(node) })
                return@subscribe

            delayTick(SimpleLuckoConfig.internalNodeAwaitDelay) {
                if (target is User) LuckPermsAPI.updateByUser(target)
                else if (target is Group) LuckPermsAPI.updateByGroup(target)
            }
        }
    }
}
