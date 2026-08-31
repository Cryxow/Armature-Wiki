# 🧮 Built-in Conditions

The modern profile loader uses the same condition language for movement rules, action rules, and additive rules:

* `when` selects a movement or additive rule.
* `condition` filters an action trigger.

The paths and aliases on this page are the built-in 1.3.0 runtime vocabulary. Provider values and event arguments are added at runtime as described below.

## Syntax

A scalar condition is a lookup, a comparison, or a boolean literal:

```yaml
when:
  - player.on-ground == true
  - player.speed < 2
  - player.movement-direction in [forward, backward]
```

Supported operators are:

| Operator             | Meaning                                                        |
| -------------------- | -------------------------------------------------------------- |
| `==`                 | Equal. Strings compare case-insensitively.                     |
| `!=`                 | Not equal.                                                     |
| `<`, `<=`, `>`, `>=` | Numeric or case-insensitive string comparison.                 |
| `in`                 | Membership in a list; a two-number list is an inclusive range. |
| `contains`           | String, collection, or map containment.                        |

A YAML list is an `all`/AND group. An explicit map can combine groups and comparisons:

```yaml
condition:
  all:
    - player.on-ground == true
    - any:
        - player.sprinting == true
        - player.movement-direction == forward
  not:
    - player.sneaking == true
```

The map form also accepts a path as a key; it is equivalent to equality with the scalar value:

```yaml
condition:
  player.on-ground: true
  player.pose: standing
```

An empty list or empty map is always true. An empty `any` group is false. A condition path is lower-cased and may not contain spaces.

### Right-hand values

Booleans, `null`, numbers, strings, lists, and durations are supported:

```yaml
condition:
  all:
    - player.health-percent <= 25
    - player.air-time >= 10t
    - player.pose == "fall-flying"
    - player.look-direction in [north, south]
```

Durations use ticks or seconds (`10t`, `0.3s`) and are converted to ticks. Unqualified identifiers on the right are literal strings. Prefix a dynamic right-hand path with `$`:

```yaml
condition:
  all:
    - weaponmechanics.ammo == $weaponmechanics.magazine-size
    - event.argument.old-mode != $event.argument.mode
```

Without `$`, `weaponmechanics.magazine-size` is compared as literal text, not looked up as another value. Missing left or right values make a comparison false. A bare lookup is true only when its value is the boolean `true`.

## Player state

### Posture, capability, and environment

| Path                    | Type    | Runtime meaning                                                        |
| ----------------------- | ------- | ---------------------------------------------------------------------- |
| `player.on-ground`      | boolean | The player is on the ground.                                           |
| `player.moving`         | boolean | Input or horizontal speed indicates movement.                          |
| `player.sneaking`       | boolean | Sneaking state or sneak input is active.                               |
| `player.sprinting`      | boolean | Sprinting state or sprint input is active.                             |
| `player.swimming`       | boolean | The player is swimming.                                                |
| `player.falling`        | boolean | Airborne and descending.                                               |
| `player.jumping`        | boolean | Airborne with upward vertical velocity.                                |
| `player.climbing`       | boolean | The player is on a climbable surface.                                  |
| `player.gliding`        | boolean | Elytra gliding is active.                                              |
| `player.flying`         | boolean | Player flying is active.                                               |
| `player.riding`         | boolean | The player is inside a vehicle.                                        |
| `player.passenger`      | boolean | The player has a vehicle/passenger relation through Bukkit getVehicle. |
| `player.has-passengers` | boolean | The player currently carries passengers.                               |
| `player.sleeping`       | boolean | The player is sleeping.                                                |
| `player.using-item`     | boolean | The player's hand is raised for item use.                              |
| `player.blocking`       | boolean | The player is actively blocking.                                       |
| `player.dead`           | boolean | Bukkit reports the player as dead.                                     |
| `player.spectator`      | boolean | The game mode is spectator.                                            |
| `player.invisible`      | boolean | Bukkit invisibility is active.                                         |
| `player.has-vehicle`    | boolean | A vehicle is present.                                                  |
| `player.swinging`       | boolean | The player is inside the short Armature swing window.                  |
| `player.in-water`       | boolean | The player is in water.                                                |
| `player.in-lava`        | boolean | The player is in lava.                                                 |
| `player.in-powder-snow` | boolean | The player is in powder snow.                                          |
| `player.underwater`     | boolean | The block at the player's eye location is liquid.                      |
| `player.pose`           | string  | Bukkit pose, lower-cased with `_` changed to `-`.                      |

