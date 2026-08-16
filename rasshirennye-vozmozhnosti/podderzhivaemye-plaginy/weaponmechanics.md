# WeaponMechanics

Armature нативно поддерживает предметы WeaponMechanics, поэтому их можно использовать в Armature для отображения анимированных рук.

### Конфигурация профиля

{% code title="Armature/profiles/m4a1.yml" %}
```yaml
m4a1:
  item: weaponmechanics:<item_id>
  model: <your_arms_model>
  hide-vanilla-hand: false
```
{% endcode %}

Пользовательские текстуры и модели _поддерживаются частично_ свойством `hide-vanilla-hand` в Armature. Лучшее решение — вручную скрыть пользовательскую модель предмета от первого лица в BlockBench или в файле модели.

## Действия провайдера

WeaponMechanics добавляет эксклюзивные действия в `animations` внутри файла профиля.

```yaml
fire:
aim:
aim-start:
aim-exit:
aim-fire:
reload-start:
reload-phase:
reload-complete:
reload-cancel:
```

#### Пример

{% code title="Armature/profiles/m4a1.yml" %}
```yaml
m4a1:
    animations:
        idle: base
        walk: base
        sprint: run
        equip: equip
        unequip: unequip
        fire: fire
        aim: aim
        aim-fire: fire_aim
        reload-start: reload
```
{% endcode %}
