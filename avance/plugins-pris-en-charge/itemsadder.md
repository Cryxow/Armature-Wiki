# ItemsAdder

Armature prend nativement en charge les items ItemsAdder, ce qui vous permet de les utiliser dans Armature pour afficher des bras animés.

### Configuration du profil

{% code title="Armature/profiles/itemsadder_test_item.yml" %}
```
ia_test_item:
  item: <namespace>:<item_id>
  model: <your_arms_model>
  hide-vanilla-hand: true
```
{% endcode %}

Les textures et modèles des items personnalisés sont pris en charge via la propriété `hide-vanilla-hand` d’Armature.
