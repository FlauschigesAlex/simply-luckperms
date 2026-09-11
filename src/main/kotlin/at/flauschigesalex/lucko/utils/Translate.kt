package at.flauschigesalex.lucko.utils

import at.flauschigesalex.lib.minecraft.paper.base.utils.sendRichMessage
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

@Suppress("unused")
internal object Translate {

    val description: String = """
        <b><#b3fffe>Simply<aqua>Luck<dark_aqua>Perms<reset>
        <gray>A plugin designed to easily manage chat, tab and team display using only meta-fields.
        
        <gray>Learn more on <dark_green>Modrinth<gray>:
        <dark_green><u>https://modrinth.com/plugin/simply-luckperms</u>
        """.trimIndent()
    val name: String = "<hover:show_text:\"$description\"><b><#b3fffe>Simply<aqua>L<dark_aqua>P<reset><reset>"
    
    fun translate(key: String, locale: Locale): String = runCatching {
        
        require(key.isNotEmpty()) { "Key must not be empty!" }

        val bundle = ResourceBundle.getBundle(
            "i18n/messages",
            locale,
            ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)
        )
        
        return bundle.getString(key)
    }.getOrNull() ?: "?($key)"
    
    fun broadcastTranslated(key: String, vararg args: Any?, richConsumer: Audience.(String) -> String = { it }) {
        val receivers: MutableList<Audience> = Bukkit.getOnlinePlayers().toMutableList()
        receivers.add(Bukkit.getConsoleSender())
        
        receivers.forEach {
            it.sendTranslated(key, *args, richConsumer = richConsumer)
        }
    }
}

internal val Audience.locale: Locale get() = when (this) {
    is Player -> this.locale()
    else -> Locale.getDefault()
}

internal fun Audience.sendTranslated(key: String, vararg args: Any?, richConsumer: Audience.(String) -> String = { it }) {
    val translation = Translate.translate(key, this.locale)
    val richTranslation = richConsumer.invoke(this, translation)
    this.sendRichMessage("${Translate.name} <dark_gray>» <gray>$richTranslation".format(*args))
}