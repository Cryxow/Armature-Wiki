---
description: "Triggers"
---

# ⚡ Triggers

Triggers are instantaneous namespaced signals. Continuous state belongs in
`animations.movement` or `animations.additives`; it must not fire an action on
every tick.

The modern tree keeps all rules under one `animations` container. Action rules
use `trigger` and optional `condition`; their playback channel is always
exclusive `replace`, so `mode` is not a valid field.

```yaml
animations:
  actions:
    - trigger: player:attack
      condition:
        - player.using-item == false
      animation:
        name: [swing, swing_1, swing_2]
        selection: random
        duration: 6t

    - trigger: weaponmechanics:reload
      condition:
        - weaponmechanics.aiming == false
      animation:
        name: inspect
        duration: 20t
      consume: true
```

All event arguments are exposed during evaluation as
`event.argument.<name>`. Multiple rules may use one trigger; the most
specific matching condition wins, then declaration order is the fallback.

Status: `implemented` is emitted by the base plugin; `provider` requires the
named optional provider; `API/Skript/Denizen` means an external caller can
publish it; `reserved` is part of the vocabulary but has no bundled emitter
yet.

## Namespace policy

| Namespace | Source | Examples | Status |
| --- | --- | --- | --- |
| `player:` | normalized player event | `player:attack` | implemented |
| `item:` | item/profile lifecycle | `item:equip` | implemented |
| `block:` | block interaction/mining | `block:place` | implemented |
| `entity:` | entity interaction/combat | `entity:hit` | implemented |
| `projectile:` | projectile launch/impact | `projectile:hit` | implemented |
| `world:` | world/environment transition | `world:change` | implemented |
| `input:` | client input or packet intent | `input:jump-press` | implemented |
| `animation:` | Armature animation lifecycle/marker | `animation:complete` | implemented |
| `<provider>:` | external plugin adapter | `weaponmechanics:reload` | provider |
| `<integration>:` | external caller-owned signal | `skript:weapon.special` | API/Skript/Denizen |

There is no `armature:` prefix. A provider or integration owns its namespace;
Armature only resolves the signal against the active item profile.

## Player actions and interactions

| Trigger | Emitted when | Useful arguments | Status |
| --- | --- | --- | --- |
| `player:attack` | Main-hand attack/swing is observed. | `hand`, `target`, `critical` | implemented |
| `player:use-item` | Use/right-click starts. | `hand`, `item`, `action` | implemented |
| `player:use-item-stop` | Item use ends before completion. | `hand`, `item`, `reason` | implemented |
| `player:use-item-complete` | Item use completes. | `hand`, `item` | implemented |
| `player:consume-item` | Food, potion, or consumable is consumed. | `hand`, `item` | implemented |
| `player:release-item` | A held/charged item is released. | `hand`, `item`, `charge` | implemented |
| `player:swap-hand` | Main/off-hand swap is requested. | `previous-main-hand`, `previous-off-hand` | implemented |
| `player:drop-item` | Player drops an item. | `hand`, `item`, `amount` | implemented |
| `player:pickup-item` | Player picks up an item. | `item`, `amount` | implemented |
| `player:hotbar-change` | Selected hotbar slot changes. | `previous-slot`, `slot`, `item` | implemented |
| `player:interact` | Generic player interaction occurs. | `hand`, `action`, `target` | implemented |

`player:use-item` and `input:secondary-press` represent right-click intent,
including clicks in air where vanilla has no item action. Paper may mark those
`PlayerInteractEvent`s cancelled by default; Armature still observes the input.

`player:attack` includes a target classification computed at the swing time:
`event.argument.target` is `air`, `block`, or `entity`. The more precise
`event.argument.target-type` contains the namespaced block/entity type, or
`minecraft:air`. This is the server ray-trace target, before another plugin
can cancel or alter the resulting damage.

```yaml
actions:
  - trigger: player:attack
    condition: event.argument.target in [air, entity]
    animation:
      name: [sword_swing_1, sword_swing_2]
      selection: sequence

  - trigger: block:damage
    animation:
      name: block_swing
```

## Movement transitions

Movement loops should use `when`, for example
`player.climbing == true` and `player.climbing-direction == down`. These
signals are for one-shot transition animations.

