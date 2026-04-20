package at.flauschigesalex.lucko.luckperms

import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.PermissionHolder
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import org.bukkit.entity.Player

val LuckPerms = LuckPermsProvider.get()

val Player.userPaired: Pair<Player, User?> 
    get() = Pair(this, this.luckPermsUser)

fun Iterable<Player>.userPaired(): List<Pair<Player, User>> = this.mapNotNull { i -> i.luckPermsUser?.let { i to it } }
fun Iterable<Player>.userPairedNullable(): List<Pair<Player, User?>> = this.map { it to it.luckPermsUser }

val Player.luckPermsUser: User?
    get() = LuckPerms.userManager.getUser(this.uniqueId)

fun Player.hasGroup(group: Group) = this.luckPermsUser?.inheritedGroups?.contains(group) ?: false

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

val User.inheritedGroups: Set<Group>
    get() = this.getInheritedGroups(this.queryOptions).toSet()

val User.mainGroup: Group?
    get() = this.inheritedGroups.firstOrNull()

fun User.getGroupMeta(key: String) = this.inheritedGroups.firstNotNullOfOrNull { group ->
    group.getMeta(key)
}

fun User.getMeta(key: String, useGroups: Boolean = true): String? {
    (this as PermissionHolder).getMeta(key)?.let { return it }
    if (useGroups.not()) return null
    
    return this.getGroupMeta(key)
}