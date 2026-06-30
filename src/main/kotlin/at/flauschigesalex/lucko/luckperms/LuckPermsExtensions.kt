package at.flauschigesalex.lucko.luckperms

import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.PermissionHolder
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import org.bukkit.entity.Player

internal val LuckPerms = LuckPermsProvider.get()

val Player.userPaired: Pair<Player, User?> 
    get() = Pair(this, this.luckPermsUser)

fun Iterable<Player>.userPaired(): List<Pair<Player, User>> = this.mapNotNull { i -> i.luckPermsUser?.let { i to it } }
fun Iterable<Player>.userPairedNullable(): List<Pair<Player, User?>> = this.map { it to it.luckPermsUser }
/**
 * Pairs every player in this iterable with its loaded [LuckPerms](https://luckperms.net/wiki/Developer-API) user and drops players without a loaded user.
 */

/**
 * Pairs every player in this iterable with its loaded [LuckPerms](https://luckperms.net/wiki/Developer-API) user, preserving players without a loaded user.
 */

/**
 * Loaded [LuckPerms](https://luckperms.net/wiki/Developer-API) user for this player, or `null` when LuckPerms has not loaded it.
 */
val Player.luckPermsUser: User?
    get() = LuckPerms.userManager.getUser(this.uniqueId)

/**
 * Checks whether this player currently inherits the given [LuckPerms](https://luckperms.net/wiki/Developer-API) group.
 */
fun Player.hasGroup(group: Group) = this.luckPermsUser?.inheritedGroups?.contains(group) ?: false

/**
 * Reads a [LuckPerms meta](https://luckperms.net/wiki/Prefixes,-Suffixes-&-Meta) value from a permission holder, including supported aliases such as `prefix`, `suffix`, and `name`.
 */
fun PermissionHolder.getMeta(key: String): String? = when {
    key == "prefix" -> cachedData.metaData.prefix
    key == "prefixes" -> cachedData.metaData.prefixes.entries.joinToString(" ")
    key == "suffix" -> cachedData.metaData.suffix
    key == "suffixes" -> cachedData.metaData.suffixes.entries.joinToString(" ")
    this is Group -> {
        when (key) {
            "name" -> this.name
            "displayName" -> this.displayName
            else -> cachedData.metaData.getMetaValue(key)
        }
    }
    this is User -> {
        when (key) {
            "primaryGroup" -> this.primaryGroup
            else -> cachedData.metaData.getMetaValue(key)
        }
    }
    else -> cachedData.metaData.getMetaValue(key)
}

/**
 * Groups inherited by this user.
 */
val User.inheritedGroups: Set<Group>
    get() = this.getInheritedGroups(this.queryOptions).toSet()

/**
 * First inherited group for this user, or `null` when no group is available.
 */
val User.mainGroup: Group?
    get() = this.inheritedGroups.firstOrNull()

/**
 * Reads the first available meta value with the given key from this user's inherited groups.
 */
fun User.getGroupMeta(key: String) = this.inheritedGroups.firstNotNullOfOrNull { group ->
    group.getMeta(key)
}

/**
 * Reads a meta value from this user and optionally falls back to inherited groups.
 *
 * @param key [LuckPerms meta](https://luckperms.net/wiki/Prefixes,-Suffixes-&-Meta) key to read.
 * @param useGroups Whether inherited groups should be queried when the user has no direct value.
 */
fun User.getMeta(key: String, useGroups: Boolean = true): String? {
    (this as PermissionHolder).getMeta(key)?.let { return it }
    if (useGroups.not()) return null
    
    return this.getGroupMeta(key)
}