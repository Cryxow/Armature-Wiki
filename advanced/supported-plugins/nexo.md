---
cover: ../../.gitbook/assets/nexo.png
coverY: 0
---

# Nexo

Install Nexo beside Armature and create the custom item identified below
using that plugin's own configuration. The Armature example uses the shipped
`arm_sword` model; it does not create the custom gameplay item.

## Complete profile

```yaml
wiki_nexo:
  match:
    item: nexo:wiki_sword
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


Armature detects Nexo automatically. The bridge prepares Armature assets, adds them to
Nexo's pack during its generation lifecycle, then completes post-generation
transforms against the matching finished pack. Readiness depends on that
complete workflow, not on a directory containing yesterday's assets.

Nexo serialization and overlays affect the final files. Inspect the finished
ZIP for the right target-version definitions and Armature namespace. After
generation, use Nexo's own delivery workflow; a copied generated input directory
is not delivery to a connected player.


After changing Armature models or renderer assets:

```text
/armature reload pack
/nexo reload pack
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