Examples of `player.pose` include `standing`, `crouching`, `swimming`, `fall-flying`, `sleeping`, `riding`, `spin-attack`, and `dying`, depending on the server state.

### Movement and location

| Path                         | Type          | Runtime meaning                                                                 |
| ---------------------------- | ------------- | ------------------------------------------------------------------------------- |
| `player.speed`               | number        | Horizontal velocity magnitude multiplied by 20.                                 |
| `player.horizontal-speed`    | number        | Alias of the current horizontal speed value.                                    |
| `player.vertical-speed`      | number        | Y velocity multiplied by 20; positive is upward.                                |
| `player.total-speed`         | number        | Full velocity magnitude multiplied by 20.                                       |
| `player.fall-distance`       | number        | Bukkit fall distance.                                                           |
| `player.air-time`            | ticks         | Airborne tick counter; reset to `0` on ground.                                  |
| `player.time-since-grounded` | ticks         | Counter since the last grounded sample; reset to `0` on ground.                 |
| `player.jump-time`           | ticks         | Airborne counter, or `0` while grounded.                                        |
| `player.acceleration`        | number        | Current horizontal speed minus the previous sampled speed.                      |
| `player.direction`           | enum          | Relative velocity direction: `forward`, `backward`, `left`, `right`, or `none`. |
| `player.movement-direction`  | enum          | Same relative velocity direction value as `player.direction`.                   |
| `player.climbing-direction`  | enum          | `up`, `down`, or `none`.                                                        |
| `player.look-direction`      | enum          | Cardinal facing: `south`, `west`, `north`, or `east`.                           |
| `player.world`               | string        | Bukkit world name.                                                              |
| `player.dimension`           | namespaced id | World key, for example `minecraft:overworld`.                                   |
| `player.biome`               | namespaced id | Biome key at the player's current block.                                        |
| `player.light-level`         | number        | Block light level at the player's current block.                                |
| `player.x`                   | number        | Player X coordinate.                                                            |
| `player.y`                   | number        | Player Y coordinate.                                                            |
| `player.z`                   | number        | Player Z coordinate.                                                            |

`player.moving` uses current input intent when available and otherwise the configured movement threshold. The speed values are server-tick-scaled, so compare them consistently within one server configuration.

## Input

| Path                           | Type    | Meaning                                                                         |
| ------------------------------ | ------- | ------------------------------------------------------------------------------- |
| `input.direction`              | enum    | First active intent in order `forward`, `backward`, `left`, `right`, or `none`. |
| `input.forward`                | boolean | Forward input is held.                                                          |
| `input.backward`               | boolean | Backward input is held.                                                         |
| `input.left`                   | boolean | Left input is held.                                                             |
| `input.right`                  | boolean | Right input is held.                                                            |
| `input.jump`                   | boolean | Jump input is held.                                                             |
| `input.sneak`                  | boolean | Sneak input is held.                                                            |
| `input.sprint`                 | boolean | Sprint input is held.                                                           |
| `input.just-pressed.forward`   | boolean | Forward changed from released to held this tick.                                |
| `input.just-pressed.backward`  | boolean | Backward changed from released to held this tick.                               |
| `input.just-pressed.left`      | boolean | Left changed from released to held this tick.                                   |
| `input.just-pressed.right`     | boolean | Right changed from released to held this tick.                                  |
| `input.just-pressed.jump`      | boolean | Jump changed from released to held this tick.                                   |
| `input.just-pressed.sneak`     | boolean | Sneak changed from released to held this tick.                                  |
| `input.just-pressed.sprint`    | boolean | Sprint changed from released to held this tick.                                 |
| `input.just-released.forward`  | boolean | Forward changed from held to released this tick.                                |
| `input.just-released.backward` | boolean | Backward changed from held to released this tick.                               |
| `input.just-released.left`     | boolean | Left changed from held to released this tick.                                   |
| `input.just-released.right`    | boolean | Right changed from held to released this tick.                                  |
| `input.just-released.jump`     | boolean | Jump changed from held to released this tick.                                   |
| `input.just-released.sneak`    | boolean | Sneak changed from held to released this tick.                                  |
| `input.just-released.sprint`   | boolean | Sprint changed from held to released this tick.                                 |

