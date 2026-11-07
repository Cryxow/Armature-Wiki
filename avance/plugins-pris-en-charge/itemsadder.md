---
cover: ../../.gitbook/assets/itemsadder.png
coverY: 0
layout:
  width: default
  cover:
    visible: true
    size: full
    mask: none
  title:
    visible: true
  description:
    visible: true
  tableOfContents:
    visible: true
  outline:
    visible: true
  pagination:
    visible: true
  metadata:
    visible: true
  tags:
    visible: true
  actions:
    visible: true
---

# 🧰 ItemsAdder

Installez ItemsAdder à côté d’Armature et créez l’objet personnalisé identifié ci-dessous en utilisant la configuration propre de ce plugin. L’exemple d’Armature utilise le modèle `arm_sword` fourni. Il ne crée pas l’objet de jeu personnalisé.

## Profil complet

```yaml
wiki_itemsadder:
  match:
    item: itemsadder:my_pack:wiki_sword
  model:
    name: arm_sword
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: player:attack
        animation:
          name: swing
          duration: 8t
```

Enregistrez sous `plugins/Armature/profiles/`, rechargez les profils, obtenez cet objet fournisseur exact et inspectez le profil correspondant. Effectuez une animation : la clip attendue est `swing`. Testez les deux mains si l’objet peut être tenu dans l’une ou l’autre.

## Flux de travail des packs de ressources


L’espace de nom et l’ID de l’objet d’ItemsAdder forment le sélecteur. Le nom visible de l’objet n’est pas son identité. Armature détecte automatiquement le fournisseur installé et prépare ses actifs générés pour le flux de travail de génération d’ItemsAdder. ItemsAdder reste responsable de la génération, de l’hébergement et de l’envoi de son ZIP final. Exécutez `/iazip` et utilisez le flux de travail normal de rafraîchissement/envoi de pack d’ItemsAdder après avoir préparé les modifications.


Après avoir modifié les modèles d’Armature ou les actifs du moteur de rendu natif :

```text
/armature reload pack
/iazip
```

La syntaxe de la commande du fournisseur peut varier selon la version du fournisseur installé. Utilisez sa propre aide pour les arguments. Choisissez la cible de pack Minecraft appropriée et acceptez/téléchargez le pack fournisseur final. Si le résultat manque de textures, inspectez le ZIP final avant de réessayer la correspondance des objets.

Le fournisseur identifie les objets. Les événements génériques d’équipement/déséquipement d’Armature et les événements de base restent disponibles. Ne supposez pas qu’il fournit des événements de type WeaponMechanics pour le tir ou le rechargement. Émettez des signaux de jeu personnalisés via l’API publique si nécessaire.

Voir [Packs de ressources](../../configuration/resource-packs.md) pour les collisions, les filtres, la livraison fractionnée et les reçus de génération.
