package at.flauschigesalex.lucko

import at.flauschigesalex.lib.minecraft.paper.base.FlauschigeLibraryPaper
import at.flauschigesalex.lucko.luckperms.LuckPermsEvents
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
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
        val metrics = Metrics(this, 30820)

        metrics.addCustomChart(SimplePie("server_brand") { Bukkit.getServer().name })
        metrics.addCustomChart(SimplePie("server_version") { Bukkit.getServer().minecraftVersion })
    }
}