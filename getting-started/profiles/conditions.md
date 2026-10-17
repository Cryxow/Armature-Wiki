---
description: "Conditions"
---

# 🔀 Conditions

This document defines the built-in condition vocabulary for modern item
animation profiles. The parser and runtime implement the core values below;
provider, PlaceholderAPI, NBT, and expensive derived values remain optional at
runtime when their source is unavailable. Names are namespaced so built-in
values, provider values, and administrator-defined variables cannot collide.

Conditions use `when` for movement/additive rules and `condition` for action
rules. The action channel is always `replace`; there is no `action.mode` in
the modern format.

## Basic syntax

Conditions in a `when` list are combined with `AND`:

```yaml
when:
  - player.on-ground == true
  - player.sneaking == false
  - player.speed < 2
```

The readable comparison syntax should support:

```text
==       equals
!=       differs
<        less than
<=       less than or equal to
>        greater than
>=       greater than or equal to
in       belongs to a list or range
contains contains a value
```

For grouped logic, an explicit form can be supported:

```yaml
when:
  all:
    - player.on-ground == true
    - any:
        - player.sprinting == true
        - player.movement-direction == forward
  not:
    - player.sneaking == true
```

The same condition engine should be usable by movement rules, action events,
and additive animations. Movement rules select one base animation; additive
rules can be active at the same time.

## Namespaces

| Namespace | Ownership | Examples |
| --- | --- | --- |
| `player.*` | Armature built-in player state | `player.speed`, `player.swimming` |
| `input.*` | Armature/client input state | `input.forward`, `input.jump` |
| `item.*` | Current item and equipment | `item.main-hand.type` |
| `world.*` | World and time context | `world.is-day`, `world.weather` |
| `block.*` | Blocks around the player | `block.below.type` |
| `movement.*` | Current movement resolver state | `movement.active`, `movement.changed` |
| `action.*` | Current action state | `action.active`, `action.progress` |
| `animation.*` | Current animation state | `animation.id`, `animation.remaining` |
| `additive.*` | Additive layer state | `additive.breathing.weight` |
| `event.*` | Current event and its arguments | `event.signal`, `event.argument.mode` |
| `variable.*` | Profile/session/external values | `variable.stamina` |
| `<provider>.*` | External plugin adapters | `weaponmechanics.ammo` |
| `placeholder.*` | Explicit PlaceholderAPI values | `placeholder.player-stamina` |

`<provider>.*`, `variable.*`, and `placeholder.*` are not built-in player
conditions, but they belong in the same condition language. The provider name
is the namespace itself; a generic `provider.` prefix is not exposed to users.

Event names use the same source namespace with a colon separator:
`weaponmechanics:reload`, `player:attack`, or `block:break`. Values use a dot
to address a property in that namespace, such as `weaponmechanics.ammo` or
`player.speed`.

Held-item visual rules expose `item.using` (boolean) and `item.use-ticks`
(elapsed use ticks) for the rendered hand. These two values are evaluated per
hand; use them in `model.held-item.rules[].when`.

## Player state

### Posture and capabilities

| Condition | Type | Meaning |
| --- | --- | --- |
| `player.on-ground` | boolean | The player is supported by the ground. |
| `player.moving` | boolean | The player has meaningful horizontal movement. |
| `player.sneaking` | boolean | The player is sneaking/crouching. |
| `player.sprinting` | boolean | The player is sprinting. |
| `player.swimming` | boolean | The player is in the swimming pose/state. |
| `player.falling` | boolean | The player is descending through the air. |
| `player.jumping` | boolean | The player has just started an upward jump. |
| `player.climbing` | boolean | The player is on a climbable surface. |
| `player.gliding` | boolean | The player is gliding with an elytra. |
| `player.flying` | boolean | The player is flying rather than falling normally. |
| `player.riding` | boolean | The player is riding a vehicle or entity. |
| `player.passenger` | boolean | The player is being carried as a passenger. |
| `player.sleeping` | boolean | The player is sleeping. |
| `player.using-item` | boolean | The player is currently using an item. |
| `player.blocking` | boolean | The player is actively blocking. |
| `player.swinging` | boolean | The player is in a hand-swing animation. |
| `player.dead` | boolean | The player is dead or in the death state. |
| `player.spectator` | boolean | The player is in spectator mode. |
| `player.invisible` | boolean | The player is invisible. |
| `player.has-vehicle` | boolean | The player has a vehicle. |