Primary and secondary hand press/release signals are triggers, not condition paths; see [Built-in triggers](triggers.md).

## World and nearby blocks

| Path                       | Type          | Runtime meaning                                                         |
| -------------------------- | ------------- | ----------------------------------------------------------------------- |
| `world.time`               | number        | World time of day.                                                      |
| `world.full-time`          | number        | World full time.                                                        |
| `world.game-time`          | number        | World game time.                                                        |
| `world.is-day`             | boolean       | Bukkit day-time state.                                                  |
| `world.is-night`           | boolean       | Inverse of `world.is-day`.                                              |
| `world.raining`            | boolean       | World has storm/rain.                                                   |
| `world.thundering`         | boolean       | World is thundering.                                                    |
| `world.weather`            | enum          | `clear`, `rain`, or `thunder`.                                          |
| `world.moon-phase`         | number        | Current phase index from `0` to `7`.                                    |
| `block.below.type`         | namespaced id | Material below the player.                                              |
| `block.above.type`         | namespaced id | Material one block above the current block.                             |
| `block.current.type`       | namespaced id | Material at the player's current block.                                 |
| `block.below.solid`        | boolean       | Block below is solid.                                                   |
| `block.above.solid`        | boolean       | Block above is solid.                                                   |
| `block.below.liquid`       | boolean       | Block below is liquid.                                                  |
| `block.current.liquid`     | boolean       | Current block is liquid.                                                |
| `block.current.passable`   | boolean       | Current block is passable.                                              |
| `block.below.slipperiness` | number        | Bukkit slipperiness of the block below.                                 |
| `block.below.climbable`    | boolean       | Block below is a ladder or vine according to Armature's built-in check. |

## Items, equipment, and player status

### Held items

| Path                                | Type          | Runtime meaning                                                                          |
| ----------------------------------- | ------------- | ---------------------------------------------------------------------------------------- |
| `item.main-hand.type`               | item id       | Resolved identity, such as `weaponmechanics:m4a1`; falls back to the Minecraft material. |
| `item.main-hand.material`           | namespaced id | Actual Minecraft material.                                                               |
| `item.main-hand.amount`             | number        | Stack amount.                                                                            |
| `item.main-hand.durability`         | number        | Current item damage; `0` for non-damageable items.                                       |
| `item.main-hand.max-durability`     | number        | Maximum damage; `0` for non-damageable items.                                            |
| `item.main-hand.durability-percent` | number        | Remaining durability percentage; `100` for non-damageable items.                         |
| `item.main-hand.uses`               | number        | Current damage value used by Armature's compatibility path.                              |
| `item.off-hand.type`                | namespaced id | Actual off-hand material identity.                                                       |
| `item.off-hand.material`            | namespaced id | Actual off-hand Minecraft material.                                                      |
| `item.off-hand.amount`              | number        | Off-hand stack amount.                                                                   |
| `item.off-hand.durability`          | number        | Off-hand current damage.                                                                 |
| `item.off-hand.max-durability`      | number        | Off-hand maximum damage.                                                                 |
| `item.off-hand.durability-percent`  | number        | Off-hand remaining durability percentage.                                                |
| `item.off-hand.uses`                | number        | Off-hand current damage compatibility value.                                             |

