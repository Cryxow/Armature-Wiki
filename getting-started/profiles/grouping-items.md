# 🛍️ Grouping Items

A profile can map multiple items to one model and one animation set. Armature resolves selectors when profiles load, then every matched item points to the same profile.

## When to use grouping

Use grouping when several items share:

* the same BetterModel model;
* the same animations;

Example: make every vanilla sword use the same \`fp\_sword\` rig.

## The \`items\` field

```yaml
swords:
  items:
    - family: swords
  model: fp_sword
  animations:
    idle: sword_idle
    swing:
      name: sword_swing
      duration-ticks: 8
```

The \`items\` list accepts exact item IDs, item families, and patterns. Every resolved item receives the \`swords\` profile.

Selectors can be mixed:

```yaml
combat_tools:
  items:
    - minecraft:diamond_sword
    - minecraft:netherite_sword
    - family: axes
    - pattern: "*_HOE"
  model: fp_combat
  animations:
    idle: combat_idle
    swing: combat_swing
```

## Selector types

### Exact item

Use the \`namespace:item\` format:

```yaml
items:
  - minecraft:diamond_sword
  - weaponmechanics:m4a1
```

Custom identities can be used when a provider exposes them. Wildcard patterns are currently supported only for vanilla items.

### Vanilla families

Families include vanilla materials classified by Armature:

```yaml
items:
  - family: swords
  - family: pickaxes
  - family: tools
```

Available families include `blocks`, `generic`, `tools`, `swords`, `pickaxes`, `axes`, `shovels`, `hoes`, `bows`, `crossbows`, `food`, `potions`, `projectiles`, and `throwables`.

Plural names are accepted. \`tools\` includes pickaxes, axes, shovels, and hoes.

The short form is also valid:

```yaml
items:
  - swords
  - tools
```

### Vanilla patterns

Patterns use \`\*\` for any number of characters and \`?\` for one character:

```yaml
items:
  - pattern: "*_SWORD"
  - pattern: "minecraft:*_LEAVES"
```

A pattern without a namespace targets Minecraft. Wildcard families and patterns are currently limited to vanilla items.

## Profile-level shortcuts

Instead of putting selectors under \`items\`, use \`families\` and \`patterns\`:

```yaml
vanilla_weapons:
  families:
    - swords
    - bows
  patterns:
    - "*_CROSSBOW"
  model: fp_weapon
  animations:
    idle: weapon_idle
    swing: weapon_swing
```

\`item\`, \`items\`, \`families\`, and \`patterns\` are combined. Duplicate selectors inside one profile are deduplicated.

## Important rules

* Each item can match only one loaded profile.
* If two profiles target the same item, reload is rejected with a duplicate-match error.
* A profile needs at least one selector, a \`model\`, and an \`animations\` section.
* Apply changes with \`/armature reload\`.
* If validation fails, the previous active profile set remains in use.
* Families and patterns expand against the vanilla materials available when profiles load.

## Complete example

```yaml
vanilla_swords:
  items:
    - family: swords
    - pattern: "*_SWORD"
  model: fp_sword
  sway-rate: 0.9
  animations:
    idle: sword_idle
    walk: sword_walk
    swing:
      name: sword_swing
      duration-ticks: 8
```

Start with exact item IDs while testing a model, then expand to a family or pattern after the presentation is correct. After each change, check the reload logs and test one representative item from each group.
