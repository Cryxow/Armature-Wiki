---
description: "Attachments & Composite Models"
---

# Attachments & Composite Models

A composition mounts independent models onto bones in a principal model.
It differs from toggling existing bones through a gameplay-plugin attachment
setting. The native renderer owns this presentation; gameplay state remains
with WeaponMechanics or your own plugin.

```yaml
charm_star:
  type: attachment
  model:
    name: charm_star
  physics:
    enabled: true
    frequency: 3.0
    damping: 0.8
  animations:
    movement:
      - animation: idle
    actions:
      - trigger: weaponmechanics:fire
        animation: flash

scope_long:
  type: attachment
  model:
    name: scope_long
  physics:
    enabled: false
```

Static accessories may omit animations. Each reference creates independent
animation channels, cooldowns and physics springs. Reusable profiles may declare
their own variants and child attachments. They are never selected by held items.

## Mounts and variants

```yaml
m4a1:
  match:
    item: weaponmechanics:m4a1
    when: variable.player-level >= 10
  model:
    name: arm_rifle
  attachments:
    scope:
      profile: scope_long
      attach-to: {parent: base, bone: scope_socket}
      when: weaponmechanics.has-attachment:scope_long == true
    charm:
      profile: charm_star
      attach-to: {bone: charm_socket}
      transform:
        position: [0, 0, 0]
        rotation: [0, 0, 0]
        scale: [1, 1, 1]
      when:
        permissions:
          any: [cosmetics.charm.star, cosmetics.admin]
        all:
          - variable.charm-unlocked == true
  animations:
    movement:
      - animation: base
      - when:
          - weaponmechanics.aiming == true
          - attachment.scope.active == false
        animation: aim
      - when:
          - weaponmechanics.aiming == true
          - attachment.scope.active == true
        animation: aim_scope
    actions:
      - trigger: weaponmechanics:reload
        animation:
          name: reload
          can-be-cancelled: false
  variants:
    gold:
      when:
        permissions:
          any: [cosmetics.rifle.gold, cosmetics.admin]
        all:
          - variable.weapon-skin == gold
      model: {name: arm_rifle_gold}
      attachments:
        charm:
          model: {name: charm_star_gold}
          physics: {damping: 0.9}
```

All named assets, bones and clips in these examples must exist in your models.
`base` names the principal model of the current profile. A mount's parent can
also be a sibling instance ID. Nested profiles use their own local instance IDs;
diagnostics show paths such as `charm/ribbon`. Parent inactivity hides descendants.
Parents are evaluated and mounted before children, regardless of declaration order.

Position uses Blockbench units (16 per block), rotation uses degrees and scale
uses three positive factors. Geometry is mounted in its authored local model
coordinates, without the native compiler's player scene offset. The parent's final
animated/physical pose is composed once before the child's own animation/physics.
Simulation follows the asset's marked physics bones and remains a spring effect
without collisions. Omitted physics settings inherit the global configuration.

`match.when` gates activation. The first matching variant in YAML order wins;
no match uses the base presentation. Variants do not stack. `variant.active` is
`base` or the chosen variant ID in the current profile. `attachment.<id>.active`
describes its direct mounts and is available to animation conditions.

Selection conditions use external state and shared variables. Variant/match
conditions cannot depend on selection or animation results. Mount conditions may
use the chosen `variant.active`, but not another mount's active state. Other
animation conditions retain the existing language.

The `permissions` shortcut accepts nonempty `any`, `all`, `none` lists. Multiple
groups and other criteria in a condition block are combined with AND. Existing
`player.has-permission:<permission>` expressions continue to work.

## Overrides and lifecycle

Apply reusable base → its selected variant → instance overrides. Model/physics
fields and attachment entries merge by key. Each supplied animation category
replaces that whole list; omitted categories inherit. Every mount requires
`profile` and `attach-to.bone` after merging. Instance overrides can replace its
model, physics, animations and children. `enabled: false` disables an inherited
mount. Changing an instance's `profile` applies its overrides to the new reference.

Changes wait while the presentation contains a non-cancellable action. On
completion, reevaluate conditions and apply the latest requested composition.
Item changes, disconnects, disabling and profile/model reloads clean up instances.
WeaponMechanics attachment checks use the owning hand's item; gameplay stays in WM.

Loading links all references before activation. Unknown variables/references,
duplicate IDs, profile/mount cycles, invalid fields and unsupported backend
features reject the library. Reload errors keep the active library. Native asset
validation checks all variants and overrides before pack publication, including
mount bones, clips and additive references. Conditions never rebuild a pack.

`/armature profile inspect <player>` includes the chosen variant and active instance
paths/models. Automated checks must be followed by a full server restart, pack
download and connected-client validation of both hands, aiming, chained mounts,
physics and changes during protected actions.

## Renderer compatibility

Composition/per-instance physics requires the native backend. The current
Fabric hand bundle is not a composite-scene transport, so a composed
presentation uses native server ItemDisplays and its required renderer pack.
Removing composition can return a compatible presentation to client rendering.
Do not describe this server fallback as a missing-model bug.

## Static accessory example

```yaml
wiki_charm:
  type: attachment
  model:
    name: wiki_charm
  physics:
    enabled: true
    frequency: 3.0
    damping: 0.9

wiki_composite:
  match:
    item: minecraft:stick
  model:
    name: wiki_player
  attachments:
    charm:
      profile: wiki_charm
      attach-to:
        bone: charm_socket
      when:
        permissions:
          any: [cosmetics.charm, cosmetics.admin]
      transform:
        position: [0, 0, 0]
        rotation: [0, 0, 0]
        scale: [1, 1, 1]
```

Provide `wiki_charm` and the principal model's `charm_socket` bone. Grant the
permission, rebuild/deliver assets and inspect the active instance path.
Put a Physics flag on the accessory bone to make its enabled spring visible.
