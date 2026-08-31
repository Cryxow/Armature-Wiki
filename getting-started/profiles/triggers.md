# Built-in triggers

An action rule reacts to an instantaneous, namespaced signal. Continuous
state belongs in a movement or additive <code>when</code> rule; it should not
start an action every tick.

~~~yaml
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
~~~

The modern loader requires every <code>trigger</code> to contain a namespace
and a colon. Multiple rules may use the same trigger. Armature selects the
matching rule with the highest condition specificity, then uses declaration
order as the tie-breaker.

## Signal context

During evaluation, the signal is available as
<code>event.signal</code> and <code>event.type</code>. Its detail map is
available as <code>event.argument.&lt;name&gt;</code>, as a direct
<code>&lt;name&gt;</code> alias, and as
<code>&lt;namespace&gt;.&lt;name&gt;</code>. Values are converted to booleans,
finite numbers, or strings.

<code>consume: true</code> requests cancellation only when the emitter owns a
cancellable event. It can cancel supported vanilla right-click or
WeaponMechanics reload input. For API and external integration signals it is
returned in the API result; the external caller still owns gameplay
cancellation.

## Player lifecycle and item state

| Trigger | Emitted when | Details |
| --- | --- | --- |
| <code>player:join</code> | A player joins. | none |
| <code>player:ready</code> | The client-ready presentation resync succeeds after join or world change. | <code>reason</code>: <code>join</code> or <code>world_change</code> |
| <code>player:quit</code> | A player leaves. | none |
| <code>player:death</code> | The player death cleanup starts. | <code>damage-type</code>: currently <code>unknown</code> |
| <code>player:respawn</code> | The player has respawned. | <code>world</code> |
| <code>player:teleport</code> | A non-cancelled same-world or synchronous cross-world teleport succeeds. | <code>cause</code>; cross-world also <code>from-world</code>, <code>world</code> |
| <code>player:world-change</code> | Bukkit reports a world change, including plugin-driven changes. | <code>from-world</code>, <code>world</code> |
| <code>world:change</code> | Alias emitted with <code>player:world-change</code>. | <code>from-world</code>, <code>world</code> |
| <code>player:swap-hand</code> | A main/off-hand swap is requested. | no detail from the low-priority cancellation path |
| <code>input:slot-change</code> | The selected hotbar slot changes. | <code>previous-slot</code>, <code>slot</code>, <code>previous-item</code>, <code>item</code> |
| <code>player:hotbar-change</code> | Alias for the selected hotbar slot change. | same slot and item details |
| <code>item:held-change</code> | The resolved held identity changes. | same slot and item details |
| <code>item:main-hand-change</code> | The main-hand slot changes. | same slot and item details |
| <code>item:off-hand-change</code> | The off-hand item swap event is accepted. | <code>previous-main-hand</code>, <code>previous-off-hand</code> |
| <code>item:equip</code> | A modern profile presentation becomes active. | <code>item</code>, optional <code>previous-item</code>, <code>hand: main-hand</code> |
| <code>item:unequip</code> | A modern profile presentation stops being active. | <code>item</code>, <code>next-slot</code>, <code>hand: main-hand</code> |

The item lifecycle signals describe presentation sessions. They do not change
the inventory or transfer gameplay ownership to Armature.

## Player actions and interactions

| Trigger | Emitted when | Details |
| --- | --- | --- |
| <code>player:attack</code> | A main-hand attack/swing packet is observed for a modern profile. | <code>hand: main-hand</code> |
| <code>player:use-item</code> | A right-click use starts on the off hand, or a supported vanilla right-click is routed through the trigger service. | <code>hand</code>, <code>action</code>, optional <code>item</code>, <code>block</code>, <code>face</code> |
| <code>player:use-item-stop</code> | Held item use changes from active to inactive during a runtime sample. | transition context |
| <code>player:use-item-complete</code> | A consumable is completed. | <code>hand</code>, <code>item</code> |
| <code>player:consume-item</code> | A consumable event is accepted. | <code>hand</code>, <code>item</code> |
| <code>item:consume</code> | Alias emitted for the accepted consumable event. | <code>hand</code>, <code>item</code> |
| <code>player:release-item</code> | Held item use changes from active to inactive. | transition context |
| <code>player:drop-item</code> | The player drops an item. | <code>item</code>, <code>amount</code>, <code>hand: main-hand</code> |
| <code>player:pickup-item</code> | The player picks up an item. | <code>item</code>, <code>amount</code> |
| <code>player:damage-taken</code> | The player takes non-cancelled damage. | <code>damage</code>, <code>damage-type</code>, optional <code>source</code> |
| <code>player:heal</code> | The player regains health. | <code>amount</code>, <code>cause</code> |
| <code>player:food-change</code> | Food level changes. | <code>food-level</code>, <code>saturation</code> |
| <code>player:effect-add</code> | A potion effect is added or changed. | <code>effect</code>, <code>action</code>, optional <code>amplifier</code>, <code>duration</code> |
| <code>player:effect-remove</code> | A potion effect is removed. | <code>effect</code>, <code>action</code> |
| <code>player:effect-change</code> | A potion effect event has another Bukkit action. | <code>effect</code>, <code>action</code>, optional <code>amplifier</code>, <code>duration</code> |
| <code>item:durability-change</code> | Bukkit reports item damage. | <code>damage</code>, <code>previous</code>, <code>value</code> |

