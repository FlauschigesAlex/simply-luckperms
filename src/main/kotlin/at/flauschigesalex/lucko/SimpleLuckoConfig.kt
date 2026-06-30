package at.flauschigesalex.lucko

import at.flauschigesalex.lib.base.file.FileManager
import at.flauschigesalex.lib.base.file.JsonManager
import at.flauschigesalex.lib.base.file.ResourceManager
import at.flauschigesalex.lib.base.file.readJson
import at.flauschigesalex.lucko.utils.scheduleAsync

object SimpleLuckoConfig {
    
    private const val VERSION = 1
    
    private val file: FileManager = FileManager(SimpleLuckoPlugin.instance.dataFolder, "config.json")
    private lateinit var json: JsonManager
    
    init {
        this.attemptCreateConfig()
        this.reloadConfig()
    }
    
    /**
     * Reloads the plugin configuration from disk.
     */
    fun reloadConfig() {
        json = file.readJson() ?: JsonManager()
    }
    
    internal val _configVersion: Int
        get() = json.getInt("_version") ?: 1
    
    internal var internalNodeAwaitDelay: Long
        get() = json.getLong("config._node.awaitTick") ?: 5
        set(value) { json.set("config._node.awaitTick", value) }
    
    /**
     * Whether chat messages should be formatted through [Simply LuckPerms](https://modrinth.com/plugin/simply-luckperms).
     */
    var useChatDisplay: Boolean
        get() = json.getBoolean("config.display.chat.use") ?: true
        set(value) { json.set("config.display.chat.use", value) }
    
    /**
     * [MiniMessage](https://docs.advntr.dev/minimessage/index.html) chat format used when [useChatDisplay] is enabled.
     */
    var chatDisplay: String
        get() = json.getString("config.display.chat.format") ?: "%prefix%%username%%suffix%<dark_gray>: %message%"
        set(value) { json.set("config.display.chat.format", value) }
    
    /**
     * Whether player list names and ordering should be updated.
     */
    var useTabDisplay: Boolean
        get() = json.getBoolean("config.display.tab.use") ?: true
        set(value) { json.set("config.display.tab.use", value) }
    
    /**
     * [MiniMessage](https://docs.advntr.dev/minimessage/index.html) player list name format used when [useTabDisplay] is enabled.
     */
    var tabDisplay: String
        get() = json.getString("config.display.tab.format") ?: "%prefix%%username%%suffix%"
        set(value) { json.set("config.display.tab.format", value) }
    
    /**
     * Whether scoreboard teams should be created and updated for players.
     */
    var useScoreboardTeams: Boolean
        get() = json.getBoolean("config.display.teams.use") ?: true
        set(value) { json.set("config.display.teams.use", value) }
    
    /**
     * Whether [locator bar](https://minecraft.wiki/w/Locator_Bar) waypoint colors should be updated from [LuckPerms meta](https://luckperms.net/wiki/Prefixes,-Suffixes-&-Meta).
     */
    var useWaypointColor: Boolean
        get() = json.getBoolean("config.display.waypoint.use") ?: true
        set(value) { json.set("config.display.waypoint.use", value) }

    /**
     * Whether [Bukkit](https://jd.papermc.io/paper/1.21.8/org/bukkit/Bukkit.html) display names should be updated from [LuckPerms meta](https://luckperms.net/wiki/Prefixes,-Suffixes-&-Meta).
     */
    var useDisplayName: Boolean
        get() = json.getBoolean("config.display.name.use") ?: true
        set(value) { json.set("config.display.name.use", value) }
    
    /**
     * Whether players on the main scoreboard should receive a private scoreboard before team updates.
     */
    var usePrivateScoreboard: Boolean
        get() = json.getBoolean("config.scoreboard.private.use") ?: true
        set(value) { json.set("config.scoreboard.private.use", value) }
    
    /**
     * Whether scoreboard team prefixes should be applied.
     */
    var useScoreboardPrefix: Boolean
        get() = json.getBoolean("config.scoreboard.prefix.use") ?: true
        set(value) { json.set("config.scoreboard.prefix.use", value) }
    
    /**
     * Whether scoreboard team suffixes should be applied.
     */
    var useScoreboardSuffix: Boolean
        get() = json.getBoolean("config.scoreboard.suffix.use") ?: true
        set(value) { json.set("config.scoreboard.suffix.use", value) }

    /**
     * Saves the current configuration if it has changed.
     *
     * @param async Whether the save should be scheduled asynchronously.
     */
    fun saveConfig(async: Boolean) {
        if (json.isOriginalContent()) return
        
        if (async) return scheduleAsync { this.saveConfig(false) }
        
        file.createFile()
        file.write(json)
    }
    
    private fun attemptCreateConfig() {
        if (file.exists) return
        file.createFile()
        
        val defaultConfig = ResourceManager("default-config.json", javaClass.classLoader)?.readBytes() ?: return
        file.write(defaultConfig)
    }
}