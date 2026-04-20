package at.flauschigesalex.lucko

import at.flauschigesalex.lib.minecraft.brigadier.CommandBuilder
import at.flauschigesalex.lib.minecraft.brigadier.types.internal.LiteralArgumentType
import at.flauschigesalex.lucko.luckperms.LuckPermsAPI
import at.flauschigesalex.lucko.utils.Permissions
import at.flauschigesalex.lucko.utils.sendTranslated

internal object Commands {
    
    init {
        CommandBuilder("slp-config") {
            this.permission(Permissions.CONFIG)
            
            this.argument("reload", LiteralArgumentType.literal()) {
                
                this.execute { context ->
                    val sender = context.sender
                    
                    SimpleLuckoConfig.reloadConfig()
                    LuckPermsAPI.attemptUpdateEverything()
                    
                    sender.sendTranslated("config.reload")
                }
            }
        }
    }
}