## Blocks, entities, and projectiles

| Trigger | Emitted when | Details |
| --- | --- | --- |
| <code>block:interact</code> | A clicked block is present in a player interaction. | <code>hand</code>, <code>action</code>, <code>item</code>, <code>block</code>, <code>face</code> |
| <code>block:damage</code> | A block damage/progress event is accepted. | <code>block</code>, <code>tool</code>, <code>face</code>, <code>phase: progress</code>, <code>instant</code> |
| <code>block:break-start</code> | The client starts digging. | <code>phase: start</code> |
| <code>block:break</code> | A block break completes. | <code>block</code>, <code>tool</code>, <code>phase: complete</code> |
| <code>block:place</code> | A block placement completes. | <code>block</code>, <code>against</code>, <code>face</code>, <code>hand</code>, <code>tool</code>, <code>phase: complete</code> |
| <code>entity:interact</code> | A player right-clicks an entity. | <code>entity</code>, <code>hand</code>, <code>action: right-click</code> |
| <code>player:interact</code> | A player interaction or entity interaction occurs. | <code>hand</code>, <code>action</code>, optional <code>item</code>, <code>block</code>, <code>face</code>, or <code>entity</code> |
| <code>entity:hit</code> | A player damages an entity. | <code>target</code>, <code>damage</code>, <code>damage-type</code> |
| <code>entity:death</code> | An entity killed by the player dies. | <code>entity</code>, <code>killer</code> player UUID |
| <code>projectile:launch</code> | A player launches a projectile. | <code>projectile</code>, resolved held <code>item</code> |
| <code>projectile:hit</code> | A player projectile hits an entity or block. | <code>projectile</code>, optional <code>target</code>, <code>block</code> |

<code>block:break-start</code> is client intent from PacketEvents;
<code>block:break</code> is the accepted completion event. Use the former for
the beginning of a mining animation and the latter for an animation that
should represent a completed break.

## Movement and environment transitions

These signals are generated when the modern runtime observes a state change.
Their common detail map contains <code>direction</code>,
<code>previous-direction</code>, <code>speed</code>,
<code>vertical-speed</code>, <code>fall-distance</code>,
<code>fall-time</code>, <code>phase</code>, and <code>weather</code>.

| Trigger | Transition |
| --- | --- |
| <code>player:move-start</code> / <code>player:move-stop</code> | Movement intent starts/stops. |
| <code>player:sprint-start</code> / <code>player:sprint-stop</code> | Sprint starts/stops. |
| <code>player:sneak-start</code> / <code>player:sneak-stop</code> | Sneak starts/stops. |
| <code>player:jump</code> / <code>player:land</code> | Upward jump starts / airborne player lands. |
| <code>player:fall-start</code> / <code>player:fall-stop</code> | Downward airborne movement starts/stops. |
| <code>player:climb-start</code> / <code>player:climb-stop</code> | Climbing starts/stops. |
| <code>player:swim-start</code> / <code>player:swim-stop</code> | Swimming starts/stops. |
| <code>player:glide-start</code> / <code>player:glide-stop</code> | Elytra gliding starts/stops. |
| <code>player:mount</code> / <code>player:dismount</code> | Vehicle relation starts/stops. |
| <code>player:direction-change</code> | Relative movement direction changes. |
| <code>world:enter-water</code> / <code>world:leave-water</code> | Water state changes. |
| <code>world:enter-lava</code> / <code>world:leave-lava</code> | Lava state changes. |
| <code>world:enter-powder-snow</code> / <code>world:leave-powder-snow</code> | Powder-snow state changes. |
| <code>player:fire-start</code> / <code>player:fire-stop</code> | Fire ticks start/stop. |
| <code>player:freeze-start</code> / <code>player:freeze-stop</code> | Freeze ticks start/stop. |
| <code>world:time-phase-change</code> | Day/night phase changes; <code>phase</code> is <code>day</code> or <code>night</code>. |
| <code>world:weather-change</code> | Storm state changes; <code>weather</code> is <code>rain</code> or <code>clear</code>. |

## Low-level input

These signals represent client intent and can occur even when a gameplay
plugin later blocks the resulting action:

