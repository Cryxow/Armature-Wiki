---
description: "Animations & Molang"
---

# 🎞️ Animations & Molang

## Author clips

Use Blockbench translation, rotation and scale tracks on model bones. Bone pivots
define the transform centre, and child bones inherit the hierarchy. Verify rest
pose and keyframes before adding procedural motion. The native parser samples
supported linear, step, Catmull-Rom and Bézier interpolation; use the same model
and interpolation on both server and optional client for comparison.

Names such as `idle`, `walk`, `spawn`, `jump`, `fire` and `reload` are ordinary
native clip names. No clip starts because of its name: select it through a
profile rule or API. Names must be nonempty and unique in the model.
BetterModel compatibility retains its reserved automatic names.

## Left and right

Use explicit bones and clips for the left hand. `.armature` v2 does not rewrite
animation targets or synthesize mirrored clips. Target each bone by its actual
ID/name as expected by the runtime; preserve IDs when editing an existing asset.
Changing a role does not rename an animation target.

## Molang expressions

The native sampler evaluates supported Molang components rather than treating
an expression as a numeric constant. For example, an animated component can use:

```text
math.sin(query.anim_time * 180) * 5
```

Molang trigonometry uses degrees. Supported arithmetic, comparisons, logical
operators and the implemented `math.*` functions can combine animation time,
look/movement and context queries. Use [the supported query/function reference](molang-reference.md)
instead of assuming every Bedrock Molang feature exists.

Molang animation queries and YAML profile conditions are different languages.
Writing `variable.weapon-skin` in a profile does not automatically create that
variable in an animation expression. Prefer profile variants/additive weights
for state selection and Molang for the supported procedural track values.

## Blending and camera

Profiles choose blend duration, speed (including reverse playback) and channels.
Sparse clips return unkeyed transforms toward the model rest pose instead of
keeping the previous clip's pose. Camera and geometry should use the same clip
timeline. Keep the camera role at zero rest rotation and animate modest offsets.

## Verify

Compile the saved model, inspect expression diagnostics, and compare rest pose,
clip start/end, loops, interpolation and both hands. An editor preview cannot
prove the supported runtime query values or provider-pack rendering.
