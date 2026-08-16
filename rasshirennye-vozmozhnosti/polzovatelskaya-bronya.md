# 👕 Пользовательская броня

## Пользовательские текстуры для vanilla-брони

Замените текстуры `armor.png` и `leggings.png` внутри `plugins/BetterModel/armors/armors/<vanilla_type>/` своими текстурами. Используйте те же имена файлов.

## Пользовательская броня, добавленная плагином

Следующие шаги предполагают, что пользовательский комплект брони уже создан плагином вроде CraftEngine, ItemsAdder или Nexo.

Допустим, есть комплект изумрудной брони с пользовательскими ID: `your_namespace:emerald_chestplate`, `your_namespace:emerald_leggings` и т. д.

### Добавление пользовательских текстур

Скопируйте текстуры брони игрока `armor.png` и `leggings.png` в `plugins/BetterModel/armors/armors/emerald/` _(замените `emerald` на имя типа брони)_.

Если не уверены, посмотрите на vanilla-броню в `plugins/BetterModel/armors/`.

### Сопоставление текстур и брони

Откройте `/Armature/config.yml` и найдите `armor-mappings:`. Используйте структуру:

```yaml
armor-mappings:
  "your_namespace:emerald_chestplate": emerald
```

* `your_namespace:emerald_chestplate` — ID пользовательского предмета брони.
* `emerald` — имя папки в `/BetterModel/armors/armors/`.
