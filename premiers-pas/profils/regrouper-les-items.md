---
description: "Sélection et regroupement des objets"
---

# 🧲 Sélection et regroupement des objets

Un sélecteur exact est compact :

```yaml
match:
  item: minecraft:diamond_sword
```

Les groupes et les motifs sont pris en charge :

```yaml
match:
  items:
    - family: blocks
    - "*_LEAVES"
    - minecraft:glass
```

Les touches dédiées équivalentes sont `families` et `patterns`. La correspondance structurée ne nécessite pas de fournisseur :

```yaml
match:
  item:
    material: minecraft:paper
    data:
      custom-model-data: 12001
      item-model: "myplugin:rifle"
      pdc:
        "myplugin:variant": rifle
      tags:
        CustomTag: value
```

Les correspondances de sélecteur peuvent exclure des objets exacts, des familles ou des motifs :

```yaml
match:
  families:
    - swords
  exclude:
    items:
      - minecraft:diamond_sword
    families:
      - shield
    patterns:
      - "*_AXE"
```

`exclude.items` accepte les identifiants d’objets exacts, `family:` les sélecteurs, et `pattern:` les sélecteurs. Toutes les exclusions sont appliquées après l’inclusion, et une exclusion prime. La correspondance structurée `item` ne peut utiliser que des exclusions de matériaux Minecraft et ne peut être combinée avec l’inclusion `items`, `families` ou `patterns`. Les correspondances d’objets dupliquées et les identifiants de profil dupliqués rendent le fichier contenant invalide. Le démarrage et le rechargement avertissent et ignorent les fichiers invalides. Les fichiers valides restent actifs. Un rechargement ne désactive jamais le plugin à cause d’un seul fichier de profil mauvais.

## Activation et permissions

`match.when` peut conditionner un profil autrement correspondant grâce à un état externe ou des variables partagées. Utilisez `player.has-permission:<node>` ou un bloc `permissions` avec `any`, `all`, `none`. Les conditions de sélection ne peuvent dépendre des résultats d’animation/variant/attachment de cette sélection elle-même.

L’accès au profil peut être désactivé pour les joueurs sélectionnés sans modifier le fichier de profil. Accordez `armature.disabled` pour désactiver toute présentation Armature pour le joueur, ou accordez `armature.disabled.<profile-id>` pour désactiver uniquement un profil. Pour le profil ci-dessus, la permission ciblée est `armature.disabled.m4a1`.

Le suffixe est la clé racine YAML minuscule du profil. Les permissions sont par défaut à false et sont destinées à servir de nœuds de désactivation gérés par LuckPerms ou un autre plugin de permissions. Supprimer un nœud permet au profil de reprendre sur le prochain cycle de joueur. Le jeu et l’objet tenu ne sont jamais modifiés. En tant qu’alternative de commande persistante, utilisez `/armature profile disable <player> <profile>` et `/armature profile enable <player> <profile>`.

## Vérifier l’identité choisie

Les espaces de noms du fournisseur identifient les objets personnalisés réels : `itemsadder:...`, `nexo:...`, `craftengine:...`, `weaponmechanics:...`. Un nom d’affichage traduit n’est pas un identifiant. Utilisez `/armature profile inspect <player>` avec l’objet tenu réel, puis testez la main secondaire. Évitez les correspondances exactes superposées dans les exemples. La composition de modèle fait référence aux identifiants de profil et n’utilise pas le dossier contenant comme espace de nom.
