# WeaponMechanics

Armature prend nativement en charge les items WeaponMechanics, ce qui vous permet de les utiliser dans Armature pour afficher des bras animés.

### Configuration du profil

{% code title="Armature/profiles/m4a1.yml" %}
```yaml
m4a1:
  item: weaponmechanics:<item_id>
  model: <your_arms_model>
  hide-vanilla-hand: false
```
{% endcode %}

Les textures et modèles personnalisés sont _partiellement pris en charge_ via la propriété `hide-vanilla-hand` d’Armature. La meilleure solution consiste à masquer manuellement votre modèle d’item personnalisé pour la vue à la première personne dans BlockBench ou dans le fichier du modèle.

## Actions fournies par le plugin

WeaponMechanics ajoute des actions exclusives sous `animations` dans votre fichier de profil.

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

#### Exemple

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
