---
cover: ../../.gitbook/assets/nexo.png
coverY: 0
---

# 🟩 Nexo

Armature natively supports Nexo items, meaning you can use them in Armature to display animated arms.

### Profile config

{% code title="Armature/profiles/nexo_test_item.yml" %}
```
nexo_test_item:
  item: <namespace>:<item_id>
  model: <your_arms_model>
  hide-vanilla-hand: true
```
{% endcode %}

Custom items textures and models are supported by `hide-vanilla-hand` property in Armature.