`player.pose` can expose the underlying pose as an enum when a single boolean
is not sufficient, for example `standing`, `crouching`, `swimming`,
`fall-flying`, `sleeping`, `riding`, `spin-attack`, or `dying`.

### Movement values

| Condition | Type | Meaning |
| --- | --- | --- |
| `player.speed` | number | Normalized horizontal movement speed. |
| `player.horizontal-speed` | number | Horizontal velocity magnitude. |
| `player.vertical-speed` | number | Signed vertical velocity; positive is up. |
| `player.total-speed` | number | Full movement velocity magnitude. |
| `player.fall-distance` | number | Distance travelled downward since leaving ground. |
| `player.air-time` | duration | Time since leaving the ground, in ticks. |
| `player.time-since-grounded` | duration | Time since the last grounded state, in ticks. |
| `player.jump-time` | duration | Time since the current jump started, in ticks. |
| `player.distance-to-ground` | number | Estimated distance to the next solid ground. Optional and more expensive. |
| `player.acceleration` | number | Change in horizontal speed. Optional derived value. |

`player.speed` must have one documented unit and remain stable across server
implementations. `player.horizontal-speed` should be available when the exact
velocity is required. `player.fall-distance` and `player.vertical-speed` must
be independent values: one describes accumulated distance, the other current
direction and intensity.

### Direction

The short form `player.direction` may be supported, with
`player.movement-direction` as the explicit name.

```text
forward
backward
left
right
up
down
none
```

The direction is relative to the player's facing direction, not to world
coordinates. `up` and `down` are meaningful for climbing and other vertical
movement. The current resolver exposes the dominant horizontal component
(`forward`, `backward`, `left`, or `right`); input booleans remain available
when a rule must distinguish a diagonal input.

Two sources should remain distinct:

| Condition | Meaning |
| --- | --- |
| `input.direction` | Direction the player is trying to move. |
| `player.movement-direction` | Direction the player is actually moving. |
| `player.climbing-direction` | `up`, `down`, or `none` while climbing. |
| `player.look-direction` | Direction of the player's view, not movement. |

Animation selection should normally use actual movement. Input direction is
useful when an animation should respond immediately to a pressed key even if
the player is blocked.

### Input state

Input conditions are useful for edge-triggered actions and for distinguishing
intent from resulting movement:

| Condition | Type | Meaning |
| --- | --- | --- |
| `input.forward` | boolean | Forward input is held. |
| `input.backward` | boolean | Backward input is held. |
| `input.left` | boolean | Left input is held. |
| `input.right` | boolean | Right input is held. |
| `input.jump` | boolean | Jump input is held or active. |
| `input.sneak` | boolean | Sneak input is held. |
| `input.sprint` | boolean | Sprint input is held. |
| `input.just-pressed.<name>` | boolean | An input was pressed this update. |
| `input.just-released.<name>` | boolean | An input was released this update. |

## Environment and location

### Player location

| Condition | Type | Meaning |
| --- | --- | --- |
| `player.world` | string | Current world name. |
| `player.dimension` | enum | Current dimension/environment. |
| `player.biome` | string/enum | Current biome. |
| `player.x` | number | Current X coordinate. |
| `player.y` | number | Current Y coordinate. |
| `player.z` | number | Current Z coordinate. |
| `player.light-level` | number | Light level at the player. |
| `player.in-water` | boolean | The player is in water. |
| `player.in-lava` | boolean | The player is in lava. |
| `player.in-powder-snow` | boolean | The player is in powder snow. |
| `player.underwater` | boolean | The player's eyes are underwater. |

### World context

| Condition | Type | Meaning |
| --- | --- | --- |
| `world.time` | number | Current world time. |
| `world.is-day` | boolean | It is daytime. |
| `world.is-night` | boolean | It is nighttime. |
| `world.weather` | enum | `clear`, `rain`, or `thunder`. |
| `world.raining` | boolean | Rain is active. |
| `world.thundering` | boolean | Thunder is active. |
| `world.moon-phase` | enum/number | Current moon phase. |
| `world.game-time` | number | World game time. |

### Nearby blocks

Block conditions should be scoped to fixed positions around the player and
should not perform unbounded searches:

