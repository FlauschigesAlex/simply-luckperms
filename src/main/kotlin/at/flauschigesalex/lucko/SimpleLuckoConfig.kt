package at.flauschigesalex.lucko

import at.flauschigesalex.lib.base.file.FileManager
import at.flauschigesalex.lib.base.file.JsonManager
import at.flauschigesalex.lib.base.file.readJson
import at.flauschigesalex.lucko.utils.scheduleAsync

object SimpleLuckoConfig {
    
    private val file: FileManager = FileManager(SimpleLuckoPlugin.instance.dataFolder, "config.json")
    private lateinit var json: JsonManager
    
    init {
        this.reloadConfig()
    }
    
    fun reloadConfig() {
        json = file.readJson() ?: JsonManager()
    }
    
    internal var internalNodeAwaitDelay: Long
        get() = json.getLong("config._node.awaitTick") ?: 5
        set(value) { json.set("config._node.awaitTick", value) }
    
    var useChatDisplay: Boolean
        get() = json.getBoolean("config.display.chat.use") ?: true
        set(value) { json.set("config.display.chat.use", value) }
    
    var chatDisplay: String
        get() = json.getString("config.display.chat.format") ?: "%prefix%%username%%suffix%<dark_gray>: %message%"
        set(value) { json.set("config.display.chat.format", value) }
    
    var useTabDisplay: Boolean
        get() = json.getBoolean("config.display.tab.use") ?: true
        set(value) { json.set("config.display.tab.use", value) }
    
    var tabDisplay: String
        get() = json.getString("config.display.tab.format") ?: "%prefix%%username%%suffix%"
        set(value) { json.set("config.display.tab.format", value) }
    
    var useScoreboardTeams: Boolean
        get() = json.getBoolean("config.display.teams.use") ?: true
        set(value) { json.set("config.display.teams.use", value) }
    
    var useWaypointColor: Boolean
        get() = json.getBoolean("config.display.waypoint.use") ?: true
        set(value) { json.set("config.display.waypoint.use", value) }

    var useDisplayName: Boolean
        get() = json.getBoolean("config.display.name.use") ?: true
        set(value) { json.set("config.display.name.use", value) }
    
    var usePrivateScoreboard: Boolean
        get() = json.getBoolean("config.scoreboard.private.use") ?: true
        set(value) { json.set("config.scoreboard.private.use", value) }
    
    var useScoreboardPrefix: Boolean
        get() = json.getBoolean("config.scoreboard.prefix.use") ?: true
        set(value) { json.set("config.scoreboard.prefix.use", value) }
    
    var useScoreboardSuffix: Boolean
        get() = json.getBoolean("config.scoreboard.suffix.use") ?: true
        set(value) { json.set("config.scoreboard.suffix.use", value) }

    fun saveConfig(async: Boolean) {
        if (json.isOriginalContent()) return
        
        if (async) return scheduleAsync { this.saveConfig(false) }
        
        file.createFile()
        file.write(json)
    }
}