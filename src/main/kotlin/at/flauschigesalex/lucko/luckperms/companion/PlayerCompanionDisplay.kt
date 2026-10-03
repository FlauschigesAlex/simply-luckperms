package at.flauschigesalex.lucko.luckperms.companion

import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay

typealias PlayerCompanionInvocation = TextDisplay.(Player) -> Unit

data class PlayerCompanionDisplay(
    val id: String,
    internal val invocation: PlayerCompanionInvocation
) {
    override fun equals(other: Any?): Boolean = other is PlayerCompanionDisplay && this.id == other.id
    override fun hashCode(): Int = id.hashCode()
}