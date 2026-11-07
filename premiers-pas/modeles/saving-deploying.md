---
description: "Enregistrer et déployer des modèles"
---

# 💾 Enregistrer et déployer des modèles

## Enregistrer la source

Utilisez **File → Save / Save As** ou le raccourci normal. Il n’existe pas de panneau d’exportation/enregistrement séparé pour Armature. L’enregistrement incrémental ouvre Enregistrer sous. Reouvrez le fichier et vérifiez la géométrie, la hiérarchie, les animations, les rôles et les valeurs de Physique.

Le document doit identifier `meta.model_format: armature_fpv`, `armature.renderer: native` et `armature.format_version: 2`. La version du format de modèle est indépendante de la version de la publication du plugin `2.0.0`. Gardez la source originale modifiable sous contrôle de version ou dans votre sauvegarde d’actifs.

## Installer sur le serveur

Copiez `my_rifle.armature` vers `plugins/Armature/models/` ou un sous-dossier. Référez-vous à `model.name: my_rifle` dans un profil. Les identifiants de modèle sont dérivés des noms de fichiers sans extension. Gardez les identifiants uniques même à travers différents sous-dossiers. Les actifs compatibles `.bbmodel` peuvent vivre à côté des actifs v2 sous le moteur de rendu natif.

Utiliser `/armature reload models` pour les modifications de source. Si des clips ou des os référencés par un profil ont changé, rechargez également les profils ; `/armature reload all` applique le candidat Armature complet.

## Générer et livrer

Reconstruisez le pack Armature, exécutez le flux de travail de génération complet de votre fournisseur et faites télécharger le pack final au joueur. Les bundles clients et les assets du moteur de rendu serveur ont des chemins de transport différents. Testez avec et sans le mod client. Voir [Pack de ressources](../../configuration/resource-packs.md).

## Échecs de validation courants

| Diagnostic | Correction |
| --- | --- |
| Version .armature non supportée | Recréez v1 en v2. Ne renommez ni ne modifiez pas simplement l’en-tête |
| Rôle inconnu / Physique booléen | Attribuez un rôle supporté et utilisez une valeur booléenne pour Physique |
| Plusieurs rôles de caméra | Gardez un seul rôle de caméra en temps réel |
| Clip ou os d’ancrage manquant | Mettre à jour le profil de référence ou ajouter un actif créé |
| Dupliquer les identifiants de modèle/clip | Choisir des identifiants uniques |
| Nom de nœud réservé `__armature_` | Renommer le nœud créé |

Les contrôles de validation de la bibliothèque inactivent également les variantes et les surcharges d’accessoires. Réparez l’ensemble du candidat. Une branche actuellement invisible nécessite toujours des actifs valides. Gardez le dernier pack connu comme bon jusqu’à ce que la nouvelle révision ait été téléchargée et vérifiée visuellement.
