---
description: "Rendu des objets tenus"
---

# 🗡️ Rendu des objets tenus

L’état visuel appartient au profil et est indépendant de l’animation du bras. Seule la copie affichée par Armature change. L’objet dans l’inventaire du joueur et ses composants restent inchangés. Cette même section est acceptée dans les profils legacy utilisant `model.name`.

Un arc peut réutiliser les branches `minecraft:using_item` et `minecraft:use_duration` du modèle source. L’arc vanilla change à 13 et 18 ticks :

```yaml
bow:
  match:
    item: minecraft:bow
  model:
    name: bow_arms
    held-item:
      mode: source
```

Pour une trident ou un bouclier dont l’animation du bras présente déjà l’action, conservez l’apparence du reste même lorsque le joueur utilise l’objet :

```yaml
trident:
  match:
    item: minecraft:trident
  model:
    name: trident_arms
    held-item:
      mode: static
```

Utilisez `mode: source` pour cette trident ou ce bouclier lorsque les branches d’utilisation du pack sont souhaitées. Le choix est générique : la définition de l’objet source est lue après la priorité du pack de ressources et la résolution de superposition. Les conditions non liées à l’utilisation restent dans le modèle d’objet pour que Minecraft les évalue.

```yaml
shield:
  match:
    item: minecraft:shield
  model:
    name: shield_arms
    held-item:
      mode: static # change to source to display the pack's blocking model
```

Pour les modèles personnalisés, les règles sont vérifiées dans l’ordre des fichiers. Chaque `model` fait référence à un `assets/<namespace>/models/<path>.json` existant dans le pack effectif. Les références manquantes échouent la génération du pack au lieu de publier un état cassé :

```yaml
custom_bow:
  match:
    item: itemsadder:custom_bow
  model:
    name: custom_bow_arms
    held-item:
      mode: rules
      default: original
      rules:
        - when: item.use-ticks >= 18t
          model: mypack:item/custom_bow_pulling_2
        - when: item.use-ticks >= 13t
          model: mypack:item/custom_bow_pulling_1
        - when: item.using == true
          model: mypack:item/custom_bow_pulling_0
```

`item.using` et `item.use-ticks` font référence à la main rendue. `original` conserve le modèle d’objet ordinaire en tant que solution de repli. Les règles sélectionnent un alias de modèle d’objet généré par Armature. Elles ne modifient ni le gameplay ni la durée d’utilisation.

Pour garder l’objet vanilla visible normalement et afficher Armature uniquement pour des événements sélectionnés, définissez la liste d’autorisation de rendu explicitement et maintenez l’option de masquer la main activée :

```yaml
model:
  name: baseball_bat
  hide-vanilla-hand: true
  render-events: [player:attack]
```

Les entrées peuvent être des identifiants d’action tels que `swing` ou des signaux nommés tels que `player:attack`. La liste d’autorisation contrôle la visibilité du modèle. La règle de correspondance sous `animations.actions` contrôle toujours quelle animation est jouée. PacketEvents est requis pour le masquage de la main à la première personne conditionnel.

Pour un modèle qui reste chargé pendant que l’objet vanilla reste visible pendant le déplacement normal, déplacez le masquage de la main vers les règles :

```yaml
model:
  name: baseball_bat
  hide-vanilla-hand: true

animations:
  movement:
    - when: player.speed >= 0
      hide-vanilla-hand: false
      animation: base
  actions:
    - trigger: player:attack
      hide-vanilla-hand: true
      animation: attack_1
```

La règle d’action active a la priorité sur la règle de déplacement sélectionnée. Une règle sans `hide-vanilla-hand` hérite du niveau suivant, puis de `model.hide-vanilla-hand`. Cette option contrôle uniquement le masquage de la main ; `model.render-events` reste la liste d’autorisation de visibilité de modèle séparée.

`animations` est un conteneur, pas un registre d’animation. Le chargeur moderne rejette `animations.definitions`, `actions` de niveau supérieur, `triggers` de niveau supérieur, et `mode` sous les règles d’action. Les animations sont déclarées à côté de la règle en utilisant celles-ci.

Une action `item:unequip` acceptée garde son modèle sortant en vie jusqu’à la fin de la clip authored, y compris sa vitesse de lecture. Les autres changements de case ne ferment pas les modèles sortants précédents. Retourner à un profil pendant son déséquipement réclame la même instance de modèle et mélange depuis sa pose actuelle vers l’équipement ou le cycle de déplacement normal, en utilisant les paramètres de mélange de l’animation entrante. Les profils partageant un actif de modèle conservent des instances séparées. Sans animation de déséquipement acceptée, la suppression est immédiate.

Le client Fabric optionnel nécessite le protocole 9 pour les présentations de barre d’outils superposées. Mettez à jour son JAR avec le plugin.

## Transférer la propriété

La copie affichée appartient à la session de présentation. Avec `render.dynamic-items.defer-until-equipped`, un modèle sortant conserve sa définition d’objet résolue et ses composants d’objet lors de la déséquipement ou de l’action différée. L’équipement entrant reçoit les objets actuels de l’inventaire. Cela modifie uniquement la présentation, jamais l’objet réel, la durée, les dégâts, l’ammo ou l’état de l’arme.

Vérifiez la sélection source/static/rules en utilisant le **pack final effectif du fournisseur**, y compris les superpositions actives. Une définition visible dans un ZIP de test autonome peut ne pas être celle fournie par le fournisseur.
