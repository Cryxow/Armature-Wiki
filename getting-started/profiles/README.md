# 📰 Profiles

Profiles live in `plugins/Armature/profiles/*.yml`. Every root entry is an independent item profile.

```yaml
# Profile used only while main hand is empty.
empty_hands:
  item: minecraft:air
  model: empty_hands
  hide-vanilla-hand: true
  sway-rate: 1.0
  #render-events: [climb, sprint, swing]

  animations:
    idle: base
    sprint: 
      start: sprint_start
      loop: sprint
    swing: 
      name: swing
      cooldown: 6
    mine: swing
    climb:
      loop: climb_up
      paused: climb_hold
      down: climb_down
    equip: equip
    unequip: unequip
    crouch: 
      #start: sneak_start
      loop: sneak
```

## Profile fields

| Field               | Required | Meaning                                                                                                                                  |
| ------------------- | -------: | ---------------------------------------------------------------------------------------------------------------------------------------- |
| `<profile-id>`      |      yes | YAML root key. Use lowercase letters, digits, `.`, `_` or `-`.                                                                           |
| `item`              |      yes | Resolved item identity, such as `minecraft:diamond_sword` or `weaponmechanics:m4a1`. `minecraft:air` is used for the empty-hand profile. |
| `model`             |      yes | BetterModel model id.                                                                                                                    |
| `model-offset`      |       no | Vertical model offset in model-space units. Default `0.0`.                                                                               |
| `sway-rate`         |       no | Per-profile multiplier for visual sway. Default `1.0`; `0` disables sway contribution for that rig.                                      |
| `hide-vanilla-hand` |       no | Hides the vanilla first-person hand for this profile. Default `true`.                                                                    |
| `animations`        |       no | Map from Armature action names to BetterModel animation definitions.                                                                     |

## Animations

Every accepted actions:

```
equip
unequip

idle
walk
sprint
crouch

jump
land
fall

climb
climb-hold
climb-down

swing
mine
place
use

eat
drink
throw

block
bow-draw
crossbow-charge
trident-charge

--- WeaponMechanics only ---

fire
aim
aim-start
aim-exit
aim-fire

reload-start
reload-phase
reload-complete
reload-cancel
```

| Field      | Meaning                                                                   |
| ---------- | ------------------------------------------------------------------------- |
| `name`     | Exact BetterModel animation name.                                         |
| `duration` | Forced action lifetime in server ticks.                                   |
| `speed`    | Playback multiplier, greater than `0`.                                    |
| `cooldown` | Minimum delay before replaying the same animation again, in server ticks. |
| `start`    | **For looping animations.** Play an animation before the loop.            |
| `loop`     | **For looping animations.**                                               |
| `end`      | **For looping animations.** Play an animation at the end of the loop.     |
| `paused`   | **For climbing action.** Animation when sneaking in a ladder.             |
| `down`     | **For climbing action.** Animation when climbing down a ladder.           |
