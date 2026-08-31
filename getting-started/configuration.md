# Configuration

Armature reads <code>plugins/Armature/config.yml</code>. Configuration changes
are applied with <code>/armature reload all</code> or the narrower reload mode
that covers the changed section.

## Default configuration

~~~yaml
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
~~~

## Input

| Key | Default | Meaning |
| --- | ---: | --- |
| <code>input.movement-threshold</code> | <code>0.015</code> | Horizontal distance per server tick before movement is considered active. |
| <code>input.movement-stop-delay-ticks</code> | <code>3</code> | Stationary ticks before a walk or sprint loop returns to idle. |
| <code>input.vertical-stop-delay-ticks</code> | <code>3</code> | Stationary ladder samples tolerated before climbing returns to idle. |

## Debug logging

All diagnostic switches are subordinate to
<code>debug.enabled</code>, which is <code>false</code> by default.

| Key | Default | Meaning |
| --- | --- | --- |
| <code>debug.enabled</code> | <code>false</code> | Master switch for detailed diagnostics. |
| <code>debug.teleportation</code> | <code>false</code> | Logs teleport and resynchronization diagnostics. |
| <code>debug.tracker-lifecycle</code> | <code>false</code> | Logs BetterModel tracker creation, recovery, and close lifecycle. |
| <code>debug.animations</code> | <code>false</code> | Logs animation dispatch and arbitration decisions. |
| <code>debug.empty-hand-mask</code> | <code>false</code> | Logs selected-slot and empty-hand mask diagnostics. |

Normal gameplay events are not console diagnostics. Keep the switches off in
production and enable only the specific area being investigated.

## Rendering

| Key | Default | Meaning |
| --- | --- | --- |
| <code>render.enabled</code> | <code>true</code> | Global Armature presentation switch. |
| <code>render.first-person-only</code> | <code>true</code> | Hides the Armature model in third person. |
| <code>render.follow-player-visibility</code> | <code>true</code> | Follows the player's vanilla invisibility state. |
| <code>render.server-anchor-sync-interval-ticks</code> | <code>-1</code> | Periodic server-anchor refresh interval. |
| <code>render.server-anchor-max-distance</code> | <code>20</code> | Distance that forces an immediate anchor refresh. |
| <code>render.vanilla-item-mode</code> | <code>mask</code> | <code>mask</code> uses the packet-side invisible item; <code>source-model</code> modifies the source item model. |
| <code>render.model-adaptation</code> | <code>true</code> | Applies Armature's first-person model adaptation in memory. |
| <code>render.anti-clipping</code> | <code>true</code> | Enables marked-bone anti-clipping resource-pack shaders. |
| <code>render.viewmodel-fov-lock</code> | <code>true</code> | Locks marked viewmodel projection to the configured FOV. |
| <code>render.viewmodel-fov-lock-degrees</code> | <code>90.0</code> | FOV used by the viewmodel lock; valid range is greater than 1 and less than 179. |
| <code>render.viewmodel-y-lock</code> | <code>true</code> | Removes camera-pitch drift from marked viewmodels. |

<code>render.camera-rotation.enabled</code> is an optional key and defaults to
<code>false</code>. When enabled, Armature generates the camera-marker pipeline
for supported clients. The optional
<code>render.camera-rotation.debug-mode</code> defaults to <code>off</code>;
allowed values are <code>off</code>, <code>identity</code>,
<code>signature</code>, <code>metadata</code>, <code>fixed-basis</code>,
<code>uv</code>, and <code>status</code>.

## Motion

Motion values are visual multipliers. Rotation gains are degrees; position
gains and offsets are model-space distances.

### Sway

<code>render.motion.sway.*</code> controls look and movement rotation.
<code>maximum-rotation</code> caps the combined rotation.
<code>look.lerp-speed</code> and <code>movement.lerp-speed</code> smooth the
respective targets. Their <code>yaw-gain</code>, <code>pitch-gain</code>, and
<code>roll-gain</code> values scale the response.

### Bob

<code>render.motion.bob.*</code> controls footstep-style movement:
<code>cycles-per-block</code>, horizontal/vertical/roll amplitudes,
<code>frequency</code>, and <code>damping</code>.

### Camera follow

<code>render.motion.camera-follow.*</code> is positional inertia, not camera
orientation. It controls frequency, damping, yaw/pitch position gains,
maximum offset, and pitch compensation.

## Resource pack and migration

<code>resource-pack.generate-zip</code> additionally writes
<code>plugins/Armature/resource_pack.zip</code> after generation. The
directory form is always the source used by the server workflow.

<code>migration.ai</code> controls the legacy-profile migration service used by
<code>/armature migrate-config</code>. The source file is validated before the
request; the result is validated against the modern loader before it is
written. <code>gateway-token-env</code> is an optional compatibility key for a
private gateway and is read from the server environment.

## Custom armor mappings

Armature supports vanilla armor automatically. Map a custom armor item to a
BetterModel armor type under <code>armor-mappings</code>:

~~~yaml
armor-mappings:
  "your_namespace:knight_helmet": knight
~~~

See [Custom Armors](../advanced/custom-armors.md).
