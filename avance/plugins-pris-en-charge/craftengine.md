---
cover: ../../.gitbook/assets/A9oDJ3u.gif
coverY: 0
coverHeight: 243
---

# 🛠️ CraftEngine

Installez CraftEngine à côté d’Armature et créez l’objet personnalisé identifié ci-dessous en utilisant la configuration propre de ce plugin. L’exemple d’Armature utilise le modèle `arm_sword` fourni. Il ne crée pas l’objet de jeu personnalisé.

## Profil complet

```yaml
wiki_craftengine:
  match:
    item: craftengine:my_pack:wiki_sword
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


Armature détecte automatiquement CraftEngine. Le pont contribue aux actifs d’Armature dans le flux de travail du pack de ressources de CraftEngine. La réutilisation est valide uniquement lorsque le contenu d’Armature et les entrées du fournisseur correspondent toujours au reçu de génération réussi. Modifier un modèle, une entrée du fournisseur ou une configuration de pack cible nécessite une nouvelle construction du fournisseur.

Utilisez le flux de travail de rechargement/construction de la version d’installation de CraftEngine et son propre système de distribution de packs. Lisez son journal de génération : émettre une commande sans construction terminée n’est pas une mise à jour d’actifs réussie.


Après avoir modifié les modèles d’Armature ou les actifs du moteur de rendu natif :

```text
/armature reload pack
/ce reload all
```

La syntaxe de la commande du fournisseur peut varier selon la version installée du fournisseur. Utilisez son aide propre pour les arguments. Choisissez la cible de pack Minecraft appropriée et acceptez/téléchargez le pack fournisseur final. Si le résultat manque de textures, inspectez le ZIP final avant de réessayer la correspondance d’objet.

Le fournisseur identifie les objets. Les événements génériques d’équipement/déséquipement d’Armature et les événements vanilla restent disponibles. Ne supposez pas qu’il fournit des événements de type WeaponMechanics pour le tir ou le rechargement. Émettez des signaux de jeu personnalisés via l’API publique si nécessaire.

Voir [Pack de ressources](../../configuration/resource-packs.md) pour les collisions, les filtres, la livraison fractionnée et les reçus de génération.
