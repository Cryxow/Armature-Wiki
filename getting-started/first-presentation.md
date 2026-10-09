---
description: "Your First Presentation"
---

# Your First Presentation

This tutorial uses `models/default/arm_sword.bbmodel`, shipped by Armature,
and its existing `base`, `swing`, `equip` and `unequip` clips. You can use a legacy
`.bbmodel` with the default native renderer; installing the Blockbench plugin is
not required for this first check.

## 1. Prepare an isolated item match

Use a test server or make a backup. The shipped sword-family profile also matches
diamond swords. Move its `.yml` file outside `profiles/` for this tutorial so only
the example below owns that item. Avoid two competing profiles during a first test.

## 2. Write the profile

Save this as `plugins/Armature/profiles/wiki-sword.yml`:

```yaml
wiki_sword:
  match:
    item: minecraft:diamond_sword
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
          cooldown: 3t
      - trigger: item:equip
        animation: equip
      - trigger: item:unequip
        animation: unequip
```

The root key `wiki_sword` is the profile ID. `arm_sword` is the model filename
without its extension; the containing folder is not part of this ID.

## 3. Load and deliver

Run `/armature reload all`. If an external provider owns your pack, run its own
generation/reload workflow and then `/armature diagnostics pack sync` as needed.
Deliver that provider's final pack to the player. Without a provider, enable
standalone ZIP generation and host/deliver it as described in [Resource packs](../configuration/resource-packs.md).

## 4. Test the presentation

Give yourself a diamond sword and hold it in the main hand. The arm should play
`base`; an attack should play `swing`; selecting/leaving the item should invoke
the configured equip/unequip clips. Use `/armature profile inspect <player>` to
confirm the selected profile and `/armature status` to confirm the native backend.

If no model appears, check the selected profile, model/clip diagnostics and the
downloaded pack before changing transforms. See [Troubleshooting](../troubleshooting.md).

When finished, remove the tutorial profile and restore the original default
profile before reloading. Continue with [Creating a model](blockbench-model.md)
or [Movement rules](profiles/movement.md).
