---
description: "Полные примеры"
---

# 🧪 Полные примеры

Каждый пример представляет собой полный файл профиля, а не фрагмент для `config.yml`. Включайте один подходящий пример за раз и удаляйте пересекающиеся профили по умолчанию или для обучения. Корневые ID должны быть уникальными во всей вашей активной библиотеке.

| Пример | Необходимые модели/клипы |
| --- | --- |
| Меч | Выпущено в `arm_sword`: база, удар, экипирование, снятие экипировки |
| Пустые руки | Выпущено в `arm_empty`: база |
| Дуга | Авторское `wiki_player`: база, прицеливание, кость роли предмета |
| Щит | Авторское `wiki_player`: база, блокировка, кость роли предмета |
| Механика оружия/две руки | `arm_rifle` с базой, прицеливанием, выстрелом и перезагрузкой, как используется. Фактическое название оружия WM `m4a1` |
| Аксессуар | `wiki_player` с charm_socket, `wiki_charm` с костью Physics, разрешение |

Модель [обучения ](../../.gitbook/assets/examples/wiki_player.armature) содержит эти демонстрационные клипы/разъемы. Для вашей собственной модели обучение объясняет авторство; `aim`/`block`/`charm_socket` должны существовать, если используются эти примеры. Парсер, принимающий YAML, не создает отсутствующие клипы или кости. Название оружия поставщика является примером, а не предоставленным оружием для игры. Условия и определения пакетов разрешаются в зависимости от основной руки.

## Меч

[Скачать профиль ](../../.gitbook/assets/examples/sword.yml)

```yaml
wiki_sword:
  match:
    item: minecraft:diamond_sword
  model:
    name: arm_sword
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: player:attack
        animation:
          name: swing
          cooldown: 3t
      - trigger: item:equip
        animation: equip
      - trigger: item:unequip
        animation: unequip
```

## Пустые руки

[Скачать профиль](../../.gitbook/assets/examples/empty-hands.yml)

```yaml
wiki_empty:
  match: {item: minecraft:air}
  model: {name: arm_empty, hide-vanilla-hand: true}
  animations:
    movement:
      - animation: base
```

## Лук

[Скачать профиль](../../.gitbook/assets/examples/bow.yml)

```yaml
wiki_bow:
  match: {item: minecraft:bow}
  model:
    name: wiki_player
    held-item: {mode: source}
  animations:
    movement:
      - animation: base
      - when: item.using == true
        animation: aim
```

## Щит

[Скачать профиль](../../.gitbook/assets/examples/shield.yml)

```yaml
wiki_shield:
  match: {item: minecraft:shield}
  model:
    name: wiki_player
    held-item: {mode: static}
  animations:
    movement:
      - animation: base
      - when: item.using == true
        animation: block
```

## Механика оружия

[Скачать профиль](../../.gitbook/assets/examples/weaponmechanics.yml)

```yaml
wiki_rifle:
  match: {item: weaponmechanics:m4a1}
  model: {name: arm_rifle}
  animations:
    movement:
      - animation: base
      - when: weaponmechanics.aiming == true
        animation: aim
    actions:
      - trigger: weaponmechanics:fire
        animation: {name: fire, cooldown: 3t}
      - trigger: weaponmechanics:reload-start
        animation: {name: reload, can-be-cancelled: false}
```

## Две руки

[Скачать профиль](../../.gitbook/assets/examples/two-arms.yml)

```yaml
wiki_two_arms:
  match: {item: weaponmechanics:m4a1}
  model: {name: arm_rifle}
  animations:
    movement:
      - animation: {name: base, arms: both}
    actions:
      - trigger: weaponmechanics:reload-start
        animation: {name: reload, arms: both, can-be-cancelled: false}
```

## Аксессуар

[Скачать профиль ](../../.gitbook/assets/examples/accessory.yml)

```yaml
wiki_charm:
  type: attachment
  model: {name: wiki_charm}
  physics: {enabled: true, frequency: 3.0, damping: 0.9}

wiki_composite:
  match: {item: minecraft:stick}
  model: {name: wiki_player}
  attachments:
    charm:
      profile: wiki_charm
      attach-to: {bone: charm_socket}
      when:
        permissions:
          any: [cosmetics.charm, cosmetics.admin]
```

[Скачать статичную модель физики для аксессуара ](../../.gitbook/assets/examples/wiki_charm.armature). Скопируйте её рядом с `wiki_player.armature` и предоставьте `cosmetics.charm` тестовому игроку. Помеченная кость симулируется на сервере. Blockbench не симулирует это движение.

## Применить и проверить

Сохранить под именем `plugins/Armature/profiles/`, перезагрузить профили и модели, пересобрать ресурсы Armature и передать финальный пакет поставщика. Используйте инспекцию профиля для подтверждения выбранной идентичности, варианта и экземпляров. Протестируйте обычные действия, защищенную перезагрузку, экипирование/снятие и обе руки перед адаптацией файла.
