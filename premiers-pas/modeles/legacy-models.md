---
description: "Anciens modèles et BetterModel"
---

# 🕰️ Anciens modèles et BetterModel

## Format et matrice backend

| Source | Natif | Compatibilité BetterModel |
| --- | --- | --- |
| Strict version `.armature` v2 | Prise en charge. Rôles explicites et Physique | Non pris en charge |
| `.armature` v1 | Rejeté. Recréer manuellement | Non pris en charge |
| Compatible version `.bbmodel` | Prise en charge avec conventions héritées | Prise en charge avec conventions BetterModel |

Conserver le backend natif pour les projets Armature FPV.

## Conventions d’os héritées

version `.bbmodel` conserve des noms tels que `pra_`/`pla_` pour les bras supérieurs, `prfa_`/`plfa_` pour les avant-bras, `pri_`/`pli_` pour les objets tenus et `s_camera` pour le marqueur caméra. Les bras complets natifs utilisent `prfull_`/`plfull_`. Les os physiques natifs utilisent `phy_*`. L’adaptation de la main secondaire et la normalisation du nom existent dans ce chemin hérité, pas dans le système de rôle strict v2.

Utiliser un pivot racine stable pour l’adaptation à la première personne héritée et placer l’animation du modèle entier sur un ancêtre enfant lorsque le squelette hérité doit conserver sa racine. Ne pas imposer le nom requis `root` sur l’affectation de rôle v2.

## Noms d’animation

Natif `.armature` et `.bbmodel` acceptent des noms ordinaires incluant `idle`, `walk`, `idle_fly`, `walk_fly`, `spawn` et `jump`. Les règles de profil les démarquent explicitement. BetterModel rejette ces noms automatiques réservés dans les profils Armature. Renommer les clips et références lors de la sélection de ce backend de compatibilité.

## Choisir BetterModel

Définir `render.backend: bettermodel`, installer BetterModel et redémarrer. Ce choix est conservé lors de la mise à niveau, mais il n’est pas la valeur par défaut de 2.0.0. La composition du modèle et la physique native nécessitent un rendu natif, et le pont client en a également besoin. La dépréciation de BetterModel est prévue pour les futures versions. Elle reste disponible pour l’instant.

## Recréer un modèle legacy

Conserver une sauvegarde. Pour un nouveau projet strict, créer un modèle FPV Armature, copier/reconstruire la géométrie et les animations visées, attribuer tous les rôles corps/objet/caméra et les drapeaux Physics, sauvegarder la version 2 et tester. Attribuer explicitement les rôles de la version 2. Les préfixes de nom de la version 1 ne sont pas convertis automatiquement. Réaffecter explicitement les cibles de la main gauche plutôt que d’attendre un miroir généré.
