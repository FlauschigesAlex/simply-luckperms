package at.flauschigesalex.lucko.luckperms.companion

@Suppress("unused")
enum class PlayerCompanionVisibility(
    internal val displayOwn: Boolean,
    internal val displayOthers: Boolean
) {
    VANILLA(false, false),
    EXCLUDE_OWN(false, true),
    ALWAYS(true, true),
    ;
}