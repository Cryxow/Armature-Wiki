# 📰 Профили

Профили находятся в `plugins/Armature/profiles/*.yml`. Каждая корневая запись — отдельный профиль предмета.

```yaml
# Профиль используется только при пустой основной руке.
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

## Поля профиля

| Поле                | Обязательно | Значение                                                                                                                                    |
| ------------------- | ----------: | ------------------------------------------------------------------------------------------------------------------------------------------- |
| `<profile-id>`      |          да | Корневой ключ YAML. Используйте строчные буквы, цифры, `.`, `_` или `-`.                                                                    |
| `item`              |          да | Разрешённая identity предмета, например `minecraft:diamond_sword` или `weaponmechanics:m4a1`. Для пустой руки используется `minecraft:air`. |
| `model`             |          да | ID модели BetterModel.                                                                                                                      |
| `model-offset`      |         нет | Вертикальный offset модели в model-space. По умолчанию `0.0`.                                                                               |
| `sway-rate`         |         нет | Множитель визуального sway для профиля. По умолчанию `1.0`; `0` отключает вклад этого rig.                                                  |
| `hide-vanilla-hand` |         нет | Скрывает vanilla руку от первого лица. По умолчанию `true`.                                                                                 |
| `animations`        |         нет | Связывает имена действий Armature с animation definitions BetterModel.                                                                      |

## Анимации

Поддерживаемые actions:

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

| Поле       | Значение                                                                       |
| ---------- | ------------------------------------------------------------------------------ |
| `name`     | Точное имя анимации BetterModel.                                               |
| `duration` | Принудительная длительность действия в серверных тиках.                        |
| `speed`    | Множитель воспроизведения, больше `0`.                                         |
| `cooldown` | Минимальная задержка до повторного запуска той же анимации, в серверных тиках. |
| `start`    | **Для loop-анимаций.** Проигрывается перед loop.                               |
| `loop`     | **Для loop-анимаций.**                                                         |
| `end`      | **Для loop-анимаций.** Проигрывается после loop.                               |
| `paused`   | **Для climb action.** Анимация при удержании на лестнице.                      |
| `down`     | **Для climb action.** Анимация при спуске по лестнице.                         |
