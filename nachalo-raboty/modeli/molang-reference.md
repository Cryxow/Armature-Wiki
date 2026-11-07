---
description: "Справочник по Molang"
---

# 📖 Справочник по Molang

Это нативный **реализованный поднабор**, общий для серверных и клиентских сэмплеров. Это не универсальный скриптовый движок Bedrock. Запросы зависят от контекста, заполняемого рендерером. Некоторые имена совместимости намеренно постоянны.

## Синтаксис выражений

Поддерживаются числа, скобки, унарные `!`, `-`, `+`, арифметика `+ - * / %`, сравнения, `&&`, `||` и условные `condition ? yes : no`. Тригонометрия использует градусы. Выражения оцениваются в момент сэмплирования, включая случайность. Блоки утверждений, произвольное выполнение скриптов и вся среда Bedrock не подразумеваются поддержкой этих выражений.

## Запросы

| Запрос/алиас | Текущее значение |
| --- | --- |
| `query.yaw` | yaw |
| `query.pitch` | pitch |
| `query.modified_distance_moved` | modifiedDistanceMoved |
| `query.modified_move_speed` | modifiedMoveSpeed |
| `query.anim_time` | animTime |
| `query.delta_time` | deltaTime |
| `query.game_time` | gameTime |
| `query.program_time` | programTime |
| `query.lifetime` | lifetime |
| `query.age` | lifetime |
| `query.is_moving` | Логический флаг контекста (1 = истина, 0 = ложь). |
| `query.is_on_ground`, `query.is_onground` | Логический флаг контекста (1 = истина, 0 = ложь). |
| `query.is_sneaking` | Логический флаг контекста (1 = истина, 0 = ложь). |
| `query.is_sprinting` | Логический флаг контекста (1 = истина, 0 = ложь). |
| `query.is_swimming` | Логический флаг контекста (1 = истина, 0 = ложь). |
| `query.first_person` | Константа совместимости `0.0`, без запроса состояния игры |
| `query.third_person` | Константа совместимости `1.0` (не является живым запросом к игре) |
| `query.health`, `query.max_health` | Текущее здоровье контекста. Max_health сейчас является алиасом, а не отдельным максимальным значением. |
| `query.hurt_time` | hurtTime |
| `query.target_x`, `query.target_y`, `query.target_z` | Константа совместимости `0.0`, без запроса состояния игры |
| `query.view_x`, `query.view_y`, `query.view_z` | Константа совместимости `0.0`, без запроса состояния игры |
| `query.camera_x`, `query.camera_y`, `query.camera_z` | Константа совместимости `0.0`, без запроса состояния игры |
| `query.uuid`, `query.player_uuid` | Константа совместимости `0.0`, без запроса состояния игры |
| `query.day_time` | gameTime |
| `query.moon_phase` | Константа совместимости `0.0`, без запроса состояния игры |

## Математические функции

`math.sin`, `math.cos`, `math.tan`, `math.asin`, `math.acos`, `math.atan`, `math.atan2`, `math.abs`, `math.sqrt`, `math.floor`, `math.ceil`, `math.round`, `math.trunc`, `math.sign`, `math.exp`, `math.ln`, `math.degrees`, `math.radians`, `math.inversesqrt`, `math.inv_sqrt`, `math.saturate`, `math.lerp`, `math.lerprotate`, `math.lerpplace`, `math.pow`, `math.mod`, `math.min`, `math.max`, `math.clamp`, `math.step_smooth`, `math.smooth`, `math.hermite_blend`, `math.random`, `math.random_integer`, `math.randomint`, `math.die_roll`, `math.die_roll_integer`, `math.roll_integer`.

Используйте аргументность, поддерживаемую функцией: например, `math.sin(angle)`, `math.clamp(value, minimum, maximum)`, `math.lerp(start, end, amount)` и `math.random(minimum, maximum)`. Неизвестные функции/запросы вызывают диагностику. Неиспользованные переменные Molang считываются как ноль. Общие переменные YAML не автоматически привязываются к этому пространству имен.

## Малый пример

```text
query.is_moving ? math.sin(query.anim_time * 180) * 3 : 0
```

Используйте это в поддерживаемом анимированном числовом компоненте, а не как строку YAML `when`. Проверьте контекст запроса в игре и сохраните выбор состояния игры в [условия профиля](../profili/conditions.md).
