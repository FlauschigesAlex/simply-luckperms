package at.flauschigesalex.lucko.luckperms

import at.flauschigesalex.lucko.SimpleLuckoConfig
import at.flauschigesalex.lucko.utils.MiniMessage
import at.flauschigesalex.lucko.utils.isMainScoreboard
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import net.luckperms.api.node.types.PermissionNode
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

object LuckPermsAPI {
    
    // LUCKPERMS IMPLEMENTATION
    
    internal fun updateByGroup(group: Group) {
        Bukkit.getOnlinePlayers().filter { it.hasGroup(group) }
            .userPaired().forEach { (player, user) ->
                this.attemptUpdateEverything(player, user)
            }
        
        this.attemptUpdateTeams()
    }
    internal fun updateByUser(user: User) {
        val player = Bukkit.getPlayer(user.uniqueId) ?: return
        this.attemptUpdateEverything(player, user)
        this.attemptUpdateTeams()
    }
    
    // IMPLEMENTATION ATTEMPTS
    
    fun attemptUpdateEverything() {
        this.attemptUpdatePlayerListOrder()
        this.attemptUpdatePlayerListNames()
        this.attemptUpdateTeams()
        this.attemptUpdateWaypoints()
        this.attemptUpdateDisplayNames()
    }
    fun attemptUpdateEverything(player: Player, user: User) {
        this.attemptUpdatePlayerListOrder(player, user)
        this.attemptUpdatePlayerListName(player, user)
        this.attemptUpdateTeam(player, user)
        this.attemptUpdateWaypoint(player, user)
        this.attemptUpdateDisplayName(player, user)
    }

    fun attemptUpdatePlayerListOrder() {
        if (SimpleLuckoConfig.useTabDisplay.not()) return
        this.updatePlayerListOrder()
    }
    fun attemptUpdatePlayerListOrder(player: Player, user: User) {
        if (SimpleLuckoConfig.useTabDisplay.not()) return
        this.updatePlayerListOrder(player, user)
    }

    fun attemptUpdatePlayerListNames() {
        if (SimpleLuckoConfig.useTabDisplay.not()) return
        this.updatePlayerListNames()
    }
    fun attemptUpdatePlayerListName(player: Player, user: User) {
        if (SimpleLuckoConfig.useTabDisplay.not()) return
        this.updatePlayerListName(player, user)
    }
    
    fun attemptUpdateDisplayNames() {
        if (SimpleLuckoConfig.useDisplayName.not()) return
        this.updateDisplayNames()
    }
    fun attemptUpdateDisplayName(player: Player, user: User) {
        if (SimpleLuckoConfig.useDisplayName.not()) return
        this.updateDisplayName(player, user)
    }

    fun attemptUpdateTeams() {
        if (SimpleLuckoConfig.useScoreboardTeams.not()) return
        this.updateTeams()
    }
    fun attemptUpdateTeam(player: Player, user: User) {
        if (SimpleLuckoConfig.useScoreboardTeams.not()) return
        this.updateTeam(player, user)
    }

    fun attemptUpdateWaypoints() {
        if (SimpleLuckoConfig.useWaypointColor.not()) return
        this.updateWaypoints()
    }
    fun attemptUpdateWaypoint(player: Player, user: User) {
        if (SimpleLuckoConfig.useWaypointColor.not()) return
        this.updateWaypoint(player, user)
    }
    
    // IMPLEMENTATION

    private fun updateEverything() {
        Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) ->
            updatePlayerListOrder(player, user)
            updatePlayerListName(player, user)
            updateWaypoint(player, user)
            updateDisplayName(player, user)
        }
        updateTeams()
    }
    private fun updateEverything(player: Player, user: User) {
        updatePlayerListOrder(player, user)
        updatePlayerListName(player, user)
        updateWaypoint(player, user)
        updateTeam(player, user)
        updateDisplayName(player, user)
    }

    private fun updatePlayerListOrder() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updatePlayerListOrder(player, user) }
    private fun updatePlayerListOrder(player: Player, user: User) {
        val weight = user.cachedData.metaData.getMetaValue("weight")?.toIntOrNull() ?: 0
        player.playerListOrder = weight
    }

    private fun updatePlayerListNames() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updatePlayerListName(player, user) }
    private fun updatePlayerListName(player: Player, user: User) {
        val meta = user.meta

        val prefix = meta.tabPrefix
        val suffix = meta.tabSuffix

        val username = meta.tabNameColor.let { if (it.contains("%s")) it else "$it%s" }.format(player.name)

        val full = SimpleLuckoConfig.tabDisplay
            .replace("%prefix%", prefix)
            .replace("%suffix%", suffix)
            .replace("%weight%", meta.weight.toString())
            .replace("%username%", username)
            .replace("%team-color%", "<color:${meta.teamColor.asHexString()}>")

        val component = MiniMessage.deserialize(full)
        player.playerListName(component)
    }

    private fun updateDisplayNames() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updatePlayerListName(player, user) }
    private fun updateDisplayName(player: Player, user: User) {
        val meta = user.meta

        val username = "<color:${meta.teamColor.asHexString()}>${player.name}"

        val component = MiniMessage.deserialize(username)
        player.displayName(component)
    }

    private fun updateTeams() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updateTeam(player, user) }
    private fun updateTeam(player: Player, user: User) {
        val scoreboard = getScoreboard(player)
        val boards = if (scoreboard.isMainScoreboard) listOf(scoreboard) else Bukkit.getOnlinePlayers().map { getScoreboard(it) }
        
        val group = user.mainGroup ?: return
        val meta = user.meta
        
        val usePrefix = SimpleLuckoConfig.useScoreboardPrefix
        val useSuffix = SimpleLuckoConfig.useScoreboardSuffix
        
        boards.forEach { board ->
            val team = board.getTeam(group.name) ?: board.registerNewTeam(group.name)

            team.color(meta.teamColor)

            if (usePrefix) team.prefix(MiniMessage.deserialize(meta.teamPrefix))
            if (useSuffix) team.suffix(MiniMessage.deserialize(meta.teamSuffix))

            team.addPlayer(player)
        }
    }
    
    private fun updateWaypoints() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updateWaypoint(player, user) }
    private fun updateWaypoint(player: Player, user: User) {
        val color = user.meta.waypointColor ?: return
        runCatching { // Change waypoint color => depends on the version
            player.waypointColor = Color.fromRGB(color.value())
        }
    }
    
    private fun getScoreboard(player: Player): Scoreboard {
        if (SimpleLuckoConfig.usePrivateScoreboard && player.scoreboard.isMainScoreboard)
            player.scoreboard = Bukkit.getScoreboardManager().newScoreboard
        
        return player.scoreboard
    }
}