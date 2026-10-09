---
description: "Physics Bones"
---

# Physics Bones

## Mark a bone

Right-click → **Armature → Physics** independently of **Assign Role**.
`.armature` v2 stores a boolean `armature_physics`; names such as `phy_chain`
have no special meaning in this format. An ordinary `none` bone can be physical,
as can a body/item bone.

The pivot is the swing centre. Place a dangling accessory pivot at its attachment
point and orient geometry relative to the bone's local axes. Child bones inherit
the parent's final animated/physical transform; avoid compensating that transform
twice in their authored coordinates.

## Enable and tune simulation

Use `render.motion.physics` for the global response. An attachment profile or
instance can override its own `physics` parameters. Frequency controls response
speed, damping controls rebound, maximum angle bounds the swing, acceleration
and turn gains tune motion sensitivity. See [Motion & physics](../../advanced/best-settings.md).

This is spring-based presentation, without collisions. **Blockbench's preview
does not simulate this physics.** Save, compile, download the new pack, then
test acceleration, stopping, turning and a moving parent in game.

Legacy `.bbmodel` native models use the `phy_*` prefix. BetterModel does not
implement the native physical-bone contract. Do not apply that naming convention
to a v2 project in place of the explicit checkbox.
