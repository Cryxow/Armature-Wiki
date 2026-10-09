---
description: "Main Hand & Offhand"
---

# Main Hand & Offhand

Armature maintains presentation context for each hand. Selectors, provider item
identities and held-item conditions resolve against the owning hand; event hand
metadata controls dispatch where the provider supplies it.

## Two-arm ownership

Movement and action animation specifications accept `arms: one` (default) or
`arms: both`. A main-hand rule with `both` can own both hand rigs for the duration
of that movement/action. Additive rules do not accept this setting.

```yaml
animations:
  actions:
    - trigger: weaponmechanics:reload
      animation:
        name: reload
        arms: both
        can-be-cancelled: false
```

Use this only with a model/clip authored for that two-arm presentation. A profile
does not obtain a new left animation by adding the option; `.armature` v2 requires
explicit left bones/clips and additive targets. Legacy `.bbmodel` has its separate
offhand adaptation conventions.

## Visual items during switches

`render.dynamic-items.defer-until-equipped: true` retains the outgoing model's
item snapshot during unequip/protected presentation. The incoming presentation
receives current items when equip activates. Inventory selection and gameplay
remain immediate. The same visual rule applies to server renderers and Fabric.

## Verify both hands

Test main/offhand items independently, attack/use with each hand, a two-arm
protected action, rapid hotbar switches, switch to a normal item and reclaim of
the original profile. With the client mod, check accepted bundle takeover and
outgoing cleanup too. [Client fallback](../../client-mod/rendering-fallback.md)
explains why a composition may use server displays instead.
