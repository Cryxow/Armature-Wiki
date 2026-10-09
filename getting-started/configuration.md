---
description: "Configuration Reference"
---

# Configuration Reference

Armature reads `plugins/Armature/config.yml`. This page reproduces the embedded
2.0.0 defaults and describes every active leaf option. Profile migration uses
internal limits and has no configurable settings. Existing administrator values
are preserved during schema upgrades, so a live installation can differ.

## Apply a change

| Requirement | Operation |
| --- | --- |
| Reload | `/armature config reload` |
| Restart | Fully stop/start the server; includes backend, client bridge and renderer-pack split |
| Pack rebuild | `/armature diagnostics pack rebuild`, then the external provider's generation workflow |
| Client reload | Deliver/download the final changed pack; a server config reload alone is insufficient |

Run `/armature config validate` for the current schema's allowed values and
requirements. Invalid reload candidates retain the last known-good runtime.
`config-version` belongs to Armature.

## Complete default config.yml

```yaml
config-version: 17

input:
  movement:
    vertical-stop-delay-ticks: 3
    # Horizontal distance travelled in one server tick before walk starts.
    threshold: 0.015
    # Consecutive stationary ticks required before walk/sprint returns to idle.
    stop-delay-ticks: 3

debug:
  # Master switch for the debug categories below.
  enabled: false
  teleportation: false
  tracker-lifecycle: true
  animations: true
  vanilla-hiding: false
  # Native writing/reload and CraftEngine pack preparation phase timings.
  pack-generation: false
  # Model warning summaries in the console; errors are always reported.
  # Full diagnostics remain available via /armature diagnostics native.
  native-compiler-warnings: false

render:
    # native is the primary renderer and requires no BetterModel installation.
    # bettermodel remains available; deprecation is planned for future versions.
  backend: native
  client-mod:
    # Experimental optional Fabric 1.21.8 renderer for native .armature v2 models.
    # Clients without the mod keep the configured server renderer.
    enabled: true
  enabled: true
  dynamic-items:
    # Preserve the outgoing model's items during unequip/deferred actions.
    # The incoming model receives the current items when equip is activated.
    # Applies to server rendering and the optional Fabric renderer.
    defer-until-equipped: true
  camera-rotation:
    # Client-side Fabulous post-effect driven by the optional s_camera bone.
    enabled: true
    debug-mode: 'off'
    zoom-correction:
      # Crops the image to hide missing edges during camera-bone rotations.
      # Disabled preserves the original FOV and does not apply any crop zoom.
      enabled: true
      # fixed keeps one constant zoom; dynamic adapts it to the bone rotation.
      mode: fixed
      # Fixed zoom factor, or maximum allowed factor in dynamic mode.
      maximum-zoom: 1.25
      # In fixed mode, widens the 3D source projection before applying the
      # crop, preserving the player's visible world and viewmodel FOV.
      # Breaks with Sodium.
      preserve-fov: false
    rotation-clamp:
      # Limits the total 3D rotation received from the Blockbench camera bone.
      enabled: true
      maximum-degrees: 7.0
  visibility:
    first-person-only: true
    follow-player-visibility: true
  viewmodel:
    model-adaptation: true
    anti-clipping: true
    y-lock: true
    fov:
      locked: true
      degrees: 90.0
  vanilla-hiding:
    # packet-mask | core-shaders
    # core-shaders sends a per-player GameTime channel and can conflict with
    # other resource-pack shaders that also use GameTime. core-shaders never
    # installs inventory-mask packets. PacketEvents action detection remains
    # independent.
    mode: core-shaders
  anchor:
    max-distance: 20
    sync-interval-ticks: -1
  motion:
    sway:
      enabled: true
      # Applies look/movement sway rotation to the s_camera bone.
      affects-camera-bone: true
      maximum-rotation: 10
      look:
        lerp-speed: 20.0
        yaw-gain: -0.1
        pitch-gain: -0.1
        roll-gain: -0.1
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
      frequency: 20.0
      damping: 0.1
      yaw-position-gain: 0
      pitch-position-gain: 0
      maximum-offset: 0.15
      pitch-compensation: 0.3
    # Physics-driven bones: model bones named phy_* stay perpendicular to
    # the ground (their down axis follows the world, not the model's server
    # rotation) and swing with movement acceleration and player turning.
    # Only the native backend implements them; BetterModel ignores the
    # prefix. The bone pivot is the rotation center.
    physics:
      enabled: true
      acceleration-gain: 4.0
      maximum-angle: 25
      # Spring response of the swing. frequency is how fast the bone chases
      # the target, damping is how much it resists overshooting it (1.0 = no
      # overshoot at all). With damping below 1.0 the bone swings past its rest
      # pose and bounces back, which reads as a snap when the player stops:
      # at frequency 4.0 / damping 0.6 a 25 degree return overshoots by
      # ~2.1 degrees. Raise damping toward 1.0 to remove that bounce, or lower
      # frequency for a slower, heavier settle.
      frequency: 4.0
      damping: 0.6
      # Tilt added per degree/second of camera yaw turn. Keep small: mouse
      # sway while looking up or down must not fling the bone sideways.
      turn-gain: 0.25

resource-pack:
  # Optional separate renderer pack. The provider continues delivering the common pack.
  # Set the external URL, enable, restart, then rebuild and publish both packs.
  renderer-pack:
    enabled: false
    # Host plugins/Armature/renderer_pack.zip here. {sha1} can version the URL.
    url: ""
    prompt: "Armature server rendering"
  # Also write plugins/Armature/resource_pack.zip when Armature generates assets.
  generate-zip: false
  # Automatic Armature publication. Explicit pack/all reloads always generate.
  generation:
    on-startup: true
    on-reload: true
    on-provider-change: true
  # Empty keeps the backend's description. Does not rename a provider's final pack.
  description: ""
  # Filters bundled overlays; does not convert models for other client versions.
  supported-version:
    min: "1.21.4"
    max: latest
  validation:
    enable: true
    fail-on-error: false
    check-models: true
    check-textures: true
    check-atlases: true
  optimization:
    json:
      enable: false
      exclude: []
    texture:
      enable: false
      compression-level: 9
      exclude: []
  # Standalone Armature ZIP only. Path relative to plugins/Armature.
  zip:
    path: resource_pack.zip
    method: deflated
    compression-level: 6
  # Deterministic ownership when two sources publish one normalized path.
  # keep-existing preserves the first external owner; replace lets Armature
  # win when its generated source is involved; error aborts the candidate.
  collision-policy: keep-existing
  # Optional first-match rules: [{source: provider, namespace: ns, path: ns:foo/**, policy: replace}]
  collision-rules: []
  exclusions:
    # Source owner IDs are normalized lowercase and match exactly.
    sources: []
    # Namespace IDs are normalized lowercase and match assets/<namespace>/**.
    namespaces: []
    # Paths use forward slashes; * stays within one segment, ** crosses segments.
    paths: []
```

