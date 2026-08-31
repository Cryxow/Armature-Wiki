# 📰 Profiles

Profiles are loaded from `plugins/Armature/profiles/*.yml`. Each root entry is one profile. Armature 1.3.0 can load legacy profiles and modern profiles in the same server, but new profiles should use the modern format.

Armature owns the first-person presentation only. The matched item, ammo, damage, cooldowns, reload logic, and inventory remain owned by Minecraft or by the gameplay plugin that provides the item.

## Minimal profile

```yaml
m4a1:
  match:
    item: weaponmechanics:m4a1

  model:
    name: fp_rifle
    offset: -0.25
    sway-rate: 0.75
    hide-vanilla-hand: true

  animations:
    movement:
      # IDLE
      - when:
          all:
            - player.on-ground == true
            - player.moving == false
        animation: armature_idle

      # WALK
      - when:
          all:
            - player.on-ground == true
            - player.moving == true
            - player.sprinting == false
        animation:
          name: armature_walk
          speed: 1.0

    actions:
      # VANILLA ATTACK
      - trigger: player:attack
        condition: player.using-item == false
        animation:
          name: [swing, swing_1, swing_2]
          selection: random
          duration: 6t
          cooldown: 3t

      # FIRING A WEAPONMECHANICS GUN
      - trigger: weaponmechanics:fire
        animation:
          name: fire
          duration: 4t
```

The entries in `movement`, `actions`, and `additives` are anonymous rules. Their YAML position is their declaration order; there is no second named animation-definition layer in the modern format.

## Profile fields

| Field                     | Required | Accepted value                                                                                 |
| ------------------------- | -------- | ---------------------------------------------------------------------------------------------- |
| `<profile-id>`            | yes      | Normalized to lowercase; after normalization it must match `[a-z0-9][a-z0-9._-]*`.             |
| `match`                   | yes      | `item`, `items`, `families`, `patterns`, optional `exclude`, or one structured `item` matcher. |
| `model.name`              | yes      | BetterModel model id.                                                                          |
| `model.offset`            | no       | Numeric model-space vertical offset; default `0.0`.                                            |
| `model.sway-rate`         | no       | Non-negative per-profile sway multiplier; default `1.0`.                                       |
| `model.hide-vanilla-hand` | no       | Boolean; default `true`. Presentation only.                                                    |
| `model.render-events`     | no       | Legacy/built-in render-event allow-list accepted by the profile loader.                        |
| `variables`               | no       | Typed constants, provider/API values, or PlaceholderAPI values.                                |
| `animations`              | no       | `movement`, `actions`, and/or `additives` anonymous-rule lists.                                |
| `integrations`            | no       | Currently `weaponmechanics` provider configuration.                                            |

If `animations` is present, it must contain at least one valid movement, action, or additive rule. A profile containing only `match` and `model` is valid and is useful when a Java, Skript, MythicMobs, or Denizen integration plays raw model assets through the public API.

Modern profiles reject unknown properties. In particular, do not add `animations.definitions`, top-level `actions`, top-level `triggers`, or an action `mode` field. Those shapes belong to older drafts or the legacy loader.

## Matching items

### Exact identities

Use a namespaced item identity. Provider identities retain their provider namespace:

```yaml
match:
  item: minecraft:diamond_sword
```

```yaml
match:
  item: weaponmechanics:m4a1
```

The matching identity can come from the built-in vanilla provider or an optional item provider such as WeaponMechanics, ItemsAdder, CraftEngine, or Nexo. The original Minecraft material remains available separately as `item.main-hand.material`.

### Lists, families, and patterns

`items` accepts exact strings and selector mappings:

```yaml
match:
  items:
    - minecraft:diamond_sword
    - family: swords
    - pattern: "*_CROSSBOW"
```

The equivalent profile-level shortcuts are `families` and `patterns`:

```yaml
match:
  families: [swords, axes]
  patterns: ["minecraft:*_HOE"]
```

