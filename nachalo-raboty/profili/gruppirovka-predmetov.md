# 🛍️ Группировка предметов

Профиль может сопоставить несколько предметов одной модели и одному набору анимаций. Armature разрешает selectors при загрузке профилей, после чего каждый найденный предмет указывает на один профиль.

## Когда использовать группировку

Используйте её, если несколько предметов имеют:

* одну модель BetterModel;
* один набор анимаций.

Пример: назначить всем vanilla-мечам один rig `fp_sword`.

## Поле `items`

```yaml
swords:
  items:
    - family: swords
  model: fp_sword
  animations:
    idle: sword_idle
    swing:
      name: sword_swing
      duration-ticks: 8
```

Список `items` принимает точные ID, item families и patterns. Каждый найденный предмет получает профиль `swords`.

Selectors можно смешивать:

```yaml
combat_tools:
  items:
    - minecraft:diamond_sword
    - minecraft:netherite_sword
    - family: axes
    - pattern: "*_HOE"
  model: fp_combat
  animations:
    idle: combat_idle
    swing: combat_swing
```

## Типы selectors

### Точный предмет

Используйте формат `namespace:item`:

```yaml
items:
  - minecraft:diamond_sword
  - weaponmechanics:m4a1
```

Пользовательские identity доступны, если их предоставляет провайдер. Wildcard patterns пока поддерживаются только для vanilla-предметов.

### Vanilla families

Доступны семейства vanilla-материалов, классифицированные Armature:

```yaml
items:
  - family: swords
  - family: pickaxes
  - family: tools
```

Доступные families: `blocks`, `generic`, `tools`, `swords`, `pickaxes`, `axes`, `shovels`, `hoes`, `bows`, `crossbows`, `food`, `potions`, `projectiles` и `throwables`.

Принимаются plural-имена. `tools` включает pickaxes, axes, shovels и hoes.

Короткая форма также допустима:

```yaml
items:
  - swords
  - tools
```

### Vanilla patterns

Patterns используют `\*` для любого количества символов и `?` для одного символа:

```yaml
items:
  - pattern: "*_SWORD"
  - pattern: "minecraft:*_LEAVES"
```

Pattern без namespace нацелен на Minecraft. Wildcard families и patterns пока ограничены vanilla-предметами.

## Сокращения на уровне профиля

Вместо selectors в `items` используйте `families` и `patterns`:

```yaml
vanilla_weapons:
  families:
    - swords
    - bows
  patterns:
    - "*_CROSSBOW"
  model: fp_weapon
  animations:
    idle: weapon_idle
    swing: weapon_swing
```

`item`, `items`, `families` и `patterns` объединяются. Дубликаты selectors внутри одного профиля удаляются.

## Важные правила

* Каждый предмет может соответствовать только одному загруженному профилю.
* Если два профиля нацелены на один предмет, reload отклоняется с ошибкой duplicate-match.
* Профилю нужны хотя бы один selector, `model` и секция `animations`.
* Применяйте изменения через `/armature reload`.
* При ошибке validation предыдущий активный набор профилей остаётся в работе.
* Families и patterns разворачиваются по vanilla-материалам, доступным при загрузке профилей.

## Полный пример

```yaml
vanilla_swords:
  items:
    - family: swords
    - pattern: "*_SWORD"
  model: fp_sword
  sway-rate: 0.9
  animations:
    idle: sword_idle
    walk: sword_walk
    swing:
      name: sword_swing
      duration-ticks: 8
```

Начинайте с точных item IDs при тестировании модели, затем расширяйте до family или pattern после проверки визуального представления. После каждого изменения проверяйте reload logs и тестируйте по одному representative item из каждой группы.