## config-version options

| Option | Default | Accepted values | Apply | Purpose |
| --- | --- | --- | --- | --- |
| `config-version` | `17` | an integer >= 0 | restart | Managed schema version; do not edit manually. |

## input options

| Option | Default | Accepted values | Apply | Purpose |
| --- | --- | --- | --- | --- |
| `input.movement.vertical-stop-delay-ticks` | `3` | an integer >= 0 | reload | Stationary vertical ticks before a vertical movement transition ends. |
| `input.movement.threshold` | `0.015` | a finite number in [0, 100] | reload | Horizontal distance travelled in one server tick before walk starts. |
| `input.movement.stop-delay-ticks` | `3` | an integer >= 0 | reload | Consecutive stationary ticks required before walk/sprint returns to idle. |

## debug options

| Option | Default | Accepted values | Apply | Purpose |
| --- | --- | --- | --- | --- |
| `debug.enabled` | `false` | true or false | reload | Master switch for the debug categories below. |
| `debug.teleportation` | `false` | true or false | reload | Enable the teleportation diagnostic category; the master debug switch also applies. |
| `debug.tracker-lifecycle` | `true` | true or false | reload | Enable the tracker lifecycle diagnostic category; the master debug switch also applies. |
| `debug.animations` | `true` | true or false | reload | Enable the animations diagnostic category; the master debug switch also applies. |
| `debug.vanilla-hiding` | `false` | true or false | reload | Enable the vanilla hiding diagnostic category; the master debug switch also applies. |
| `debug.pack-generation` | `false` | true or false | reload | Native writing/reload and CraftEngine pack preparation phase timings. |
| `debug.native-compiler-warnings` | `false` | true or false | reload | Model warning summaries in the console; errors are always reported. Full diagnostics remain available via /armature diagnostics native. |

## render options

