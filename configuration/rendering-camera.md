---
description: "Rendering & Camera"
---

# Rendering & Camera

## Renderer and visibility

`render.backend: native` selects the primary renderer. BetterModel is an explicit
compatibility selection and requires its plugin; backend changes need a restart.
The optional client bridge needs native rendering, but vanilla clients still
use the server renderer.

`render.enabled` controls rendering. `render.visibility.first-person-only` and
`follow-player-visibility` control presentation visibility. A profile can use
`model.render-events` to show its rig only for selected actions; the corresponding
animation rule must still exist.

## Hide the original first-person hand

`model.hide-vanilla-hand` is per profile. `render.vanilla-hiding.mode` selects:

| Mode | Effect and constraints |
| --- | --- |
| `packet-mask` | Sends presentation copies to the client; the server inventory is unchanged. Test plugins that inspect/intercept inventory packets. |
| `core-shaders` | Uses the generated shaders and a per-player GameTime channel; no inventory-mask packets. Can conflict with another pack using the same shader channel. |

PacketEvents action detection is independent of the hiding mode. The optional
client mod also masks vanilla hands while it owns an accepted presentation.

## Viewmodel projection

`render.viewmodel.anti-clipping`, `y-lock`, and `fov.locked` generate the appropriate
pack/shader path. `fov.degrees` is the viewmodel FOV, not the player's world FOV.
Source-model adaptation is in memory and does not rewrite your assets.
Pack-affecting settings require generation and a client download.

## Animated camera

Assign the `camera` role in `.armature` v2; old `.bbmodel` assets use `s_camera`.
Only one camera role is allowed. Keep the authored rest rotation `[0, 0, 0]` and
animate small offsets. Do not add a 180-degree yaw: scene compensation is handled
by the native renderer. The editor camera is an authoring preview, not the runtime
camera-bone pose.

The server pack camera path uses a Fabulous post-effect and therefore depends
on graphics mode and compatible resource-pack shaders. The optional Fabric mod
has its own camera/render path; test its selected graphics/shader combination.

`render.camera-rotation.rotation-clamp.maximum-degrees` bounds the received
rotation. Zoom correction crops edges exposed by rotation. `mode: fixed` keeps
a constant zoom; `dynamic` varies it up to `maximum-zoom`. `preserve-fov` widens
the source projection in fixed mode, but the current setting is not compatible
with Sodium and defaults to `false`.

## Verify a setting

Start from a known-good model. Change one group, run config reload and the required
pack rebuild/provider generation, then download the new pack. Compare standing,
walking, looking up/down and both hands. If camera rotation fails, temporarily
disable it to isolate the projection path before changing the bone hierarchy.
See [Client limitations](../client-mod/settings.md) and [Troubleshooting](../troubleshooting.md).
