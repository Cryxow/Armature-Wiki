---
description: "Installation et prérequis"
---

# 📥 Installation et prérequis

## Prérequis

| Composant | Prérequis |
| --- | --- |
| Serveur | Paper ou Folia avec l’API Paper 1.21.8 ou une version plus récente. Vérifiez la version cible avant le déploiement. |
| Java côté serveur | Java 25 |
| PacketEvents | Obligatoire. L’intégration cible la version 2.13.0 pour la compilation et l’exécution. |
| BetterModel | Requis seulement avec `render.backend: bettermodel`. L’intégration de compatibilité cible la version 3.4.1. |
| Pack de ressources | Les joueurs doivent accepter le pack final contenant les ressources générées par Armature. |
| Mod client | Facultatif. Les builds Fabric ciblent des versions précises entre 1.21.8 et 26.2. Consultez la [matrice de compatibilité](../mod-client/installation.md). |

Les versions couvertes par les overlays du pack sont indépendantes des versions
de l’API serveur et de Minecraft prises en charge par le mod Fabric.

## Installation

1. Arrêtez le serveur. Placez le JAR de distribution `Armature-2.0.0.jar` et
   PacketEvents dans `plugins/`. Les JAR `-plain` et de l’API sont destinés à
   d’autres usages.
2. Avant de démarrer, installez les plugins facultatifs qui fournissent les
   objets ou événements utilisés par vos profils. Installez BetterModel si
   vous avez choisi ce moteur.
3. Lancez le serveur avec Java 25. Le moteur par défaut est `native`.
4. Consultez `/armature diagnostics startup` et `/armature status`. Corrigez
   les erreurs de chargement avant de tester les profils.
5. Générez et distribuez le pack via votre fournisseur, ou hébergez le ZIP
   autonome généré. Vérifiez que le client a bien reçu le fichier. Voir
   [Packs de ressources](../configuration/resource-packs.md).
6. Connectez-vous depuis un client Minecraft, acceptez le pack et suivez
   [Votre première animation en jeu](first-presentation.md).

## Fichiers créés

```text
plugins/Armature/
  config.yml
  messages.yml
  variables.yml
  models/
  profiles/
  armors/
  resource_pack/
```

Armature copie les fichiers intégrés de `default-assets/` lors de la première
installation et préserve les fichiers de l’administrateur. Les mises à jour
peuvent ajouter de nouveaux fichiers par défaut. Lancez
`/armature config restore-defaults` pour voir quels fichiers seraient
remplacés. Relancez la commande avec `--confirm` seulement pour les restaurer.

`internal/` contient les caches générés et les instantanés des fournisseurs.
Les réglages du pack déterminent la génération de `resource_pack.zip` et
`renderer_pack.zip`.

## Mises à jour et téléchargements

Récupérez le plugin, le mod client et l’extension Blockbench compatibles depuis
votre canal de distribution Armature. Conservez l’ancien JAR et votre
configuration jusqu’à la validation de la nouvelle combinaison. L’artefact
de l’API sert à la compilation de vos intégrations.
