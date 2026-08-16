# ItemsAdder

Armature нативно поддерживает предметы ItemsAdder, поэтому их можно использовать в Armature для отображения анимированных рук.

### Конфигурация профиля

{% code title="Armature/profiles/itemsadder_test_item.yml" %}
```
ia_test_item:
  item: <namespace>:<item_id>
  model: <your_arms_model>
  hide-vanilla-hand: true
```
{% endcode %}

Пользовательские текстуры и модели предметов поддерживаются свойством `hide-vanilla-hand` в Armature.
