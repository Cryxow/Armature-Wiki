---
description: "Denizen"
---

# 🤖 Denizen

Адаптер Denizen встроен в `Armature.jar`. При установке Denizen и наличии публичного API Armature адаптер регистрирует один нативный `armature`-команду и семейство событий жизненного цикла анимации.

Адаптер выполняет только операции представления. Denizen или другая игровая плагин остаются ответственными за предметы, патроны, урон, таймеры перезарядки и отмену событий.

## Синтаксис команды

```
armature [action/animation/raw_animation/loop/raw_loop/stop_action/stop_loop/signal] (<value>) (profile:<name>) (targets:<player>|...)
```

Команда по умолчанию использует контекст игрока скрипта-ввода. Используйте `targets:|...` для выбора одного или нескольких конкретных игроков. Каждый конкретный цель должен быть игроком.

| Операция | Значение | Опциональные аргументы |
| --------------------- | -------------------------- | ------------------ |
| `action` | ID встроенного действия | нет |
| `animation` или `anim` | ID анимации | `profile:` |
| `raw_animation` | ID актива модели | нет |
| `loop` | ID встроенного цикла | нет |
| `raw_loop` | ID актива цикла модели | нет |
| `stop_action` | нет | нет |
| `stop_loop` | нет | нет |
| `signal` | Триггер с пространственным именем | нет |

Команда также принимает тире-разделенные и компактные алиасы для операций raw и stop: `raw-animation`, `rawanimation`, `raw-loop`, `rawloop`, `stop-action`, `stopaction`, `stop-loop`, `stoploop`, `send_signal` и `send-signal`.

Примеры:

```
- armature action FIRE
- armature animation inspect
- armature animation inspect profile:m4a1
- armature raw_animation fire
- armature loop AIM
- armature raw_loop aim
- armature stop_action
- armature stop_loop
- armature signal denizen:weapon.fire
```

`profile:` поддерживается только для `animation`. Для каждой операции, кроме `stop_action` и `stop_loop`, требуется значение.

## События жизненного цикла

```
on armature animation starts:
  - narrate "started <context.animation> for <context.player>"

on armature animation ends:
  - narrate "ended with <context.reason>"

on armature animation completes:
  - narrate "naturally completed <context.animation>"
```

Доступные контексты:

* `profile`
* `action`
* `animation`
* `reason`
* `phase`
* `token`
* `player`

У `starts` пустая причина. `ends` сообщает о `completed`, `cancelled`, `replaced` или `removed`. `completes` излучается только по естественной причине `completed`.

## Современные правила сигналов

Команда может направить сигнал на анонимное современное правило действия:

```yaml
animations:
  actions:
    - trigger: denizen:weapon.fire
      animation:
        name: fire
        duration: 5t
```

```
- armature signal denizen:weapon.fire
```

Для всех доступных полей триггера, условий и поведения выбора действия см. [Встроенные триггеры](../../nachalo-raboty/profili/triggers.md), [Встроенные условия](../../nachalo-raboty/profili/conditions.md) и [Публичный API](../public-api.md).


## Заполните профиль для тестирования адаптера

```yaml
wiki_denizen:
  match:
    item: minecraft:iron_sword
  model:
    name: wiki_player
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: denizen:weapon.fire
        animation:
          name: inspect
          duration: 12t
```

Создайте `wiki_player` с `base` и `inspect`, используя учебник Blockbench. Возьмите железный меч и отправьте соответствующий сигнал, показанный выше, из скрипта/навыка. Ожидаемый результат: одно действие `inspect`, затем `base`. Общие сигналы скрипта не отменяют игровой событие хоста автоматически. Используйте событие завершения жизненного цикла только для естественного завершения, а планирование последующих действий в игре используйте обычный механизм безопасного игрока хоста.
