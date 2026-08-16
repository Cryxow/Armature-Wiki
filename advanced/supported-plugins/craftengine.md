---
cover: ../../.gitbook/assets/A9oDJ3u.gif
coverY: 0
coverHeight: 243
---

# 🟨 CraftEngine

Armature natively supports CraftEngine items, meaning you can use them in Armature to display animated arms.

### Profile config

{% code title="Armature/profiles/craftengine_test_item.yml" %}
```
ce_test_item:
  item: <namespace>:<item_id>
  model: <your_arms_model>
  hide-vanilla-hand: true
```
{% endcode %}

Custom items textures and models are supported by `hide-vanilla-hand` property in Armature.
