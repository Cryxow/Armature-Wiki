# 🧿 Denizen

Armature's Denizen adapter is embedded in `Armature.jar`. When Denizen is installed and Armature's public API is available, the adapter registers one native `armature` command and the animation lifecycle event family.

The adapter performs presentation operations only. Denizen or another gameplay plugin remains responsible for items, ammo, damage, cooldowns, and event cancellation.

## Command syntax

```
armature [action/animation/raw_animation/loop/raw_loop/stop_action/stop_loop/signal] (<value>) (profile:<name>) (targets:<player>|...)
```

The command uses the script-entry player context by default. Use `targets:|...` to target one or more explicit players. Every explicit target must be a player.

| Operation             | Value                      | Optional arguments |
| --------------------- | -------------------------- | ------------------ |
| `action`              | Built-in action id         | none               |
| `animation` or `anim` | Animation id               | `profile:`         |
| `raw_animation`       | Active-model asset id      | none               |
| `loop`                | Built-in loop id           | none               |
| `raw_loop`            | Active-model loop asset id | none               |
| `stop_action`         | none                       | none               |
| `stop_loop`           | none                       | none               |
| `signal`              | Namespaced trigger         | none               |

The command also accepts hyphenated and compact aliases for raw and stop operations: `raw-animation`, `rawanimation`, `raw-loop`, `rawloop`, `stop-action`, `stopaction`, `stop-loop`, `stoploop`, `send_signal`, and `send-signal`.

Examples:

```
- armature action FIRE
- armature animation inspect
- armature animation inspect profile:m4a1
- armature raw_animation fire
- armature loop AIM
- armature raw_loop aim
- armature stop_action
- armature stop_loop
- armature signal denizen:weapon.fire
```

`profile:` is supported for `animation` only. A value is required for every operation except `stop_action` and `stop_loop`.

## Lifecycle events

```
on armature animation starts:
  - narrate "started <context.animation> for <context.player>"

on armature animation ends:
  - narrate "ended with <context.reason>"

on armature animation completes:
  - narrate "naturally completed <context.animation>"
```

Available contexts:

* `profile`
* `action`
* `animation`
* `reason`
* `phase`
* `token`
* `player`

`starts` has an empty reason. `ends` reports `completed`, `cancelled`, `replaced`, or `removed`. `completes` is emitted only for the natural `completed` reason.

## Modern signal rules

The command can route a signal to an anonymous modern action rule:

```yaml
animations:
  actions:
    - trigger: denizen:weapon.fire
      animation:
        name: fire
        duration: 5t
```

```
- armature signal denizen:weapon.fire
```

For all available trigger fields, conditions, and action selection behavior, see [Built-in triggers](../../getting-started/profiles/triggers.md), [Built-in conditions](../../getting-started/profiles/conditions.md), and [Public API](../public-api.md).
