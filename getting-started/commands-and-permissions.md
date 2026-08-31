# Commands and permissions

The main command is <code>/armature</code>. The alias is
<code>/arm</code>. The base command permission is
<code>armature.command</code>; each administrative subcommand also checks its
specific permission.

## Commands

| Command | Permission | Description |
| --- | --- | --- |
| <code>/armature help</code> | <code>armature.command</code> | Shows available commands for the sender. |
| <code>/armature status</code> | <code>armature.command.status</code> | Shows version, rendering/runtime state, model/profile counts, rig health, camera counters, and providers. |
| <code>/armature perf</code> or <code>/armature performance</code> | <code>armature.command.performance</code> | Toggles the live performance bossbar for a player. |
| <code>/armature reload [all&#124;pack&#124;models&#124;profiles]</code> | <code>armature.command.reload</code> | Reloads the selected subsystem. No argument means <code>all</code>. |
| <code>/armature regenerate-defaults</code> | <code>armature.command.reload</code> | Restores Armature's default profiles and models. |
| <code>/armature enable</code> | <code>armature.command.toggle</code> | Enables runtime presentation. |
| <code>/armature disable</code> | <code>armature.command.toggle</code> | Disables runtime presentation. |
| <code>/armature play &lt;player_name&gt; &lt;profile&gt; &lt;animation&gt;</code> | <code>armature.command.play</code> | Plays an animation from an explicitly active profile. |
| <code>/armature editor</code> | <code>armature.command.editor</code> | Opens the live bone editor. Player-only. |
| <code>/armature migrate-config &lt;file&gt; [--output &lt;file&gt;]</code> | <code>armature.command.migrate-config</code> | Converts one legacy profile without replacing its source file. |

The migration source and optional output are resolved inside
<code>plugins/Armature/profiles</code>. The source must be a legacy profile.
The candidate is validated against the modern loader before it is written.

## Reload modes

| Mode | Reloads |
| --- | --- |
| <code>all</code> | Configuration, models/resource-pack assets, and profiles |
| <code>pack</code> | Generated resource-pack assets |
| <code>models</code> | BetterModel model assets and generated model data |
| <code>profiles</code> | Profile YAML and animation rules |

Reload output reports the selected mode, relevant counts, unavailable
configured animations, stage timings, and total time. A failed reload keeps
the current active configuration.

## Permissions

<table><thead><tr><th>Permission</th><th>Default</th><th>Scope</th></tr></thead><tbody><tr><td><code>armature.command</code></td><td>operator</td><td>Access to the command.</td></tr><tr><td><code>armature.command.status</code></td><td>operator</td><td>Status.</td></tr><tr><td><code>armature.command.performance</code></td><td>operator</td><td>Performance bossbar.</td></tr><tr><td><code>armature.command.reload</code></td><td>operator</td><td>Reload and regenerate defaults.</td></tr><tr><td><code>armature.command.toggle</code></td><td>operator</td><td>Enable or disable runtime rendering.</td></tr><tr><td><code>armature.command.play</code></td><td>operator</td><td>Play a profile animation for a player.</td></tr><tr><td><code>armature.command.editor</code></td><td>operator</td><td>Open the bone editor.</td></tr><tr><td><code>armature.command.migrate-config</code></td><td>operator</td><td>Run legacy-profile migration.</td></tr><tr><td><code>armature.admin</code></td><td>operator</td><td>Parent permission granting every Armature administration permission.</td></tr></tbody></table>

## Operational notes

Use <code>/armature reload</code> after changing YAML or generated model
assets. A full server restart is required after changing the plugin jar or its
dependencies. Do not use Bukkit <code>/reload</code> as release validation:
plugin lifecycle state, provider registrations, BetterModel trackers, and
Folia scheduling must be tested with a real restart.