A string in `items` is treated as an exact identity unless it is written as a family/pattern selector by the legacy selector resolver. Families and wildcard patterns are intended for vanilla materials; provider items should normally use their exact namespaced identity.

### Structured item matching

Use a mapping under `match.item` when material and item data must match:

```yaml
match:
  item:
    material: minecraft:paper
    data:
      custom-model-data: 12001
      item-model: "myplugin:rifle"
      pdc:
        "myplugin:variant": rifle
      tags:
        CustomTag: value
```

`material` is required and must use the `minecraft` namespace. The optional `data` section supports `custom-model-data`, `item-model`, `pdc`, and `tags`. A structured matcher cannot be combined with `items`, `families`, or `patterns`.

Use `match.exclude` to remove vanilla exact items, families, or patterns from the selector result:

```yaml
match:
  families: [swords]
  exclude:
    items:
      - minecraft:golden_sword
    patterns:
      - "*_NETHERITE"
```

Structured `match.item` can use `exclude`, but structured-match exclusions must resolve to Minecraft items.

Every item may resolve to only one profile. Duplicate profile ids, duplicate item matches, malformed selectors, and invalid YAML are reported on startup or reload. A failed profile set does not silently replace the last valid active set.

## Animation specifications

Each rule's `animation` is either one asset name or a mapping:

```yaml
animation:
  name: [swing, swing_1, swing_2]
  selection: sequence
  speed: 1.0
  duration: 6t
  cooldown: 3t
  can-be-cancelled: true
```

| Property           | Movement       | Action   | Additive | Meaning                                                               |
| ------------------ | -------------- | -------- | -------- | --------------------------------------------------------------------- |
| `name`             | required       | required | required | One non-empty asset name or a list of names.                          |
| `selection`        | optional       | optional | optional | `sequence` or `random`; default `sequence`.                           |
| `speed`            | optional       | optional | optional | Finite playback multiplier greater than zero; default `1.0`.          |
| `sway-rate`        | optional       | invalid  | invalid  | Movement-only override of the profile sway multiplier.                |
| `loop`             | must be `true` | invalid  | invalid  | Movement rules are persistent loops.                                  |
| `start`            | optional       | invalid  | invalid  | Asset played when this movement rule becomes active.                  |
| `end`              | optional       | invalid  | invalid  | Asset played when this movement rule is left.                         |
| `duration`         | invalid        | optional | invalid  | Fallback finite-action duration.                                      |
| `cooldown`         | invalid        | optional | invalid  | Minimum delay after a successful action; default `3t`.                |
| `can-be-cancelled` | invalid        | optional | invalid  | Whether another action may replace the active action; default `true`. |

Durations accept non-negative numeric ticks or strings such as `10t` and `0.3s`. Seconds are converted to ticks and rounded up. A configured action duration, when present, must be at least `1t`. The loaded BetterModel clip length is used when it is available; the profile duration is the fallback.

`sequence` advances through a list after successful dispatches. `random` chooses a variant independently for each successful dispatch. All variants in one rule share that rule's cooldown.

## Movement rules

Movement rules use `when` and are evaluated continuously. The most specific matching condition wins; declaration order breaks ties. Use transitions only for one-shot entry/exit clips; use the movement asset itself for the persistent loop.

```yaml
animations:
  movement:
    - when:
        all:
          - player.climbing == true
          - player.climbing-direction == up
      animation:
        name: climb_up
        start: climb_start

    - when:
        all:
          - player.climbing == true
          - player.climbing-direction == down
      animation: climb_down

    - when: player.falling == true
      animation:
        name: fall
        start: fall_start
        end: fall_end
```

See [Built-in conditions](conditions.md) for every available value and operator.

## Action rules

Action rules use a namespaced `trigger` and an optional `condition`:

```yaml
animations:
  actions:
    - trigger: block:place
      condition: item.main-hand.type == minecraft:stone
      animation:
        name: block_place
        duration: 8t

    - trigger: weaponmechanics:reload-phase
      condition:
        all:
          - event.argument.final-round == true
          - weaponmechanics.ammo == $weaponmechanics.magazine-size
      animation:
        name: reload_done
        duration: 16t
      consume: false
```

