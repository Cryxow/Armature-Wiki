# 🛠️ Configuration

Armature reads `plugins/Armature/config.yml`. Values are loaded at startup and by `/armature reload`.

## Complete `config.yml`

```yaml
input:
  movement-threshold: 0.015
  movement-stop-delay-ticks: 3

debug:
  enabled: false
  tracker-lifecycle: true
  animations: true
  empty-hand-mask: false

render:
  enabled: true
  first-person-only: true
  model-adaptation: true
  vanilla-item-mode: mask
  empty-hand-mask:
    repair-server-materialization: true
  motion:
    sway:
      enabled: true
      maximum-rotation: 4.0
      look:
        lerp-speed: 0.0
        yaw-gain: 0.12
        pitch-gain: 0.10
        roll-gain: 0.05
      movement:
        lerp-speed: 14.0
        yaw-gain: 0.20
        pitch-gain: 0.06
        roll-gain: 0.35
    bob:
      enabled: true
      cycles-per-block: 1.8
      horizontal-amplitude: 0.025
      vertical-amplitude: 0.018
      roll-amplitude: 0.8
      frequency: 9.0
      damping: 0.9
    camera-follow:
      enabled: true
      frequency: 9.0
      damping: 0.9
      yaw-position-gain: 0.0025
      pitch-position-gain: 0.0020
      maximum-offset: 0.035
      pitch-compensation: 0.20

armor-mappings:
  "your_namespace:knight_helmet": knight
```

## Input

`input.*`

<table><thead><tr><th>Key</th><th>Type</th><th width="250">Meaning</th></tr></thead><tbody><tr><td><code>movement-threshold</code></td><td>number</td><td>Horizontal distance per server tick required before Armature considers the player walking. Lower values detect subtle movement; higher values reduce jitter.</td></tr><tr><td><code>movement-stop-delay-ticks</code></td><td>integer</td><td>Stationary ticks required before the walk/sprint loop returns to idle. Higher values prevent flicker when movement briefly stops.</td></tr></tbody></table>

## Debug

`debug.*`

<table><thead><tr><th>Key</th><th>Type</th><th width="250">Meaning</th></tr></thead><tbody><tr><td><code>enabled</code></td><td>boolean</td><td>Master switch for diagnostic logging. Keep <code>false</code> in production.</td></tr><tr><td><code>tracker-lifecycle</code></td><td>boolean</td><td>Logs BetterModel tracker creation, recovery and close lifecycle when debug is enabled.</td></tr><tr><td><code>animations</code></td><td>boolean</td><td>Logs animation dispatch, replacement, speed and completion decisions when debug is enabled.</td></tr><tr><td><code>empty-hand-mask</code></td><td>boolean</td><td>Logs selected-slot history, server inventory mutations and client mask state. Use only while diagnosing empty-hand masking.</td></tr></tbody></table>

## Rendering

`render.*`

| Key                                             | Type    | Meaning                                                                                                                                                                                                               |
| ----------------------------------------------- | ------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `enabled`                                       | boolean | Global Armature rendering switch. `false` removes Armature presentation while leaving provider/gameplay plugins running.                                                                                              |
| `first-person-only`                             | boolean | `true` hides the model in third person.                                                                                                                                                                               |
| `model-adaptation`                              | boolean | Adapts the model to bypass in-game limitations. It does not modify your model.                                                                                                                                        |
| `vanilla-item-model`                            | option  | <p><code>source-model</code> edits the real item model to hide player's hand.<br><code>mask</code> shows a fake item to the player to hide their hand.<br>⇒ <code>mask</code> is a better option for flexibility.</p> |
| `empty-hand-mask.repair-server-materialization` | boolean | Keep this enabled if you are using `mask` to prevent inventory glitches.                                                                                                                                              |

## Motion

`render.motion.*`

All gains are visual multipliers. Rotation values are degrees; offsets are model-space distance. Motion layers compose on the current model transform.

### Sway

`render.motion.sway.*`

| Key                   | Meaning                                                                                                                   |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| `enabled`             | Enables camera-look and movement rotation sway.                                                                           |
| `maximum-rotation`    | Absolute cap for the combined sway rotation. Prevents extreme angles when several gains stack.                            |
| `look.lerp-speed`     | Smoothing speed for camera-look sway. `0` applies camera deltas directly; larger values follow the target more gradually. |
| `look.yaw-gain`       | Look yaw converted into model yaw sway.                                                                                   |
| `look.pitch-gain`     | Look pitch converted into model pitch sway.                                                                               |
| `look.roll-gain`      | Look movement converted into model roll sway.                                                                             |
| `movement.lerp-speed` | Smoothing speed for movement sway. Larger values settle faster.                                                           |
| `movement.yaw-gain`   | Strafing/movement direction converted into yaw sway.                                                                      |
| `movement.pitch-gain` | Movement converted into pitch sway.                                                                                       |
| `movement.roll-gain`  | Movement converted into roll sway.                                                                                        |

### Bob

`render.motion.bob.*`

| Key                    | Meaning                                                                            |
| ---------------------- | ---------------------------------------------------------------------------------- |
| `enabled`              | Enables footstep-style bob while moving.                                           |
| `cycles-per-block`     | Bob oscillations per block travelled. Higher values produce tighter, faster steps. |
| `horizontal-amplitude` | Side-to-side bob distance.                                                         |
| `vertical-amplitude`   | Up/down bob distance.                                                              |
| `roll-amplitude`       | Roll angle applied by the bob cycle.                                               |
| `frequency`            | Responsiveness of the bob spring. Higher values react faster.                      |
| `damping`              | Energy loss in the bob spring. Higher values reduce overshoot.                     |

### Camera follow

`render.motion.camera-follow.*`

Camera follow is positional inertia, not camera-facing orientation. Billboard orientation remains responsible for facing the player.

| Key                   | Meaning                                               |
| --------------------- | ----------------------------------------------------- |
| `enabled`             | Enables the positional camera-follow effect.          |
| `frequency`           | Response speed of the positional spring.              |
| `damping`             | Stabilizes the spring and limits overshoot.           |
| `yaw-position-gain`   | Local translation generated by camera yaw.            |
| `pitch-position-gain` | Local translation generated by camera pitch.          |
| `maximum-offset`      | Maximum camera-follow translation.                    |
| `pitch-compensation`  | Compensates vertical placement while looking up/down. |

## Custom Armor Mappings

`armor-mappings.*`

Armature automatically supports vanilla player armors, but there is a little step to support your custom armors added by a plugin. Learn more in [Custom Armors](../advanced/custom-armors.md).

## Tuning recipes

**Stable competitive view:** set `sway.maximum-rotation` to `2.0~3.0`, reduce movement roll, and keep camera-follow damping near `0.9`.

**Cinematic view:** increase look and movement gains gradually, raise bob amplitudes, then raise caps only if clipping is acceptable.

**Diagnose stutter:** enable `debug.enabled`, `debug.tracker-lifecycle` and `debug.animations`; test one player; disable after collecting logs. Motion tuning cannot fix a tracker that is no longer scheduled.
