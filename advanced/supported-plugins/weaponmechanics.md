# 🔫 WeaponMechanics

Armature natively supports WeaponMechanics items, meaning you can use them in Armature to display animated arms.

### Profile config

{% code title="Armature/profiles/m4a1.yml" %}
```yaml
m4a1:
  item: weaponmechanics:<item_id>
  model: <your_arms_model>
  hide-vanilla-hand: false
```
{% endcode %}

Custom items textures and models are _partially supported_ by `hide-vanilla-hand` property in Armature. The best solution is to manually hide your custom item model for first-person view in BlockBench or in the model file.

## Provider-driven Actions

WeaponMechanics adds exclusive actions under `animations`  inside your profile file.

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

#### Example

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
