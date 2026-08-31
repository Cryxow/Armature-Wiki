# 🦾 Supported plugins

Armature detects optional providers at startup. Providers expose item identity
or presentation events; they do not move gameplay ownership into Armature.

| Plugin | Item identity | Presentation actions | Documentation |
| --- | --- | --- | --- |
| ItemsAdder | yes | no provider action stream | [ItemsAdder](itemsadder.md) |
| Nexo | yes | no provider action stream | [Nexo](nexo.md) |
| CraftEngine | yes | no provider action stream | [CraftEngine](craftengine.md) |
| QualityArmory | yes when available | legacy action provider support | See profile and API references |
| WeaponMechanics | yes | fire, aim, reload, firearm state, fire mode, equipment | [WeaponMechanics](weaponmechanics.md) |
| Skript | n/a | effects, signals, lifecycle events, expressions | [Skript](skript.md) |
| MythicMobs | n/a | mechanics and lifecycle triggers | [MythicMobs](mythicmobs.md) |
| Denizen | n/a | command and lifecycle events | [Denizen](denizen.md) |

Skript, MythicMobs, and Denizen integration classes are embedded in the main
Armature distribution. Install the host plugin separately; do not copy the
standalone Gradle module artifacts to a production server.

## Presentation-only boundary

Armature can render an animation after a provider event or external signal,
but it does not decide whether the gameplay action succeeded. Keep ammo,
damage, projectiles, reload state, cooldowns, item consumption, and event
cancellation in the gameplay plugin.

For a provider-independent integration, use the [Public API](../public-api.md)
or a modern action rule with a custom namespaced trigger.
