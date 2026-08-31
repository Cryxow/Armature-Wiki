# 📜 Skript

Armature's Skript adapter is embedded in the distribution `Armature.jar`. Install `Skript` beside Armature; the syntax is registered only when both Armature's public API and Skript are available.

The adapter calls the public presentation API. It does not validate weapons, consume ammo, apply damage, or cancel the gameplay event that caused a Skript effect.

## Effects

```skript
play armature animation "inspect" for player
play armature action "FIRE" for player
start armature loop "AIM" for player
stop armature action for player
stop armature loop for player
send armature signal "myplugin:weapon.fire" to player
play armature animation "inspect" from profile "m4a1" for player
play raw armature animation "fire" for player
start raw armature loop "aim" for player
```

The effects map to these API operations:

| Syntax                        | API operation                               |
| ----------------------------- | ------------------------------------------- |
| `play armature animation`     | `playAnimation(player, animation)`          |
| `play armature action`        | Parses a built-in `ArmatureAction`          |
| `start armature loop`         | Selects a built-in loop action              |
| `stop armature action`        | `stopAction(player)`                        |
| `stop armature loop`          | `stopLoop(player)`                          |
| `send armature signal`        | `sendSignal(player, signal)`                |
| `... from profile ...`        | `playAnimation(player, profile, animation)` |
| `play raw armature animation` | `playRawAnimation(player, animation)`       |
| `start raw armature loop`     | `startRawLoop(player, animation)`           |

Use a namespaced signal when the profile contains a modern action rule:

```yaml
animations:
  actions:
    - trigger: skript:weapon.fire
      animation:
        name: pistol_fire
        duration: 6t
```

```skript
send armature signal "skript:weapon.fire" to player
```

The YAML rule can inspect the event details and set `consume: true`, but that value is returned as routing metadata. It does not cancel a Skript event automatically.

## Lifecycle events

```skript
on armature animation start:
    broadcast "started [armature animation name]"

on armature animation end:
    broadcast "ended [armature animation name]: [armature animation end reason]"

on armature animation complete:
    # This is only a natural finite-animation completion.
    broadcast "completed [armature animation name]"
```

The adapter exposes these expressions:

* `armature animation profile`
* `armature animation action`
* `armature animation name`
* `armature animation end reason`

The end reason is available on `animation end` and `animation complete`. It is one of `completed`, `cancelled`, `replaced`, or `removed`. The complete event is emitted only for `completed`.

## Ownership and failures

Keep gameplay logic in the owning Skript code:

```skript
on arm swing:
    # Check the weapon, ammo and cooldown in the gameplay script.
    play armature action "FIRE" for player
```

If the player is offline, Armature is unavailable, or the requested profile or asset is missing, the adapter simply has no presentation operation to apply. For result statuses and modern signal routing, see [Public API](../public-api.md) and [Built-in conditions](../../getting-started/profiles/conditions.md).
