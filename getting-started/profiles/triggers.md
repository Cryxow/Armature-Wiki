# ⚡ Built-in Triggers

An action rule reacts to an instantaneous, namespaced signal. Continuous state belongs in a movement or additive `when` rule; it should not start an action every tick.

```yaml
animations:
  actions:
    - trigger: player:attack
      condition: player.using-item == false
      animation:
        name: swing
        duration: 6t

    - trigger: weaponmechanics:reload
      condition:
        - weaponmechanics.aiming == false
      animation:
        name: inspect
        duration: 20t
      consume: true
```

The modern loader requires every `trigger` to contain a namespace and a colon. Multiple rules may use the same trigger. Armature selects the matching rule with the highest condition specificity, then uses declaration order as the tie-breaker.

## Signal context

During evaluation, the signal is available as `event.signal` and `event.type`. Its detail map is available as `event.argument.<name>`, as a direct `<name>` alias, and as `<namespace>.<name>`. Values are converted to booleans, finite numbers, or strings.

`consume: true` requests cancellation only when the emitter owns a cancellable event. It can cancel supported vanilla right-click or WeaponMechanics reload input. For API and external integration signals it is returned in the API result; the external caller still owns gameplay cancellation.

## Player lifecycle and item state

| Trigger                 | Emitted when                                                              | Details                                             |
| ----------------------- | ------------------------------------------------------------------------- | --------------------------------------------------- |
| `player:join`           | A player joins.                                                           | none                                                |
| `player:ready`          | The client-ready presentation resync succeeds after join or world change. | `reason`: `join` or `world_change`                  |
| `player:quit`           | A player leaves.                                                          | none                                                |
| `player:death`          | The player death cleanup starts.                                          | `damage-type`: currently `unknown`                  |
| `player:respawn`        | The player has respawned.                                                 | `world`                                             |
| `player:teleport`       | A non-cancelled same-world or synchronous cross-world teleport succeeds.  | `cause`; cross-world also `from-world`, `world`     |
| `player:world-change`   | Bukkit reports a world change, including plugin-driven changes.           | `from-world`, `world`                               |
| `world:change`          | Alias emitted with `player:world-change`.                                 | `from-world`, `world`                               |
| `player:swap-hand`      | A main/off-hand swap is requested.                                        | no detail from the low-priority cancellation path   |
| `input:slot-change`     | The selected hotbar slot changes.                                         | `previous-slot`, `slot`, `previous-item`, `item`    |
| `player:hotbar-change`  | Alias for the selected hotbar slot change.                                | same slot and item details                          |
| `item:held-change`      | The resolved held identity changes.                                       | same slot and item details                          |
| `item:main-hand-change` | The main-hand slot changes.                                               | same slot and item details                          |
| `item:off-hand-change`  | The off-hand item swap event is accepted.                                 | `previous-main-hand`, `previous-off-hand`           |
| `item:equip`            | A modern profile presentation becomes active.                             | `item`, optional `previous-item`, `hand: main-hand` |
| `item:unequip`          | A modern profile presentation stops being active.                         | `item`, `next-slot`, `hand: main-hand`              |

The item lifecycle signals describe presentation sessions. They do not change the inventory or transfer gameplay ownership to Armature.

## Player actions and interactions

| Trigger                    | Emitted when                                                                                                        | Details                                              |
| -------------------------- | ------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------- |
| `player:attack`            | A main-hand attack/swing packet is observed for a modern profile.                                                   | `hand: main-hand`                                    |
| `player:use-item`          | A right-click use starts on the off hand, or a supported vanilla right-click is routed through the trigger service. | `hand`, `action`, optional `item`, `block`, `face`   |
| `player:use-item-stop`     | Held item use changes from active to inactive during a runtime sample.                                              | transition context                                   |
| `player:use-item-complete` | A consumable is completed.                                                                                          | `hand`, `item`                                       |
| `player:consume-item`      | A consumable event is accepted.                                                                                     | `hand`, `item`                                       |
| `item:consume`             | Alias emitted for the accepted consumable event.                                                                    | `hand`, `item`                                       |
| `player:release-item`      | Held item use changes from active to inactive.                                                                      | transition context                                   |
| `player:drop-item`         | The player drops an item.                                                                                           | `item`, `amount`, `hand: main-hand`                  |
| `player:pickup-item`       | The player picks up an item.                                                                                        | `item`, `amount`                                     |
| `player:damage-taken`      | The player takes non-cancelled damage.                                                                              | `damage`, `damage-type`, optional `source`           |
| `player:heal`              | The player regains health.                                                                                          | `amount`, `cause`                                    |
| `player:food-change`       | Food level changes.                                                                                                 | `food-level`, `saturation`                           |
| `player:effect-add`        | A potion effect is added or changed.                                                                                | `effect`, `action`, optional `amplifier`, `duration` |
| `player:effect-remove`     | A potion effect is removed.                                                                                         | `effect`, `action`                                   |
| `player:effect-change`     | A potion effect event has another Bukkit action.                                                                    | `effect`, `action`, optional `amplifier`, `duration` |
| `item:durability-change`   | Bukkit reports item damage.                                                                                         | `damage`, `previous`, `value`                        |