| Option | Default | Accepted values | Apply | Purpose |
| --- | --- | --- | --- | --- |
| `render.backend` | `native` | bettermodel or native | restart | native is the primary renderer and requires no BetterModel installation. bettermodel remains available; deprecation is planned for future versions. |
| `render.client-mod.enabled` | `true` | true or false | restart | Experimental optional Fabric 1.21.8 renderer for native .armature v2 models. Clients without the mod keep the configured server renderer. |
| `render.enabled` | `true` | true or false | reload | Enable first-person rendering while the plugin remains loaded. |
| `render.dynamic-items.defer-until-equipped` | `true` | true or false | reload | Preserve the outgoing model's items during unequip/deferred actions. The incoming model receives the current items when equip is activated. Applies to server rendering and the optional Fabric renderer. |
| `render.camera-rotation.enabled` | `true` | true or false | pack rebuild, client reload | Client-side Fabulous post-effect driven by the optional s_camera bone. |
| `render.camera-rotation.debug-mode` | `'off'` | off, identity, signature, metadata, fixed-basis, uv, or status | pack rebuild, client reload | Debug mode. |
| `render.camera-rotation.zoom-correction.enabled` | `true` | true or false | pack rebuild, client reload | Crops the image to hide missing edges during camera-bone rotations. Disabled preserves the original FOV and does not apply any crop zoom. |
| `render.camera-rotation.zoom-correction.mode` | `fixed` | fixed or dynamic | pack rebuild, client reload | fixed keeps one constant zoom; dynamic adapts it to the bone rotation. |
| `render.camera-rotation.zoom-correction.maximum-zoom` | `1.25` | a finite number in [1, 4] | pack rebuild, client reload | Fixed zoom factor, or maximum allowed factor in dynamic mode. |
| `render.camera-rotation.zoom-correction.preserve-fov` | `false` | true or false | pack rebuild, client reload | In fixed mode, widens the 3D source projection before applying the crop, preserving the player's visible world and viewmodel FOV. Breaks with Sodium. |
| `render.camera-rotation.rotation-clamp.enabled` | `true` | true or false | pack rebuild, client reload | Clamp animated camera correction to the configured maximum. |
| `render.camera-rotation.rotation-clamp.maximum-degrees` | `7.0` | a finite number in [0, 180] | pack rebuild, client reload | Maximum camera correction magnitude, in degrees, when the clamp is enabled. |
| `render.visibility.first-person-only` | `true` | true or false | reload | Restrict the presentation to first-person viewing. |
| `render.visibility.follow-player-visibility` | `true` | true or false | reload | Follow player visibility/invisibility when deciding whether to render. |
| `render.viewmodel.model-adaptation` | `true` | true or false | pack rebuild | Adapt supported source models into the first-person scene without rewriting files. |
| `render.viewmodel.anti-clipping` | `true` | true or false | pack rebuild, client reload | Generate the marked viewmodel shader path so Armature geometry can render through blocks. |
| `render.viewmodel.y-lock` | `true` | true or false | reload | Keep the viewmodel vertical placement stable in the supported shader path. |
| `render.viewmodel.fov.locked` | `true` | true or false | pack rebuild, client reload | Use a fixed viewmodel field of view independent of world FOV. |
| `render.viewmodel.fov.degrees` | `90.0` | a finite number in (1, 179) | pack rebuild, client reload | Viewmodel FOV in degrees when locking is enabled. |
| `render.vanilla-hiding.mode` | `core-shaders` | packet-mask or core-shaders | reload, pack rebuild, client reload | packet-mask  /  core-shaders core-shaders sends a per-player GameTime channel and can conflict with other resource-pack shaders that also use GameTime. core-shaders never installs inventory-mask packets. PacketEvents action detection remains independent. |
| `render.anchor.max-distance` | `20` | a finite number in [0, 128] | reload | Maximum server anchor separation before correction. |
| `render.anchor.sync-interval-ticks` | `-1` | an integer in [-1, 20000] | reload | Anchor synchronization interval; -1 uses the backend policy. |
| `render.motion.sway.enabled` | `true` | true or false | reload | Enable procedural look/movement sway. |
| `render.motion.sway.affects-camera-bone` | `true` | true or false | reload | Applies look/movement sway rotation to the s_camera bone. |
| `render.motion.sway.maximum-rotation` | `10` | a finite number in [0, 180] | reload | Maximum combined sway rotation in degrees. |
| `render.motion.sway.look.lerp-speed` | `20.0` | a finite number in [0, 1000] | reload | Look smoothing response; zero removes look interpolation. |
| `render.motion.sway.look.yaw-gain` | `-0.1` | a finite number in [-1000, 1000] | reload | Signed yaw gain for this motion layer. |
| `render.motion.sway.look.pitch-gain` | `-0.1` | a finite number in [-1000, 1000] | reload | Signed pitch gain for this motion layer. |
| `render.motion.sway.look.roll-gain` | `-0.1` | a finite number in [-1000, 1000] | reload | Signed roll gain for this motion layer. |
| `render.motion.sway.movement.lerp-speed` | `6.0` | a finite number in [0, 1000] | reload | Movement smoothing response. |
| `render.motion.sway.movement.yaw-gain` | `0` | a finite number in [-1000, 1000] | reload | Signed yaw gain for this motion layer. |
| `render.motion.sway.movement.pitch-gain` | `0` | a finite number in [-1000, 1000] | reload | Signed pitch gain for this motion layer. |
| `render.motion.sway.movement.roll-gain` | `-5` | a finite number in [-1000, 1000] | reload | Signed roll gain for this motion layer. |
| `render.motion.bob.enabled` | `false` | true or false | reload | Enable walking bob. |
| `render.motion.bob.cycles-per-block` | `1` | a finite number in [0, 1000] | reload | Walking bob cycles per block travelled. |
| `render.motion.bob.horizontal-amplitude` | `0.025` | a finite number in [0, 1000] | reload | Horizontal bob amplitude. |
| `render.motion.bob.vertical-amplitude` | `0.03` | a finite number in [0, 1000] | reload | Vertical bob amplitude. |
| `render.motion.bob.roll-amplitude` | `0.8` | a finite number in [0, 1000] | reload | Roll bob amplitude in degrees. |
| `render.motion.bob.frequency` | `9.0` | a finite number in [0, 100] | reload | Spring response frequency; higher values settle faster. |
| `render.motion.bob.damping` | `0.9` | a finite number in [0, 10] | reload | Spring damping; near 1 avoids overshoot, below 1 permits rebound. |
| `render.motion.camera-follow.enabled` | `false` | true or false | reload | Enable spring-based camera-follow displacement. |
| `render.motion.camera-follow.frequency` | `20.0` | a finite number in [0, 100] | reload | Spring response frequency; higher values settle faster. |
| `render.motion.camera-follow.damping` | `0.1` | a finite number in [0, 10] | reload | Spring damping; near 1 avoids overshoot, below 1 permits rebound. |
| `render.motion.camera-follow.yaw-position-gain` | `0` | a finite number in [-1000, 1000] | reload | Signed yaw position gain for this motion layer. |
| `render.motion.camera-follow.pitch-position-gain` | `0` | a finite number in [-1000, 1000] | reload | Signed pitch position gain for this motion layer. |
| `render.motion.camera-follow.maximum-offset` | `0.15` | a finite number in [0, 100] | reload | Maximum positional camera-follow offset. |
| `render.motion.camera-follow.pitch-compensation` | `0.3` | a finite number in [0, 100] | reload | Compensate displacement for the view pitch. |
| `render.motion.physics.enabled` | `true` | true or false | reload | Enable native physics for marked bones; no simulation in BetterModel. |
| `render.motion.physics.acceleration-gain` | `4.0` | a finite number in [-1000, 1000] | reload | Signed gain converting player acceleration to bone tilt. |
| `render.motion.physics.maximum-angle` | `25` | a finite number in [0, 180] | reload | Clamp native physics rotation in degrees. |
| `render.motion.physics.frequency` | `4.0` | a finite number in [0, 100] | reload | Spring response of the swing. frequency is how fast the bone chases the target, damping is how much it resists overshooting it (1.0 = no overshoot at all). With damping below 1.0 the bone swings past its rest pose and bounces back, which reads as a snap when the player stops: at frequency 4.0 / damping 0.6 a 25 degree return overshoots by ~2.1 degrees. Raise damping toward 1.0 to remove that bounce, or lower frequency for a slower, heavier settle. |
| `render.motion.physics.damping` | `0.6` | a finite number in [0, 10] | reload | Spring damping; near 1 avoids overshoot, below 1 permits rebound. |
| `render.motion.physics.turn-gain` | `0.25` | a finite number in [0, 1000] | reload | Tilt added per degree/second of camera yaw turn. Keep small: mouse sway while looking up or down must not fling the bone sideways. |

