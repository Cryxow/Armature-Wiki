# ☠️ MythicMobs

Armature's MythicMobs adapter is embedded in `Armature.jar`. Install `MythicMobs` beside Armature; it registers its mechanics and lifecycle triggers only when the public Armature API and MythicMobs are available.

All mechanics target a player and call Armature's presentation API. Keep projectiles, damage, ammo, cooldowns, and other gameplay mechanics in the MythicMobs skill.

## Mechanics

```yaml
Skills:
  PistolFire:
    Skills:
      - armatureaction{action=FIRE} @self
      - armatureanimation{animation=inspect} @self
      - armaturesignal{signal=mythicmobs:weapon.fire} @self

  AimStart:
    Skills:
      - armatureloop{action=AIM} @self

  AimStop:
    Skills:
      - armatureloop{stop=true} @self
```

| Mechanic                                        | Main configuration                                   | Effect                                                |
| ----------------------------------------------- | ---------------------------------------------------- | ----------------------------------------------------- |
| `armatureaction` (`armatureactionplay`)         | `action` or `a`                                      | Plays a built-in `ArmatureAction`                     |
| `armatureanimation` (`armatureanim`)            | `animation`, or `action`/`a`; optional `profile`/`p` | Plays an active-profile or explicit-profile animation |
| `armatureloop` (`armaturestartloop`)            | `action`/`a`, or `stop`/`s`                          | Selects a built-in loop or stops the selected loop    |
| `armaturerawanimation` (`armatureanimationraw`) | `animation`/`a`                                      | Plays a raw asset from the active rendered model      |
| `armaturerawloop` (`armatureloopraw`)           | `animation`/`a`, or `stop`/`s`                       | Selects or stops a raw loop                           |
| `armaturesignal` (`armatureemit`)               | `signal`, `event`, or `s`                            | Routes a modern configured trigger                    |
| `armaturestopaction` (`armaturecancelaction`)   | none                                                 | Cancels the current one-shot action                   |
| `armaturestoploop` (`armaturecancelloop`)       | none                                                 | Stops the selected loop                               |

Use a player target such as `@self`, `@target`, or `@trigger` when that entity is a player. An invalid target or configuration produces the normal MythicMobs skill failure result.

## Lifecycle triggers

The adapter registers:

| Canonical trigger             | Aliases                                                    |
| ----------------------------- | ---------------------------------------------------------- |
| `ARMATURE_ANIMATION_START`    | `armatureanimationstart`, `armature-animation-start`       |
| `ARMATURE_ANIMATION_END`      | `armatureanimationend`, `armature-animation-end`           |
| `ARMATURE_ANIMATION_COMPLETE` | `armatureanimationcomplete`, `armature-animation-complete` |

Example:

```yaml
Triggers:
  - ARMATURE_ANIMATION_END
```

The trigger metadata contains:

* `armature.profile`
* `armature.action`
* `armature.animation`
* `armature.reason`
* `armature.token`

`ARMATURE_ANIMATION_END` fires for every finite-animation termination. `ARMATURE_ANIMATION_COMPLETE` fires only when the animation reaches its natural end.

## Modern signal rules

Use `armaturesignal` when the profile owns the mapping:

```yaml
Skills:
  WeaponFire:
    Skills:
      - armaturesignal{signal=mythicmobs:weapon.fire} @self
```

```yaml
animations:
  actions:
    - trigger: mythicmobs:weapon.fire
      condition:
        all:
          - player.on-ground == true
          - weaponmechanics.ammo > 0
      animation:
        name: fire
        duration: 5t
```

Dynamic right-hand values in conditions require the `$` prefix. For example, `weaponmechanics.ammo == $weaponmechanics.magazine-size` compares two runtime values; without `$`, a path-like right-hand string is literal.

See [Built-in triggers](../../getting-started/profiles/triggers.md), [Built-in conditions](../../getting-started/profiles/conditions.md), and [Public API](../public-api.md).
