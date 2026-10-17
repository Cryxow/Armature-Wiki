---
description: "Shared Variables & PlaceholderAPI"
---

# 🔤 Shared Variables & PlaceholderAPI

Only `plugins/Armature/variables.yml` declares YAML variables:

```yaml
variables:
  weapon-skin:
    type: string
    source: placeholderapi
    placeholder: "%myplugin_weapon_skin%"
    refresh: 10t
    default: classic
  charm-unlocked:
    type: boolean
    source: placeholderapi
    placeholder: "%myplugin_charm_unlocked%"
    refresh: 10t
    default: false
  player-level:
    type: number
    source: placeholderapi
    placeholder: "%player_level%"
    refresh: 10t
    default: 0
```

The placeholder names above are examples, not built-in expansions. Constants,
provider/API sources and `number`, `boolean`, `string`, `duration`, `enum` types
retain their existing syntax. Use `variable.<id>` in any profile without imports.
`placeholder.<id>` remains an alias. Integers use `number`.

Definitions are shared, values are not: caches belong to a player and hand/session.
Mounted models share their owning hand's variable resolution. Only referenced
definitions are resolved. Provider/API values use the current context, including
current event arguments; event data is not reused as a later event's variable value.
Missing/unconvertible external values use the declared default, then the type's
neutral value. A missing file is an empty registry.

Local YAML declarations are rejected, including inside variants and instances.
Move old declarations into the common file manually, resolve conflicting IDs and
remove local blocks. Common declarations remain administrator-owned. Standalone Java
`ArmaturePresentation` requests retain their existing inline-variable contract.

## Sources and types

| Source | Declaration/use |
| --- | --- |
| `constant` | `value` supplies a fixed typed value |
| `placeholderapi` | `placeholder` is resolved through installed PlaceholderAPI/expansions |
| `provider` | Provider/path identifies a namespaced provider value |
| `api` | Path reads the current API/signal context |

Types are `number`, `boolean`, `string`, `duration`, `enum`. Use `number` for
integers too. `clamp` applies to number/duration values. Durations accept ticks,
seconds or numeric ticks. Placeholders shown here are examples, not expansions
bundled with Armature.

## Declaration reference

| Field | Required/default | Meaning |
| --- | --- | --- |
| `type` | Required | `number`, `boolean`, `string`, `duration`, `enum` |
| `source` | `constant` | One source from the table above |
| `value` | Required for constant | Fixed typed scalar; forbidden for other sources |
| `placeholder` | Required for PlaceholderAPI | Placeholder string including `%`; forbidden for other sources |
| `provider` | Required for provider; optional for API | Namespace for lookup; forbidden for other sources |
| `path` | Variable ID when omitted | Current context field for provider/API sources |
| `refresh` | `1t`, at least 1 tick | Placeholder refresh duration; provider/API values read current context |
| `default` | Type's neutral value | Typed fallback when lookup/conversion fails |
| `clamp` | Omitted | Finite ordered `[minimum, maximum]`, only number/duration |

`enum` is a string-valued type, not a separately declared `values` list.
Boolean and numeric constant/default values must use their YAML types.
Provider/API lookup checks current event details, then current context values;
it does not keep arguments from an earlier event.

```yaml
variables:
  rounds:
    type: number
    source: provider
    provider: weaponmechanics
    path: ammo
    default: 0
  input-allowed:
    type: boolean
    source: api
    path: allowed
    default: false
  mode:
    type: enum
    source: constant
    value: semi
  animation-time:
    type: duration
    source: constant
    value: 12t
    clamp: [0, 40]
```

## A complete constant example

Save this as `plugins/Armature/variables.yml`:

```yaml
variables:
  rifle-enabled:
    type: boolean
    source: constant
    value: true
    default: false
  weapon-skin:
    type: string
    source: constant
    value: classic
    default: classic
  player-level:
    type: number
    source: constant
    value: 10
    default: 0
```

Use `variable.rifle-enabled == true` or `variable.player-level >= 10` from any
profile without imports. Reload profiles after changing the registry, then
inspect the player's selected presentation.

Missing/unconvertible external values use `default`, then the type's neutral
value. Event data uses the current event and is not retained for a later event.
Changing a selection variable does not rebuild the pack: all referenced models
must have passed validation and already be present in the published assets.
