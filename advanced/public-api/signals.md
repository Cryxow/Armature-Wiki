---
description: "Signals"
---

# 📡 Signals

Plugins can send a namespaced signal through modern `animations.actions` rules.
The legacy `bindings` map (and compatible `triggers` map) is still accepted by
the compatibility loader. The action condition is evaluated by Armature and
its `consume` value is returned as metadata; Armature cannot cancel an event
that belongs to another plugin.

```yaml
animations:
  actions:
    - trigger: skript:weapon.pistol.fire
      condition: player.on-ground == true
      animation:
        name: pistol_fire
        duration: 6t
      consume: false
```

```java
ArmatureApi.animations()
    .sendSignal(playerId, "skript:weapon.pistol.fire", Map.of())
    .thenAccept(result -> {
        if (result.matched() && result.animation().accepted()) {
            // The gameplay plugin already performed its own firing logic.
        }
    });
```

This is the primitive used by the optional Skript, MythicMobs, and Denizen
integrations bundled in `Armature.jar`. Armature registers their effects,
mechanics, or commands only when the corresponding host plugin is present;
there is no magic `emit` syntax in native Skript or Denizen.

For typed signal metadata, use bounded scalar data. Strings, booleans, and
finite numbers are supported; Bukkit objects, entities, item stacks, and
unbounded payloads are rejected at the API boundary:

```java
api.emitSignal(playerId, "myplugin:weapon.fire",
    ArmatureSignalData.of(Map.of("ammo", 7, "mode", "semi")));
```

The compatibility `Map<String, String>` overload is subject to the same
limits: at most 64 entries, 128-character keys, and 1024-character strings.

The current trigger runtime receives a compatibility string view. Gameplay
plugins remain responsible for validating and mutating their own ammo,
cooldowns, damage, and event cancellation.

## YAML-aligned trigger alias

```java
api.trigger(playerId, "myplugin:inspect",
    ArmatureSignalData.of(Map.of("allowed", true)));
```

`event.argument.allowed` is available to the matching modern action condition.
Use the exact namespaced trigger in YAML. Signals return matched/consumed status
and the animation result; caller-owned gameplay decides how to use it.

The built-in WeaponMechanics reload-input adapter explicitly interprets a
consumed `weaponmechanics:reload` signal by cancelling its own provider event.
That specific adapter behavior is not automatic cancellation of arbitrary
Bukkit, Skript, MythicMobs or Denizen events.

Test allowed and denied payloads, cooldown rejection, protected actions and
unavailable players. Sending a signal without a matching rule is not raw clip
playback. See [Conditions](../../getting-started/profiles/conditions.md).
