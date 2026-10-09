---
description: "Commands & Permissions"
---

# Commands & Permissions

Armature registers one Paper `BasicCommand` through the lifecycle command
registrar. The root command is `/armature`; Paper also exposes `/arm` and
`/armatureadmin`. `plugin.yml` keeps only the Bukkit command metadata and usage
string, so it does not declare competing aliases.

Administrator headings use `plugins/Armature/messages.yml`. `locale: auto`,
`en`, and `fr` are supported; diagnostic codes remain language-independent.
Every command
requires `armature.command` at the root and then its domain permission. Console
senders are supported unless a command explicitly needs a player context.

## Root commands

```text
/armature help [domain]
/armature status
/armature reload [all|pack|config|models|profiles]
/armature enable
/armature disable
/armature config ...
/armature profile ...
/armature animation ...
/armature editor
/armature diagnostics ...
/armature debug ...
```

The root shortcuts `help`, `status`, `reload`, `enable`, `disable`, and bare
`editor` remain available. `/armature help` lists only domains permitted for
the sender. The `debug` domain is omitted unless the sender has
`armature.command.native-probe`.

`help` accepts a domain to show contextual usage, for example
`/armature profile help`. Suggestions are filtered by permission and by the
text already typed. Player, profile, model, animation, migration-file and
native-probe completions are supplied where applicable.

## `/armature status`

Shows the active backend, runtime and render switches, configured model/profile
counts, unavailable configured animations, provider counts, and renderer
metrics. Native catalog details are included when the native backend has loaded
a catalog.

Permission: `armature.command.status`.

## Explicit reload operations

```text
/armature reload all
/armature reload [pack|config|models|profiles]
/armature config reload
/armature profile reload
/armature diagnostics models reload
/armature diagnostics pack rebuild
/armature diagnostics pack sync
/armature diagnostics startup
```

`/armature reload all` is the recommended one-command full Armature reload. It
rebuilds configuration, profiles, renderer models, and Armature's resource
pack, but does not regenerate external provider packs. Run the provider's own
reload command afterward when its pack must include the new Armature assets
(for example, `/ce reload all` for CraftEngine). The other
`/armature reload <target>` forms remain supported as quick operator shortcuts.
The grouped commands expose the same targeted operations with more contextual
help. Every operation reports elapsed stages and preserves the last known-good
runtime when its candidate fails.

Permission: `armature.command.reload`.

## `/armature config`

```text
/armature config reload
/armature config validate
/armature config migrate <file> [--output <file>] [--local|--ai]
/armature config restore-defaults [--confirm]
```

`validate` reports configuration findings without applying changes.

`migrate` reads a legacy profile relative to `plugins/Armature/profiles/` and
writes a separate validated candidate, defaulting to `<name>.migrated.yml`.
It never replaces the source or reloads the active presentation. `--local` and
`--ai` are compatibility flags: both use the local deterministic converter,
without network calls. Source and candidate files have a fixed internal
256 KiB limit; there are no migration options in `config.yml`.
See [Upgrading to 2.0.0](upgrading.md) before activating the output.

`restore-defaults` first prints a preview. It overwrites matching files from
embedded `default-assets/` only after the same sender repeats the command with
`--confirm`; run the explicit reload command matching the changed files afterward.

Permission: `armature.command.reload` for `validate` and `restore-defaults`;
`armature.command.migrate-config` for `migrate`.

## `/armature profile`

```text
/armature profile reload
/armature profile list [player]
/armature profile inspect <player>
/armature profile enable <player> <profile>
/armature profile disable <player> <profile>
```

Without a player, `list` displays configured profile IDs. With an online player,
it displays command-managed profile opt-outs. `inspect` is read-only and
reports the live resolution observed by the server; it does not claim that a
connected client rendered the result.

`enable` and `disable` manage the persistent UUID-based profile opt-out. The
permission-based opt-outs remain higher priority than command-managed state.

Permission: `armature.command.profile`.

## `/armature animation`

```text
/armature animation play <player> <model> <animation>
/armature animation stop <player>
```

`play` accepts any model loaded by the selected renderer (`.bbmodel` or native
`.armature`, using its model ID) and an exact clip name. The target player must
be online; neither a matching active model nor a profile for the requested model
is required. During playback, the requested model temporarily replaces the
held-item presentation, retaining its profile's model offset, sway rate and
vanilla-hand visibility. Without a held-item profile, presentation defaults apply.
The temporary view uses only the requested clip, so clips, conditions and
additives from the previous model cannot block playback. Missing models or clips
are rejected before replacing the current view. Once the animation finishes,
Armature restores the profile for the item currently held, including when the
player changed items during playback. `stop` cancels the target's active action and direct loop,
then allows normal presentation state to resume.

Permission: `armature.command.play`.

## `/armature editor`

```text
/armature editor open
/armature editor close [--confirm]
/armature editor reset [item|profile] [--confirm]
/armature editor export [item|profile]
/armature editor undo
/armature editor redo
/armature editor set <bone> <pos|rot> <x> <y> <z>
/armature editor diff [item|profile]
/armature editor mirror <left|right>
```

Bare `/armature editor` is the shortcut for `editor open`. The editor requires
a player holding an item with an active Armature profile and editable model
bones. Its Dialog exposes sliders plus a visible `temporary preview` state.
`editor set` provides exact numeric position (pixels) or rotation (degrees)
entry when a client does not offer text entry in the Dialog.

