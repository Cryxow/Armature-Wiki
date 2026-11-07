---
description: "Animations et Molang"
---

# 🎞️ Animations et Molang

## Clips d’auteur

Utilisez les pistes de traduction, de rotation et d’échelle de Blockbench sur les os du modèle. Les pivots des os définissent le centre de transformation, et les os enfants héritent de la hiérarchie. Vérifiez la pose au repos et les clés avant d’ajouter un mouvement procédural. Le parseur natif échantillonne les interpolations linéaires, par étapes, Catmull-Rom et Bézier prises en charge. Utilisez le même modèle et la même interpolation sur le serveur et le client optionnel pour la comparaison.

Les noms tels que `idle`, `walk`, `spawn`, `jump`, `fire` et `reload` sont des noms de clips natifs ordinaires. Aucun clip ne démarre à cause de son nom : sélectionnez-le via une règle de profil ou une API. Les noms doivent être non vides et uniques dans le modèle. La compatibilité BetterModel conserve ses noms automatiques réservés.

## Gauche et droite

Utilisez des os et des clips explicites pour la main gauche. `.armature` v2 ne réécrit pas les cibles d’animation ni ne synthétise des clips miroirs. Ciblez chaque os par son ID réel/nom tel que attendu par l’exécution. Conservez les IDs lors de la modification d’un actif existant. Changer un rôle ne renomme pas une cible d’animation.

## Expressions Molang

L’échantillonneur natif évalue les composants Molang pris en charge plutôt que de traiter une expression comme une constante numérique. Par exemple, un composant animé peut utiliser :

```text
math.sin(query.anim_time * 180) * 5
```

La trigonométrie Molang utilise des degrés. Les opérations arithmétiques, les comparaisons, les opérateurs logiques et les fonctions implémentées `math.*` prises en charge peuvent combiner le temps d’animation, les regards/mouvements et les requêtes de contexte. Utilisez [la référence de requête/fonction supportée](molang-reference.md) au lieu de supposer que toutes les fonctionnalités Molang Bedrock existent.

Les requêtes d’animation Molang et les conditions de profil YAML sont des langages différents. Écrire `variable.weapon-skin` dans un profil ne crée pas automatiquement cette variable dans une expression d’animation. Préférez les variantes de profil/poids additifs pour la sélection d’état et Molang pour les valeurs de piste procédurales prises en charge.

## Mélange et caméra

Les profils choisissent la durée de mélange, la vitesse (y compris la lecture inverse) et les canaux. Les clips épars retournent des transformations non clonées vers la pose de repos du modèle au lieu de conserver la pose du clip précédent. La caméra et la géométrie doivent utiliser la même timeline de clip. Gardez le rôle de la caméra à une rotation de repos de zéro et animez des décalages modérés.

## Vérification

Compilez le modèle sauvegardé, inspectez les diagnostics d’expression et comparez la pose de repos, le début/fin du clip, les boucles, l’interpolation et les deux mains. Un aperçu d’éditeur ne peut pas prouver les valeurs de requête de temps d’exécution prises en charge ou le rendu fourni par le pack.