## Blocks, entities, and projectiles

| Trigger             | Emitted when                                        | Details                                                         |
| ------------------- | --------------------------------------------------- | --------------------------------------------------------------- |
| `block:interact`    | A clicked block is present in a player interaction. | `hand`, `action`, `item`, `block`, `face`                       |
| `block:damage`      | A block damage/progress event is accepted.          | `block`, `tool`, `face`, `phase: progress`, `instant`           |
| `block:break-start` | The client starts digging.                          | `phase: start`                                                  |
| `block:break`       | A block break completes.                            | `block`, `tool`, `phase: complete`                              |
| `block:place`       | A block placement completes.                        | `block`, `against`, `face`, `hand`, `tool`, `phase: complete`   |
| `entity:interact`   | A player right-clicks an entity.                    | `entity`, `hand`, `action: right-click`                         |
| `player:interact`   | A player interaction or entity interaction occurs.  | `hand`, `action`, optional `item`, `block`, `face`, or `entity` |
| `entity:hit`        | A player damages an entity.                         | `target`, `damage`, `damage-type`                               |
| `entity:death`      | An entity killed by the player dies.                | `entity`, `killer` player UUID                                  |
| `projectile:launch` | A player launches a projectile.                     | `projectile`, resolved held `item`                              |
| `projectile:hit`    | A player projectile hits an entity or block.        | `projectile`, optional `target`, `block`                        |

`block:break-start` is client intent from PacketEvents; `block:break` is the accepted completion event. Use the former for the beginning of a mining animation and the latter for an animation that should represent a completed break.

## Movement and environment transitions

These signals are generated when the modern runtime observes a state change. Their common detail map contains `direction`, `previous-direction`, `speed`, `vertical-speed`, `fall-distance`, `fall-time`, `phase`, and `weather`.

| Trigger                                               | Transition                                            |
| ----------------------------------------------------- | ----------------------------------------------------- |
| `player:move-start` / `player:move-stop`              | Movement intent starts/stops.                         |
| `player:sprint-start` / `player:sprint-stop`          | Sprint starts/stops.                                  |
| `player:sneak-start` / `player:sneak-stop`            | Sneak starts/stops.                                   |
| `player:jump` / `player:land`                         | Upward jump starts / airborne player lands.           |
| `player:fall-start` / `player:fall-stop`              | Downward airborne movement starts/stops.              |
| `player:climb-start` / `player:climb-stop`            | Climbing starts/stops.                                |
| `player:swim-start` / `player:swim-stop`              | Swimming starts/stops.                                |
| `player:glide-start` / `player:glide-stop`            | Elytra gliding starts/stops.                          |
| `player:mount` / `player:dismount`                    | Vehicle relation starts/stops.                        |
| `player:direction-change`                             | Relative movement direction changes.                  |
| `world:enter-water` / `world:leave-water`             | Water state changes.                                  |
| `world:enter-lava` / `world:leave-lava`               | Lava state changes.                                   |
| `world:enter-powder-snow` / `world:leave-powder-snow` | Powder-snow state changes.                            |
| `player:fire-start` / `player:fire-stop`              | Fire ticks start/stop.                                |
| `player:freeze-start` / `player:freeze-stop`          | Freeze ticks start/stop.                              |
| `world:time-phase-change`                             | Day/night phase changes; `phase` is `day` or `night`. |
| `world:weather-change`                                | Storm state changes; `weather` is `rain` or `clear`.  |

## Low-level input

These signals represent client intent and can occur even when a gameplay plugin later blocks the resulting action:

