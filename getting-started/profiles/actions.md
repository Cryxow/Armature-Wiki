---
description: "Action Rules"
---

# Action Rules

Actions use an event `trigger` and optional `condition`:

```yaml
animations:
  actions:
    - trigger: block:place
      condition:
        - item.main-hand.type == minecraft:stone
      animation:
        name: block-place
        duration: 8t

    - trigger: api:custom-hit
      condition:
        - event.argument.critical == true
      animation:
        name: hit
        duration: 6t
      consume: false
```

Actions always use the exclusive `replace` channel. There is no `mode` field.
`can-be-cancelled: false` protects an active action from replacement; it does
not create an overlay. Additive rules are the separate Armature-composed
channel.

`consume: true` is meaningful for an event owner that supports cancellation,
such as vanilla right-click or a provider input. For an API/Skript/Denizen signal it
is returned in `ArmatureSignalResult`; the external caller still owns
gameplay cancellation.

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

## Protected actions and transitions

`can-be-cancelled: false` protects the active finite action from ordinary
replacement. Pending variant/composition changes are reevaluated after the
protected action finishes. Item changes/disconnects and renderer cleanup still
need lifecycle handling; this flag is not a gameplay lock.

An accepted unequip clip uses the outgoing instance/pose. Rapid A → B → A can
reclaim the matching outgoing profile/model instance. Negative nonzero `speed`
plays the authored timeline in reverse; zero speed is invalid.

`consume` is a provider/input dispatch decision, not automatic ammo/reload logic.
Some cancellable input/provider signals can be consumed; see
[WeaponMechanics](../../advanced/supported-plugins/weaponmechanics.md) for its
reload-input cancellation behavior. Keep weapon authority in that plugin.

Verify repeated inputs, cooldown rejection, interruptible/protected clips,
switching to a vanilla item and switching back. [Hands](hands.md) explains
how `arms: both` affects simultaneous presentations.
