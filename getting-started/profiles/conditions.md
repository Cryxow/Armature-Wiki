# Built-in conditions

The modern profile loader uses the same condition language for movement rules,
action rules, and additive rules:

* <code>when</code> selects a movement or additive rule.
* <code>condition</code> filters an action trigger.

The paths and aliases on this page are the built-in 1.3.0 runtime vocabulary.
Provider values and event arguments are added at runtime as described below.

## Syntax

A scalar condition is a lookup, a comparison, or a boolean literal:

~~~yaml
when:
  - player.on-ground == true
  - player.speed < 2
  - player.movement-direction in [forward, backward]
~~~

Supported operators are:

| Operator | Meaning |
| --- | --- |
| <code>==</code> | Equal. Strings compare case-insensitively. |
| <code>!=</code> | Not equal. |
| <code>&lt;</code>, <code>&lt;=</code>, <code>&gt;</code>, <code>&gt;=</code> | Numeric or case-insensitive string comparison. |
| <code>in</code> | Membership in a list; a two-number list is an inclusive range. |
| <code>contains</code> | String, collection, or map containment. |

A YAML list is an <code>all</code>/AND group. An explicit map can combine
groups and comparisons:

~~~yaml
condition:
  all:
    - player.on-ground == true
    - any:
        - player.sprinting == true
        - player.movement-direction == forward
  not:
    - player.sneaking == true
~~~

The map form also accepts a path as a key; it is equivalent to equality with
the scalar value:

~~~yaml
condition:
  player.on-ground: true
  player.pose: standing
~~~

An empty list or empty map is always true. An empty <code>any</code> group is
false. A condition path is lower-cased and may not contain spaces.

### Right-hand values

Booleans, <code>null</code>, numbers, strings, lists, and durations are
supported:

~~~yaml
condition:
  all:
    - player.health-percent <= 25
    - player.air-time >= 10t
    - player.pose == "fall-flying"
    - player.look-direction in [north, south]
~~~

Durations use ticks or seconds (<code>10t</code>, <code>0.3s</code>) and are
converted to ticks. Unqualified identifiers on the right are literal strings.
Prefix a dynamic right-hand path with <code>$</code>:

~~~yaml
condition:
  all:
    - weaponmechanics.ammo == $weaponmechanics.magazine-size
    - event.argument.old-mode != $event.argument.mode
~~~

Without <code>$</code>, <code>weaponmechanics.magazine-size</code> is compared
as literal text, not looked up as another value. Missing left or right values
make a comparison false. A bare lookup is true only when its value is the
boolean <code>true</code>.

## Player state

### Posture, capability, and environment

| Path | Type | Runtime meaning |
| --- | --- | --- |
| <code>player.on-ground</code> | boolean | The player is on the ground. |
| <code>player.moving</code> | boolean | Input or horizontal speed indicates movement. |
| <code>player.sneaking</code> | boolean | Sneaking state or sneak input is active. |
| <code>player.sprinting</code> | boolean | Sprinting state or sprint input is active. |
| <code>player.swimming</code> | boolean | The player is swimming. |
| <code>player.falling</code> | boolean | Airborne and descending. |
| <code>player.jumping</code> | boolean | Airborne with upward vertical velocity. |
| <code>player.climbing</code> | boolean | The player is on a climbable surface. |
| <code>player.gliding</code> | boolean | Elytra gliding is active. |
| <code>player.flying</code> | boolean | Player flying is active. |
| <code>player.riding</code> | boolean | The player is inside a vehicle. |
| <code>player.passenger</code> | boolean | The player has a vehicle/passenger relation through Bukkit getVehicle. |
| <code>player.has-passengers</code> | boolean | The player currently carries passengers. |
| <code>player.sleeping</code> | boolean | The player is sleeping. |
| <code>player.using-item</code> | boolean | The player's hand is raised for item use. |
| <code>player.blocking</code> | boolean | The player is actively blocking. |
| <code>player.dead</code> | boolean | Bukkit reports the player as dead. |
| <code>player.spectator</code> | boolean | The game mode is spectator. |
| <code>player.invisible</code> | boolean | Bukkit invisibility is active. |
| <code>player.has-vehicle</code> | boolean | A vehicle is present. |
| <code>player.swinging</code> | boolean | The player is inside the short Armature swing window. |
| <code>player.in-water</code> | boolean | The player is in water. |
| <code>player.in-lava</code> | boolean | The player is in lava. |
| <code>player.in-powder-snow</code> | boolean | The player is in powder snow. |
| <code>player.underwater</code> | boolean | The block at the player's eye location is liquid. |
| <code>player.pose</code> | string | Bukkit pose, lower-cased with <code>_</code> changed to <code>-</code>. |

