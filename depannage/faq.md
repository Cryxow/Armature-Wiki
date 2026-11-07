---
description: "FAQ et compatibilité"
---

# ❓ FAQ et compatibilité

## Armature implémente-t-il le gameplay d’armes ?

Non. Les dégâts, l’ammunition, les projectiles et la propriété du gameplay restent gérés par leur plugin ou script. Armature gère uniquement la présentation. Son adaptateur WeaponMechanics pour les entrées de rechargement peut annuler cet événement fourni lorsqu’une règle configurée le consomme. Les signaux personnalisés génériques ne retournent que des métadonnées de routage.

## BetterModel est-il requis ?

Non pour le rendu natif. Le natif est le moteur principal par défaut de la version 2.0.0. BetterModel reste un backend de compatibilité optionnel et est prévu pour être déprécié dans des versions ultérieures. Il ne peut pas charger les modèles stricts natifs `.armature` v2 ou les compositions.

## Chaque joueur doit-il installer le mod client ?

Non. Le moteur de rendu du serveur prend en charge les joueurs sans lui. Le mod Fabric optionnel utilise des JAR Minecraft cibles exactes et un protocole correspondant. Les profils composites retournent actuellement au rendu du serveur. Consultez le tableau de compatibilité client.

## Puis-je garder les fichiers `.bbmodel` ?

Les modèles legacy compatibles restent supportés. Leurs conventions de nommage diffèrent des rôles v2 explicites. `.armature` v1 est rejeté sans conversion automatique. Sauvegardez/reconstruisez avec l’éditeur actuel et vérifiez les rôles/clip avant de changer de format.

## Pourquoi une variable fonctionne-t-elle dans un seul profil ?

Les profils disque modernes utilisent un seul `plugins/Armature/variables.yml` global. Les variables locales à l’intérieur des profils, des variantes ou des instances montées ne sont pas acceptées. Vérifiez la déclaration, la source, le type, le repli et l’intervalle de rafraîchissement. Les variables YAML ne sont pas automatiquement des variables de modèle Molang.

## Pourquoi la règle de mouvement première n’est-elle pas toujours sélectionnée ?

Les règles de mouvement correspondent d’abord par spécificité des conditions, puis par ordre de déclaration en cas d’égalité. Les variantes utilisent leur propre sélection du premier éligible. Ne considérez pas toutes les listes de règles comme utilisant le même algorithme de sélection.

## Pourquoi une animation de déséquipement garde-t-elle l’ancien objet visible ?

Une déséquipement sortant accepté possède son propre modèle, pose et visuels d’objet jusqu’à sa fin. Sans déséquipement sortant accepté, la présentation ancienne est supprimée immédiatement. Cela empêche un nouvel objet tenu de remplacer la visualisation sortante.

## Un aperçu Blockbench est-il une preuve de comportement dans le jeu ?

Non. Il aide à inspecter les rôles, objets et clips. Il ne simule pas la physique des os du serveur, tous les prédicats d’objet des fournisseurs, la livraison des packs ou tous les pipelines graphiques. Exécutez le tutoriel avec les artefacts finaux avant publication.

## Envoie-t `/armature reload pack` le pack du fournisseur ?

Il prépare les actifs du pack et le flux de travail du pont d’Armature. Le fournisseur configuré possède le pack final et la livraison. Terminez son flux de travail et inspectez/acceptez le résultat. Consultez [Packs de ressources](../configuration/resource-packs.md).
