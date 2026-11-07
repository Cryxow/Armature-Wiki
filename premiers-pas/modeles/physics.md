---
description: "Os avec physique"
---

# 🪀 Os avec physique

## Marquer un os

Cliquez avec le bouton droit → **Armature → Physique** indépendamment de **Assigner un rôle**. `.armature` v2 stocke un booléen `armature_physics`. Des noms tels que `phy_chain` n’ont aucun sens spécial dans ce format. Un os `none` ordinaire peut être physique, tout comme un os de corps/objet.

Le pivot est le centre de balancement. Placez un pivot d’accessoire suspendu à son point d’attache et orientez la géométrie par rapport aux axes locaux de l’os. Les os enfants héritent de la transformation finale animée/physique du parent. Évitez de compenser cette transformation deux fois dans leurs coordonnées d’auteur.

## Activer et régler la simulation

Utilisez `render.motion.physics` pour la réponse globale. Un profil d’attache ou une instance peut surcharger ses propres paramètres `physics`. La fréquence contrôle la vitesse de réponse, l’amortissement contrôle le rebond, les limites d’angle maximal limitent le balancement, et les gains d’accélération et de rotation ajustent la sensibilité du mouvement. Voir [Mouvement & physique](../../avance/meilleurs-reglages.md).

Il s’agit d’une présentation basée sur un ressort, sans collisions. **La prévisualisation de Blockbench ne simule pas cette physique.** Sauvegardez, compilez, téléchargez le nouveau pack, puis testez l’accélération, l’arrêt, la rotation et un parent en mouvement dans le jeu.

Les modèles natifs `.bbmodel` hérités utilisent le préfixe `phy_*`. BetterModel ne met pas en œuvre le contrat d’os physique natif. Ne pas appliquer cette convention de nommage à un projet v2 à la place de la case à cocher explicite.