`undo` and `redo` keep a bounded (50 change) session history. `diff` prints the
authored model baseline, the unsaved `temporary preview`, and the persisted
`item-override` or `profile-override`. `mirror left` copies corresponding
right-side values into left bones; `mirror right` does the inverse and mirrors
position/rotation signs across the X axis.

`reset` prints a destructive `item-override`/`profile-override` preview and
requires `--confirm`; it removes persisted values for that scope and refreshes
live presentation. `close` blocks while the `temporary preview` is dirty;
repeat it with `--confirm` to discard those unsaved values. `export` emits a
profile-ready YAML fragment for `editor-overrides.yml`, includes the dirty
summary, and never writes source profile configuration.

All three value layers are explicit in editor messages: `temporary preview`
is live and unsaved, `item-override` is persisted for one item/profile scope,
and `profile-override` is persisted for every item resolved to that profile.
Saving an override or applying a preview changes presentation only. Source
profile files are never mutated by the editor.

Permission: `armature.command.editor`.

## `/armature diagnostics`

```text
/armature diagnostics doctor
/armature diagnostics performance
/armature diagnostics pack
/armature diagnostics models reload
/armature diagnostics pack rebuild
/armature diagnostics pack sync
/armature diagnostics startup
/armature diagnostics export
```

`doctor` produces a preflight report with stable finding codes where available.
`performance` retains the existing player-only live performance bossbar.
`pack` reports generated-pack and ownership state. `pack rebuild` regenerates the
Armature candidate. `pack sync` requests external provider publication without
rebuilding Armature models. `models reload` reloads renderer assets only.
`startup` reports live operation state and stage timings. `export` immediately schedules
an anonymized, bounded support bundle off-thread, then reports the relative
`plugins/Armature/diagnostics/armature-support-<timestamp>.zip` path when the
atomic archive move completes. Configuration values containing credentials or
endpoints, player identifiers, IP addresses, and unrelated log lines are
redacted; export never performs synchronous archive I/O in the command handler.

Permission: `armature.command.status` for `doctor`, `pack`, and `export`;
`armature.command.performance` for `performance`.

## `/armature debug`

```text
/armature debug native-probe <model> [animation]
/armature debug stress <entity-count> <model-name> <animation>
/armature debug stress clear
```

Runs the existing generated native static-model probe. The implementation also
retains its catalog-preparation behavior when no model is supplied, so existing
operator workflows do not regress. Native model and animation names are
completed from the loaded catalog.

`stress` spawns a square grid of up to 1,024 temporary native models in front of
the invoking player. Each model loops the selected animation and is visible only
to that player. A grid expires after ten minutes, is cleared when its owner
disconnects, or can be removed immediately with `stress clear`. This debug
action requires the native renderer backend.

Permission: `armature.command.native-probe`.

## Compatibility command forms

Deprecated forms still execute their existing operation and then emit one hint
with the canonical replacement. Their arguments are preserved in the hint.

| Deprecated form | Canonical form |
| --- | --- |
| `/armature perf` | `/armature diagnostics performance` |
| `/armature regenerate-defaults` | `/armature config restore-defaults` |
| `/armature migrate-config <file> [--output <file>]` | `/armature config migrate <file> [--output <file>]` |
| `/armature play <player> <model> <animation>` | `/armature animation play <player> <model> <animation>` |
| `/armature native-probe <model> [animation]` | `/armature debug native-probe <model> [animation]` |

`performance` remains accepted as an additional spelling of the deprecated
`perf` form for compatibility with older operator habits.

## Per-player disable permissions

The global `enable` and `disable` commands affect runtime rendering. To opt a
player out without changing global runtime state, grant one of these
permissions through a permissions plugin:

- `armature.disabled` disables Armature presentation for every profile;
- `armature.disabled.<profile-id>` disables only the matching profile.

For example:

```text
/lp user Alice permission set armature.disabled.m4a1 true
```

Permission-based opt-outs take precedence over `/armature profile enable` and
are observed on the next player tick. They do not change held items or gameplay
state.

## Permission summary

| Permission | Scope | Default |
| --- | --- | --- |
| `armature.command` | Root command access | op |
| `armature.command.status` | Status, doctor, pack, diagnostics export | op |
| `armature.command.performance` | Performance bossbar | op |
| `armature.command.reload` | Explicit config/profile/model/pack/provider operations, config validation, restore defaults | op |
| `armature.command.toggle` | Enable/disable runtime | op |
| `armature.command.play` | Animation play/stop | op |
| `armature.command.editor` | Editor operations | op |
| `armature.command.profile` | Profile operations | op |
| `armature.command.migrate-config` | Legacy profile migration | op |
| `armature.command.native-probe` | Armature debug commands | op |
| `armature.disabled` | Player opt-out | false |


Pack generation controls: `/armature reload pack`, `/armature reload all` and `/armature diagnostics pack rebuild` explicitly generate even when automatic triggers are disabled. Config/profile/model reloads obey `resource-pack.generation.on-reload`. `/armature diagnostics pack` reports published generation options, validation findings, size and duration. `/armature diagnostics pack sync` requests existing provider synchronization; it does not bypass Armature generation settings.
