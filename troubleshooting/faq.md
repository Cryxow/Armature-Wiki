---
description: "FAQ & Compatibility"
---

# FAQ & Compatibility

## Does Armature implement weapon gameplay?

No. Damage, ammunition, projectiles and gameplay ownership remain with their
plugin/script. Armature controls presentation. Its WeaponMechanics reload-input
adapter can cancel that provider event when a configured rule consumes it;
generic custom signals only return routing metadata.

## Is BetterModel required?

No for native rendering. Native is the primary/default 2.0.0 engine. BetterModel
remains an optional compatibility backend and is planned for deprecation in
later versions. It cannot load strict native `.armature` v2 or compositions.

## Must every player install the client mod?

No. The server renderer supports players without it. The optional Fabric mod
uses exact Minecraft-target JARs and matching protocol; composite profiles
currently fall back to server rendering. See the client compatibility table.

## Can I keep `.bbmodel` files?

Compatible legacy models remain supported. Their naming conventions differ
from explicit v2 roles. `.armature` v1 is rejected without automatic conversion.
Re-save/rebuild with the current editor and check roles/clips before switching formats.

## Why does a variable work in one profile only?

Modern disk profiles use one global `plugins/Armature/variables.yml`. Local
variables inside profiles, variants or mounted instances are not accepted.
Check declaration, source, type, fallback and refresh interval. YAML variables
are not automatically Molang model variables.

## Why is the first movement rule not always selected?

Matching movement rules use condition specificity, then declaration order for
ties. Variants use their own first-eligible selection. Do not treat all rule
lists as the same selection algorithm.

## Why did an unequip animation keep the old item visible?

An accepted outgoing unequip owns its original model, pose and item visuals
until it ends. Without an accepted unequip the old presentation is removed
immediately. This prevents a new held item from replacing the outgoing visual.

## Is a Blockbench preview proof of in-game behavior?

No. It helps inspect roles, items and clips; it does not simulate server bone
physics, all provider item predicates, pack delivery or all graphics pipelines.
Run the tutorial with the final artifacts before publication.

## Does `/armature reload pack` send the provider pack?

It prepares Armature's pack assets and bridge workflow. The configured provider
owns the final pack and delivery. Finish its workflow and inspect/accept the
result. See [Resource Packs](../configuration/resource-packs.md).
