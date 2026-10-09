---
description: "Additive Layers"
---

# Additive Layers

Additives are evaluated continuously and composed by Armature on top of the
current rendered movement/action pose. They are not action modes:

```yaml
animations:
  additives:
    - animation:
        name: breathing
        speed: 1.0
      playback: loop
      bones: [body, right_arm, left_arm]
      weight:
        parameter: variable.stamina
        input: [0.0, 100.0]
        output: [1.0, 0.15]
        easing: smoothstep
```

Movement and action animation sections also accept `blend-in` and `blend-out`
inside their `animation` mapping. Durations are expressed in ticks (`4t`) or
seconds (`0.3s`); numeric values are ticks. Defaults preserve the historical
renderer behavior (`2t`/`2t` for loops and `1t`/`2t` for actions), and `0t`
disables a side of the blend.

Supported additive fields are `when`, `animation`, `playback` (`loop`, `hold`,
or `once`), `group`, `priority`, `weight`, `blend-in`, `blend-out`, `bones`,
`channels`, `position-gain`, `rotation-gain`, and `reference`.

`reference.animation` and `reference.time` define the pose from which the
additive delta is sampled. Conceptually:

```text
final pose = base pose + sum(additive delta * resolved weight)
```

This is the supported solution for fall height, stamina-dependent breathing,
and similar continuous pose control. Additives compose sampled deltas over the
selected movement/action pose.

## Choose the affected bones

Additives combine a sampled pose with the current movement/action presentation.
Use `bones`, `channels`, `position-gain`, `rotation-gain` and `reference` to limit their contribution.
Authored bone targets must exist, and v2 does not rewrite them for the other hand.
Additive rules do not accept `arms: both`; hand ownership belongs to movement/actions.

## Use a small weight first

Start with a fixed `weight: 0.25`, then add parameter-driven mappings after the
pose looks correct. A dynamic falling response can map `player.fall-distance`
from `[0, 4]` to `[0, 1]` with `smoothstep`, rather than abruptly switching a pose.
The additive rule has its own blend durations; animation name/selection/speed
belong inside the `animation` mapping.

Reference clips/time define the neutral pose for the additive difference.
Missing reference bones/clips are asset errors, including in inactive variants.
Test multiple simultaneous groups, entry/exit blends and sparse tracks against
the authored rest pose.
