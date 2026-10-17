---
description: "Motion & Physics"
---

# 🌊 Motion & Physics

Procedural motion is composed with authored animations. Start from the embedded
defaults, then change one layer at a time. A setting that feels right for a rifle
may be excessive for a lightweight item.

## Sway

`render.motion.sway` adds look/movement rotation. Separate `look` and `movement`
gains control yaw, pitch and roll. Signed gains reverse the response. `lerp-speed`
controls smoothing; look `0.0` removes look interpolation. `maximum-rotation`
limits the combined effect. `affects-camera-bone` decides whether sway reaches
the camera bone; individual movement rules can override `sway-rate`.

## Bob and camera follow

Bob uses distance travelled, `cycles-per-block`, horizontal/vertical amplitude
and roll amplitude. Frequency and damping tune its spring response.
Camera follow uses positional yaw/pitch gains, maximum offset and pitch
compensation. Enable a layer before adjusting its parameters; disabled layers
do not contribute.

## Physics

Native physical bones use spring motion from acceleration and turning.
`.armature` v2 uses the explicit **Physics** property; legacy `.bbmodel` uses
`phy_*`. Physics is independent of visual role and is not a collision simulation.
An accessory can override physics settings per instance.

| Parameter | Effect |
| --- | --- |
| `enabled` | Activate simulation for marked bones |
| `acceleration-gain` | Response to movement acceleration |
| `maximum-angle` | Limit swing angle |
| `frequency` | Response speed |
| `damping` | Resistance to oscillation; near 1 settles without rebound |
| `turn-gain` | Added response to view turning |

## Tuning recipes

- **Heavy accessory:** lower frequency and reduce turn gain; keep damping near
  1 if stopping causes a snap/rebound.
- **Quiet camera:** reduce look gains and disable camera sway before adding
  camera-bone animation.
- **Walking motion only:** enable bob while disabling look sway and camera follow.
- **Find an alignment issue:** disable procedural layers temporarily and compare
  the model's rest pose. Restore desired layers after fixing pivots.

Apply these settings with `/armature config reload`. Rebuild/download only when
also changing a pack-affecting option. Test on the client that will actually be
used: server-tick rendering and the optional frame-rate client renderer can feel
different. [Physics authoring](../getting-started/models/physics.md) explains pivots.
