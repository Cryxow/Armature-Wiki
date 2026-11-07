---
description: "Skript"
---

# 📜 Skript

Адаптер Skript Armature встроен в дистрибутив `Armature.jar`. Установите `Skript` рядом с Armature. Синтаксис регистрируется только когда доступны публичный API Armature и Skript.

Адаптер вызывает публичный API представления. Он не валидирует оружие, не тратит патроны, не наносит урон и не отменяет игровой событие, вызвавшее эффект Skript.

## Эффекты

```skript
play armature animation "inspect" for player
play armature action "FIRE" for player
start armature loop "AIM" for player
stop armature action for player
stop armature loop for player
send armature signal "myplugin:weapon.fire" to player
play armature animation "inspect" from profile "m4a1" for player
play raw armature animation "fire" for player
start raw armature loop "aim" for player
```

Эффекты соответствуют этим операциям API:

| Синтаксис | Операция API |
| ----------------------------- | ------------------------------------------- |
| `play armature animation` | `playAnimation(player, animation)` |
| `play armature action` | Парсит встроенный `ArmatureAction` |
| `start armature loop` | Выбирает встроенное действие цикла |
| `stop armature action` | `stopAction(player)` |
| `stop armature loop` | `stopLoop(player)` |
| `send armature signal` | `sendSignal(player, signal)` |
| `... from profile ...` | `playAnimation(player, profile, animation)` |
| `play raw armature animation` | `playRawAnimation(player, animation)` |
| `start raw armature loop` | `startRawLoop(player, animation)` |

Используйте именное сообщение, когда профиль содержит современное правило действия:

```yaml
animations:
  actions:
    - trigger: skript:weapon.fire
      animation:
        name: pistol_fire
        duration: 6t
```

```skript
send armature signal "skript:weapon.fire" to player
```

YAML-правило может проверить детали события и установить `consume: true`, но это значение возвращается как метаданные маршрутизации. Оно не автоматически отменяет событие Skript.

## События жизненного цикла

```skript
on armature animation start:
    broadcast "started [armature animation name]"

on armature animation end:
    broadcast "ended [armature animation name]: [armature animation end reason]"

on armature animation complete:
    # This is only a natural finite-animation completion.
    broadcast "completed [armature animation name]"
```

Адаптер предоставляет эти выражения:

* `armature animation profile`
* `armature animation action`
* `armature animation name`
* `armature animation end reason`

Причина завершения доступна на `animation end` и `animation complete`. Это одно из `completed`, `cancelled`, `replaced` или `removed`. Полное событие эмитируется только для `completed`.

## Владение и сбои

Сохраняйте игровую логику в коде Skript владельца:

```skript
on arm swing:
    # Check the weapon, ammo and cooldown in the gameplay script.
    play armature action "FIRE" for player
```

Если игрок офлайн, Armature недоступен или запрошенный профиль/актив отсутствует, у адаптера просто нет операции представления для применения. Для статусов результатов и современной маршрутизации сигналов см. [Публичный API](../public-api.md) и [Встроенные условия](../../nachalo-raboty/profili/conditions.md).


## Заполненный профиль для тестирования адаптера

```yaml
wiki_skript:
  match:
    item: minecraft:iron_sword
  model:
    name: wiki_player
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: skript:weapon.fire
        animation:
          name: inspect
          duration: 12t
```

Создайте `wiki_player` с помощью `base` и `inspect`, используя учебник Blockbench. Возьмите железный меч и отправьте соответствующий сигнал, показанный выше, из скрипта/навыка. Ожидаемый результат: одно действие `inspect`, затем `base`. Общие сигналы скрипта не отменяют игровой событие хоста автоматически. Используйте событие завершения жизненного цикла только для естественного завершения, а планирование последующих игровых действий проводите с помощью обычного механизма безопасного игрока хоста.
