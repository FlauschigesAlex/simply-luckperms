package at.flauschigesalex.lucko.luckperms

import at.flauschigesalex.lucko.SimpleLuckoConfig
import at.flauschigesalex.lucko.luckperms._events.SLPDisplayNameUpdateEvent
import at.flauschigesalex.lucko.luckperms._events.SLPFieldAttemptUpdateEvent
import at.flauschigesalex.lucko.luckperms._events.SLPPlayerListNameUpdateEvent
import at.flauschigesalex.lucko.luckperms._events.SLPPlayerListOrderUpdateEvent
import at.flauschigesalex.lucko.luckperms._events.SLPScoreboardTeamUpdateEvent
import at.flauschigesalex.lucko.luckperms._events.SLPWaypointUpdateEvent
import at.flauschigesalex.lucko.luckperms._events.UpdateField
import at.flauschigesalex.lucko.utils.MiniMessage
import at.flauschigesalex.lucko.utils.isMainScoreboard
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

/**
 * Public API for applying [LuckPerms](https://luckperms.net/wiki/Developer-API) meta to Bukkit player displays.
 */
@Suppress("unused")
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
    
    /**
     * Attempts to apply all configured display updates for every online player.
     * This attempt can be cancelled per player and field by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListOrderUpdateEvent
     * @see SLPPlayerListNameUpdateEvent
     * @see SLPDisplayNameUpdateEvent
     * @see SLPScoreboardTeamUpdateEvent
     * @see SLPWaypointUpdateEvent
     */
    fun attemptUpdateEverything() {
        Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) ->
            this.attemptUpdateEverything(player, user)
        }
    }
    /**
     * Attempts to apply all configured display updates for one player and LuckPerms user.
     * This attempt can be cancelled per field by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListOrderUpdateEvent
     * @see SLPPlayerListNameUpdateEvent
     * @see SLPDisplayNameUpdateEvent
     * @see SLPScoreboardTeamUpdateEvent
     * @see SLPWaypointUpdateEvent
     */
    fun attemptUpdateEverything(player: Player, user: User) {
        this.attemptUpdatePlayerListOrder(player, user)
        this.attemptUpdatePlayerListName(player, user)
        this.attemptUpdateTeam(player, user)
        this.attemptUpdateWaypoint(player, user)
        this.attemptUpdateDisplayName(player, user)
    }

    /**
     * Attempts to update player list order for every online player when tab display updates are enabled.
     * This attempt can be cancelled per player by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListOrderUpdateEvent
     * @see UpdateField.PLAYER_LIST_ORDER
     */
    fun attemptUpdatePlayerListOrder() {
        if (SimpleLuckoConfig.useTabDisplay.not()) return
        Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) ->
            this.attemptUpdatePlayerListOrder(player, user)
        }
    }
    /**
     * Attempts to update player list order for one player when tab display updates are enabled and the attempt event is not cancelled.
     * This attempt can be cancelled by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListOrderUpdateEvent
     * @see UpdateField.PLAYER_LIST_ORDER
     */
    fun attemptUpdatePlayerListOrder(player: Player, user: User) {
        if (SimpleLuckoConfig.useTabDisplay.not()) return
        if (SLPFieldAttemptUpdateEvent(player, UpdateField.PLAYER_LIST_ORDER).callEvent().not()) return
        this.updatePlayerListOrder(player, user)
    }

    /**
     * Attempts to update player list names for every online player when tab display updates are enabled.
     * This attempt can be cancelled per player by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListNameUpdateEvent
     * @see UpdateField.PLAYER_LIST_NAME
     */
    fun attemptUpdatePlayerListNames() {
        if (SimpleLuckoConfig.useTabDisplay.not()) return
        Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) ->
            this.attemptUpdatePlayerListName(player, user)
        }
    }
    /**
     * Attempts to update the player list name for one player when tab display updates are enabled and the attempt event is not cancelled.
     * This attempt can be cancelled by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListNameUpdateEvent
     * @see UpdateField.PLAYER_LIST_NAME
     */
    fun attemptUpdatePlayerListName(player: Player, user: User) {
        if (SimpleLuckoConfig.useTabDisplay.not()) return
        if (SLPFieldAttemptUpdateEvent(player, UpdateField.PLAYER_LIST_NAME).callEvent().not()) return
        this.updatePlayerListName(player, user)
    }
    
    /**
     * Attempts to update display names for every online player when display name updates are enabled.
     * This attempt can be cancelled per player by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPDisplayNameUpdateEvent
     * @see UpdateField.DISPLAY_NAME
     */
    fun attemptUpdateDisplayNames() {
        if (SimpleLuckoConfig.useDisplayName.not()) return
        Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) ->
            this.attemptUpdateDisplayName(player, user)
        }
    }
    /**
     * Attempts to update the display name for one player when display name updates are enabled and the attempt event is not cancelled.
     * This attempt can be cancelled by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPDisplayNameUpdateEvent
     * @see UpdateField.DISPLAY_NAME
     */
    fun attemptUpdateDisplayName(player: Player, user: User) {
        if (SimpleLuckoConfig.useDisplayName.not()) return
        if (SLPFieldAttemptUpdateEvent(player, UpdateField.DISPLAY_NAME).callEvent().not()) return
        this.updateDisplayName(player, user)
    }

    /**
     * Attempts to update scoreboard teams for every online player when scoreboard team updates are enabled.
     * This attempt can be cancelled per player by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPScoreboardTeamUpdateEvent
     * @see UpdateField.SCOREBOARD_TEAM
     */
    fun attemptUpdateTeams() {
        if (SimpleLuckoConfig.useScoreboardTeams.not()) return
        Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) ->
            this.attemptUpdateTeam(player, user)
        }
    }
    /**
     * Attempts to update the scoreboard team for one player when scoreboard team updates are enabled and the attempt event is not cancelled.
     * This attempt can be cancelled by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPScoreboardTeamUpdateEvent
     * @see UpdateField.SCOREBOARD_TEAM
     */
    fun attemptUpdateTeam(player: Player, user: User) {
        if (SimpleLuckoConfig.useScoreboardTeams.not()) return
        if (SLPFieldAttemptUpdateEvent(player, UpdateField.SCOREBOARD_TEAM).callEvent().not()) return
        this.updateTeam(player, user)
    }

    /**
     * Attempts to update locator bar waypoint colors for every online player when waypoint updates are enabled.
     * This attempt can be cancelled per player by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPWaypointUpdateEvent
     * @see UpdateField.LOCATOR_BAR_WAYPOINT
     */
    fun attemptUpdateWaypoints() {
        if (SimpleLuckoConfig.useWaypointColor.not()) return
        Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) ->
            this.attemptUpdateWaypoint(player, user)
        }
    }
    /**
     * Attempts to update the locator bar waypoint color for one player when waypoint updates are enabled and the attempt event is not cancelled.
     * This attempt can be cancelled by [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPWaypointUpdateEvent
     * @see UpdateField.LOCATOR_BAR_WAYPOINT
     */
    fun attemptUpdateWaypoint(player: Player, user: User) {
        if (SimpleLuckoConfig.useWaypointColor.not()) return
        if (SLPFieldAttemptUpdateEvent(player, UpdateField.LOCATOR_BAR_WAYPOINT).callEvent().not()) return
        this.updateWaypoint(player, user)
    }
    
    // IMPLEMENTATION

    /**
     * Applies all display updates directly for every online player.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListOrderUpdateEvent
     * @see SLPPlayerListNameUpdateEvent
     * @see SLPDisplayNameUpdateEvent
     * @see SLPScoreboardTeamUpdateEvent
     * @see SLPWaypointUpdateEvent
     */
    fun updateEverything() {
        Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) ->
            updatePlayerListOrder(player, user)
            updatePlayerListName(player, user)
            updateWaypoint(player, user)
            updateDisplayName(player, user)
        }
        updateTeams()
    }
    /**
     * Applies all display updates directly for one player and LuckPerms user.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListOrderUpdateEvent
     * @see SLPPlayerListNameUpdateEvent
     * @see SLPDisplayNameUpdateEvent
     * @see SLPScoreboardTeamUpdateEvent
     * @see SLPWaypointUpdateEvent
     */
    fun updateEverything(player: Player, user: User) {
        updatePlayerListOrder(player, user)
        updatePlayerListName(player, user)
        updateWaypoint(player, user)
        updateTeam(player, user)
        updateDisplayName(player, user)
    }

    /**
     * Applies player list order directly for every online player.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListOrderUpdateEvent
     */
    fun updatePlayerListOrder() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updatePlayerListOrder(player, user) }
    private fun updatePlayerListOrder(player: Player, user: User) {
        val weight = user.cachedData.metaData.getMetaValue("weight")?.toIntOrNull() ?: 0
        val event = SLPPlayerListOrderUpdateEvent(player, weight).apply { callEvent() }
        player.playerListOrder = event.playerListOrderWeight
    }

    /**
     * Applies player list names directly for every online player.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListNameUpdateEvent
     */
    fun updatePlayerListNames() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updatePlayerListName(player, user) }

    /**
     * Applies the player list name directly for one player using [MiniMessage](https://docs.advntr.dev/minimessage/index.html) formatting.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPPlayerListNameUpdateEvent
     */
    fun updatePlayerListName(player: Player, user: User) {
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

        val event = SLPPlayerListNameUpdateEvent(player, full).apply { callEvent() }

        val component = MiniMessage.deserialize(event.richPlayerListName)
        player.playerListName(component)
    }

    /**
     * Applies display names directly for every online player.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPDisplayNameUpdateEvent
     */
    fun updateDisplayNames() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updateDisplayName(player, user) }

    /**
     * Applies the display name directly for one player using [MiniMessage](https://docs.advntr.dev/minimessage/index.html) formatting.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPDisplayNameUpdateEvent
     */
    fun updateDisplayName(player: Player, user: User) {
        val meta = user.meta

        val username = "<color:${meta.teamColor.asHexString()}>${player.name}"
        val event = SLPDisplayNameUpdateEvent(player, username).apply { callEvent() }

        val component = MiniMessage.deserialize(event.richDisplayName)
        player.displayName(component)
    }

    /**
     * Applies scoreboard teams directly for every online player.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPScoreboardTeamUpdateEvent
     */
    fun updateTeams() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updateTeam(player, user) }

    /**
     * Applies the scoreboard team directly for one player using LuckPerms group and meta values.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPScoreboardTeamUpdateEvent
     */
    fun updateTeam(player: Player, user: User) {
        val scoreboard = getScoreboard(player)
        val boards = if (scoreboard.isMainScoreboard) listOf(scoreboard) else Bukkit.getOnlinePlayers().map { getScoreboard(it) }
        
        val group = user.mainGroup ?: return
        val meta = user.meta
        
        var usePrefix = SimpleLuckoConfig.useScoreboardPrefix
        var useSuffix = SimpleLuckoConfig.useScoreboardSuffix
        
        val event = SLPScoreboardTeamUpdateEvent(
            player,
            usePrefix,
            useSuffix,
            meta.teamColor,
            meta.teamPrefix,
            meta.teamSuffix,
        ).apply { callEvent() }
        
        usePrefix = event.usePrefix
        useSuffix = event.useSuffix
        
        val teamPrefix = MiniMessage.deserialize(event.richPrefix)
        val teamSuffix = MiniMessage.deserialize(event.richSuffix)
        
        boards.forEach { board ->
            val team = board.getTeam(group.name) ?: board.registerNewTeam(group.name)

            team.color(event.color)
            
            if (usePrefix) team.prefix(teamPrefix)
            if (useSuffix) team.suffix(teamSuffix)

            team.addPlayer(player)
        }
    }
    
    /**
     * Applies locator bar waypoint colors directly for every online player.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPWaypointUpdateEvent
     */
    fun updateWaypoints() = Bukkit.getOnlinePlayers().userPaired().forEach { (player, user) -> updateWaypoint(player, user) }

    /**
     * Applies the locator bar waypoint color directly for one player.
     * This method does not call the cancellable [SLPFieldAttemptUpdateEvent].
     *
     * @see SLPFieldAttemptUpdateEvent
     * @see SLPWaypointUpdateEvent
     */
    fun updateWaypoint(player: Player, user: User) {
        val color = user.meta.waypointColor ?: return
        runCatching { // Change waypoint color => depends on the version
            val color = Color.fromRGB(color.value())
            val event = SLPWaypointUpdateEvent(player, color).apply { callEvent() }
            player.waypointColor = event.color
        }
    }
    
    private fun getScoreboard(player: Player): Scoreboard {
        if (SimpleLuckoConfig.usePrivateScoreboard && player.scoreboard.isMainScoreboard)
            player.scoreboard = Bukkit.getScoreboardManager().newScoreboard
        
        return player.scoreboard
    }
}