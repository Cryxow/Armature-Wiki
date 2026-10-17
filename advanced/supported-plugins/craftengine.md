---
cover: ../../.gitbook/assets/A9oDJ3u.gif
coverY: 0
coverHeight: 243
---

# 🛠️ CraftEngine

Install CraftEngine beside Armature and create the custom item identified below
using that plugin's own configuration. The Armature example uses the shipped
`arm_sword` model; it does not create the custom gameplay item.

## Complete profile

```yaml
wiki_craftengine:
  match:
    item: craftengine:my_pack:wiki_sword
  model:
    name: arm_sword
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: player:attack
        animation:
          name: swing
          duration: 8t
```

Save under `plugins/Armature/profiles/`, reload profiles, obtain that exact
provider item and inspect the matched profile. Swing it: the expected clip is
`swing`. Test both hands if the item can be held in either.

## Resource-pack workflow


Armature detects CraftEngine automatically. The bridge contributes Armature assets to
CraftEngine's resource-pack workflow. Reuse is valid only when Armature content
and provider inputs still match the successful generation receipt. Changing a
model, provider input or target pack setting requires a fresh provider build.

Use the installed CraftEngine version's reload/build workflow and its own pack
delivery. Read its generation log: issuing a command without a completed build
is not a successful asset refresh.


After changing Armature models or renderer assets:

```text
/armature reload pack
/ce reload all
```

Provider command syntax can differ by installed provider version; use its own
help for arguments. Choose the correct
Minecraft pack target and accept/download the final provider pack. If the
result is missing textures, inspect the final ZIP before retrying item matching.

The provider identifies items; Armature's generic equip/unequip and vanilla
events remain available. Do not assume it supplies WeaponMechanics-style fire
or reload events. Emit custom gameplay signals via the public API when needed.

See [Resource Packs](../../configuration/resource-packs.md) for collisions,
filters, split delivery and generation receipts.
