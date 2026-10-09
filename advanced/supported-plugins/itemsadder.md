---
cover: ../../.gitbook/assets/itemsadder.png
coverY: 0
layout:
  width: default
  cover:
    visible: true
    size: full
    mask: none
  title:
    visible: true
  description:
    visible: true
  tableOfContents:
    visible: true
  outline:
    visible: true
  pagination:
    visible: true
  metadata:
    visible: true
  tags:
    visible: true
  actions:
    visible: true
---

# ItemsAdder

Install ItemsAdder beside Armature and create the custom item identified below
using that plugin's own configuration. The Armature example uses the shipped
`arm_sword` model; it does not create the custom gameplay item.

## Complete profile

```yaml
wiki_itemsadder:
  match:
    item: itemsadder:my_pack:wiki_sword
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


ItemsAdder's namespace and item id form the selector; the visible item name is
not its identity. Armature detects the installed provider automatically and stages
its generated assets for the ItemsAdder generation workflow. ItemsAdder remains
responsible for generating, hosting and sending its final ZIP. Run `/iazip` and
use ItemsAdder's normal pack refresh/send workflow after staging changes.


After changing Armature models or renderer assets:

```text
/armature reload pack
/iazip
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
