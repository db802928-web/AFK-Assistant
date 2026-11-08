# AFK Assistant 1.1 — Fabric 1.21.4

Client-side starter mod. Java 21 is required.

## Build
Open this folder as a Gradle project in IntelliJ IDEA and allow Gradle sync.
Run the Gradle task `build`. The output jar is in `build/libs/`.

Alternatively, install Gradle and run:
- Windows: `gradle.bat build`
- macOS/Linux: `gradle build`

## Install with Crystal Launcher
1. Build the jar first; this ZIP is source code, not a ready-to-install mod jar.
2. In Crystal Launcher, select the Minecraft 1.21.4 instance/profile.
3. Set up Fabric Loader 1.21.4 for that instance (launcher version options may vary).
4. Open that instance's game directory from its settings.
5. Put `afk-assistant-1.1.0.jar` and the matching Fabric API 1.21.4 jar in that instance's `mods` folder.
6. Launch that Fabric profile.

Do not put the source ZIP in `mods`.

## In game
Press Right Shift to open the menu.
- Block IDs: comma-separated registry IDs, e.g. `minecraft:cobblestone, minecraft:stone`.
- Dimensions: width/length/height around the player's current position.
- Corner mode: switch to two-corner mode, stand at each corner and click the matching buttons.
- Add command: enter a command and interval in seconds. Minimum interval is 10 seconds.
- Toggle mining and command scheduler separately.

The GUI is a first usable configuration screen, not a full polished editor. The command list can be edited in `config/afk-assistant.json`.

## Config
File: `.minecraft/config/afk-assistant.json` (or the selected instance's config directory).
Example:
```json
{
  "minerEnabled": false,
  "schedulerEnabled": false,
  "useDimensionArea": true,
  "width": 5,
  "length": 5,
  "height": 1,
  "blockIds": ["minecraft:cobblestone", "minecraft:stone"],
  "commands": [
    { "command": "/repair", "intervalSeconds": 300, "enabled": true }
  ]
}
```

## Notes
This is a client-side helper, not a server plugin. It cannot grant permissions or guarantee that a server accepts a command. Public servers may prohibit macros, unattended mining, or repeated commands. Check the server rules and do not use this to evade anti-cheat or AFK detection. Test in singleplayer or a private world first.
