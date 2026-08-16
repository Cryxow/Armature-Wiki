# 🛍️ Regrouper les items

Un profil peut associer plusieurs items à un modèle et un ensemble d’animations. Armature résout les sélecteurs lors du chargement des profils, puis chaque item correspondant pointe vers le même profil.

## Quand utiliser le regroupement

Utilisez-le lorsque plusieurs items partagent :

* le même modèle BetterModel ;
* les mêmes animations.

Exemple : faire utiliser le rig `fp_sword` à toutes les épées vanilla.

## Le champ `items`

```yaml
swords:
  items:
    - family: swords
  model: fp_sword
  animations:
    idle: sword_idle
    swing:
      name: sword_swing
      duration-ticks: 8
```

La liste `items` accepte les IDs d’items exacts, familles d’items et motifs. Chaque item résolu reçoit le profil `swords`.

Les sélecteurs peuvent être mélangés :

```yaml
combat_tools:
  items:
    - minecraft:diamond_sword
    - minecraft:netherite_sword
    - family: axes
    - pattern: "*_HOE"
  model: fp_combat
  animations:
    idle: combat_idle
    swing: combat_swing
```

## Types de sélecteurs

### Item exact

Utilisez le format `namespace:item` :

```yaml
items:
  - minecraft:diamond_sword
  - weaponmechanics:m4a1
```

Les identités personnalisées sont utilisables lorsqu’un fournisseur les expose. Les motifs génériques sont actuellement pris en charge uniquement pour les items vanilla.

### Familles vanilla

Les familles regroupent les matériaux vanilla classés par Armature :

```yaml
items:
  - family: swords
  - family: pickaxes
  - family: tools
```

Familles disponibles : `blocks`, `generic`, `tools`, `swords`, `pickaxes`, `axes`, `shovels`, `hoes`, `bows`, `crossbows`, `food`, `potions`, `projectiles` et `throwables`.

Les noms pluriels sont acceptés. `tools` inclut les pioches, haches, pelles et houes.

La forme courte est aussi valide :

```yaml
items:
  - swords
  - tools
```

### Motifs vanilla

Les motifs utilisent `\*` pour un nombre quelconque de caractères et `?` pour un caractère :

```yaml
items:
  - pattern: "*_SWORD"
  - pattern: "minecraft:*_LEAVES"
```

Un motif sans namespace cible Minecraft. Les familles et motifs génériques sont actuellement limités aux items vanilla.

## Raccourcis au niveau du profil

Au lieu de placer les sélecteurs dans `items`, utilisez `families` et `patterns` :

```yaml
vanilla_weapons:
  families:
    - swords
    - bows
  patterns:
    - "*_CROSSBOW"
  model: fp_weapon
  animations:
    idle: weapon_idle
    swing: weapon_swing
```

`item`, `items`, `families` et `patterns` sont combinés. Les sélecteurs dupliqués dans un profil sont dédoublonnés.

## Règles importantes

* Chaque item ne peut correspondre qu’à un seul profil chargé.
* Si deux profils ciblent le même item, le rechargement est refusé avec une erreur de correspondance dupliquée.
* Un profil doit avoir au moins un sélecteur, un `model` et une section `animations`.
* Appliquez les changements avec `/armature reload`.
* Si la validation échoue, l’ensemble de profils actif précédent reste utilisé.
* Familles et motifs sont développés à partir des matériaux vanilla disponibles au chargement des profils.

## Exemple complet

```yaml
vanilla_swords:
  items:
    - family: swords
    - pattern: "*_SWORD"
  model: fp_sword
  sway-rate: 0.9
  animations:
    idle: sword_idle
    walk: sword_walk
    swing:
      name: sword_swing
      duration-ticks: 8
```

Commencez par des IDs exacts pour tester un modèle, puis élargissez à une famille ou un motif lorsque la présentation est correcte. Après chaque changement, vérifiez les logs de rechargement et testez un item représentatif de chaque groupe.