Examples of <code>player.pose</code> include <code>standing</code>,
<code>crouching</code>, <code>swimming</code>, <code>fall-flying</code>,
<code>sleeping</code>, <code>riding</code>, <code>spin-attack</code>, and
<code>dying</code>, depending on the server state.

### Movement and location

| Path | Type | Runtime meaning |
| --- | --- | --- |
| <code>player.speed</code> | number | Horizontal velocity magnitude multiplied by 20. |
| <code>player.horizontal-speed</code> | number | Alias of the current horizontal speed value. |
| <code>player.vertical-speed</code> | number | Y velocity multiplied by 20; positive is upward. |
| <code>player.total-speed</code> | number | Full velocity magnitude multiplied by 20. |
| <code>player.fall-distance</code> | number | Bukkit fall distance. |
| <code>player.air-time</code> | ticks | Airborne tick counter; reset to <code>0</code> on ground. |
| <code>player.time-since-grounded</code> | ticks | Counter since the last grounded sample; reset to <code>0</code> on ground. |
| <code>player.jump-time</code> | ticks | Airborne counter, or <code>0</code> while grounded. |
| <code>player.acceleration</code> | number | Current horizontal speed minus the previous sampled speed. |
| <code>player.direction</code> | enum | Relative velocity direction: <code>forward</code>, <code>backward</code>, <code>left</code>, <code>right</code>, or <code>none</code>. |
| <code>player.movement-direction</code> | enum | Same relative velocity direction value as <code>player.direction</code>. |
| <code>player.climbing-direction</code> | enum | <code>up</code>, <code>down</code>, or <code>none</code>. |
| <code>player.look-direction</code> | enum | Cardinal facing: <code>south</code>, <code>west</code>, <code>north</code>, or <code>east</code>. |
| <code>player.world</code> | string | Bukkit world name. |
| <code>player.dimension</code> | namespaced id | World key, for example <code>minecraft:overworld</code>. |
| <code>player.biome</code> | namespaced id | Biome key at the player's current block. |
| <code>player.light-level</code> | number | Block light level at the player's current block. |
| <code>player.x</code> | number | Player X coordinate. |
| <code>player.y</code> | number | Player Y coordinate. |
| <code>player.z</code> | number | Player Z coordinate. |

<code>player.moving</code> uses current input intent when available and
otherwise the configured movement threshold. The speed values are
server-tick-scaled, so compare them consistently within one server
configuration.

## Input

| Path | Type | Meaning |
| --- | --- | --- |
| <code>input.direction</code> | enum | First active intent in order <code>forward</code>, <code>backward</code>, <code>left</code>, <code>right</code>, or <code>none</code>. |
| <code>input.forward</code> | boolean | Forward input is held. |
| <code>input.backward</code> | boolean | Backward input is held. |
| <code>input.left</code> | boolean | Left input is held. |
| <code>input.right</code> | boolean | Right input is held. |
| <code>input.jump</code> | boolean | Jump input is held. |
| <code>input.sneak</code> | boolean | Sneak input is held. |
| <code>input.sprint</code> | boolean | Sprint input is held. |
| <code>input.just-pressed.forward</code> | boolean | Forward changed from released to held this tick. |
| <code>input.just-pressed.backward</code> | boolean | Backward changed from released to held this tick. |
| <code>input.just-pressed.left</code> | boolean | Left changed from released to held this tick. |
| <code>input.just-pressed.right</code> | boolean | Right changed from released to held this tick. |
| <code>input.just-pressed.jump</code> | boolean | Jump changed from released to held this tick. |
| <code>input.just-pressed.sneak</code> | boolean | Sneak changed from released to held this tick. |
| <code>input.just-pressed.sprint</code> | boolean | Sprint changed from released to held this tick. |
| <code>input.just-released.forward</code> | boolean | Forward changed from held to released this tick. |
| <code>input.just-released.backward</code> | boolean | Backward changed from held to released this tick. |
| <code>input.just-released.left</code> | boolean | Left changed from held to released this tick. |
| <code>input.just-released.right</code> | boolean | Right changed from held to released this tick. |
| <code>input.just-released.jump</code> | boolean | Jump changed from held to released this tick. |
| <code>input.just-released.sneak</code> | boolean | Sneak changed from held to released this tick. |
| <code>input.just-released.sprint</code> | boolean | Sprint changed from held to released this tick. |