| Trigger                                             | Meaning                                             |
| --------------------------------------------------- | --------------------------------------------------- |
| `input:primary-press` / `input:primary-release`     | Main-hand attack or dig input starts/stops.         |
| `input:secondary-press` / `input:secondary-release` | Use input starts/stops.                             |
| `input:forward-press` / `input:forward-release`     | Forward key changes.                                |
| `input:backward-press` / `input:backward-release`   | Backward key changes.                               |
| `input:left-press` / `input:left-release`           | Left key changes.                                   |
| `input:right-press` / `input:right-release`         | Right key changes.                                  |
| `input:jump-press` / `input:jump-release`           | Jump key changes.                                   |
| `input:sneak-press` / `input:sneak-release`         | Sneak key changes.                                  |
| `input:sprint-press` / `input:sprint-release`       | Sprint key changes.                                 |
| `input:slot-change`                                 | Selected hotbar slot changes.                       |
| `item:charge-start`                                 | A right-click is observed while the hand is raised. |
| `item:charge-release`                               | Packet use/charge release is observed.              |

## Animation lifecycle and markers

| Trigger                      | Emitted when                                                | Details                                                                  |
| ---------------------------- | ----------------------------------------------------------- | ------------------------------------------------------------------------ |
| `animation:start`            | A finite modern action or movement transition starts.       | `animation`, `action`, `layer`, `token`                                  |
| `animation:complete`         | A finite non-transition action reaches its natural end.     | `animation`, `action`, `layer: action`, `reason: completed`, `token`     |
| `animation:cancel`           | An action or transition is cancelled, replaced, or removed. | same fields with `reason`                                                |
| `animation:transition-start` | A movement start/end transition starts.                     | `animation`, `to`, `token`                                               |
| `animation:transition-end`   | A movement transition completes.                            | `animation`, `action`, `layer: transition`, `reason: completed`, `token` |
| `animation:marker`           | BetterModel sends an animation marker signal.               | `signal`                                                                 |
| `animation:signal`           | Generic marker signal alias.                                | `signal`                                                                 |
| `animation:<name>`           | A marker without a namespace is normalized to this signal.  | `signal`                                                                 |

The public Bukkit events `ArmatureAnimationStartEvent` and `ArmatureAnimationEndEvent` expose the same finite-animation lifecycle. `animation:complete` is emitted only for a natural completion; an end caused by replacement or removal is a cancellation signal.

## WeaponMechanics signals

When WeaponMechanics is enabled, the action provider emits these signals under the `weaponmechanics` namespace:

| Signal                                              | Source state                                                |
| --------------------------------------------------- | ----------------------------------------------------------- |
| `weaponmechanics:equip` / `weaponmechanics:unequip` | Weapon equipment changes in either hand.                    |
| `weaponmechanics:aim-enter`                         | Main-hand scope enters.                                     |
| `weaponmechanics:aim`                               | Main-hand scope is active or its zoom is refreshed.         |
| `weaponmechanics:aim-exit`                          | Main-hand scope exits.                                      |
| `weaponmechanics:fire`                              | Main-hand shot while not scoped.                            |
| `weaponmechanics:aim-fire`                          | Main-hand shot while scoped.                                |
| `weaponmechanics:reload`                            | Reload input before WeaponMechanics starts its normal path. |
| `weaponmechanics:reload-phase`                      | Each accepted reload iteration.                             |
| `weaponmechanics:reload-complete`                   | The reload reaches magazine capacity.                       |
| `weaponmechanics:reload-cancel`                     | A reload is cancelled.                                      |
| `weaponmechanics:firearm-state-change`              | Firearm open/close/state event.                             |
| `weaponmechanics:fire-mode-change`                  | Selective fire mode changes.                                |

All provider signals include `weapon-title` and `hand` details. See the [WeaponMechanics page](../../advanced/supported-plugins/weaponmechanics.md) for the full detail payload and reload conditions.

## External and custom signals

Armature accepts any valid namespaced signal sent through the public API or an optional integration. There is no fixed `armature:` prefix and no requirement to register a custom signal first:

```yaml
animations:
  actions:
    - trigger: myplugin:weapon-special
      condition: event.argument.critical == true
      animation:
        name: special
        duration: 6t
```

Java, Skript, MythicMobs, and Denizen callers can publish the same signal. Those callers own the gameplay event; Armature only resolves the matching profile rule and presents the configured animation.

## Compatibility identifiers

The legacy profile converter maps old action names to modern signals. For a WeaponMechanics item, firearm actions use the `weaponmechanics:` namespace; vanilla actions such as swing, mine, place, use, eat, drink, throw, jump, and land use their corresponding `player:`, `block:`, or `projectile:` signal. New profiles should write the modern namespaced signal directly rather than depending on the legacy action map.
