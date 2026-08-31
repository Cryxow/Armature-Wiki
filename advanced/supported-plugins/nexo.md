---
cover: ../../.gitbook/assets/nexo.png
coverY: 0
---

# 🟩 Nexo

Armature detects Nexo as an item identity provider. Nexo's
<code>idFromItem</code> result is normalized to the explicit
<code>nexo</code> namespace.

## Modern profile

Always write the Nexo namespace in the profile selector:

~~~yaml
nexo_test_item:
  match:
    item: nexo:custom_item
  model:
    name: fp_custom_item
    hide-vanilla-hand: true
  animations:
    actions:
      - trigger: player:use-item
        animation:
          name: armature_use
          duration: 6t
~~~

Do not use a bare <code>custom_item</code> selector for a Nexo item. The
provider identity is <code>nexo:custom_item</code>, regardless of the
Minecraft material used to render it.

The modern profile shape uses <code>match.item</code>,
<code>model.name</code>, and <code>model.hide-vanilla-hand</code>. The old
root-level <code>item</code>, scalar <code>model</code>, and
root-level <code>hide-vanilla-hand</code> shape remains legacy syntax only.

## First-person item masking

With <code>render.vanilla-item-mode: mask</code>, Armature builds a copy from
the held Nexo item and applies the generated invisible Armature
<code>item-model</code> to the first-person packet. Nexo and Minecraft
inventory state are not changed.

If the model already contains the item or the vanilla item should remain
visible, set <code>model.hide-vanilla-hand: false</code>.

## Nexo resource pack

Armature can bridge generated assets into the Nexo resource-pack workflow when
Nexo is enabled. After changing a model, profile, or pack setting, run:

~~~text
/armature reload pack
~~~

Then reconnect or re-accept the generated resource pack. See
[Profiles](../../getting-started/profiles/README.md),
[Blockbench model](../../getting-started/blockbench-model.md), and
[Configuration](../../getting-started/configuration.md).