Primary and secondary hand press/release signals are triggers, not condition
paths; see [Built-in triggers](triggers.md).

## World and nearby blocks

| Path | Type | Runtime meaning |
| --- | --- | --- |
| <code>world.time</code> | number | World time of day. |
| <code>world.full-time</code> | number | World full time. |
| <code>world.game-time</code> | number | World game time. |
| <code>world.is-day</code> | boolean | Bukkit day-time state. |
| <code>world.is-night</code> | boolean | Inverse of <code>world.is-day</code>. |
| <code>world.raining</code> | boolean | World has storm/rain. |
| <code>world.thundering</code> | boolean | World is thundering. |
| <code>world.weather</code> | enum | <code>clear</code>, <code>rain</code>, or <code>thunder</code>. |
| <code>world.moon-phase</code> | number | Current phase index from <code>0</code> to <code>7</code>. |
| <code>block.below.type</code> | namespaced id | Material below the player. |
| <code>block.above.type</code> | namespaced id | Material one block above the current block. |
| <code>block.current.type</code> | namespaced id | Material at the player's current block. |
| <code>block.below.solid</code> | boolean | Block below is solid. |
| <code>block.above.solid</code> | boolean | Block above is solid. |
| <code>block.below.liquid</code> | boolean | Block below is liquid. |
| <code>block.current.liquid</code> | boolean | Current block is liquid. |
| <code>block.current.passable</code> | boolean | Current block is passable. |
| <code>block.below.slipperiness</code> | number | Bukkit slipperiness of the block below. |
| <code>block.below.climbable</code> | boolean | Block below is a ladder or vine according to Armature's built-in check. |

## Items, equipment, and player status

### Held items

| Path | Type | Runtime meaning |
| --- | --- | --- |
| <code>item.main-hand.type</code> | item id | Resolved identity, such as <code>weaponmechanics:m4a1</code>; falls back to the Minecraft material. |
| <code>item.main-hand.material</code> | namespaced id | Actual Minecraft material. |
| <code>item.main-hand.amount</code> | number | Stack amount. |
| <code>item.main-hand.durability</code> | number | Current item damage; <code>0</code> for non-damageable items. |
| <code>item.main-hand.max-durability</code> | number | Maximum damage; <code>0</code> for non-damageable items. |
| <code>item.main-hand.durability-percent</code> | number | Remaining durability percentage; <code>100</code> for non-damageable items. |
| <code>item.main-hand.uses</code> | number | Current damage value used by Armature's compatibility path. |
| <code>item.off-hand.type</code> | namespaced id | Actual off-hand material identity. |
| <code>item.off-hand.material</code> | namespaced id | Actual off-hand Minecraft material. |
| <code>item.off-hand.amount</code> | number | Off-hand stack amount. |
| <code>item.off-hand.durability</code> | number | Off-hand current damage. |
| <code>item.off-hand.max-durability</code> | number | Off-hand maximum damage. |
| <code>item.off-hand.durability-percent</code> | number | Off-hand remaining durability percentage. |
| <code>item.off-hand.uses</code> | number | Off-hand current damage compatibility value. |

### Equipment and status

