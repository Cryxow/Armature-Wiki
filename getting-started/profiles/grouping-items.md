# Grouping items

A modern profile can map several items to one BetterModel model and one set of
animation rules. All selectors live under <code>match</code>.

## Exact items

Use <code>match.items</code> for a list of exact identities:

~~~yaml
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
~~~

Provider item identities keep their namespace. For example, a WeaponMechanics
title is matched as <code>weaponmechanics:m4a1</code>, while an ItemsAdder or
CraftEngine identity uses the namespace returned by that provider. Nexo ids
use the explicit <code>nexo:</code> namespace.

## Families

Use <code>match.families</code> for Armature's vanilla item categories:

~~~yaml
vanilla_tools:
  match:
    families:
      - swords
      - tools
  model:
    name: fp_tool
~~~

Supported family names are:
<code>blocks</code>, <code>generic</code> or <code>items</code>,
<code>tools</code>, <code>pickaxes</code>, <code>axes</code>,
<code>shovels</code>, <code>hoes</code>, <code>swords</code>,
<code>shields</code>, <code>bows</code>, <code>crossbows</code>,
<code>tridents</code>, <code>food</code>, <code>drinks</code>,
<code>throwables</code>, and <code>firearms</code>. Singular forms are
accepted for category names; <code>tools</code> includes pickaxes, axes,
shovels, and hoes.

## Patterns

Use <code>match.patterns</code> for Minecraft material-name patterns.
<code>*</code> matches any number of characters and <code>?</code> matches one
character:

~~~yaml
vanilla_blades:
  match:
    patterns:
      - "*_SWORD"
      - "minecraft:*_AXE"
  model:
    name: fp_blade
~~~

Wildcard patterns currently expand Minecraft materials only. A provider item
should use its exact namespaced identity.

## Mixed selectors and exclusions

Positive selector forms can be combined:

~~~yaml
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
~~~

Remove exact items, families, or patterns with <code>match.exclude</code>:

~~~yaml
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
~~~

Structured item matching is different: use a mapping under
<code>match.item</code> when material, custom model data, item model, PDC, or
custom tags must be checked. It cannot be combined with positive selectors,
but it can use <code>exclude</code>; those exclusions must be Minecraft items.

## Rules

* Every profile needs <code>match</code> and <code>model.name</code>.
* An <code>animations</code> section is optional when a Java, Skript,
  MythicMobs, or Denizen integration uses raw model playback.
* Each item can resolve to only one profile.
* Duplicate selectors inside one profile are deduplicated.
* A duplicate match across profiles rejects the new profile set.
* Families and patterns expand when profiles load.
* Apply changes with <code>/armature reload profiles</code>.

See [Profiles](README.md) for the complete modern schema and
[Built-in conditions](conditions.md) for rule selection.
