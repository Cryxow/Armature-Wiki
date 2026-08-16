---
cover: ../../.gitbook/assets/itemsadder.png
coverY: 0
layout:
  width: default
  cover:
    visible: true
    size: full
    mask: none
  title:
    visible: true
  description:
    visible: true
  tableOfContents:
    visible: true
  outline:
    visible: true
  pagination:
    visible: true
  metadata:
    visible: true
  tags:
    visible: true
  actions:
    visible: true
---

# 🟪 ItemsAdder

Armature natively supports ItemsAdder items, meaning you can use them in Armature to display animated arms.

### Profile config

{% code title="Armature/profiles/itemsadder_test_item.yml" %}
```
ia_test_item:
  item: <namespace>:<item_id>
  model: <your_arms_model>
  hide-vanilla-hand: true
```
{% endcode %}

Custom items textures and models are supported by `hide-vanilla-hand` property in Armature.

