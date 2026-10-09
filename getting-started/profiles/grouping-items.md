---
description: "Matching & Grouping Items"
---

# Matching & Grouping Items

An exact selector is compact:

```yaml
match:
  item: minecraft:diamond_sword
```

Groups and patterns are supported:

```yaml
match:
  items:
    - family: blocks
    - "*_LEAVES"
    - minecraft:glass
```

Equivalent dedicated keys are `families` and `patterns`. Structured matching
does not require a provider:

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

Selector matches can exclude exact items, families, or patterns:

```yaml
match:
  families:
    - swords
  exclude:
    items:
      - minecraft:diamond_sword
    families:
      - shield
    patterns:
      - "*_AXE"
```

`exclude.items` accepts exact item IDs, `family:` selectors, and `pattern:`
selectors. All exclusions are applied after inclusion, and an exclusion wins.
Structured `item` matching can only use Minecraft material exclusions and
cannot be combined with inclusion `items`, `families`, or `patterns`.
Duplicate item matches and duplicate profile IDs make the
containing file invalid. Startup and reload both warn and skip invalid files;
valid files remain active. A reload never disables the plugin because of one
bad profile file.

## Activation and permissions

`match.when` can gate an otherwise matching profile using external state or
shared variables. Use `player.has-permission:<node>` or a `permissions` block
with `any`, `all`, `none`. Selection conditions cannot depend on that selection's
own animation/variant/attachment results.

Profile access can be disabled for selected players without changing the
profile file. Grant `armature.disabled` to disable all Armature presentation for
the player, or grant `armature.disabled.<profile-id>` to disable only one
profile. For the profile above, the targeted permission is
`armature.disabled.m4a1`.

The suffix is the profile's lowercase YAML root key. The permissions default to
false and are intended as opt-out nodes managed by LuckPerms or another
permission plugin. Removing a node lets the profile resume on the next player
tick; gameplay and the held item are never modified. As a persistent command
alternative, use `/armature profile disable <player> <profile>` and
`/armature profile enable <player> <profile>`.

## Check the chosen identity

Provider namespaces identify real custom items: `itemsadder:...`, `nexo:...`,
`craftengine:...`, `weaponmechanics:...`. A translated display name is not an ID.
Use `/armature profile inspect <player>` with the actual held item, then test the
other hand. Avoid overlapping exact matches in examples; model composition
references profile IDs and does not use the containing folder as a namespace.
