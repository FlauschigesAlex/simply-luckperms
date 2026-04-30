# Simply LuckPerms
A plugin designed to easily manage chat, tab and team display using only meta-fields.<br>
It provides a simple and intuitive way to configure player prefixes, suffixes, team colors and more without the need for complex configuration files, just luckperms builtin- and meta-fields.

### Required Plugins
- [LuckPerms 5.4 (or newer)](https://luckperms.net/)

### Supported platforms and versions
- [Paper](https://papermc.io/software/paper/) Versions: 1.21.10 - 1.21.11

Recommended Version: Paper 1.21.11 (or newer)
<br>Although simple-maintenance-plugin may work on other platforms or versions, I do not guarantee for their stability or functionality.

## Setup
### Prefix Setup
![prefix default](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/prefix-default.png?raw=true)<br>
`prefix` *(builtin)* - Sets the holder's global prefix. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/).

### Suffix Setup
![suffix default](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/suffix-default.png?raw=true)<br>
`suffix` *(builtin)* - Sets the holder's global suffix. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/).

### Scoreboard Coloring
![color team](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/color-team.png?raw=true)<br>
![color team minecraft](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/color-team-mc.png?raw=true)<br>
`color-team` *(meta field)* - Sets the holder's global team color (above player color). Limited to [NamedTextColor](https://jd.advntr.dev/api/latest/net/kyori/adventure/text/format/NamedTextColor.html).
<br>Defaults to `WHITE` if not set.
<br><br>
![color waypoint](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/color-waypoint.png?raw=true)<br>
![color waypoint minecraft](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/color-waypoint-mc.png?raw=true)<br>
**(1.21.11+)** `color-waypoint` *(meta field)* - Sets the holder's [waypoint](https://minecraft.wiki/w/Commands/waypoint) color in the [locator bar](https://minecraft.wiki/w/Locator_Bar). Limited to [NamedTextColor](https://jd.advntr.dev/api/latest/net/kyori/adventure/text/format/NamedTextColor.html).
<br>Inherits from `color-team` if not set.

### Name Coloring
![color name](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/color-name.png?raw=true)<br>
![color name minecraft](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/color-name-mc.png?raw=true)<br>
`color-chat` *(meta field)* - Sets the holder's name color. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/), [Name-Placeholder (%s)](https://www.w3schools.com/java/ref_string_format.asp).
<br>Inherits from `prefix` if not set.

### Chat Setup
![color chat](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/color-chat.png?raw=true)<br>
![color chat minecraft](https://github.com/FlauschigesAlex/simply-luckperms/blob/branding/preview/color-chat-mc.png?raw=true)<br>
`color-chat` *(meta field)* - Sets the holder's chat color. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/), [Message-Placeholder (%s)](https://www.w3schools.com/java/ref_string_format.asp).
<br>Defaults to `WHITE` if not set.

## Configuration file
SimplyLuckPerms uses a configuration file to enable/disable features.
<br>**If you want to make any changes to the config, create one at `plugins/simply-luckperms/config.json`.**
<br>By default, all features are enabled, you can disable them by setting the corresponding option to `false` in the configuration file.
### REMOVE COMMENTS BEFORE APPLYING CONFIG
```json
{
  "config": {
    "_node": {
      "awaitTick": 5 // The delay (in ticks) to wait after luckperms recalculates the holder's permissions.
    },
    "display": {
      "chat": {
        "use": true, // Toggles chat formatting.
        "format": "%prefix%%username%%suffix%<dark_gray>: %message%" // Format to be parsed in chat.
      },
      "name": {
        "use": true // Toggles display name formatting.
      },
      "tab": {
        "use": true, // Toggles playerlist name (tab) formatting.
        "format": "%prefix%%username%%suffix%" // Format to be parsed in the playerlist (tab).
      },
      "teams": {
        "use": true // Toggles scoreboard team creation & formatting.
      },
      "waypoints": {
        "use": true // Toggles waypoint coloring.
      }
    },
    "scoreboard": {
      "private": {
        "use": true // Toggles wether to use a private scoreboard for the holder or the bukkit default.
      },
      "prefix": {
        "use": true // Toggles wether to display prefixes on scoreboard teams.
      },
      "suffix": {
        "use": true // Toggles wether to display suffixes on scoreboard teams.
      }
    }
  }
}
```
### REMOVE COMMENTS BEFORE APPLYING CONFIG

## Commands
`/slp-config reload` - Reloads the configuration file. (Permission `slp.config`)

## Builtin Fields & Meta Fields
LuckPerm's fields are based on `holders`, which are either players or groups.
<br>The inheritance of a field is ordered by `player` -> `group (descending weight)`, this means a group's prefix can be overridden for a player by setting player's prefix at a higher weight.
<br>This works the same for all builtin fields.
<br><br>
When working with meta-fields, LuckPerms will choose the first field it finds. (`player` -> `group (descending weight)`)
<br>If a player has multiple meta-fields with the same key, a random one will be chosen.

## Setup overrides
Overrides can be used to modify specific fields for a holder such as their chat prefix without modifying the global prefix.

### Prefix Overrides
`prefix-chat` *(meta field)* - Overrides the holder's chat prefix. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/).
<br>`prefix-tab` *(meta field)* - Overrides the holder's tab (playerlist) prefix. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/).
<br>`prefix-team` *(meta field)* - Overrides the holder's scoreboard team prefix. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/).

### Suffix Overrides
`suffix-chat` *(meta field)* - Overrides the holder's chat suffix. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/).
<br>`suffix-tab` *(meta field)* - Overrides the holder's tab (playerlist) suffix. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/).
<br>`suffix-team` *(meta field)* - Overrides the holder's scoreboard team suffix. Supports [MiniMessage](https://docs.papermc.io/adventure/minimessage/format/).
