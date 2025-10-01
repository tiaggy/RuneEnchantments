# RuneEnchantments Plugin

**RuneEnchantments** is a Minecraft 1.21.4 plugin that adds one rune enchantment: **VeinSmelt**, which offers vein mining and auto-smelt capabilities based on configuration.

## VeinSmelt Features

* **VeinMine**: When enabled, mining one block triggers mining of adjacent blocks with the same type in 3x3 area.
* **AutoSmelt**: When enabled, mined gold/iron ores are automatically smelted (as if via a furnace) when dropped.

## Configuration

```yaml
veinsmelt:
  spawn_chance: 0.05
  VeinMine: true
  AutoSmelt: true
```

| Key            | Type    | Default | Description                                                                                       |
| -------------- | ------- | ------- |---------------------------------------------------------------------------------------------------|
| `spawn_chance` | double  | `0.05`  | The probability (0.0 to 1.0) that the VeinSmelt rune will apply or spawn when conditions are met. |
| `VeinMine`     | boolean | `true`  | Enables VeinMine feature.                                                                         |
| `AutoSmelt`    | boolean | `true`  | Enables AutoSmelt feature.                                                                        |

## Installation & Usage

1. Clone this repository:

   ```bash
   git clone https://github.com/tiaggy/RuneEnchantments
   ```
2. Build the plugin (e.g. using Maven or Gradle) and produce the `.jar` file.
3. Place the `.jar` in your server’s `plugins/` folder and start the server.
4. After first run, a `config.yml` will be generated.
5. Customize `config.yml` as needed and restart the server.

## Commands

### `/rune give <player> <type>`

* **Description:** Gives a player a rune book with a specific enchantment.
* **Parameters:**
    * `<player>` – Target player.
    * `<type>`   – Type of rune. Currently, only `veinsmelt` is supported.
* **Permissions:** `rune.give`

