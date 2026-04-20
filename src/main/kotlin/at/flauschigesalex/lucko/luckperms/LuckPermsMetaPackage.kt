package at.flauschigesalex.lucko.luckperms

import at.flauschigesalex.lib.minecraft.paper.base.utils.Paper
import at.flauschigesalex.lib.minecraft.paper.base.utils.Paper.name
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.luckperms.api.model.PermissionHolder

@OptIn(InternalMeta::class)
data class LuckPermsMetaPackage(
    @InternalMeta val prefixOrNull: String?,
    @InternalMeta val tabPrefixOrNull: String?,
    @InternalMeta val chatPrefixOrNull: String?,
    @InternalMeta val teamPrefixOrNull: String?,
    @InternalMeta val suffixOrNull: String?,
    @InternalMeta val tabSuffixOrNull: String?,
    @InternalMeta val chatSuffixOrNull: String?,
    @InternalMeta val teamSuffixOrNull: String?,
    @InternalMeta val weightOrNull: Int?,
    @InternalMeta val nameColorOrNull: String?,
    @InternalMeta val tabNameColorOrNull: String?,
    @InternalMeta val chatNameColorOrNull: String?,
    @InternalMeta val chatColorOrNull: String?,
    @InternalMeta val teamColorOrNull: NamedTextColor?,
    private val waypointColorOrNull: TextColor?,
) {
    constructor(user: PermissionHolder) : this(
        prefixOrNull = user.getMeta("prefix"),
        tabPrefixOrNull = user.getMeta("prefix-tab"),
        chatPrefixOrNull = user.getMeta("prefix-chat"),
        teamPrefixOrNull = user.getMeta("prefix-team"),
        suffixOrNull = user.getMeta("suffix"),
        tabSuffixOrNull = user.getMeta("suffix-tab"),
        chatSuffixOrNull = user.getMeta("suffix-chat"),
        teamSuffixOrNull = user.getMeta("suffix-team"),
        weightOrNull = user.getMeta("weight")?.toIntOrNull(),
        nameColorOrNull = user.getMeta("color-name"),
        tabNameColorOrNull = user.getMeta("color-name-tab"),
        chatNameColorOrNull = user.getMeta("color-name-chat"),
        chatColorOrNull = user.getMeta("color-chat"),
        teamColorOrNull = user.getMeta("color-team")?.let { name -> Paper.getNamedTextColorValues().find { it.name.equals(name, true) } },
        waypointColorOrNull = user.getMeta("color-waypoint")?.let { name -> Paper.getNamedTextColorValues().find { it.name.equals(name, true) } ?: TextColor.fromHexString(name) },
    )
    
    val prefix: String = prefixOrNull ?: ""
    val suffix: String = suffixOrNull ?: ""
    val weight: Int = weightOrNull ?: 0
    
    val chatColor: String = chatColorOrNull ?: "<white>%s"
    val teamColor: NamedTextColor = teamColorOrNull ?: NamedTextColor.WHITE
    val waypointColor: TextColor? = waypointColorOrNull ?: teamColorOrNull
    
    val tabPrefix: String = tabPrefixOrNull ?: prefix
    val tabSuffix: String = tabSuffixOrNull ?: suffix
    val chatPrefix: String = chatPrefixOrNull ?: prefix
    val chatSuffix: String = chatSuffixOrNull ?: suffix
    val teamPrefix: String = teamPrefixOrNull ?: prefix
    val teamSuffix: String = teamSuffixOrNull ?: suffix
    
    val tabNameColor = tabNameColorOrNull ?: nameColorOrNull ?: ""
    val chatNameColor = chatNameColorOrNull ?: nameColorOrNull ?: ""
}

val PermissionHolder.meta: LuckPermsMetaPackage
    get() = LuckPermsMetaPackage(this)

@RequiresOptIn("Unlikely to use property annotated with this.", RequiresOptIn.Level.ERROR)
@Target(AnnotationTarget.PROPERTY)
annotation class InternalMeta