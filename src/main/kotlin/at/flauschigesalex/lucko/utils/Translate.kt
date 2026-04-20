package at.flauschigesalex.lucko.utils

import at.flauschigesalex.lib.minecraft.paper.base.utils.sendRichMessage
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

internal object Translate {
    
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

val Audience.locale: Locale get() = when (this) {
    is Player -> this.locale()
    else -> Locale.getDefault()
}

fun Audience.sendTranslated(key: String, vararg args: Any?, richConsumer: Audience.(String) -> String = { it }) {
    val translation = Translate.translate(key, this.locale).format(*args)
    val richTranslation = richConsumer.invoke(this, translation)
    this.sendRichMessage("<dark_gray>› <gray>$richTranslation")
}