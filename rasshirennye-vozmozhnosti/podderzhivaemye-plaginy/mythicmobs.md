---
description: "MythicMobs"
---

# 🐉 MythicMobs

Адаптер MythicMobs Armature встроен в `Armature.jar`. Установите `MythicMobs` рядом с Armature. Он регистрирует свои механики и триггеры жизненного цикла только когда доступны публичный API Armature и MythicMobs.

Все механики нацелены на игрока и вызывают API представления Armature. Сохраняйте снаряды, урон, патроны, время перезарядки и другие игровые механики в навыке MythicMobs.

## Механики

```yaml
PistolFire:
  Skills:
    - armatureaction{action=FIRE} @self
    - armatureanimation{animation=inspect} @self
    - armaturesignal{signal=mythicmobs:weapon.fire} @self

AimStart:
  Skills:
    - armatureloop{action=AIM} @self

AimStop:
  Skills:
    - armatureloop{stop=true} @self
```

| Механика | Основная конфигурация | Эффект |
| ----------------------------------------------- | ---------------------------------------------------- | ----------------------------------------------------- |
| `armatureaction` (`armatureactionplay`) | `action` или `a` | Играет встроенную `ArmatureAction` |
| `armatureanimation` (`armatureanim`) | `animation`, или `action`/`a`. Опционально `profile`/`p` | Играет анимацию активного профиля или явного профиля |
| `armatureloop` (`armaturestartloop`) | `action`/`a`, или `stop`/`s` | Выбирает встроенный цикл или останавливает выбранный цикл |
| `armaturerawanimation` (`armatureanimationraw`) | `animation`/`a` | Играет сырой актив из активного рендеренной модели |
| `armaturerawloop` (`armatureloopraw`) | `animation`/`a`, или `stop`/`s` | Выбирает или останавливает сырой цикл |
| `armaturesignal` (`armatureemit`) | `signal`, `event`, или `s` | Маршрутизирует современный настроенный триггер |
| `armaturestopaction` (`armaturecancelaction`) | нет | Отменяет текущую одноразовую анимацию |
| `armaturestoploop` (`armaturecancelloop`) | нет | Останавливает выбранный цикл |

Используйте цель игрока, например `@self`, `@target` или `@trigger`, если эта сущность является игроком. Неверная цель или конфигурация приводят к стандартному результату неудачи навыка MythicMobs.

## Триггеры жизненного цикла

Адаптер регистрирует:

| Канонический триггер | Алиасы |
| ----------------------------- | ---------------------------------------------------------- |
| `ARMATURE_ANIMATION_START` | `armatureanimationstart`, `armature-animation-start` |
| `ARMATURE_ANIMATION_END` | `armatureanimationend`, `armature-animation-end` |
| `ARMATURE_ANIMATION_COMPLETE` | `armatureanimationcomplete`, `armature-animation-complete` |

Пример:

```yaml
Triggers:
  - ARMATURE_ANIMATION_END
```

Метаданные триггера содержат:

* `armature.profile`
* `armature.action`
* `armature.animation`
* `armature.reason`
* `armature.token`

`ARMATURE_ANIMATION_END` срабатывает при завершении каждой конечной анимации. `ARMATURE_ANIMATION_COMPLETE` срабатывает только когда анимация достигает своего естественного конца.

## Современные правила сигналов

Используйте `armaturesignal`, когда профиль владеет маппингом:

```yaml
WeaponFire:
  Skills:
    - armaturesignal{signal=mythicmobs:weapon.fire} @self
```

```yaml
animations:
  actions:
    - trigger: mythicmobs:weapon.fire
      condition:
        all:
          - player.on-ground == true
          - weaponmechanics.ammo > 0
      animation:
        name: fire
        duration: 5t
```

Динамические значения правой руки в условиях требуют префикса `$`. Например, `weaponmechanics.ammo == $weaponmechanics.magazine-size` сравнивает два значения времени выполнения. Без `$` строка правой руки в виде пути воспринимается как литерал.

См. [Встроенные триггеры](../../nachalo-raboty/profili/triggers.md), [Встроенные условия](../../nachalo-raboty/profili/conditions.md) и [Публичный API](../public-api.md).


## Заполненный профиль для тестирования адаптера

```yaml
wiki_mythicmobs:
  match:
    item: minecraft:iron_sword
  model:
    name: wiki_player
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: mythicmobs:weapon.fire
        animation:
          name: inspect
          duration: 12t
```

Создайте `wiki_player` с `base` и `inspect`, используя учебник Blockbench. Возьмите железный меч и отправьте соответствующий сигнал, показанный выше, из скрипта/навыка. Ожидаемый результат: одно действие `inspect`, затем `base`. Общие сигналы скрипта не отменяют игровой событие хоста автоматически. Используйте событие завершения жизненного цикла только для естественного завершения, а планирование последующих действий игры используйте через обычный механизм безопасного взаимодействия игрока хоста.
