---
cover: ../../.gitbook/assets/nexo.png
coverY: 0
---

# 💎 Nexo

Installez Nexo à côté d’Armature et créez l’objet personnalisé identifié ci-dessous en utilisant la configuration propre de ce plugin. L’exemple d’Armature utilise le modèle `arm_sword` fourni. Il ne crée pas l’objet de jeu personnalisé.

## Profil complet

```yaml
wiki_nexo:
  match:
    item: nexo:wiki_sword
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

Enregistrez sous `plugins/Armature/profiles/`, rechargez les profils, obtenez cet objet fournisseur exact et inspectez le profil correspondant. Effectuez une attaque : la clip attendue est `swing`. Testez les deux mains si l’objet peut être tenu dans l’une ou l’autre.

## Flux de travail du pack de ressources


Armature détecte Nexo automatiquement. Le pont prépare les actifs d’Armature, les ajoute au pack de Nexo pendant son cycle de génération, puis effectue les transformations post-génération sur le pack fini correspondant. La disponibilité dépend de ce flux de travail complet, pas d’un dossier contenant les actifs d’hier.

La sérialisation de Nexo et les superpositions affectent les fichiers finaux. Inspectez le ZIP terminé pour les définitions de la version cible et l’espace de nom d’Armature. Après la génération, utilisez le flux de livraison propre de Nexo. Un dossier d’entrée généré copié n’est pas une livraison à un joueur connecté.


Après avoir modifié les modèles d’Armature ou les actifs du moteur de rendu natif :

```text
/armature reload pack
/nexo reload pack
```

La syntaxe de la commande fournisseur peut varier selon la version installée du fournisseur. Utilisez son aide pour les arguments. Choisissez la cible de pack Minecraft appropriée et acceptez/téléchargez le pack fournisseur final. Si le résultat manque de textures, inspectez le ZIP final avant de réessayer la correspondance de l’objet.

Le fournisseur identifie les objets. Les événements d’équipement/déséquipement génériques d’Armature et les événements de la version originale restent disponibles. Ne supposez pas qu’il fournit des événements de tir ou de rechargement au style WeaponMechanics. Émettez des signaux de jeu personnalisés via l’API publique si nécessaire.

Voir [Pack de ressources](../../configuration/resource-packs.md) pour les collisions, les filtres, la livraison fractionnée et les reçus de génération.
