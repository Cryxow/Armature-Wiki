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

# 🟪 ItemsAdder

Armature detects ItemsAdder as an item identity provider. The provider resolves
the ItemsAdder namespaced id from the held custom item and keeps the original
ItemsAdder item as the gameplay source.

## Modern profile

Use the exact ItemsAdder id returned by ItemsAdder:

~~~yaml
ia_test_item:
  match:
    item: your_namespace:custom_item
  model:
    name: fp_custom_item
    hide-vanilla-hand: true
  animations:
    actions:
      - trigger: player:attack
        animation:
          name: armature_swing
          duration: 6t
~~~

The old root-level <code>item</code>, scalar <code>model</code>, and
root-level <code>hide-vanilla-hand</code> shape belongs to the legacy loader.
New profiles use <code>match.item</code>, <code>model.name</code>, and
<code>model.hide-vanilla-hand</code>.

## First-person item masking

With <code>render.vanilla-item-mode: mask</code>, Armature asks ItemsAdder for
a copy of the custom item and applies the generated Armature invisible
<code>item-model</code> to the first-person packet. The original inventory
item and its gameplay identity are not replaced.

If the model should show the item itself, set
<code>model.hide-vanilla-hand: false</code> or model the item directly inside
the BetterModel asset.

## Reload and troubleshooting

Install ItemsAdder before Armature, then run:

~~~text
/armature reload all
~~~

Check the startup/reload provider list and verify the exact namespace/id. See
[Profiles](../../getting-started/profiles/README.md),
[Blockbench model](../../getting-started/blockbench-model.md), and
[Public API](../public-api.md).