| Path | Type | Runtime meaning |
| --- | --- | --- |
| <code>player.held-slot</code> | number | Current hotbar slot. |
| <code>player.held-hand</code> | enum | Current held hand; the current runtime publishes <code>main-hand</code>. |
| <code>player.has-off-hand-item</code> | boolean | Off hand is not air. |
| <code>player.armor-piece:&lt;slot&gt;</code> | item id | Armor material for a Bukkit equipment slot. |
| <code>player.armor-piece:helmet</code> | item id | Alias for <code>HEAD</code>. |
| <code>player.armor-piece:chestplate</code> | item id | Alias for <code>CHEST</code>. |
| <code>player.armor-piece:leggings</code> | item id | Alias for <code>LEGS</code>. |
| <code>player.armor-piece:boots</code> | item id | Alias for <code>FEET</code>. |
| <code>player.health</code> | number | Current health. |
| <code>player.max-health</code> | number | Maximum health. |
| <code>player.health-percent</code> | number | Health percentage from <code>0</code> to <code>100</code>. |
| <code>player.food-level</code> | number | Food level. |
| <code>player.saturation</code> | number | Saturation. |
| <code>player.air</code> | number | Remaining air. |
| <code>player.level</code> | number | Experience level. |
| <code>player.experience-percent</code> | number | Progress to the next level from <code>0</code> to <code>100</code>. |
| <code>player.on-fire</code> | boolean | Fire ticks are active. |
| <code>player.freezing</code> | boolean | Freeze ticks are active. |

### Parameterized lookups

These paths are resolved dynamically even when they are not present in the
snapshot map:

| Path | Example | Meaning |
| --- | --- | --- |
| <code>player.has-effect:&lt;id&gt;</code> | <code>player.has-effect:speed</code> | Player has the namespaced or short effect id. |
| <code>player.effect-amplifier:&lt;id&gt;</code> | <code>player.effect-amplifier:minecraft:speed</code> | Current amplifier of that effect. |
| <code>player.effect-remaining:&lt;id&gt;</code> | <code>player.effect-remaining:speed</code> | Remaining effect duration in ticks. |
| <code>player.has-permission:&lt;permission&gt;</code> | <code>player.has-permission:armature.command.status</code> | Bukkit permission result. |
| <code>player.distance-to-ground</code> | <code>player.distance-to-ground &lt; 4</code> | Distance to the next non-passable block below, bounded to 64 blocks. |
| <code>item.main-hand.has-enchantment:&lt;key&gt;</code> | <code>item.main-hand.has-enchantment:sharpness</code> | Main-hand enchantment exists. |
| <code>item.main-hand.has-pdc:&lt;key&gt;</code> | <code>item.main-hand.has-pdc:myplugin:variant</code> | Main-hand PDC key exists. |
| <code>item.main-hand.has-tag:&lt;key&gt;</code> | <code>item.main-hand.has-tag:CustomTag</code> | Main-hand custom NBT tag exists. |

An absent effect or unavailable ground returns an absent value, so a comparison
using it is false. The current dynamic item checks are main-hand checks; no
off-hand <code>has-enchantment</code>, <code>has-pdc</code>, or
<code>has-tag</code> aliases are published.

## Runtime animation state

### Movement

| Path | Type | Meaning |
| --- | --- | --- |
| <code>movement.active</code> | boolean | A movement rule currently matches. |
| <code>movement.id</code> | string | Selected movement asset/rule name, or <code>none</code>. |
| <code>movement.previous</code> | string | Previous movement selection, or <code>none</code>. |
| <code>movement.changed</code> | boolean | Selection changed during this update. |
| <code>movement.transitioning</code> | boolean | A movement <code>start</code>/<code>end</code> transition is playing. |
| <code>movement.transition-progress</code> | number | Transition progress from <code>0.0</code> to <code>1.0</code>. |
| <code>movement.entered:&lt;asset&gt;</code> | boolean | Named movement was entered this update. |
| <code>movement.left:&lt;asset&gt;</code> | boolean | Named movement was left this update. |

### Action and animation

