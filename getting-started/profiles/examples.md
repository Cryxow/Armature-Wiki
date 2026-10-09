---
description: "Complete Examples"
---

# Complete Examples

Each example is a complete profile file, not a fragment for `config.yml`.
Enable one matching example at a time and remove overlapping default/tutorial
profiles. Root IDs must be unique throughout your active library.

| Example | Required models/clips |
| --- | --- |
| Sword | Shipped `arm_sword`: base, swing, equip, unequip |
| Empty hands | Shipped `arm_empty`: base |
| Bow | Authored `wiki_player`: base, aim, item-role bone |
| Shield | Authored `wiki_player`: base, block, item-role bone |
| WeaponMechanics/two arms | `arm_rifle` with base, aim, fire, reload as used; actual WM weapon title `m4a1` |
| Accessory | `wiki_player` with charm_socket, `wiki_charm` with a Physics bone, permission |

The [tutorial model](../../.gitbook/assets/examples/wiki_player.armature) contains these demonstration clips/socket. For your own model, the tutorial explains authoring; `aim`/`block`/`charm_socket` must
exist if using those examples. A parser accepting YAML does not create its
missing clips or bones. The provider weapon title is an example, not a supplied
gameplay weapon. Conditions and pack definitions resolve per owning hand.

## Sword

[Download profile](../../.gitbook/assets/examples/sword.yml)

```yaml
wiki_sword:
  match:
    item: minecraft:diamond_sword
  model:
    name: arm_sword
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: player:attack
        animation:
          name: swing
          cooldown: 3t
      - trigger: item:equip
        animation: equip
      - trigger: item:unequip
        animation: unequip
```

## Empty Hands

[Download profile](../../.gitbook/assets/examples/empty-hands.yml)

```yaml
wiki_empty:
  match: {item: minecraft:air}
  model: {name: arm_empty, hide-vanilla-hand: true}
  animations:
    movement:
      - animation: base
```

## Bow

[Download profile](../../.gitbook/assets/examples/bow.yml)

```yaml
wiki_bow:
  match: {item: minecraft:bow}
  model:
    name: wiki_player
    held-item: {mode: source}
  animations:
    movement:
      - animation: base
      - when: item.using == true
        animation: aim
```

## Shield

[Download profile](../../.gitbook/assets/examples/shield.yml)

```yaml
wiki_shield:
  match: {item: minecraft:shield}
  model:
    name: wiki_player
    held-item: {mode: static}
  animations:
    movement:
      - animation: base
      - when: item.using == true
        animation: block
```

## Weaponmechanics

[Download profile](../../.gitbook/assets/examples/weaponmechanics.yml)

```yaml
wiki_rifle:
  match: {item: weaponmechanics:m4a1}
  model: {name: arm_rifle}
  animations:
    movement:
      - animation: base
      - when: weaponmechanics.aiming == true
        animation: aim
    actions:
      - trigger: weaponmechanics:fire
        animation: {name: fire, cooldown: 3t}
      - trigger: weaponmechanics:reload-start
        animation: {name: reload, can-be-cancelled: false}
```

## Two Arms

[Download profile](../../.gitbook/assets/examples/two-arms.yml)

```yaml
wiki_two_arms:
  match: {item: weaponmechanics:m4a1}
  model: {name: arm_rifle}
  animations:
    movement:
      - animation: {name: base, arms: both}
    actions:
      - trigger: weaponmechanics:reload-start
        animation: {name: reload, arms: both, can-be-cancelled: false}
```

## Accessory

[Download profile](../../.gitbook/assets/examples/accessory.yml)

```yaml
wiki_charm:
  type: attachment
  model: {name: wiki_charm}
  physics: {enabled: true, frequency: 3.0, damping: 0.9}

wiki_composite:
  match: {item: minecraft:stick}
  model: {name: wiki_player}
  attachments:
    charm:
      profile: wiki_charm
      attach-to: {bone: charm_socket}
      when:
        permissions:
          any: [cosmetics.charm, cosmetics.admin]
```

[Download the static Physics charm model](../../.gitbook/assets/examples/wiki_charm.armature)
for the accessory example. Copy it beside `wiki_player.armature` and grant
`cosmetics.charm` to the test player. The charm's marked bone is simulated on
the server; Blockbench does not simulate that motion.

## Apply and check

Save under `plugins/Armature/profiles/`, reload profiles and models, rebuild
Armature assets and deliver the provider's final pack. Use profile inspection
to confirm the chosen identity, variant and instances. Test ordinary actions,
protected reload, equip/unequip and both hands before adapting the file.
