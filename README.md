---
cover: .gitbook/assets/Banner Final.png
coverY: 0
coverHeight: 318
---

# 📘 Introduction

Armature 1.3.0 is a first-person presentation engine for Paper and Folia. It
binds a BetterModel rig to the item held by a player, evaluates movement and
event rules, and renders finite actions, persistent loops, and additive pose
layers.

Armature controls presentation only. It does not own item stacks, ammo,
reloads, damage, projectiles, cooldowns, mana, or gameplay cancellation. Those
responsibilities stay with Minecraft and the gameplay/provider plugin.

## Requirements

* Paper or Folia using the 1.21.8 API line or newer, with a Java 25 runtime.
* [BetterModel](https://modrinth.com/plugin/bettermodel), required for model
  rendering.
* [PacketEvents](https://modrinth.com/plugin/packetevents), required by the
  first-person input and client presentation path.
* A client that accepts the generated Armature resource pack.

Optional providers and integrations are detected at startup:

* Item identity: WeaponMechanics, QualityArmory, ItemsAdder, CraftEngine, and
  Nexo.
* Action provider: WeaponMechanics.
* Automation/API integrations: Skript, MythicMobs, and Denizen.

Install the optional plugin before starting Armature when its item identities,
events, or scripting syntax are required. Armature remains presentation-only
when an optional plugin is absent.

## Start here

1. Import a model into <code>plugins/Armature/models/</code>.
2. Create a modern profile in <code>plugins/Armature/profiles/</code>.
3. Match the real item identity and set <code>model.name</code>.
4. Bind movement, action, and additive rules under <code>animations</code>.
5. Run <code>/armature reload</code> and apply the generated resource pack.

The [Profiles](getting-started/profiles/README.md) page contains the complete
modern YAML shape. [Built-in conditions](getting-started/profiles/conditions.md)
and [Built-in triggers](getting-started/profiles/triggers.md) list the full
runtime vocabulary.

## What changed in 1.3.0

The 1.3.0 line adds:

* modern anonymous profile rules with typed variables, conditions, variants,
  movement transitions, additive blending, and provider namespaces;
* a channel-aware public Java API with lifecycle events, handles, snapshots,
  catalogs, and bounded typed signal data;
* bundled Skript, MythicMobs, and Denizen adapters;
* expanded WeaponMechanics fire, aim, reload, firearm-state, and fire-mode
  signals;
* deterministic and optional AI-assisted legacy-profile migration;
* live Paper Dialog bone editing with persistent profile/item overrides;
* generated anti-clipping, viewmodel FOV lock, viewmodel Y lock, and optional
  camera-rotation resource-pack paths;
* safer Folia/player lifecycle recovery while keeping the BetterModel carrier
  mounted on the owning client.

See [Supported plugins](advanced/supported-plugins/README.md) for provider
details and [Public API](advanced/public-api.md) for integration code.

<div align="left"><figure><img src=".gitbook/assets/MinecraftNeoForge_1.21.11-Multiplayer3rd-partyServer2026-08-1111-59-57-ezgif.com-optimize.gif" alt=""><figcaption></figcaption></figure> <figure><img src=".gitbook/assets/MinecraftNeoForge_1.21.11-Multiplayer3rd-partyServer2026-08-1111-59-37-ezgif.com-optimize.gif" alt=""><figcaption></figcaption></figure></div>
