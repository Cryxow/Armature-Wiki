# 🛍️ Grouping Items

A modern profile can map several items to one BetterModel model and one set of animation rules. All selectors live under `match`.

## Exact items

Use `match.items` for a list of exact identities:

```yaml
vanilla_weapons:
  match:
    items:
      - minecraft:diamond_sword
      - minecraft:netherite_sword
      - weaponmechanics:m4a1
  model:
    name: fp_weapon
  animations:
    actions:
      - trigger: player:attack
        animation:
          name: sword_swing
          duration: 8t
```

Provider item identities keep their namespace. For example, a WeaponMechanics title is matched as `weaponmechanics:m4a1`, while an ItemsAdder or CraftEngine identity uses the namespace returned by that provider. Nexo ids use the explicit `nexo:` namespace.

## Families

Use `match.families` for Armature's vanilla item categories:

```yaml
vanilla_tools:
  match:
    families:
      - swords
      - tools
  model:
    name: fp_tool
```

Supported family names are: `blocks`, `generic` or `items`, `tools`, `pickaxes`, `axes`, `shovels`, `hoes`, `swords`, `shields`, `bows`, `crossbows`, `tridents`, `food`, `drinks`, `throwables`, and `firearms`. Singular forms are accepted for category names; `tools` includes pickaxes, axes, shovels, and hoes.

## Patterns

Use `match.patterns` for Minecraft material-name patterns. `*` matches any number of characters and `?` matches one character:

```yaml
vanilla_blades:
  match:
    patterns:
      - "*_SWORD"
      - "minecraft:*_AXE"
  model:
    name: fp_blade
```

Wildcard patterns currently expand Minecraft materials only. A provider item should use its exact namespaced identity.

## Mixed selectors and exclusions

Positive selector forms can be combined:

```yaml
combat_tools:
  match:
    items:
      - minecraft:diamond_sword
      - family: axes
      - pattern: "*_HOE"
    families:
      - bows
    patterns:
      - "*_CROSSBOW"
  model:
    name: fp_combat
```

Remove exact items, families, or patterns with `match.exclude`:

```yaml
combat_tools:
  match:
    families: [swords]
    exclude:
      items:
        - minecraft:golden_sword
      patterns:
        - "*_NETHERITE"
  model:
    name: fp_combat
```

Structured item matching is different: use a mapping under `match.item` when material, custom model data, item model, PDC, or custom tags must be checked. It cannot be combined with positive selectors, but it can use `exclude`; those exclusions must be Minecraft items.

## Rules

* Every profile needs `match` and `model.name`.
* An `animations` section is optional when a Java, Skript, MythicMobs, or Denizen integration uses raw model playback.
* Each item can resolve to only one profile.
* Duplicate selectors inside one profile are deduplicated.
* A duplicate match across profiles rejects the new profile set.
* Families and patterns expand when profiles load.
* Apply changes with `/armature reload profiles`.

See [Profiles](./) for the complete modern schema and [Built-in conditions](conditions.md) for rule selection.