### Equipment and status

| Path                            | Type    | Runtime meaning                                               |
| ------------------------------- | ------- | ------------------------------------------------------------- |
| `player.held-slot`              | number  | Current hotbar slot.                                          |
| `player.held-hand`              | enum    | Current held hand; the current runtime publishes `main-hand`. |
| `player.has-off-hand-item`      | boolean | Off hand is not air.                                          |
| `player.armor-piece:<slot>`     | item id | Armor material for a Bukkit equipment slot.                   |
| `player.armor-piece:helmet`     | item id | Alias for `HEAD`.                                             |
| `player.armor-piece:chestplate` | item id | Alias for `CHEST`.                                            |
| `player.armor-piece:leggings`   | item id | Alias for `LEGS`.                                             |
| `player.armor-piece:boots`      | item id | Alias for `FEET`.                                             |
| `player.health`                 | number  | Current health.                                               |
| `player.max-health`             | number  | Maximum health.                                               |
| `player.health-percent`         | number  | Health percentage from `0` to `100`.                          |
| `player.food-level`             | number  | Food level.                                                   |
| `player.saturation`             | number  | Saturation.                                                   |
| `player.air`                    | number  | Remaining air.                                                |
| `player.level`                  | number  | Experience level.                                             |
| `player.experience-percent`     | number  | Progress to the next level from `0` to `100`.                 |
| `player.on-fire`                | boolean | Fire ticks are active.                                        |
| `player.freezing`               | boolean | Freeze ticks are active.                                      |

### Parameterized lookups

These paths are resolved dynamically even when they are not present in the snapshot map:

| Path                                   | Example                                         | Meaning                                                              |
| -------------------------------------- | ----------------------------------------------- | -------------------------------------------------------------------- |
| `player.has-effect:<id>`               | `player.has-effect:speed`                       | Player has the namespaced or short effect id.                        |
| `player.effect-amplifier:<id>`         | `player.effect-amplifier:minecraft:speed`       | Current amplifier of that effect.                                    |
| `player.effect-remaining:<id>`         | `player.effect-remaining:speed`                 | Remaining effect duration in ticks.                                  |
| `player.has-permission:<permission>`   | `player.has-permission:armature.command.status` | Bukkit permission result.                                            |
| `player.distance-to-ground`            | `player.distance-to-ground < 4`                 | Distance to the next non-passable block below, bounded to 64 blocks. |
| `item.main-hand.has-enchantment:<key>` | `item.main-hand.has-enchantment:sharpness`      | Main-hand enchantment exists.                                        |
| `item.main-hand.has-pdc:<key>`         | `item.main-hand.has-pdc:myplugin:variant`       | Main-hand PDC key exists.                                            |
| `item.main-hand.has-tag:<key>`         | `item.main-hand.has-tag:CustomTag`              | Main-hand custom NBT tag exists.                                     |

An absent effect or unavailable ground returns an absent value, so a comparison using it is false. The current dynamic item checks are main-hand checks; no off-hand `has-enchantment`, `has-pdc`, or `has-tag` aliases are published.

## Runtime animation state

### Movement

| Path                           | Type    | Meaning                                         |
| ------------------------------ | ------- | ----------------------------------------------- |
| `movement.active`              | boolean | A movement rule currently matches.              |
| `movement.id`                  | string  | Selected movement asset/rule name, or `none`.   |
| `movement.previous`            | string  | Previous movement selection, or `none`.         |
| `movement.changed`             | boolean | Selection changed during this update.           |
| `movement.transitioning`       | boolean | A movement `start`/`end` transition is playing. |
| `movement.transition-progress` | number  | Transition progress from `0.0` to `1.0`.        |
| `movement.entered:<asset>`     | boolean | Named movement was entered this update.         |
| `movement.left:<asset>`        | boolean | Named movement was left this update.            |

### Action and animation

