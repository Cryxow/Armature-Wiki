---
description: "Profile Structure"
---

# Profile Structure

Profiles are YAML files below `plugins/Armature/profiles/**/*.yml`. Every root
key is a globally unique profile ID; file/folder names do not scope references.
Modern and legacy files can coexist, but use the modern format for new profiles.

## Minimal item profile

```yaml
wiki_stick:
  match:
    item: minecraft:stick
  model:
    name: wiki_player
```

The `wiki_player` model must exist. A static profile can omit animations; adding
rules selects authored clips rather than defining new model geometry.

## Complete shape

```yaml
wiki_rifle:
  match:
    item: weaponmechanics:m4a1
    when: variable.rifle-enabled == true
  model:
    name: arm_rifle
    offset: 0.0
    sway-rate: 1.0
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: weaponmechanics:fire
        animation:
          name: fire
          cooldown: 3t
    additives:
      - when: player.falling == true
        animation: fall
        playback: loop
        weight: 0.5
```

Declare `rifle-enabled` in [variables.yml](variables.md), and provide all model
clips used above. Placeholder asset names are not built-in capabilities.

## Fields and presentation

| Field | Required | Meaning |
| --- | --- | --- |
| `<profile-id>` | yes | Root ID: lowercase letters, numbers, `.`, `_`, or `-`. |
| `match` | yes | Exact item IDs, item groups, patterns, or a structured matcher. |
| `model.name` | yes | Loaded model ID for the active renderer. |
| `model.offset` | no | Model-space vertical offset; default `0.0`. |
| `model.sway-rate` | no | Per-profile sway multiplier; default `1.0`. |
| `model.hide-vanilla-hand` | no | Hides the original first-person item; default `true`. |
| `model.render-events` | no | Optional render-event allow-list for conditional sessions. |
| `model.held-item` | no | Visual state of the held-item copy: `static`, `source`, or `rules`. Omission preserves existing behavior. |
| `animations.movement[].hide-vanilla-hand` | no | Overrides the model value while that movement rule is selected. |
| `animations.actions[].hide-vanilla-hand` | no | Overrides the movement/model value while that action is active. |
| `type` | no | `item` (default) or reusable `attachment`; see [composite profiles](attachments.md). |
| `animations` | no | Contains `movement`, `actions`, and/or `additives`. |
| `integrations` | no | Provider-owned presentation settings. |

`model.offset` moves the model root in model space. `sway-rate` scales look/movement sway without changing bob or
camera-follow.

`hide-vanilla-hand` controls presentation only. Armature uses the configured
client mask/resource-pack path; the server inventory item is never replaced.
An empty hand can be selected with `match.item: minecraft:air`.

`match.when` gates activation; [variants](variants.md) choose a conditional
presentation within an active profile, and [attachments](attachments.md) mount
reusable model instances. YAML variables belong exclusively in `variables.yml`.
API-created presentations have their own explicit inline-variable contract.

## Load safely

Run `/armature profile reload`, inspect warnings, and use
`/armature profile inspect <player>`. A malformed file is not repaired by reload.
Linking errors such as unknown accessory profiles, cycles, unknown variables
or invalid selection conditions reject the candidate library; the active
library remains in place. Model/clip/mount validation also covers inactive variants.

New model references require model/pack generation and delivery, not just a
profile reload. See [Complete examples](examples.md) and [Upgrade](../upgrading.md).