| Trigger | Emitted when | Status |
| --- | --- | --- |
| `player:move-start` | Meaningful movement begins. | implemented |
| `player:move-stop` | Meaningful movement stops. | implemented |
| `player:sprint-start` | Sprinting begins. | implemented |
| `player:sprint-stop` | Sprinting ends. | implemented |
| `player:sneak-start` | Sneaking begins. | implemented |
| `player:sneak-stop` | Sneaking ends. | implemented |
| `player:jump` | Upward jump starts. | implemented |
| `player:land` | Player returns to ground. | implemented |
| `player:fall-start` | Airborne downward movement starts. | implemented |
| `player:fall-stop` | Downward movement stops. | implemented |
| `player:climb-start` | Climbing starts. | implemented |
| `player:climb-stop` | Climbing ends. | implemented |
| `player:swim-start` | Swimming starts. | implemented |
| `player:swim-stop` | Swimming ends. | implemented |
| `player:glide-start` | Elytra gliding starts. | implemented |
| `player:glide-stop` | Elytra gliding ends. | implemented |
| `player:mount` | Player mounts a vehicle/entity. | implemented |
| `player:dismount` | Player leaves a vehicle/entity. | implemented |
| `player:direction-change` | Actual relative movement direction changes. | implemented |

Transition details include `speed`, `vertical-speed`, `fall-distance`,
`fall-time`, `direction`, and `previous-direction`.

## Player lifecycle and status

| Trigger | Emitted when | Status |
| --- | --- | --- |
| `player:join` | Player joins. | implemented |
| `player:ready` | Presentation resync is ready for the client. | implemented |
| `player:quit` | Player leaves. | implemented |
| `player:respawn` | Player respawns. | implemented |
| `player:death` | Player enters the death state. | implemented |
| `player:teleport` | Same-world or cross-world teleport succeeds. | implemented |
| `player:world-change` | Player changes world. | implemented |
| `player:damage-taken` | Player takes damage. | implemented |
| `player:heal` | Player health increases. | implemented |
| `player:effect-add` | Potion effect is added or changed. | implemented |
| `player:effect-remove` | Potion effect is removed. | implemented |
| `player:effect-change` | Potion effect action has no add/remove mapping. | implemented |
| `player:food-change` | Food level changes. | implemented |
| `player:fire-start` | Player catches fire. | implemented |
| `player:fire-stop` | Player stops burning. | implemented |
| `player:freeze-start` | Freezing starts. | implemented |
| `player:freeze-stop` | Freezing stops/recedes. | implemented |

## Item and equipment

| Trigger | Emitted when | Status |
| --- | --- | --- |
| `item:equip` | A modern profile becomes active. | implemented |
| `item:unequip` | A modern profile stops being active. | implemented |
| `item:held-change` | Resolved held identity changes. | implemented |
| `item:main-hand-change` | Main-hand slot changes. | implemented |
| `item:off-hand-change` | Off-hand item changes. | implemented |
| `item:durability-change` | Item durability changes. | implemented |
| `item:consume` | Held item is consumed/decremented. | implemented |
| `item:charge-start` | Charge/use begins. | implemented |
| `item:charge-release` | Charge/use is released. | implemented |

`item:equip` and `item:unequip` describe presentation sessions. They never
transfer inventory or gameplay ownership to Armature.

## Blocks, entities, and projectiles

| Trigger | Emitted when | Useful arguments | Status |
| --- | --- | --- | --- |
| `block:break-start` | Client begins mining. | `phase: start` | implemented |
| `block:damage` | Bukkit reports block damage, usually at the start. | `block`, `tool`, `face`, `phase` | implemented |
| `block:break` | Block break completes. | `block`, `tool`, `phase: complete` | implemented |
| `block:mining-start` | A mining session starts. | `block`, `tool`, `face`, `elapsed: 0` | implemented |
| `block:mining-stop` | A mining session ends or is cancelled. | `block`, `tool`, `face`, `elapsed` | implemented |
| `block:place` | Block placement succeeds. | `block`, `against`, `face`, `hand`, `tool` | implemented |
| `block:interact` | Player interacts with a block. | `block`, `face`, `hand`, `action` | implemented |
| `entity:interact` | Player interacts with an entity. | `entity`, `hand`, `action` | implemented |
| `entity:hit` | Player damages an entity. | `target`, `damage`, `damage-type` | implemented |
| `entity:death` | Player's killed entity dies. | `entity`, `killer` | implemented |
| `projectile:launch` | Player launches a projectile. | `projectile`, `item` | implemented |
| `projectile:hit` | Player projectile hits entity/block. | `projectile`, `target`, `block` | implemented |