## resource-pack options

| Option | Default | Accepted values | Apply | Purpose |
| --- | --- | --- | --- | --- |
| `resource-pack.renderer-pack.enabled` | `false` | true or false | restart | Split optional externally hosted server renderer assets from the common pack; restart required. |
| `resource-pack.renderer-pack.url` | `""` | an external HTTP(S) ZIP URL | restart | Host plugins/Armature/renderer_pack.zip here. {sha1} can version the URL. |
| `resource-pack.renderer-pack.prompt` | `"Armature server rendering"` | a resource-pack prompt | restart | Client pack request prompt for the separate server renderer pack. |
| `resource-pack.generate-zip` | `false` | true or false | pack rebuild | Generate a standalone ZIP in addition to the directory output. |
| `resource-pack.generation.on-startup` | `true` | true or false | reload | Generate automatically for this lifecycle event; explicit pack rebuild remains available. |
| `resource-pack.generation.on-reload` | `true` | true or false | reload | Generate automatically for this lifecycle event; explicit pack rebuild remains available. |
| `resource-pack.generation.on-provider-change` | `true` | true or false | reload | Generate automatically for this lifecycle event; explicit pack rebuild remains available. |
| `resource-pack.description` | `""` | a description, optionally empty | pack rebuild | Empty keeps the backend's description. Does not rename a provider's final pack. |
| `resource-pack.supported-version.min` | `"1.21.4"` | a bundled client version, server or latest | pack rebuild | Lowest bundled overlay retained; this does not convert source models. |
| `resource-pack.supported-version.max` | `latest` | a bundled client version, server or latest | pack rebuild | Highest bundled overlay retained; latest selects the latest bundled definition. |
| `resource-pack.validation.enable` | `true` | true or false | pack rebuild | Validate the generated candidate pack. |
| `resource-pack.validation.fail-on-error` | `false` | true or false | pack rebuild | Reject candidates on validation errors when enabled. |
| `resource-pack.validation.check-models` | `true` | true or false | pack rebuild | Check generated model references. |
| `resource-pack.validation.check-textures` | `true` | true or false | pack rebuild | Check generated texture references. |
| `resource-pack.validation.check-atlases` | `true` | true or false | pack rebuild | Check atlas references and assets. |
| `resource-pack.optimization.json.enable` | `false` | true or false | pack rebuild | Optimize eligible generated JSON; excluded paths are retained. |
| `resource-pack.optimization.json.exclude` | `[]` | pack-relative glob patterns | pack rebuild | Pack-relative glob exclusions for this optimization only. |
| `resource-pack.optimization.texture.enable` | `false` | true or false | pack rebuild | Optimize eligible generated PNG textures; excluded paths are retained. |
| `resource-pack.optimization.texture.compression-level` | `9` | an integer in [0,9] | pack rebuild | PNG compression level (0-9); does not change gameplay. |
| `resource-pack.optimization.texture.exclude` | `[]` | pack-relative glob patterns | pack rebuild | Pack-relative glob exclusions for this optimization only. |
| `resource-pack.zip.path` | `resource_pack.zip` | a relative ZIP path inside Armature | pack rebuild | Relative standalone ZIP output path below plugins/Armature. |
| `resource-pack.zip.method` | `deflated` | stored or deflated | pack rebuild | Choose uncompressed entries (stored) or ZIP deflate (deflated). |
| `resource-pack.zip.compression-level` | `6` | an integer in [0,9] | pack rebuild | ZIP deflate compression level (0-9). |
| `resource-pack.collision-policy` | `keep-existing` | keep-existing, replace, or error | pack rebuild | Deterministic ownership when two sources publish one normalized path. keep-existing preserves the first external owner; replace lets Armature win when its generated source is involved; error aborts the candidate. |
| `resource-pack.collision-rules` | `[]` | scoped collision rules | pack rebuild | Optional first-match rules: [{source: provider, namespace: ns, path: ns:foo/**, policy: replace}] |
| `resource-pack.exclusions.sources` | `[]` | list of lowercase source owner IDs | pack rebuild | Source owner IDs are normalized lowercase and match exactly. |
| `resource-pack.exclusions.namespaces` | `[]` | list of namespace IDs | pack rebuild | Namespace IDs are normalized lowercase and match assets/<namespace>/**. |
| `resource-pack.exclusions.paths` | `[]` | list of pack-relative globs; * one segment, ** multiple segments | pack rebuild | Paths use forward slashes; * stays within one segment, ** crosses segments. |


## Related files

`variables.yml` declares shared profile variables; `messages.yml` controls
administrator messages (`auto`, `en`, `fr`). Profile files, models and custom
armor textures have their own directories. This configuration does not contain
profile animation rules.

Continue with [Rendering & camera](../configuration/rendering-camera.md),
[Motion & physics](../advanced/best-settings.md),
[Resource packs](../configuration/resource-packs.md),
[Skins & armor](../advanced/custom-armors.md), or the
[in-game editor](in-game-editor.md).
