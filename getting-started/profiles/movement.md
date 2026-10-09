---
description: "Movement Rules"
---

# Movement Rules

Movement rules use `when`. Armature evaluates matching rules, selects the most
specific condition, and uses declaration order only as the final tie-breaker.
No movement `priority` is required.

Movement `animation.sway-rate` overrides the model sway multiplier only while
that rule is selected. If omitted, the movement uses `model.sway-rate`; an
explicit value of `0.0` disables procedural sway for that movement.

```yaml
animations:
  movement:
    - when:
        - player.climbing == true
        - player.climbing-direction == up
      animation: climb-up

    - when:
        - player.climbing == true
        - player.climbing-direction == down
      animation: climb-down

    - when:
        - player.falling == true
      animation:
        name: fall
        start: fall-start
        end: fall-end
```

`player.climbing-direction` is `up`, `down`, or `none`. For free fall, use
`player.falling` or `player.vertical-speed < 0`; for a smooth fall response,
use an additive weight driven by `player.fall-distance`.

## Animation options

With `render.backend: native`, animation asset names are freely chosen, including `idle`, `walk`, `idle_fly`, `walk_fly`, `spawn` and `jump`. Movement/action/additive rules and their start/end/reference clips use the explicitly configured names; no clip starts automatically because of its name. The same policy applies to legacy profiles and API presentations, at startup and profile reload. Names must be non-empty and match a clip in the model. The BetterModel backend retains its reserved-name validation.

Every `animation` accepts either one asset name or a mapping:

```yaml
animation:
  name: [swing, swing_1, swing_2]
  selection: random
  duration: 6t
  speed: 1.0
  cooldown: 3t
  can-be-cancelled: true
```

| Field | Movement | Action | Additive | Meaning |
| --- | --- | --- | --- | --- |
| `name` | yes | yes | yes | One asset or variant list. |
| `arms` | optional | optional | no | `one` (default) or `both`; a main-hand movement/action can own both hand rigs. |
| `selection` | yes | yes | yes | `sequence` or `random`; default `sequence`. |
| `speed` | yes | yes | yes | Non-zero playback multiplier; negative values play the animation in reverse. |
| `blend-in` | optional | optional | no | Entry blend duration; default `2t` for movement and `1t` for action. |
| `blend-out` | optional | optional | no | Exit blend duration; default `2t`; `0t` disables it. |
| `sway-rate` | yes | no | no | Optional movement override; inherits `model.sway-rate` when omitted. |
| `loop` | true | false/omitted | n/a | Movement rules must loop. |
| `start` | optional | no | no | One-shot animation before a movement loop. |
| `end` | optional | no | no | One-shot animation after leaving a movement loop. |
| `duration` | no | optional | no | Fallback action duration, in ticks or seconds. |
| `cooldown` | no | optional | no | Delay between successful action dispatches; default `3t`. |
| `can-be-cancelled` | no | optional | no | Whether another replace action may interrupt it; default `true`. |

For finite actions, the renderer's known animation length has priority over the
configured fallback duration. `selection: sequence` advances after each
successful dispatch; `random` chooses independently per dispatch. Cooldown is
shared by all variants of one rule.

## A baseline and a specific rule

```yaml
animations:
  movement:
    - animation: base
    - when: player.sprinting == true
      animation:
        name: sprint
        speed: 1.0
        blend-in: 2t
        blend-out: 2t
```

The unconditional rule supplies a baseline. More-specific matching conditions
win; declaration order breaks equal specificity. This is different from variant
selection, where the first eligible variant wins. `0t` blending disables that side.
Rule names do not select clips automatically.
