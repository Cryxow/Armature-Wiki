---
description: "Rendu et caméra"
---

# 🎥 Rendu et caméra

## Rendu et visibilité

`render.backend: native` sélectionne le moteur de rendu principal. BetterModel est une sélection de compatibilité explicite et nécessite son plugin. Les changements de backend nécessitent un redémarrage. Le pont client optionnel nécessite un rendu natif, mais les clients vanilla utilisent toujours le rendu serveur.

`render.enabled` contrôle le rendu. `render.visibility.first-person-only` et `follow-player-visibility` contrôlent la visibilité de la présentation. Un profil peut utiliser `model.render-events` pour afficher uniquement son squelette pour des actions sélectionnées. La règle d’animation correspondante doit toujours exister.

## Cacher la main à la première personne originale

`model.hide-vanilla-hand` est par profil. `render.vanilla-hiding.mode` sélectionne :

| Mode | Effets et contraintes |
| --- | --- |
| `packet-mask` | Envoie des copies de présentation au client. L’inventaire serveur reste inchangé. Testez les plugins qui inspectent/interceptent les paquets d’inventaire. |
| `core-shaders` | Utilise les shaders générés et un canal GameTime par joueur. Aucun paquet de masquage d’inventaire. Peut entrer en conflit avec un autre pack utilisant le même canal de shader. |

La détection d’action PacketEvents est indépendante du mode de masquage. Le mod client optionnel masque également les mains vanilla tant qu’il possède une présentation acceptée.

## Projection du viewmodel

`render.viewmodel.anti-clipping`, `y-lock` et `fov.locked` génèrent le chemin pack/shader approprié. `fov.degrees` est le champ de vision du viewmodel, non le champ de vision du monde du joueur. L’adaptation du modèle source se fait en mémoire et ne réécrit pas vos actifs. Les paramètres affectant le pack nécessitent une génération et un téléchargement client.

## Caméra animée

Assignez le rôle `camera` dans `.armature` v2. Les anciens actifs `.bbmodel` utilisent `s_camera`. Seule une caméra peut avoir un rôle. Gardez la rotation d’auteur `[0, 0, 0]` et animez les petits décalages. Ne pas ajouter un yaw de 180 degrés : la compensation de scène est gérée par le moteur de rendu natif. La caméra de l’éditeur est une prévisualisation d’auteur, pas la pose de la caméra-os à l’exécution.

Le chemin de la caméra du pack serveur utilise un effet post-Fabulous et dépend donc du mode graphique et des shaders compatibles du pack de ressources. Le mod Fabric optionnel possède son propre chemin caméra/rendu. Testez sa combinaison graphique/shader sélectionnée.

`render.camera-rotation.rotation-clamp.maximum-degrees` limite la rotation reçue. La correction de zoom recoupe les bords exposés par la rotation. `mode: fixed` maintient un zoom constant ; `dynamic` le varie jusqu’à `maximum-zoom`. `preserve-fov` élargit la projection source en mode fixe, mais le réglage actuel n’est pas compatible avec Sodium et se déplace par défaut sur `false`.

## Vérifiez un réglage

Commencez avec un modèle connu. Modifiez un groupe, lancez le rechargement de la configuration et la génération requise du pack/fournisseur, puis téléchargez le nouveau pack. Comparez debout, marchant, regardant vers le haut/bas et les deux mains. Si la rotation de la caméra échoue, désactivez-la temporairement pour isoler le chemin de projection avant de modifier la hiérarchie des os. Voir [Limitations du client](../mod-client/settings.md) et [Dépannage](../depannage.md).
