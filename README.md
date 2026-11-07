---
cover: .gitbook/assets/Banner Final.png
coverY: 0
coverHeight: 318
---

# 👋 Introduction

Armature **2.0.0** est un moteur d’animation à la première personne pour
Paper et Folia. Il sélectionne le profil de l’objet tenu, affiche les membres
du joueur et les objets animés, combine mouvements, actions et couches additives,
et génère les ressources nécessaires au rendu.

Le **moteur de rendu natif** est activé par défaut. Il prend en charge le format
strict `.armature` v2 et les modèles `.bbmodel` compatibles sans BetterModel.
BetterModel reste disponible comme moteur de compatibilité. Sa dépréciation est
prévue dans une prochaine version. Les choix explicites du moteur sont conservés.

Minecraft et les plugins de gameplay gèrent les inventaires, les munitions, les
dégâts, les projectiles, les rechargements et les délais de récupération. Une
animation nommée `fire` ne déclenche aucun tir.

{% hint style="info" %}
Cette documentation couvre Armature 2.0.0. Vérifiez la version installée avant
d’utiliser les exemples. L’[archive 1.3.0 en anglais](https://github.com/Cryxow/Armature-Wiki/tree/wiki-v1.3.0)
reste disponible sur GitHub.
{% endhint %}

## Par où commencer

- Pour configurer un serveur, suivez le guide d’[installation](premiers-pas/installation.md),
  puis testez [votre première animation en jeu](premiers-pas/first-presentation.md).
- Pour créer un modèle, [installez l’extension Blockbench](premiers-pas/modeles/installing-plugin.md),
  puis suivez le [tutoriel](premiers-pas/modele-blockbench.md).
- Pour créer un profil, consultez la [structure des profils](premiers-pas/profils/README.md)
  et les [exemples complets](premiers-pas/profils/examples.md).
- Pour intégrer Armature à votre plugin, consultez l’[API publique](avance/public-api.md).
- Si Armature est déjà installé, suivez le [guide de mise à niveau](premiers-pas/upgrading.md).

## Nouveautés de la 2.0.0

| Fonctionnalité | Guide |
| --- | --- |
| Rendu natif et rôles explicites pour le corps, les objets et la caméra | [Rôles des os](premiers-pas/modeles/bone-roles.md) |
| Création et import de modèles avec l’extension officielle | [Blockbench](premiers-pas/modele-blockbench.md) |
| Rendu indépendant des deux mains et règles d’animation à deux bras | [Mains](premiers-pas/profils/hands.md) |
| Variables globales typées, conditions et variantes conditionnelles | [Variables](premiers-pas/profils/variables.md), [Variantes](premiers-pas/profils/variants.md) |
| Accessoires animés réutilisables et modèles composites imbriqués | [Accessoires](premiers-pas/profils/attachments.md) |
| Rendu facultatif sous Fabric à la fréquence d’images du client | [Mod client](mod-client/installation.md) |
| Packs générés par les fournisseurs et pack de rendu séparé facultatif | [Packs de ressources](configuration/resource-packs.md) |
| Commandes centralisées sous `/armature reload <target>` | [Commandes](premiers-pas/commandes-et-permissions.md#commandes-de-rechargement) |

Le format de profil moderne, l’API publique et les intégrations de scripts
restent disponibles. Le guide des [plugins pris en charge](avance/plugins-pris-en-charge/README.md)
détaille la détection d’objets, les événements et la distribution des packs.
