---
description: "Variants"
---

# 🎨 Variants

A variant selects a presentation within one active item profile. The first
eligible variant in YAML declaration order wins; no match uses the base.
Variants do not stack. `match.when` gates the whole profile before variant choice.

```yaml
wiki_variant:
  match:
    item: minecraft:stick
  model:
    name: wiki_player
  variants:
    gold:
      when:
        permissions:
          any: [cosmetics.gold, cosmetics.admin]
        all:
          - variable.weapon-skin == gold
      model:
        name: wiki_player_gold
    alternate:
      when: variable.weapon-skin == alternate
      model:
        name: wiki_player_alternate
```

Declare `weapon-skin` globally and provide all three models. Model/physics fields
and attachments merge by key. Supplying an animation category replaces its
whole list; omitting it inherits the base list. Variants may replace model,
physics, animations and accessory presentation.

Selection conditions use external/shared state, not their own animation or
variant/attachment results. Animation conditions can inspect `variant.active`
(`base` or the chosen ID). Mount conditions can use the chosen variant state,
but cannot depend on another mount's active result.

Protected non-cancellable actions defer changes until completion; then the
latest eligible presentation is reevaluated. All variants are asset-validated
before pack publication, including inactive ones. Variable changes therefore
select existing published assets instead of rebuilding a pack.

Use `/armature profile inspect <player>` to see the selected variant. Test no
match, overlapping conditions, permission changes and a change during reload.
