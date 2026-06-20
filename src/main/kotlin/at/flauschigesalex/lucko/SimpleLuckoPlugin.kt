package at.flauschigesalex.lucko

import at.flauschigesalex.lib.minecraft.paper.base.FlauschigeLibraryPaper
import at.flauschigesalex.lucko.luckperms.LuckPermsEvents
import at.flauschigesalex.lucko.utils.Commons
import at.flauschigesalex.lucko.utils.scheduleAsync
import at.flauschigesalex.lucko.utils.sendNewerVersionMessage
import at.flauschigesalex.rinth.project.version.checker.VersionChecker
import at.flauschigesalex.rinth.project.version.listener.PaperVersionUpdateListener
import at.flauschigesalex.rinth.project.version.onChanges
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNUSED_EXPRESSION")
internal class SimpleLuckoPlugin : JavaPlugin() {
    
    companion object {
        lateinit var instance: SimpleLuckoPlugin
            private set
    }
    
    override fun onEnable() {
        instance = this
        FlauschigeLibraryPaper.init(this, javaClass.packageName)
        
        SimpleLuckoConfig // LOAD CONFIG
        Commands // LOAD COMMANDS
        LuckPermsEvents // LOAD LUCKPERMS EVENTS
        
        // BEGIN BSTATS
        val metrics = Metrics(this, 31006)

        // BEGIN VERSION CHECKER
        PaperVersionUpdateListener(this) { audience ->
            scheduleAsync {
                val changes = VersionChecker.check(Commons.slug).currentVersionDiff(server).getOrNull() ?: return@scheduleAsync
                changes.onChanges {
                    audience.sendNewerVersionMessage(this)
                }
            }
        }
    }
}