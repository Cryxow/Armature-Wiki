---
cover: .gitbook/assets/Banner Final.png
coverY: 0
coverHeight: 318
---

# 👋 Introduction

Armature **2.0.0** is a first-person presentation engine for Paper and Folia.
It selects item profiles, renders animated player limbs and held items, combines
movement, actions and additive layers, and generates the presentation assets.

The **native renderer is the primary engine** and the default. It supports
strict `.armature` v2 and compatible `.bbmodel` models without BetterModel.
BetterModel remains an optional compatibility backend; deprecation is planned
for future releases. Existing explicit backend selections are preserved.

Armature controls presentation. Minecraft and gameplay plugins retain item
inventories, ammo, damage, projectiles, reload logic and cooldown authority.
An animation named `fire` does not fire a weapon.

## Choose your starting point

- **Server administrator:** [Installation](getting-started/installation.md), then
  [Your first presentation](getting-started/first-presentation.md).
- **Model creator:** [Install the Blockbench plugin](getting-started/models/installing-plugin.md),
  then [Create a model](getting-started/blockbench-model.md).
- **Profile author:** [Profile structure](getting-started/profiles/README.md),
  then [Complete examples](getting-started/profiles/examples.md).
- **Plugin developer:** [Public API](advanced/public-api.md).
- **Existing installation:** [Upgrade to 2.0.0](getting-started/upgrading.md).

## What changes in 2.0.0

| Capability | Where to learn more |
| --- | --- |
| Native rendering and explicit body/item/camera roles | [Bone roles](getting-started/models/bone-roles.md) |
| Official Armature Blockbench workflow | [Model creation](getting-started/blockbench-model.md) |
| Independent main-hand/offhand presentation and two-arm rules | [Hands](getting-started/profiles/hands.md) |
| Global typed variables, conditions and conditional variants | [Variables](getting-started/profiles/variables.md), [Variants](getting-started/profiles/variants.md) |
| Reusable animated accessories and nested composite models | [Attachments](getting-started/profiles/attachments.md) |
| Optional Fabric rendering at client frame rate | [Client mod](client-mod/installation.md) |
| Provider-generated packs and an optional separate renderer pack | [Resource packs](configuration/resource-packs.md) |

The modern profile format, public API and scripting integrations continue to be
available. [Supported plugins](advanced/supported-plugins/README.md) distinguishes
item detection, events and resource-pack delivery instead of treating them as
one capability.