| Path | Type | Meaning |
| --- | --- | --- |
| <code>action.active</code> | boolean | A finite action is active. |
| <code>action.id</code> | string | Current action id, or <code>none</code>. |
| <code>action.animation</code> | string | Current BetterModel animation asset, or <code>none</code>. |
| <code>action.progress</code> | number | Current action progress from <code>0.0</code> to <code>1.0</code>. |
| <code>action.elapsed</code> | ticks | Elapsed action ticks. |
| <code>action.remaining</code> | ticks | Remaining action ticks. |
| <code>action.interruptible</code> | boolean | Active action can be replaced. |
| <code>action.completed</code> | boolean | The current state recorded a completion this update. |
| <code>action.cancelled</code> | boolean | The current state recorded a cancellation this update. |
| <code>animation.id</code> | string | Currently rendered finite asset or movement asset. |
| <code>animation.progress</code> | number | Current animation progress. |
| <code>animation.elapsed</code> | ticks | Elapsed animation ticks. |
| <code>animation.remaining</code> | ticks | Remaining animation ticks. |
| <code>animation.loop-index</code> | number | Current movement loop index. |
| <code>animation.marker:&lt;marker&gt;</code> | boolean | Named marker was received on the current tick. |

### Additives and cooldowns

| Path | Type | Meaning |
| --- | --- | --- |
| <code>additive.active</code> | boolean | At least one additive group is active. |
| <code>additive.&lt;animation&gt;.active</code> | boolean | Additive with that primary animation name is active. |
| <code>additive.&lt;animation&gt;.weight</code> | number | Resolved additive weight, or <code>0</code> while inactive. |
| <code>cooldown.&lt;trigger&gt;.ready</code> | boolean | Action rule for the exact namespaced trigger can run. |
| <code>cooldown.&lt;trigger&gt;.remaining</code> | ticks | Remaining cooldown for that trigger. |
| <code>action.cooldown-ready:&lt;trigger&gt;</code> | boolean | Alias for the trigger cooldown readiness. |

For a trigger containing a colon, keep the colon in the path, for example
<code>cooldown.weaponmechanics:reload.ready</code>.

### Presentation session

| Path | Type | Meaning |
| --- | --- | --- |
| <code>session.age</code> | ticks | Ticks since the current item/profile presentation session started. |
| <code>session.first-equip</code> | boolean | True during the first initialization update for the current session. |

## Event context, variables, and providers

### Event context

These values are present while an action rule is evaluating a signal:

| Path | Type | Meaning |
| --- | --- | --- |
| <code>event.signal</code> | namespaced string | Normalized signal, for example <code>player:attack</code>. |
| <code>event.type</code> | namespaced string | Alias of <code>event.signal</code>. |
| <code>event.source</code> | string | Emitter source such as <code>vanilla</code>, <code>provider</code>, <code>api</code>, <code>animation</code>, or <code>marker</code>. |
| <code>event.elapsed</code> | number | <code>0</code> for the triggering evaluation. |
| <code>event.argument.&lt;name&gt;</code> | scalar | Event detail supplied by the emitter. |
| <code>&lt;name&gt;</code> | scalar | Direct event-detail alias. |
| <code>&lt;namespace&gt;.&lt;name&gt;</code> | scalar | Event-detail alias under the signal namespace. |

For <code>weaponmechanics:reload-phase</code>, for example, a detail named
<code>round-index</code> is available as
<code>event.argument.round-index</code>, <code>round-index</code>, and
<code>weaponmechanics.round-index</code> during evaluation. The provider
namespace is also cached for later profile evaluations until the item/profile
session is changed or cleared.

### Profile variables

Each declared variable is exposed as both <code>variable.&lt;id&gt;</code> and
<code>placeholder.&lt;id&gt;</code>. The alias does not mean PlaceholderAPI is
installed; <code>source: placeholderapi</code> is the source that performs a
PlaceholderAPI lookup.

### Provider values

There is no generic <code>provider.*</code> namespace. An action provider owns
its namespace, for example:

~~~yaml
condition:
  all:
    - weaponmechanics.ammo == $weaponmechanics.magazine-size
    - weaponmechanics.aiming == false
~~~

Any scalar detail sent with a namespaced signal is converted to a boolean,
finite number, or string. A provider can therefore expose more values without
changing the condition syntax; consult the provider page for its exact fields.
