package at.flauschigesalex.lucko

import at.flauschigesalex.lib.minecraft.brigadier.CommandBuilder
import at.flauschigesalex.lib.minecraft.brigadier.types.internal.LiteralArgumentType
import at.flauschigesalex.lib.minecraft.brigadier.types.primitive.EnumArgumentType
import at.flauschigesalex.lucko.config.SimpleLuckoConfig
import at.flauschigesalex.lucko.luckperms.LuckPermsAPI
import at.flauschigesalex.lucko.luckperms._events.UpdateField
import at.flauschigesalex.lucko.utils.Permissions
import at.flauschigesalex.lucko.utils.Translate
import at.flauschigesalex.lucko.utils.locale
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

        CommandBuilder("slp-reload") {
            this.permission(Permissions.RELOAD)
            
            this.argument("enum", EnumArgumentType.enum(UpdateField::class.java)) {
                this.execute { context ->
                    val sender = context.sender
                    val field = context.arguments.byType<UpdateField>()?.value ?: return@execute

                    field.invoke()
                    sender.sendTranslated("reload.${field.name.lowercase()}.try")
                }
            }
            this.argument("all", LiteralArgumentType.literal()) {
                this.execute { context ->
                    val sender = context.sender

                    LuckPermsAPI.attemptUpdateEverything()
                    sender.sendTranslated("reload.all.try")
                }
            }
        }

        CommandBuilder("slp-docs") {
            this.alias("simply-luckperms", "slp")
            
            this.execute { context ->
                val sender = context.sender

                LuckPermsAPI.attemptUpdateEverything()
                
                val docs = "https://github.com/FlauschigesAlex/simply-luckperms/blob/master/readme.md"
                sender.sendTranslated("version.current", "<aqua>" + SimpleLuckoPlugin.instance.pluginMeta.version + "</aqua>") {
                    "<gray>$it"
                }
                val clicky = "<dark_aqua><b>" + Translate.translate("docs.click_me", sender.locale) + "<reset>"
                sender.sendTranslated("docs", clicky) {
                    "<gray><click:open_url:$docs><hover:show_text:'<aqua>$docs'>$it<reset>"
                }
            }
        }
    }
}