| Path                        | Type    | Meaning                                                |
| --------------------------- | ------- | ------------------------------------------------------ |
| `action.active`             | boolean | A finite action is active.                             |
| `action.id`                 | string  | Current action id, or `none`.                          |
| `action.animation`          | string  | Current BetterModel animation asset, or `none`.        |
| `action.progress`           | number  | Current action progress from `0.0` to `1.0`.           |
| `action.elapsed`            | ticks   | Elapsed action ticks.                                  |
| `action.remaining`          | ticks   | Remaining action ticks.                                |
| `action.interruptible`      | boolean | Active action can be replaced.                         |
| `action.completed`          | boolean | The current state recorded a completion this update.   |
| `action.cancelled`          | boolean | The current state recorded a cancellation this update. |
| `animation.id`              | string  | Currently rendered finite asset or movement asset.     |
| `animation.progress`        | number  | Current animation progress.                            |
| `animation.elapsed`         | ticks   | Elapsed animation ticks.                               |
| `animation.remaining`       | ticks   | Remaining animation ticks.                             |
| `animation.loop-index`      | number  | Current movement loop index.                           |
| `animation.marker:<marker>` | boolean | Named marker was received on the current tick.         |

### Additives and cooldowns

| Path                              | Type    | Meaning                                               |
| --------------------------------- | ------- | ----------------------------------------------------- |
| `additive.active`                 | boolean | At least one additive group is active.                |
| `additive.<animation>.active`     | boolean | Additive with that primary animation name is active.  |
| `additive.<animation>.weight`     | number  | Resolved additive weight, or `0` while inactive.      |
| `cooldown.<trigger>.ready`        | boolean | Action rule for the exact namespaced trigger can run. |
| `cooldown.<trigger>.remaining`    | ticks   | Remaining cooldown for that trigger.                  |
| `action.cooldown-ready:<trigger>` | boolean | Alias for the trigger cooldown readiness.             |

For a trigger containing a colon, keep the colon in the path, for example `cooldown.weaponmechanics:reload.ready`.

### Presentation session

| Path                  | Type    | Meaning                                                              |
| --------------------- | ------- | -------------------------------------------------------------------- |
| `session.age`         | ticks   | Ticks since the current item/profile presentation session started.   |
| `session.first-equip` | boolean | True during the first initialization update for the current session. |

## Event context, variables, and providers

### Event context

These values are present while an action rule is evaluating a signal:

| Path                    | Type              | Meaning                                                                        |
| ----------------------- | ----------------- | ------------------------------------------------------------------------------ |
| `event.signal`          | namespaced string | Normalized signal, for example `player:attack`.                                |
| `event.type`            | namespaced string | Alias of `event.signal`.                                                       |
| `event.source`          | string            | Emitter source such as `vanilla`, `provider`, `api`, `animation`, or `marker`. |
| `event.elapsed`         | number            | `0` for the triggering evaluation.                                             |
| `event.argument.<name>` | scalar            | Event detail supplied by the emitter.                                          |
| `<name>`                | scalar            | Direct event-detail alias.                                                     |
| `<namespace>.<name>`    | scalar            | Event-detail alias under the signal namespace.                                 |

For `weaponmechanics:reload-phase`, for example, a detail named `round-index` is available as `event.argument.round-index`, `round-index`, and `weaponmechanics.round-index` during evaluation. The provider namespace is also cached for later profile evaluations until the item/profile session is changed or cleared.

### Profile variables

Each declared variable is exposed as both `variable.<id>` and `placeholder.<id>`. The alias does not mean PlaceholderAPI is installed; `source: placeholderapi` is the source that performs a PlaceholderAPI lookup.

### Provider values

There is no generic `provider.*` namespace. An action provider owns its namespace, for example:

```yaml
condition:
  all:
    - weaponmechanics.ammo == $weaponmechanics.magazine-size
    - weaponmechanics.aiming == false
```

Any scalar detail sent with a namespaced signal is converted to a boolean, finite number, or string. A provider can therefore expose more values without changing the condition syntax; consult the provider page for its exact fields.