| Condition | Type | Meaning |
| --- | --- | --- |
| `block.below.type` | material | Block directly below the player. |
| `block.above.type` | material | Block directly above the player. |
| `block.current.type` | material | Block at the player's position. |
| `block.below.solid` | boolean | The block below is solid. |
| `block.current.liquid` | boolean | The current block is liquid. |
| `block.below.slipperiness` | number | Surface slipperiness below the player. |
| `block.below.climbable` | boolean | The block below or beside the player is climbable. |

Regions, claims, permissions, and arbitrary nearby-entity searches should be
provided by dedicated integrations rather than added to the core vocabulary.

## Player status and equipment

### Status values

| Condition | Type | Meaning |
| --- | --- | --- |
| `player.health` | number | Current health. |
| `player.max-health` | number | Maximum health. |
| `player.health-percent` | number | Current health as a percentage from `0` to `100`. |
| `player.food-level` | number | Current food level. |
| `player.saturation` | number | Current saturation. |
| `player.air` | number | Remaining air/breath value. |
| `player.experience-percent` | number | Progress toward the next experience level from `0` to `100`. |
| `player.level` | number | Current experience level. |
| `player.on-fire` | boolean | The player is burning. |
| `player.freezing` | boolean | The player is freezing. |
| `player.has-effect` | parameterized | The player has a named status effect. |
| `player.effect-amplifier` | parameterized number | Amplifier of a named status effect. |
| `player.effect-remaining` | parameterized duration | Remaining duration of a named effect. |
| `player.has-permission` | parameterized boolean | The player has a named permission. |

Examples:

```yaml
when:
  - player.health-percent < 25
  - player.has-effect:speed == true
  - player.has-permission:armature.debug == true
```

For profile-wide player opt-out, use the runtime permissions
`armature.disabled` or `armature.disabled.<profile-id>` instead of repeating a
permission condition in every rule. `player.has-permission` remains useful for
rule-specific animation conditions.

### Current item and equipment

The profile match remains the primary item selector. These conditions are for
additional runtime checks:

| Condition | Type | Meaning |
| --- | --- | --- |
| `item.main-hand.type` | item ID | Type/identity of the main-hand item. |
| `item.off-hand.type` | item ID | Type/identity of the off-hand item. |
| `item.main-hand.amount` | number | Stack amount in the main hand. |
| `item.main-hand.durability` | number | Current durability or damage value. |
| `item.main-hand.durability-percent` | number | Remaining durability percentage. |
| `item.main-hand.uses` | number | Remaining or consumed uses when available. |
| `item.main-hand.has-enchantment` | parameterized boolean | Whether a named enchantment exists. |
| `item.main-hand.has-tag` | parameterized boolean | Whether a custom tag exists. |
| `item.main-hand.has-pdc` | parameterized boolean | Whether a namespaced PDC value exists. |
| `player.held-slot` | number | Current hotbar slot. |
| `player.held-hand` | enum | `main-hand` or `off-hand`. |
| `player.has-off-hand-item` | boolean | The off hand is not empty. |
| `player.armor-piece` | parameterized item | A named armor slot's item. |

## Animation and session state

These values allow events and animation rules to react to Armature's own
runtime state without reading renderer internals.

### Movement state

| Condition | Type | Meaning |
| --- | --- | --- |
| `movement.active` | boolean | A movement rule currently matches. |
| `movement.id` | string | Identifier of the selected movement rule. |
| `movement.previous` | string | Previously selected movement rule. |
| `movement.changed` | boolean | The selected movement changed this update. |
| `movement.entered` | parameterized boolean | A named movement was just entered. |
| `movement.left` | parameterized boolean | A named movement was just left. |
| `movement.transitioning` | boolean | A movement transition is in progress. |
| `movement.transition-progress` | number | Transition progress from `0.0` to `1.0`. |

### Action state

| Condition | Type | Meaning |
| --- | --- | --- |
| `action.active` | boolean | An action is currently playing. |
| `action.id` | string | Current action identifier. |
| `action.progress` | number | Current action progress from `0.0` to `1.0`. |
| `action.elapsed` | duration | Time since the action started. |
| `action.remaining` | duration | Time remaining in the action. |
| `action.completed` | boolean | The current action completed this update. |
| `action.cancelled` | boolean | The current action was cancelled this update. |
| `action.interruptible` | boolean | The current action can be replaced. |
| `action.cooldown-ready` | parameterized boolean | A named action can trigger again. |

