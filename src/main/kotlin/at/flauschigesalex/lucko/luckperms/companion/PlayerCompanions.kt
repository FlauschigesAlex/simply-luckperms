package at.flauschigesalex.lucko.luckperms.companion

import at.flauschigesalex.lib.base.cache.Cache
import at.flauschigesalex.lib.base.cache.CacheIdentifier
import at.flauschigesalex.lucko.SimpleLuckoPlugin
import at.flauschigesalex.lucko.config.SimpleLuckoConfig
import at.flauschigesalex.lucko.luckperms.luckPermsUser
import at.flauschigesalex.lucko.luckperms.meta
import at.flauschigesalex.lucko.utils.MiniMessage
import at.flauschigesalex.lucko.utils.uuid
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.uuid.Uuid

@Suppress("unused")
object PlayerCompanions {
    val displays: Set<PlayerCompanionDisplay>
        field = mutableSetOf<PlayerCompanionDisplay>()
    
    init {
        this.register(PlayerCompanionDisplay("displayName") { player ->
            val user = player.luckPermsUser ?: return@PlayerCompanionDisplay
            
            val meta = user.meta

            val usePrefix = SimpleLuckoConfig.useScoreboardPrefix
            val useSuffix = SimpleLuckoConfig.useScoreboardSuffix

            val teamPrefix = MiniMessage.deserialize(meta.teamPrefix)
            val teamSuffix = MiniMessage.deserialize(meta.teamSuffix)
            
            var base = Component.empty()
            if (usePrefix) base = base.append(teamPrefix)
            base = base.append(player.displayName())
            if (useSuffix) base = base.append(teamSuffix)
            
            this.text(base)
        })
        this.register(PlayerCompanionDisplay("displayName1") { player -> 
            this.text(Component.text("test-text").color(TextColor.color(100, 0, 0)))
        })
    }
    
    fun register(display: PlayerCompanionDisplay) {
        displays += display
    }
    fun unregisterById(displayId: String) {
        displays.removeIf { it.id == displayId }
    }
    fun unregister(display: PlayerCompanionDisplay) {
        displays -= display
    }
    
    operator fun get(player: Player): PlayerCompanion {
        val identifier = CacheIdentifier<PlayerCompanion>(player.uuid)
        val cached = Cache[identifier]
        
        if (cached != null) return cached
        
        val companion = PlayerCompanion(player.uuid)
        Cache.put(identifier, companion)
        return companion
    }

    fun remove(player: Player) = remove(player.uuid)
    fun remove(uuid: Uuid) {
        val identifier = CacheIdentifier<PlayerCompanion>(uuid)
        Cache[identifier]?.remove()
        
        Cache.remove(identifier)
    }
}

data class PlayerCompanion(
    val owner: Uuid
) {
    var isAlive: Boolean = false
        private set
    
    private val entities = mutableMapOf<PlayerCompanionDisplay, TextDisplay>()
    private fun getEntity(
        display: PlayerCompanionDisplay,
        player: Player
    ) = entities.getOrPut(display) {
        player.world.spawnEntity(player.location, EntityType.TEXT_DISPLAY) as TextDisplay
    } 
    
    context(player: Player, display: PlayerCompanionDisplay)
    private fun updateDisplay(textDisplay: TextDisplay, index: Int) {
        textDisplay.alignment = TextDisplay.TextAlignment.CENTER
        textDisplay.billboard = Display.Billboard.CENTER
        textDisplay.isSeeThrough = true
        textDisplay.backgroundColor = Color.fromARGB(35, 0, 0, 0)
        textDisplay.textOpacity = -1
        
        var translationY = (index +1) * .25f
        if (player.isSneaking) {
            translationY -= .1f
            textDisplay.textOpacity = 125.toByte()
            textDisplay.isSeeThrough = false
        }
        
        textDisplay.transformation = Transformation(
            Vector3f(0f, translationY, 0f), // TRANSLATE
            Quaternionf(), // LEFT ROTATION
            Vector3f(1f, 1f, 1f), // SCALE
            Quaternionf(), // RIGHT ROTATION
        )

        display.invocation.invoke(textDisplay, player)
        textDisplay.isPersistent = false

        Bukkit.getOnlinePlayers().forEach { online ->
            if (online.canSee(player).not()) {
                online.hideEntity(SimpleLuckoPlugin.instance, textDisplay)
                return@forEach
            }

            if (player.gameMode == GameMode.SPECTATOR && online.gameMode != GameMode.SPECTATOR) {
                online.hideEntity(SimpleLuckoPlugin.instance, textDisplay)
                return@forEach
            }
            
            if (online == player && SimpleLuckoConfig.companionVisibility.displayOwn.not()) {
                online.hideEntity(SimpleLuckoPlugin.instance, textDisplay)
                return@forEach
            }
            
            online.showEntity(SimpleLuckoPlugin.instance, textDisplay)
        }
    }
    
    fun updateOrSpawn(player: Player) {
        if (SimpleLuckoConfig.companionVisibility == PlayerCompanionVisibility.VANILLA)
            return
        
        PlayerCompanions.displays.toSet().forEachIndexed { index, display ->
            val textDisplay = getEntity(display, player)
            
            context(player, display) {
                this.updateDisplay(textDisplay, index)
            }
            
            player.addPassenger(textDisplay)
        }
    }
    
    fun remove() {
        entities.values.forEach { it.remove() }
        entities.clear()
        isAlive = false
    }
}

val Player.Companion: PlayerCompanion
    get() = PlayerCompanions[this]
