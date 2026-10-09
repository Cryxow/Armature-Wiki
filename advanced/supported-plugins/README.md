---
description: "Supported Plugins"
---

# Supported Plugins

Integrations are optional. Armature presents an item; its owning plugin supplies
identity, gameplay and, where applicable, final resource-pack delivery.

| Integration | Item identity | Events/state | Pack bridge | Script/API adapter |
| --- | --- | --- | --- | --- |
| [ItemsAdder](itemsadder.md) | `itemsadder:namespace:id` | Identity; generic item/vanilla triggers | Yes, ItemsAdder owns delivery | — |
| [Nexo](nexo.md) | `nexo:id` | Identity; generic item/vanilla triggers | Yes, Nexo owns delivery | — |
| [CraftEngine](craftengine.md) | `craftengine:namespace:id` | Identity; generic item/vanilla triggers | Yes, CraftEngine owns delivery | — |
| [WeaponMechanics](weaponmechanics.md) | `weaponmechanics:weapon_title` | Fire, aim, reload, firearm/fire mode and weapon state | Not a pack provider | Presentation events and attachment overrides |
| QualityArmory | `qualityarmory:id` | Item identification and compatibility action provider; no full namespaced event/state integration promised | No | Use owning plugin/public API for additional signals |
| [Skript](skript.md) | Use the item's existing identity | Script-emitted signals, animation lifecycle | No | Embedded effects/expressions/events |
| [MythicMobs](mythicmobs.md) | Use the item's existing identity | Skill signals, animation lifecycle | No | Embedded mechanics/triggers |
| [Denizen](denizen.md) | Use the item's existing identity | Script signals, animation lifecycle | No | Embedded command/events |

[PlaceholderAPI](../../getting-started/profiles/variables.md) supplies shared
variable values. It is not an item or resource-pack provider.

Use a namespaced selector instead of a display name. A provider being present
does not create the example item or gameplay weapon. After provider updates,
check identities, enabled bridges, logs and final generated assets.

For pack providers, finish the complete provider generation workflow and deliver
its resulting pack. Rebuilding Armature alone does not prove that the player
received the updated provider ZIP.
