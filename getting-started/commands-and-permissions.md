# 📟 Commands & Permissions

The main command is `/armature`. The alias is `/arm`. The base command permission is `armature.command`; each administrative subcommand also checks its specific permission.

## Commands

| Command                                              | Permission                        | Description                                                                                               |
| ---------------------------------------------------- | --------------------------------- | --------------------------------------------------------------------------------------------------------- |
| `/armature help`                                     | `armature.command`                | Shows available commands for the sender.                                                                  |
| `/armature status`                                   | `armature.command.status`         | Shows version, rendering/runtime state, model/profile counts, rig health, camera counters, and providers. |
| `/armature perf` or `/armature performance`          | `armature.command.performance`    | Toggles the live performance bossbar for a player.                                                        |
| `/armature reload [all\|pack\|models\|profiles]`     | `armature.command.reload`         | Reloads the selected subsystem. No argument means `all`.                                                  |
| `/armature regenerate-defaults`                      | `armature.command.reload`         | Restores Armature's default profiles and models.                                                          |
| `/armature enable`                                   | `armature.command.toggle`         | Enables runtime presentation.                                                                             |
| `/armature disable`                                  | `armature.command.toggle`         | Disables runtime presentation.                                                                            |
| `/armature play <player_name> <profile> <animation>` | `armature.command.play`           | Plays an animation from an explicitly active profile.                                                     |
| `/armature editor`                                   | `armature.command.editor`         | Opens the live bone admin editor. Player-only.                                                            |
| `/armature migrate-config <file> [--output <file>]`  | `armature.command.migrate-config` | Converts one legacy profile without replacing its source file.                                            |

The migration source and optional output are resolved inside `plugins/Armature/profiles`. The source must be a legacy profile. The candidate is validated against the modern loader before it is written.

See [In-game bone editor](in-game-editor.md) for the complete editor workflow,
scope rules, persistence format, and troubleshooting.

## Reload modes

| Mode       | Reloads                                                  |
| ---------- | -------------------------------------------------------- |
| `all`      | Configuration, models/resource-pack assets, and profiles |
| `pack`     | Generated resource-pack assets                           |
| `models`   | BetterModel model assets and generated model data        |
| `profiles` | Profile YAML and animation rules                         |

Reload output reports the selected mode, relevant counts, unavailable configured animations, stage timings, and total time. A failed reload keeps the current active configuration.

## Permissions

| Permission                        | Default  | Scope                                                                |
| --------------------------------- | -------- | -------------------------------------------------------------------- |
| `armature.command`                | operator | Access to the command.                                               |
| `armature.command.status`         | operator | Status.                                                              |
| `armature.command.performance`    | operator | Performance bossbar.                                                 |
| `armature.command.reload`         | operator | Reload and regenerate defaults.                                      |
| `armature.command.toggle`         | operator | Enable or disable runtime rendering.                                 |
| `armature.command.play`           | operator | Play a profile animation for a player.                               |
| `armature.command.editor`         | operator | Open the bone editor.                                                |
| `armature.command.migrate-config` | operator | Run legacy-profile migration.                                        |
| `armature.admin`                  | operator | Parent permission granting every Armature administration permission. |

## Operational notes

Use `/armature reload` after changing YAML or generated model assets. A full server restart is required after changing the plugin jar or its dependencies. Do not use Bukkit `/reload` as release validation: plugin lifecycle state, provider registrations, BetterModel trackers, and Folia scheduling must be tested with a real restart.
