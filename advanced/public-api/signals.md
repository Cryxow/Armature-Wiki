# 📡 Signals

Signals let a gameplay plugin announce an event while the Armature profile
chooses the animation. This is the least-coupled integration style:

<div align="center">

<code>weapon plugin event → namespaced signal → profile rule → visual animation</code>

</div>

The signal is an event name, not the name of the YAML list entry. Use a
namespace owned by your plugin, for example
<code>myweapons:m4a1.fire</code>.

## Declare the rules

Modern profiles put signal rules under <code>animations.actions</code>:

```yaml
m4a1:
  match:
    item:
      material: minecraft:paper
      data:
        custom-model-data: 41001
  model:
    name: fp_rifle
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base

    actions:
      - trigger: myweapons:m4a1.fire
        condition:
          all:
            - event.argument.ammo > 0
            - event.argument.mode == semi
        animation:
          name: fire
          duration: 4t
          cooldown: 2t
        consume: false

      - trigger: myweapons:m4a1.fire
        condition:
          - event.argument.ammo == 0
        animation:
          name: fire_no
          duration: 10t

      - trigger: myweapons:m4a1.reload
        animation:
          name: reload
          duration: 35t
```

Multiple rules may use the same trigger. Armature evaluates their conditions
and selects the applicable rule according to the modern profile rules. The
values under <code>event.argument.*</code> exist only when the sender supplied
them; your integration owns their names and meaning.

See [Built-in conditions](../../getting-started/profiles/conditions.md) for the
condition vocabulary and [Profiles](../../getting-started/profiles/README.md)
for the complete YAML shape.

## Send a signal

The stable API provides <code>signal</code>, <code>sendSignal</code>, and
<code>emitSignal</code> aliases. Use the typed overload for new Java code:

```java
import com.armaturemc.api.ArmatureApi;
import com.armaturemc.api.ArmatureSignalData;

var playerId = player.getUniqueId();

ArmatureApi.tryAnimations().ifPresent(api ->
    api.signal(playerId, "myweapons:m4a1.fire",
            ArmatureSignalData.of(Map.of(
                "ammo", weapon.ammo(),
                "mode", weapon.fireMode(),
                "aiming", weapon.isAiming()
            )))
        .thenAccept(result -> {
            if (!result.matched()) {
                plugin.getLogger().fine(
                    "No Armature rule matched the M4A1 fire signal.");
            } else if (!result.animation().accepted()) {
                plugin.getLogger().fine(
                    "Armature rejected the visual action: "
                        + result.animation().status());
            }
        })
);
```

Send the signal after your gameplay plugin has performed the event's
gameplay-side validation. The signal only requests presentation:

```java
if (!weapon.canFire()) {
    return;
}

weapon.consumeRound();
weapon.spawnProjectile();
weapon.playSound();

ArmatureApi.tryAnimations().ifPresent(api ->
    api.sendSignal(player.getUniqueId(), "myweapons:m4a1.fire",
        ArmatureSignalData.of(Map.of("ammo", weapon.ammo())))
);
```

Armature does not consume ammunition, apply damage, spawn a projectile, or
cancel the event that caused the signal.

## Signal result

<code>ArmatureSignalResult</code> contains three values:

| Value | Meaning |
| --- | --- |
| <code>matched()</code> | At least one configured rule matched the signal and its conditions |
| <code>consumed()</code> | A matching rule declared <code>consume: true</code> |
| <code>animation()</code> | The normal visual playback result |

<code>consumed()</code> is routing metadata. It does not cancel a Bukkit event
owned by your plugin. If your event must be cancelled, cancel it in your
plugin.

Always inspect <code>animation().accepted()</code> separately from
<code>matched()</code>: a rule can match while playback is rejected because the
player has no profile, is on cooldown, or cannot be rendered.

## Typed metadata

<code>ArmatureSignalData</code> is deliberately limited to bounded scalar
values. It accepts:

* strings up to 1024 characters;
* booleans;
* finite numeric values;
* at most 64 entries with keys up to 128 characters.

```java
var data = ArmatureSignalData.of(Map.of(
    "ammo", 7,
    "magazine-size", 30,
    "aiming", true,
    "mode", "burst"
));

api.emitSignal(playerId, "myweapons:m4a1.fire", data);
```

Entities, players, ItemStacks, Bukkit events, arbitrary objects, nulls, and
non-finite numbers are rejected. Put large or complex gameplay state in your
own plugin and send only the scalar values that a profile condition needs.

The compatibility overload accepts
<code>Map&lt;String, String&gt;</code>:

```java
api.signal(playerId, "myweapons:m4a1.inspect",
    Map.of("source", "command"));
```

It is subject to the same entry, key, and string limits.

## Provider values in conditions

A signal may carry event arguments, while provider integrations expose their
own read-only values. For example, a WeaponMechanics profile can select a
different reload animation from ammunition state:

```yaml
animations:
  actions:
    - trigger: myweapons:m4a1.reload
      condition:
        all:
          - weaponmechanics.ammo < weaponmechanics.magazine-size
          - event.argument.phase == start
      animation:
        name: reload

    - trigger: myweapons:m4a1.reload
      condition:
        - event.argument.phase == complete
      animation:
        name: reload_complete
```

Provider namespaces are read-only from Armature's point of view. The gameplay
plugin remains the authority for ammunition, fire mode, cooldowns, and reload
state.

## Signals versus direct playback

| Situation | Recommended call |
| --- | --- |
| One plugin owns the exact clip and needs no profile decision | <code>playAsset</code> |
| Profile authors should choose variants or conditions | <code>signal</code> |
| A loaded-model clip is not listed in a profile action rule | <code>playRawAnimation</code> |
| An external system must observe accepted finite animations | Bukkit lifecycle events |

For Skript, MythicMobs, and Denizen adapters, use their documented
namespaced-signal integration rather than inventing a native Armature syntax:
[Supported plugins](../supported-plugins/README.md).