Modern action playback is always the exclusive finite action layer. There is no `mode` property. `can-be-cancelled: false` protects the active action from replacement; it does not create an overlay. Additives are the separate composed layer.

`consume: true` requests cancellation only from an event owner that supports cancellation, such as a vanilla right-click or the WeaponMechanics reload event. For Java/API/Skript/MythicMobs/Denizen signals, it is returned in the `ArmatureSignalResult`; the caller still owns gameplay cancellation.

## Additive rules \[EXPERIMENTAL]

Additives apply a weighted delta on top of the current base/action pose:

```yaml
animations:
  additives:
    - when: player.moving == true
      animation:
        name: breathing
        speed: 1.0
      playback: loop
      group: locomotion
      priority: 10
      bones: [body, right_arm, left_arm]
      channels: [position, rotation]
      weight:
        parameter: variable.stamina
        input: [0.0, 100.0]
        output: [1.0, 0.15]
        easing: smoothstep
        clamp: true
      blend-in: 4t
      blend-out: 6t
      position-gain: [1.0, 1.0, 0.5]
      rotation-gain: 1.0
      reference:
        animation: armature_idle
        time: 0t
```

{% hint style="warning" %}
Additive animations might not always work or can look bad.
{% endhint %}

The accepted additive rule fields are `when`, `animation`, `playback`, `group`, `priority`, `weight`, `blend-in`, `blend-out`, `bones`, `channels`, `position-gain`, `rotation-gain`, and `reference`.

`playback` is `loop`, `hold`, or `once`. `weight` is a non-negative number or a mapping with `parameter`, two-value `input` and `output` ranges, optional `easing`, and optional `clamp`. Gains are a scalar or `[x, y, z]`. If `channels` is omitted, both position and rotation are composed. A `group` allows one additive to win over another using priority, condition specificity, then declaration order.

Conceptually, the rendered pose is:

```
final pose = base pose + sum(additive delta * resolved weight)
```

## Variables

Variables are declared once and exposed as `variable.<id>` during condition evaluation. They also have a `placeholder.<id>` compatibility alias:

```yaml
variables:
  stamina:
    type: number
    source: placeholderapi
    placeholder: "%player_stamina%"
    refresh: 2t
    default: 100.0
    clamp: [0.0, 100.0]
```

Supported types are `number`, `boolean`, `string`, `duration`, and `enum`. Sources are:

| Source               | Required fields                      | Value resolution                                                       |
| -------------------- | ------------------------------------ | ---------------------------------------------------------------------- |
| `constant` (default) | `value`                              | Uses the declared literal.                                             |
| `placeholderapi`     | `placeholder`                        | Resolves the placeholder at the configured refresh interval.           |
| `provider`           | `provider`                           | Reads a provider namespace, optionally using `path`.                   |
| `api`                | optional `provider`, optional `path` | Reads details from the current API/provider signal or current context. |

`refresh` is a non-negative duration and defaults to `1` tick. `clamp` is `[min, max]` and is valid only for `number` and `duration` variables. Missing optional sources use the declared `default`, then the neutral value for the declared type.

## Editor overrides

Operators with `armature.command.editor` can run `/armature editor` while holding an item assigned to a profile. The Paper Dialog editor changes selected BetterModel bone position and rotation live, then can save an item-specific or profile-wide override to `plugins/Armature/editor-overrides.yml`.

Overrides change presentation only. See [Commands & Permissions](../commands-and-permissions.md) for the permission and save scope.

## Migrating legacy profiles

Generate a modern copy without overwriting or activating the source:

```
/armature migrate-config <file> [--output <file>]
```

LLM-powered migration is enabled by default, it can produce errors or inaccuracies; the candidate is still schema-validated and requires manual review. The default output is `<file>.migrated.yml`. A migration never changes the source file and does not reload the result automatically.

For the complete condition and trigger vocabulary, continue with [Built-in conditions](conditions.md) and [Built-in triggers](triggers.md).
