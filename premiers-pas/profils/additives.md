---
description: "Couches additives"
---

# ➕ Couches additives

Les couches additives sont évaluées en continu et composées par Armature au-dessus de la pose de mouvement/action actuellement rendue. Elles ne sont pas des modes d’action :

```yaml
animations:
  additives:
    - animation:
        name: breathing
        speed: 1.0
      playback: loop
      bones: [body, right_arm, left_arm]
      weight:
        parameter: variable.stamina
        input: [0.0, 100.0]
        output: [1.0, 0.15]
        easing: smoothstep
```

Les sections de mouvement et d’animation d’action acceptent également `blend-in` et `blend-out` dans leur mapping `animation`. Les durées sont exprimées en ticks (`4t`) ou en secondes (`0.3s`). Les valeurs numériques sont des ticks. Les valeurs par défaut conservent le comportement historique du moteur de rendu natif (`2t`/`2t` pour les boucles et `1t`/`2t` pour les actions), et `0t` désactive un côté du mélange.

Les champs de couches additives supportés sont `when`, `animation`, `playback` (`loop`, `hold` ou `once`), `group`, `priority`, `weight`, `blend-in`, `blend-out`, `bones`, `channels`, `position-gain`, `rotation-gain` et `reference`.

`reference.animation` et `reference.time` définissent la pose à partir de laquelle le delta de la couche additive est échantillonné. Conceptuellement :

```text
final pose = base pose + sum(additive delta * resolved weight)
```

C’est la solution supportée pour la hauteur de chute, la respiration dépendante de la stamina et des contrôles de pose continus similaires. Les couches additives composent les deltas échantillonnés sur la pose de mouvement/action sélectionnée.

## Choisissez les os affectés

Les couches additives combinent une pose échantillonnée avec la présentation de mouvement/action actuelle. Utilisez `bones`, `channels`, `position-gain`, `rotation-gain` et `reference` pour limiter leur contribution. Les cibles osseilles créées doivent exister, et la version 2 ne les réécrit pas pour l’autre main. Les règles de couches additives n’acceptent pas `arms: both`. La propriété de la main appartient aux mouvements/actions.

## Commencez par utiliser un petit poids

Commencez par un `weight: 0.25` fixe, puis ajoutez des correspondances pilotées par des paramètres une fois la pose correcte. Une réponse de chute dynamique peut mapper `player.fall-distance` de `[0, 4]` à `[0, 1]` avec `smoothstep`, plutôt que de basculer brusquement une pose. La règle additive possède ses propres durées de mélange. Le nom de l’animation, la sélection et la vitesse doivent se trouver dans le `animation` de mappage.

Les clips de référence et le temps définissent la pose neutre pour la différence additive. Les os de référence ou clips manquants constituent des erreurs d’actifs, y compris dans les variantes inactives. Testez plusieurs groupes simultanés, les mélanges d’entrée/sortie et les pistes dispersées contre la pose au repos rédigée.
