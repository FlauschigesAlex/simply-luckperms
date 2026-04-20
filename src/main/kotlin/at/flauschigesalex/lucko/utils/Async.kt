package at.flauschigesalex.lucko.utils

import at.flauschigesalex.lucko.SimpleLuckoPlugin
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit

internal fun scheduleSync(block: () -> Unit) = Bukkit.getScheduler().callSyncMethod(SimpleLuckoPlugin.instance, block)
internal fun scheduleAsync(block: suspend () -> Unit) {
    Bukkit.getScheduler().runTaskAsynchronously(SimpleLuckoPlugin.instance) { _ ->
        runBlocking { block.invoke() }
    }
}