Modern actions always use the exclusive `replace` channel. Additive rules
provide the separate continuous composition channel; they do not set an
action mode.

### Mining state

| Condition | Type | Meaning |
| --- | --- | --- |
| `block.mining` | boolean | A main-hand mining session is active. |
| `block.mining.elapsed` | duration | Ticks elapsed since mining started. |
| `block.mining.block` | string | Namespaced block being mined. |
| `block.mining.tool` | string | Namespaced item in the main hand. |
| `block.mining.face` | enum | Block face being mined. |
| `block.mining.instant` | boolean | The latest block damage reported instant break. |

`block.mining.elapsed` is elapsed time, not a percentage: the public Bukkit
events do not expose the server's internal block-break percentage. Use the
boolean state for a loop and the `block:mining-start`/`block:mining-stop`
signals for one-shot start and end animations. Creative mode has no survival
mining-progress session, but repeated block-start/break signals keep
`block.mining` active while the player continues hitting blocks. It expires
after eight ticks without another hit; the last non-air block ID is retained
when the scheduled packet callback sees a block already removed by the break.

### Animation and additive state

| Condition | Type | Meaning |
| --- | --- | --- |
| `animation.id` | string | Current rendered animation identifier. |
| `animation.progress` | number | Current animation progress. |
| `animation.elapsed` | duration | Time since the animation started. |
| `animation.remaining` | duration | Time remaining. |
| `animation.marker` | parameterized boolean | A named marker was reached this update. |
| `animation.loop-index` | number | Current loop iteration. |
| `additive.active` | boolean | At least one additive layer is active. |
| `additive.<id>.active` | boolean | A named additive layer is active. |
| `additive.<id>.weight` | number | Current weight of a named additive layer. |

## Event and timing context

These conditions are only meaningful while evaluating an event or callback:

| Condition | Type | Meaning |
| --- | --- | --- |
| `event.type` | string | Normalized event type. |
| `event.signal` | namespaced string | External or provider signal. |
| `event.source` | enum | Built-in, provider, Skript, Denizen, API, or marker. |
| `event.argument.<name>` | typed value | Argument supplied with the event. |
| `event.elapsed` | duration | Time since the triggering event. |
| `session.age` | duration | Time since the profile session started. |
| `session.first-equip` | boolean | This is the first activation of the session. |
| `cooldown.<id>.ready` | boolean | A named cooldown is ready. |
| `cooldown.<id>.remaining` | duration | Remaining cooldown time. |

## Custom and external values

Built-in conditions should remain stable and cheap. Custom values can extend
the same syntax. Declare shared variables in `plugins/Armature/variables.yml`:

```yaml
variables:
  stamina:
    type: number
    source: placeholderapi
    placeholder: "%player_stamina%"
    refresh: 2t
```

Then reference them in a profile:

```yaml
animations:
  additives:
    - animation: breathing
      weight:
        parameter: variable.stamina
        input: [0, 100]
        output: [1.0, 0.15]
```

PlaceholderAPI values should be declared with a type and a refresh interval.
They should not be parsed independently for every condition on every tick.

Provider-specific gameplay state remains namespaced:

```yaml
when:
  - weaponmechanics.ammo == weaponmechanics.magazine-size
  - weaponmechanics.aiming == false
```

Armature should expose provider values through adapters but should not invent
generic built-in conditions for ammo, damage, reloads, mana, or other gameplay
state.



## Selection-specific conditions

`match.when` and variant selection use external state/shared variables, not
animation or selected-variant/accessory results. Mount conditions may use their
profile's selected `variant.active`, but not another mount's active state.
Animation rules can use `variant.active` and `attachment.<id>.active`.

```yaml
when:
  permissions:
    any: [cosmetics.gold, cosmetics.admin]
    none: [cosmetics.disabled]
  all:
    - variable.weapon-skin == gold
```

Criteria in a structured condition block combine with AND; a permissions `any`
list supplies alternatives. [Variables](variables.md) explains missing values,
types and refresh. Query availability depends on the actual runtime/provider
context; an optional source must be installed and configured before relying on it.
