# MythicMobs

Armature's MythicMobs adapter is embedded in
<code>Armature.jar</code>. Install <code>MythicMobs</code> beside Armature; it
registers its mechanics and lifecycle triggers only when the public Armature
API and MythicMobs are available.

All mechanics target a player and call Armature's presentation API. Keep
projectiles, damage, ammo, cooldowns, and other gameplay mechanics in the
MythicMobs skill.

## Mechanics

~~~yaml
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
~~~

| Mechanic | Main configuration | Effect |
| --- | --- | --- |
| <code>armatureaction</code> (<code>armatureactionplay</code>) | <code>action</code> or <code>a</code> | Plays a built-in <code>ArmatureAction</code> |
| <code>armatureanimation</code> (<code>armatureanim</code>) | <code>animation</code>, or <code>action</code>/<code>a</code>; optional <code>profile</code>/<code>p</code> | Plays an active-profile or explicit-profile animation |
| <code>armatureloop</code> (<code>armaturestartloop</code>) | <code>action</code>/<code>a</code>, or <code>stop</code>/<code>s</code> | Selects a built-in loop or stops the selected loop |
| <code>armaturerawanimation</code> (<code>armatureanimationraw</code>) | <code>animation</code>/<code>a</code> | Plays a raw asset from the active rendered model |
| <code>armaturerawloop</code> (<code>armatureloopraw</code>) | <code>animation</code>/<code>a</code>, or <code>stop</code>/<code>s</code> | Selects or stops a raw loop |
| <code>armaturesignal</code> (<code>armatureemit</code>) | <code>signal</code>, <code>event</code>, or <code>s</code> | Routes a modern configured trigger |
| <code>armaturestopaction</code> (<code>armaturecancelaction</code>) | none | Cancels the current one-shot action |
| <code>armaturestoploop</code> (<code>armaturecancelloop</code>) | none | Stops the selected loop |

Use a player target such as <code>@self</code>, <code>@target</code>, or
<code>@trigger</code> when that entity is a player. An invalid target or
configuration produces the normal MythicMobs skill failure result.

## Lifecycle triggers

The adapter registers:

| Canonical trigger | Aliases |
| --- | --- |
| <code>ARMATURE_ANIMATION_START</code> | <code>armatureanimationstart</code>, <code>armature-animation-start</code> |
| <code>ARMATURE_ANIMATION_END</code> | <code>armatureanimationend</code>, <code>armature-animation-end</code> |
| <code>ARMATURE_ANIMATION_COMPLETE</code> | <code>armatureanimationcomplete</code>, <code>armature-animation-complete</code> |

Example:

~~~yaml
Triggers:
  - ARMATURE_ANIMATION_END
~~~

The trigger metadata contains:

* <code>armature.profile</code>
* <code>armature.action</code>
* <code>armature.animation</code>
* <code>armature.reason</code>
* <code>armature.token</code>

<code>ARMATURE_ANIMATION_END</code> fires for every finite-animation
termination. <code>ARMATURE_ANIMATION_COMPLETE</code> fires only when the
animation reaches its natural end.

## Modern signal rules

Use <code>armaturesignal</code> when the profile owns the mapping:

~~~yaml
Skills:
  WeaponFire:
    Skills:
      - armaturesignal{signal=mythicmobs:weapon.fire} @self
~~~

~~~yaml
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
~~~

Dynamic right-hand values in conditions require the
<code>$</code> prefix. For example,
<code>weaponmechanics.ammo == $weaponmechanics.magazine-size</code> compares
two runtime values; without <code>$</code>, a path-like right-hand string is
literal.

See [Built-in triggers](../../getting-started/profiles/triggers.md),
[Built-in conditions](../../getting-started/profiles/conditions.md), and
[Public API](../public-api.md).
