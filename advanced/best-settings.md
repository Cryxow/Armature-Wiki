# 🔥 Best settings

Armature uses BetterModel as its production renderer. For a first-person viewmodel, start with this BetterModel configuration:

```yaml
lerp-frame-time: 1
packet-bundling-size: 4
```

`lerp-frame-time: 1` inserts animation keyframes at the smallest supported interval and gives the smoothest interpolation. It can cost more CPU and packets.

`packet-bundling-size: 4` keeps some packet coalescing while reducing visible batching latency. Set it to `0` to disable bundling, but benchmark packet overhead on the target server.

These are starting values, not universal performance guarantees. Test with the real model, player count, resource pack, and network conditions.

## Armature rendering baseline

Armature's default first-person presentation is:

```yaml
render:
  enabled: true
  first-person-only: true
  model-adaptation: true
  anti-clipping: true
  viewmodel-fov-lock: true
  viewmodel-fov-lock-degrees: 90.0
  viewmodel-y-lock: true
  vanilla-item-mode: mask
```

The anti-clipping, FOV-lock, and Y-lock paths use generated resource-pack assets. Reload the pack after changing them:

```
/armature reload pack
```

Use `/armature status` to inspect camera packet and force-update counters. They measure server-side input delivery and BetterModel update requests; they do not measure client frame latency.

## Tuning

* Reduce `render.motion.sway.maximum-rotation` or movement gains when the viewmodel feels too aggressive.
* Keep `render.motion.camera-follow` disabled until basic model placement and sway are correct.
* Use `debug.enabled: true` with only the required diagnostic category while investigating tracker or animation issues.
* Validate on a connected client after a full restart before promoting a renderer change.