`block:break-start` comes from PacketEvents and is an intent signal;
`block:break` is the accepted completion event. This distinction avoids
playing a completion animation when mining is cancelled. The continuous state
is exposed as `block.mining` and is refreshed with the player tick; Armature
does not emit a new action signal every tick. In Creative, repeated start/break
signals refresh the mining state; it stops after eight ticks without another
hit because the client does not maintain Survival's destroy-progress session.

## World and environment

| Trigger | Emitted when | Status |
| --- | --- | --- |
| `world:change` | World-change alias is observed. | implemented |
| `world:weather-change` | Rain/thunder state changes. | implemented |
| `world:time-phase-change` | Day/night phase changes. | implemented |
| `world:enter-water` | Player enters water. | implemented |
| `world:leave-water` | Player leaves water. | implemented |
| `world:enter-lava` | Player enters lava. | implemented |
| `world:leave-lava` | Player leaves lava. | implemented |
| `world:enter-powder-snow` | Player enters powder snow. | implemented |
| `world:leave-powder-snow` | Player leaves powder snow. | implemented |

## Low-level input

These describe client intent. They can fire even when a gameplay plugin blocks
the resulting action.

| Trigger | Status |
| --- | --- |
| `input:primary-press` | implemented |
| `input:primary-release` | implemented |
| `input:secondary-press` | implemented |
| `input:secondary-release` | implemented |
| `input:forward-press` / `input:forward-release` | implemented |
| `input:backward-press` / `input:backward-release` | implemented |
| `input:left-press` / `input:left-release` | implemented |
| `input:right-press` / `input:right-release` | implemented |
| `input:jump-press` / `input:jump-release` | implemented |
| `input:sneak-press` / `input:sneak-release` | implemented |
| `input:sprint-press` / `input:sprint-release` | implemented |
| `input:slot-change` | implemented |

## Animation lifecycle

| Trigger | Emitted when | Status |
| --- | --- | --- |
| `animation:start` | A modern action/transition starts. | implemented |
| `animation:complete` | A finite action completes. | implemented |
| `animation:cancel` | An action is replaced, cancelled, or removed. | implemented |
| `animation:transition-start` | A movement transition starts. | implemented |
| `animation:transition-end` | A movement transition completes. | implemented |
| `animation:marker` | BetterModel emits an animation marker. | implemented |
| `animation:signal` | A marker signal is forwarded generically. | implemented |

These are Armature session events, not BetterModel implementation details.

## Provider and custom triggers

WeaponMechanics uses its own namespace. The base adapter normalizes its action
events and forwards provider details such as `weaponmechanics.ammo`,
`weaponmechanics.aiming`, `weaponmechanics.reloading`,
`weaponmechanics.rounds-remaining`, and `weaponmechanics.firearm-state`.

| Signal family | Examples | Status |
| --- | --- | --- |
| WeaponMechanics | `weaponmechanics:fire`, `weaponmechanics:reload`, `weaponmechanics:reload-phase`, `weaponmechanics:firearm-state-change`, `weaponmechanics:fire-mode-change`, `weaponmechanics:reload-complete` | provider |
| Java API | `api:custom-hit`, `custom:parry` | API |
| Skript addon | `skript:weapon.special` | API/Skript |
| Denizen | `denizen:weapon.parry` | API/Denizen |
| Other plugin | `<plugin-id>:<event>` | API |

```yaml
animations:
  actions:
    - trigger: api:custom-hit
      condition:
        - event.argument.critical == true
      animation:
        name: hit
        duration: 6t
```

Java callers use `ArmatureApi.animations().sendSignal(playerId, signal,
details)`. The Skript and Denizen addons expose the equivalent signal
operations; Denizen also provides native lifecycle events and raw playback
commands.

Gameplay plugins remain responsible for deciding whether the gameplay event
succeeds. `consume: true` is meaningful only when the event owner allows
cancellation; for external API signals it is returned as result metadata.


## Equip and unequip

`item:equip` belongs to the incoming presentation. An accepted `item:unequip`
keeps the outgoing instance/pose alive for that clip, including rapid switches
and switching back to the same profile. Without an accepted unequip clip it is
removed immediately. `render.dynamic-items.defer-until-equipped` retains its
visual item snapshot until the incoming presentation is equipped.
This never delays inventory selection or weapon gameplay.
