---
description: "Passer à la version 2.0.0"
---

# 🔄 Passer à la version 2.0.0

## Avant de remplacer le JAR

Sauvegardez `plugins/Armature/`, votre configuration de fournisseur/pack final, et les anciennes versions du plugin. Effectuez la mise à niveau sur une copie d’essai d’abord, avec la même configuration client et pack de ressources que vous prévoyez de publier.

## Sélection du moteur de rendu

Les nouvelles installations utilisent `render.backend: native`. Une sélection explicite `bettermodel` existante est conservée. Réglez l’option sur `native` et redémarrez complètement pour changer les moteurs. Un rechargement de configuration ne peut remplacer le backend de rendu actif. BetterModel reste disponible dans 2.0.0, avec une dépréciation prévue pour des versions ultérieures.

## Configuration et commandes

La version du schéma intégré est `config-version: 17`. Armature gère cette valeur et ajoute les valeurs par défaut manquantes en préservant les réglages de l’administrateur. Consultez la [référence de configuration](configuration.md), notamment les réglages de visibilité, de modèle en vue subjective, de masquage de la main vanilla, de mouvement, de mod client et de génération du pack. Évitez de remplacer la nouvelle configuration par un ancien fichier complet ou de modifier le numéro de schéma pour contourner la validation.

Utilisez `/armature config validate` et les [domaines de commande actuels](commandes-et-permissions.md). `/armature reload all` recharge Armature. Il ne lance pas la commande de génération du fournisseur externe. Le `/reload` global au niveau serveur n’est pas une procédure de mise à niveau.

## Profil et variables partagées

Les fichiers anciens et modernes peuvent coexister. Les nouveaux fichiers devraient utiliser les règles de mouvement anonyme, d’action et de couche additive. Comparez vos profils avec les exemples modernes complets avant d’activer la nouvelle configuration.

Déplacez **toutes les déclarations de variables YAML** dans `plugins/Armature/variables.yml`. Résolvez les IDs en double vous-même et supprimez les sections locales `variables` des profils, des variantes et des instances d’accessoires. Les déclarations locales sont rejetées. Armature ne remplace pas votre registre partagé. Les requêtes Java `ArmaturePresentation` conservent leur contrat d’API de variables en ligne.

### Convertir un profil legacy

Après avoir déplacé toutes les variables locales dans le registre partagé, utilisez un fichier legacy sauvegardé sous `plugins/Armature/profiles/` :

```text
/armature config migrate sword.yml --output sword.migrated.yml
```

Le convertisseur s’exécute localement et écrit un fichier YAML au format moderne après validation. Le fichier source et le rendu en cours restent intacts. Lisez les avertissements et les étapes manuelles, comparez le résultat à la [structure des profils](profils/README.md) et vérifiez que tous les modèles et clips existent. La taille du fichier source et du résultat est limitée à 256 Kio. Les paramètres de migration ont quitté `config.yml`. Au démarrage, Armature sauvegarde la configuration et supprime les anciens blocs `migration`.

Déplacez l’original hors du dossier des profils chargés avant d’activer son remplacement, afin que les IDs de profil en double ne puissent pas entrer en concurrence. Ensuite, exécutez `/armature reload profiles` et `/armature profile inspect <player>`, équipez l’objet correspondant et testez les clips de mouvement/action. Un fichier `.migrated.yml` reste un profil chargeable : examinez-le avant d’émettre n’importe quel rechargement de profil ou complet.

## Modèles

- Les actifs `.bbmodel` compatibles existants restent pris en charge.
- Les nouveaux projets Blockbench Armature sauvegardent le format strict `.armature` **version 2**.
- La version 1 de `.armature` est refusée. Recréez le projet et attribuez les rôles explicitement. Changer l’extension ou le numéro de version n’est pas une conversion.
- Les clips natifs peuvent utiliser des noms tels que `idle` et `walk`. BetterModel conserve ses restrictions de noms réservés.
- `.armature` v2 ne synthétise pas les clips de la main gauche miroir ni ne déduit les rôles à partir des noms d’os. Voir [Compatibilité avec la version précédente](modeles/legacy-models.md).

## Terminer la mise à niveau

Redémarrez, chargez la bibliothèque complète du modèle/profil, régénérez le pack final du fournisseur et demandez au client de le télécharger. Mettez à niveau le mod Fabric optionnel vers la version du protocole correspondante. Testez les deux mains, l’équipement/déséquipement, les actions protégées, les peaux/armures, la caméra, la reconnexion et les changements de monde avant de remplacer la production. La documentation 1.3.0 est conservée dans l’étiquette `wiki-v1.3.0` du dépôt wiki.
