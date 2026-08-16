# 🛠️ Конфигурация

Armature читает `plugins/Armature/config.yml`. Значения загружаются при запуске и через `/armature reload`.

## Полный `config.yml`

```yaml
input:
  movement-threshold: 0.015
  movement-stop-delay-ticks: 3

debug:
  enabled: false
  tracker-lifecycle: true
  animations: true
  empty-hand-mask: false

render:
  enabled: true
  first-person-only: true
  model-adaptation: true
  vanilla-item-mode: mask
  empty-hand-mask:
    repair-server-materialization: true
  motion:
    sway:
      enabled: true
      maximum-rotation: 4.0
      look:
        lerp-speed: 0.0
        yaw-gain: 0.12
        pitch-gain: 0.10
        roll-gain: 0.05
      movement:
        lerp-speed: 14.0
        yaw-gain: 0.20
        pitch-gain: 0.06
        roll-gain: 0.35
    bob:
      enabled: true
      cycles-per-block: 1.8
      horizontal-amplitude: 0.025
      vertical-amplitude: 0.018
      roll-amplitude: 0.8
      frequency: 9.0
      damping: 0.9
    camera-follow:
      enabled: true
      frequency: 9.0
      damping: 0.9
      yaw-position-gain: 0.0025
      pitch-position-gain: 0.0020
      maximum-offset: 0.035
      pitch-compensation: 0.20

armor-mappings:
  "your_namespace:knight_helmet": knight
```

## Ввод

`input.*`

| Ключ                        | Тип   | Значение                                                                                                                                               |
| --------------------------- | ----- | ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `movement-threshold`        | число | Горизонтальное расстояние за серверный тик, необходимое для определения ходьбы. Меньшие значения замечают слабое движение, большие уменьшают дрожание. |
| `movement-stop-delay-ticks` | целое | Число неподвижных тиков до возврата цикла ходьбы/бега в idle. Большие значения предотвращают мерцание при краткой остановке.                           |

## Отладка

`debug.*`

| Ключ                | Тип     | Значение                                                                                                                                                |
| ------------------- | ------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `enabled`           | boolean | Главный переключатель диагностических логов. В production оставляйте `false`.                                                                           |
| `tracker-lifecycle` | boolean | Логирует создание, восстановление и закрытие BetterModel tracker при включённой отладке.                                                                |
| `animations`        | boolean | Логирует запуск, замену, скорость и завершение анимаций.                                                                                                |
| `empty-hand-mask`   | boolean | Логирует историю выбранного слота, изменения серверного инвентаря и состояние клиентского маскирования. Используйте только при диагностике пустой руки. |

## Рендеринг

`render.*`

| Ключ                                            | Тип     | Значение                                                                                                                                   |
| ----------------------------------------------- | ------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| `enabled`                                       | boolean | Глобальный переключатель рендера Armature. `false` убирает визуальное представление, но оставляет провайдеры и gameplay-плагины активными. |
| `first-person-only`                             | boolean | `true` скрывает модель от третьего лица.                                                                                                   |
| `model-adaptation`                              | boolean | Адаптирует модель для обхода игровых ограничений, но не изменяет саму модель.                                                              |
| `vanilla-item-model`                            | option  | `source-model` изменяет реальную модель предмета, чтобы скрыть руку; `mask` показывает игроку фальшивый предмет. `mask` гибче.             |
| `empty-hand-mask.repair-server-materialization` | boolean | Оставляйте включённым при использовании `mask`, чтобы избежать сбоев инвентаря.                                                            |

## Движение

`render.motion.*`

Все gain — визуальные множители. Углы указаны в градусах, offsets — расстояния в пространстве модели. Слои движения применяются к текущему transform модели.

### Покачивание

`render.motion.sway.*`

| Ключ                  | Значение                                                                                                               |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------- |
| `enabled`             | Включает покачивание от камеры и движения.                                                                             |
| `maximum-rotation`    | Абсолютный предел общей ротации, предотвращающий чрезмерные углы.                                                      |
| `look.lerp-speed`     | Сглаживание покачивания от камеры. `0` применяет camera deltas напрямую; большие значения следуют за целью постепенно. |
| `look.yaw-gain`       | Преобразует yaw взгляда в yaw модели.                                                                                  |
| `look.pitch-gain`     | Преобразует pitch взгляда в pitch модели.                                                                              |
| `look.roll-gain`      | Преобразует движение взгляда в roll модели.                                                                            |
| `movement.lerp-speed` | Сглаживание покачивания от движения. Большие значения быстрее стабилизируются.                                         |
| `movement.yaw-gain`   | Преобразует направление движения в yaw.                                                                                |
| `movement.pitch-gain` | Преобразует движение в pitch.                                                                                          |
| `movement.roll-gain`  | Преобразует движение в roll.                                                                                           |

### Покачивание шагов

`render.motion.bob.*`

| Ключ                   | Значение                                                                            |
| ---------------------- | ----------------------------------------------------------------------------------- |
| `enabled`              | Включает покачивание шагов при движении.                                            |
| `cycles-per-block`     | Колебания на пройденный блок. Большие значения создают более частые и быстрые шаги. |
| `horizontal-amplitude` | Боковое смещение.                                                                   |
| `vertical-amplitude`   | Вертикальное смещение.                                                              |
| `roll-amplitude`       | Roll-угол цикла.                                                                    |
| `frequency`            | Скорость реакции пружины. Большие значения реагируют быстрее.                       |
| `damping`              | Потеря энергии пружины; большие значения уменьшают overshoot.                       |

### Следование за камерой

`render.motion.camera-follow.*`

Camera follow — позиционная инерция, а не ориентация к камере. За направление к игроку по-прежнему отвечает billboard.

| Ключ                  | Значение                                                    |
| --------------------- | ----------------------------------------------------------- |
| `enabled`             | Включает позиционный camera-follow.                         |
| `frequency`           | Скорость реакции позиционной пружины.                       |
| `damping`             | Стабилизирует пружину и ограничивает overshoot.             |
| `yaw-position-gain`   | Локальная трансляция от yaw камеры.                         |
| `pitch-position-gain` | Локальная трансляция от pitch камеры.                       |
| `maximum-offset`      | Максимальная трансляция camera-follow.                      |
| `pitch-compensation`  | Компенсирует вертикальное положение при взгляде вверх/вниз. |

## Пользовательские armor mappings

`armor-mappings.*`

Armature автоматически поддерживает vanilla-броню игрока, но для брони, добавленной плагином, требуется дополнительный шаг. См. [Пользовательская броня](../rasshirennye-vozmozhnosti/polzovatelskaya-bronya.md).

## Рецепты настройки

**Стабильный competitive view:** установите `sway.maximum-rotation` в `2.0~3.0`, уменьшите movement roll и держите camera-follow damping около `0.9`.

**Cinematic view:** постепенно увеличивайте gains взгляда и движения, затем amplitudes bob; limits повышайте только если clipping приемлем.

**Диагностика stutter:** включите `debug.enabled`, `debug.tracker-lifecycle` и `debug.animations`, протестируйте одного игрока и отключите после сбора логов. Настройка движения не исправит tracker, который больше не запланирован.