| Trigger | Meaning |
| --- | --- |
| <code>input:primary-press</code> / <code>input:primary-release</code> | Main-hand attack or dig input starts/stops. |
| <code>input:secondary-press</code> / <code>input:secondary-release</code> | Use input starts/stops. |
| <code>input:forward-press</code> / <code>input:forward-release</code> | Forward key changes. |
| <code>input:backward-press</code> / <code>input:backward-release</code> | Backward key changes. |
| <code>input:left-press</code> / <code>input:left-release</code> | Left key changes. |
| <code>input:right-press</code> / <code>input:right-release</code> | Right key changes. |
| <code>input:jump-press</code> / <code>input:jump-release</code> | Jump key changes. |
| <code>input:sneak-press</code> / <code>input:sneak-release</code> | Sneak key changes. |
| <code>input:sprint-press</code> / <code>input:sprint-release</code> | Sprint key changes. |
| <code>input:slot-change</code> | Selected hotbar slot changes. |
| <code>item:charge-start</code> | A right-click is observed while the hand is raised. |
| <code>item:charge-release</code> | Packet use/charge release is observed. |

## Animation lifecycle and markers

| Trigger | Emitted when | Details |
| --- | --- | --- |
| <code>animation:start</code> | A finite modern action or movement transition starts. | <code>animation</code>, <code>action</code>, <code>layer</code>, <code>token</code> |
| <code>animation:complete</code> | A finite non-transition action reaches its natural end. | <code>animation</code>, <code>action</code>, <code>layer: action</code>, <code>reason: completed</code>, <code>token</code> |
| <code>animation:cancel</code> | An action or transition is cancelled, replaced, or removed. | same fields with <code>reason</code> |
| <code>animation:transition-start</code> | A movement start/end transition starts. | <code>animation</code>, <code>to</code>, <code>token</code> |
| <code>animation:transition-end</code> | A movement transition completes. | <code>animation</code>, <code>action</code>, <code>layer: transition</code>, <code>reason: completed</code>, <code>token</code> |
| <code>animation:marker</code> | BetterModel sends an animation marker signal. | <code>signal</code> |
| <code>animation:signal</code> | Generic marker signal alias. | <code>signal</code> |
| <code>animation:&lt;name&gt;</code> | A marker without a namespace is normalized to this signal. | <code>signal</code> |

The public Bukkit events <code>ArmatureAnimationStartEvent</code> and
<code>ArmatureAnimationEndEvent</code> expose the same finite-animation
lifecycle. <code>animation:complete</code> is emitted only for a natural
completion; an end caused by replacement or removal is a cancellation signal.

## WeaponMechanics signals

When WeaponMechanics is enabled, the action provider emits these signals under
the <code>weaponmechanics</code> namespace:

| Signal | Source state |
| --- | --- |
| <code>weaponmechanics:equip</code> / <code>weaponmechanics:unequip</code> | Weapon equipment changes in either hand. |
| <code>weaponmechanics:aim-enter</code> | Main-hand scope enters. |
| <code>weaponmechanics:aim</code> | Main-hand scope is active or its zoom is refreshed. |
| <code>weaponmechanics:aim-exit</code> | Main-hand scope exits. |
| <code>weaponmechanics:fire</code> | Main-hand shot while not scoped. |
| <code>weaponmechanics:aim-fire</code> | Main-hand shot while scoped. |
| <code>weaponmechanics:reload</code> | Reload input before WeaponMechanics starts its normal path. |
| <code>weaponmechanics:reload-phase</code> | Each accepted reload iteration. |
| <code>weaponmechanics:reload-complete</code> | The reload reaches magazine capacity. |
| <code>weaponmechanics:reload-cancel</code> | A reload is cancelled. |
| <code>weaponmechanics:firearm-state-change</code> | Firearm open/close/state event. |
| <code>weaponmechanics:fire-mode-change</code> | Selective fire mode changes. |

All provider signals include <code>weapon-title</code> and <code>hand</code>
details. See the [WeaponMechanics page](../../advanced/supported-plugins/weaponmechanics.md)
for the full detail payload and reload conditions.

## External and custom signals

Armature accepts any valid namespaced signal sent through the public API or an
optional integration. There is no fixed <code>armature:</code> prefix and no
requirement to register a custom signal first:

~~~yaml
animations:
  actions:
    - trigger: myplugin:weapon-special
      condition: event.argument.critical == true
      animation:
        name: special
        duration: 6t
~~~

Java, Skript, MythicMobs, and Denizen callers can publish the same signal.
Those callers own the gameplay event; Armature only resolves the matching
profile rule and presents the configured animation.

## Compatibility identifiers

The legacy profile converter maps old action names to modern signals. For a
WeaponMechanics item, firearm actions use the <code>weaponmechanics:</code>
namespace; vanilla actions such as swing, mine, place, use, eat, drink, throw,
jump, and land use their corresponding <code>player:</code>,
<code>block:</code>, or <code>projectile:</code> signal. New profiles should
write the modern namespaced signal directly rather than depending on the
legacy action map.
