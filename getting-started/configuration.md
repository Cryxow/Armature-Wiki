# 🛠️ Configuration

Armature reads `plugins/Armature/config.yml`. Configuration changes are applied with `/armature reload all` or the narrower reload mode that covers the changed section.

## Default configuration

```yaml
config-version: 11

input:
  movement-threshold: 0.015
  movement-stop-delay-ticks: 3
  vertical-stop-delay-ticks: 3

debug:
  enabled: false
  teleportation: false
  tracker-lifecycle: false
  animations: false
  empty-hand-mask: false

render:
  enabled: true
  first-person-only: true
  follow-player-visibility: true
  server-anchor-sync-interval-ticks: -1
  server-anchor-max-distance: 20
  vanilla-item-mode: mask
  model-adaptation: true
  anti-clipping: true
  viewmodel-fov-lock: true
  viewmodel-fov-lock-degrees: 90.0
  viewmodel-y-lock: true
  motion:
    sway:
      enabled: true
      maximum-rotation: 10
      look:
        lerp-speed: 20.0
        yaw-gain: 0.3
        pitch-gain: 0.3
        roll-gain: 0.3
      movement:
        lerp-speed: 6.0
        yaw-gain: 0
        pitch-gain: 0
        roll-gain: -5
    bob:
      enabled: false
      cycles-per-block: 1
      horizontal-amplitude: 0.025
      vertical-amplitude: 0.03
      roll-amplitude: 0.8
      frequency: 9.0
      damping: 0.9
    camera-follow:
      enabled: false
      frequency: 16.0
      damping: 0.9
      yaw-position-gain: 0
      pitch-position-gain: 0
      maximum-offset: 0.15
      pitch-compensation: 0.3

resource-pack:
  generate-zip: false

migration:
  ai:
    enabled: true
    gateway-url: https://armature-migration-gateway.vercel.app/api/migrate
    timeout-seconds: 30
    max-file-size: 262144
    min-interval-seconds: 1

armor-mappings:
```

## Input

| Key                               | Default | Meaning                                                                   |
| --------------------------------- | ------: | ------------------------------------------------------------------------- |
| `input.movement-threshold`        | `0.015` | Horizontal distance per server tick before movement is considered active. |
| `input.movement-stop-delay-ticks` |     `3` | Stationary ticks before a walk or sprint loop returns to idle.            |
| `input.vertical-stop-delay-ticks` |     `3` | Stationary ladder samples tolerated before climbing returns to idle.      |

## Debug logging

All diagnostic switches are subordinate to `debug.enabled`, which is `false` by default.

| Key                       | Default | Meaning                                                           |
| ------------------------- | ------- | ----------------------------------------------------------------- |
| `debug.enabled`           | `false` | Master switch for detailed diagnostics.                           |
| `debug.teleportation`     | `false` | Logs teleport and resynchronization diagnostics.                  |
| `debug.tracker-lifecycle` | `false` | Logs BetterModel tracker creation, recovery, and close lifecycle. |
| `debug.animations`        | `false` | Logs animation dispatch and arbitration decisions.                |
| `debug.empty-hand-mask`   | `false` | Logs selected-slot and empty-hand mask diagnostics.               |

Normal gameplay events are not console diagnostics. Keep the switches off in production and enable only the specific area being investigated.

## Rendering

| Key                                        | Default | Meaning                                                                                    |
| ------------------------------------------ | ------- | ------------------------------------------------------------------------------------------ |
| `render.enabled`                           | `true`  | Global Armature presentation switch.                                                       |
| `render.first-person-only`                 | `true`  | Hides the Armature model in third person.                                                  |
| `render.follow-player-visibility`          | `true`  | Follows the player's vanilla invisibility state.                                           |
| `render.server-anchor-sync-interval-ticks` | `-1`    | Periodic server-anchor refresh interval.                                                   |
| `render.server-anchor-max-distance`        | `20`    | Distance that forces an immediate anchor refresh.                                          |
| `render.vanilla-item-mode`                 | `mask`  | `mask` uses the packet-side invisible item; `source-model` modifies the source item model. |
| `render.model-adaptation`                  | `true`  | Applies Armature's first-person model adaptation in memory.                                |
| `render.anti-clipping`                     | `true`  | Enables marked-bone anti-clipping resource-pack shaders.                                   |
| `render.viewmodel-fov-lock`                | `true`  | Locks marked viewmodel projection to the configured FOV.                                   |
| `render.viewmodel-fov-lock-degrees`        | `90.0`  | FOV used by the viewmodel lock; valid range is greater than 1 and less than 179.           |
| `render.viewmodel-y-lock`                  | `true`  | Removes camera-pitch drift from marked viewmodels.                                         |

`render.camera-rotation.enabled` is an optional key and defaults to `false`. When enabled, Armature generates the camera-marker pipeline for supported clients. The optional `render.camera-rotation.debug-mode` defaults to `off`; allowed values are `off`, `identity`, `signature`, `metadata`, `fixed-basis`, `uv`, and `status`.

## Motion

Motion values are visual multipliers. Rotation gains are degrees; position gains and offsets are model-space distances.

### Sway

`render.motion.sway.*` controls look and movement rotation. `maximum-rotation` caps the combined rotation. `look.lerp-speed` and `movement.lerp-speed` smooth the respective targets. Their `yaw-gain`, `pitch-gain`, and `roll-gain` values scale the response.

### Bob

`render.motion.bob.*` controls footstep-style movement: `cycles-per-block`, horizontal/vertical/roll amplitudes, `frequency`, and `damping`.

### Camera follow

`render.motion.camera-follow.*` is positional inertia, not camera orientation. It controls frequency, damping, yaw/pitch position gains, maximum offset, and pitch compensation.

## Resource pack and migration

`resource-pack.generate-zip` additionally writes `plugins/Armature/resource_pack.zip` after generation. The directory form is always the source used by the server workflow.

`migration.ai` controls the legacy-profile migration service used by `/armature migrate-config`. The source file is validated before the request; the result is validated against the modern loader before it is written. `gateway-token-env` is an optional compatibility key for a private gateway and is read from the server environment.

## Custom armor mappings

Armature supports vanilla armor automatically. Map a custom armor item to a BetterModel armor type under `armor-mappings`:

```yaml
armor-mappings:
  "your_namespace:knight_helmet": knight
```

See [Custom Armors](../advanced/custom-armors.md).
