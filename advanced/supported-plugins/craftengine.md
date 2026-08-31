---
cover: ../../.gitbook/assets/A9oDJ3u.gif
coverY: 0
coverHeight: 243
---

# 🟨 CraftEngine

Armature detects CraftEngine as an item identity provider. It resolves the
CraftEngine custom item key from the held <code>ItemStack</code> and keeps
CraftEngine responsible for the item and its gameplay behavior.

## Modern profile

Use the exact namespaced CraftEngine item key:

~~~yaml
ce_test_item:
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

The modern shape is <code>match.item</code>, <code>model.name</code>, and
<code>model.hide-vanilla-hand</code>. The old root-level
<code>item</code>, scalar <code>model</code>, and root-level
<code>hide-vanilla-hand</code> shape is legacy syntax.

## First-person item masking

With <code>render.vanilla-item-mode: mask</code>, Armature builds a copy from
the CraftEngine item definition and applies the generated invisible Armature
<code>item-model</code> to the first-person packet. The inventory item is not
replaced.

Set <code>model.hide-vanilla-hand: false</code> when the item should remain
visible or is already modeled inside the BetterModel asset.

## Reload

Install CraftEngine before Armature and run:

~~~text
/armature reload all
~~~

If the profile does not match, verify the namespace and item id returned by
CraftEngine rather than the display name. See
[Profiles](../../getting-started/profiles/README.md),
[Blockbench model](../../getting-started/blockbench-model.md), and
[Configuration](../../getting-started/configuration.md).
