---
description: "Mouvement et physique"
---

# 🌊 Mouvement et physique

Le mouvement procédural est composé d’animations créées par l’utilisateur. Commencez par les valeurs par défaut intégrées, puis modifiez une couche à la fois. Un réglage qui convient à un fusil peut être excessif pour un objet léger.

## Écartement

`render.motion.sway` ajoute la rotation de regard/mouvement. Séparez `look` et `movement` pour contrôler la rotation, la hauteur et le roulis. Les gains signés inversent la réponse. `lerp-speed` contrôle l’adoucissement. Regardez `0.0` pour supprimer l’interpolation du regard. `maximum-rotation` limite l’effet combiné. `affects-camera-bone` décide si l’écartement atteint l’os de la caméra. Les règles de mouvement individuelles peuvent surcharger `sway-rate`.

## Bob et suivi de la caméra

Le Bob utilise la distance parcourue, `cycles-per-block`, l’amplitude horizontale/verticale et l’amplitude du roulis. La fréquence et l’amortissement ajustent sa réponse élastique. Le suivi de la caméra utilise les gains de rotation/pente positionnels, le décalage maximal et la compensation de la pente. Activez une couche avant d’ajuster ses paramètres. Les couches désactivées ne contribuent pas.

## Physics

Les os physiques natifs utilisent un mouvement élastique issu de l’accélération et du virage. `.armature` v2 utilise la propriété explicite **Physics**. Le héritage `.bbmodel` utilise `phy_*`. La physique est indépendante du rôle visuel et n’est pas une simulation de collision. Un accessoire peut surcharger les paramètres physiques par instance.

| Paramètre | Effet |
| --- | --- |
| `enabled` | Activer la simulation pour les os marqués |
| `acceleration-gain` | Réponse à l’accélération du mouvement |
| `maximum-angle` | Limite de l’angle d’écartement |
| `frequency` | Vitesse de réponse |
| `damping` | Résistance à l’oscillation. Proche de 1 s’installe sans rebond |
| `turn-gain` | Réponse ajoutée au virage du regard |

## Recettes de réglage

- **Accessoire lourd :** abaissez la fréquence et réduisez le gain de virage. Gardez l’amortissement proche de 1 si l’arrêt provoque un snap/rebond.
- **Caméra silencieuse :** réduisez les gains de regard et désactivez l’écartement de la caméra avant d’ajouter l’animation de l’os de la caméra.
- **Mouvement de marche uniquement :** activez le bobbing tout en désactivant l’oscillation de la vue et le suivi de la caméra.
- **Détection d’un problème d’alignement :** désactivez temporairement les couches procédurales et comparez la pose au repos du modèle. Rétablissez les couches souhaitées après avoir corrigé les pivots.

Appliquez ces paramètres avec `/armature reload config`. Rechargez ou téléchargez uniquement si vous modifiez également une option affectant le pack de ressources. Testez sur le client qui sera réellement utilisé : le rendu par tick du serveur et le client rendu par frame (optionnel) peuvent se sentir différemment. [L’auteur de la physique](../premiers-pas/modeles/physics.md) explique les pivots.
