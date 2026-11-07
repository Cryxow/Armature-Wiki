---
description: "Skins et armures personnalisées"
---

# 🛡️ Skins et armures personnalisées

## Les skins de joueur

Les os du rôle corporel utilisent le skin classique/slim réel du joueur et sa deuxième couche. Bodybench utilise un skin de prévisualisation neutre inclus. Il n’est pas sérialisé dans le projet comme des cubes créés. Les largeurs des bras classiques et slim diffèrent, donc testez les deux. Les cubes créés restent une partie de `.armature` v2 et ne sont pas des aides de skin automatiques.

## Armure équipée avec moteur de rendu natif .armature v2

| Rôle | Couche d’équipement |
| --- | --- |
| `head` | Casque |
| `body` | Ceinture de la cuirasse et des jambières |
| Bras complets, bras supérieurs, avant-bras | Manches de la cuirasse |
| Jambes complètes, cuisses, mollets | Couches de jambières et de bottes indépendantes |

Les mises à jour d’équipement incluent le remplacement, la suppression et le changement des métadonnées d’objet. Les rôles dupliqués ont des maillages visibles indépendants. La présentation de la teinture et du motif utilise les actifs d’armure/palettes. La visibilité de l’armure ne change pas la protection de jeu. Les maillages séparés suivent leurs propres poses d’os et omettent les caps articulation internes.

Les superpositions d’ancien `.bbmodel` des bras conservent leurs modèles anciens. BetterModel résout ses propres types d’armure et textures. Le mod client optionnel utilise son chemin d’équipement/rendu côté client : ne supposez pas qu’il peut consommer une texture personnalisée connue uniquement par le résolveur de type d’armure du serveur.

## Cartographier des objets personnalisés

Ajoutez cette section optionnelle à `config.yml` (utilisez les IDs d’objet de fournisseur réels) :

```yaml
armor-mappings:
  "itemsadder:my_pack:knight_helmet": knight
  "itemsadder:my_pack:knight_chestplate": knight
  "itemsadder:my_pack:knight_leggings": knight
  "itemsadder:my_pack:knight_boots": knight
```

La valeur est un type de texture d’armure. Pour le rendu natif, stockez `armor.png` et `leggings.png` sous `plugins/Armature/armors/armors/knight/`. La texture de la cuirasse/casque/bottes utilise `armor.png`. La couche de jambières utilise `leggings.png`. Gardez la disposition UV d’armure attendue. Une correspondance peut cibler un type personnalisé ou surcharger un type vanilla.

Avec le backend de compatibilité BetterModel, le répertoire correspondant est `plugins/BetterModel/armors/armors/knight/`. Ne supposez pas qu’un fichier dans le répertoire d’un moteur de rendu est automatiquement installé pour l’autre.

## Appliquer et vérifier

Exécutez `/armature reload config` après la modification des mappings, reconstruisez les actifs d’Armature, régénérez le pack final du fournisseur et téléchargez-le. Testez chaque emplacement d’équipement, les matériaux de jambières/bottes mélangés, le cuir teinté, les finitions, la suppression et le déplacement des articulations. Si la texture est manquante, vérifiez à la fois l’identité/type sélectionné et les références du pack final. [Les rôles d’os ](../premiers-pas/modeles/bone-roles.md) expliquent quelles couches corporelles sont générées.
