---
description: "Installer l’extension Armature pour Blockbench"
---

# 🔌 Installer l’extension Armature pour Blockbench

Utilisez **Blockbench Desktop 5.0+**, le plugin officiel **Cameras** et une
version de l’extension Armature compatible avec le format de votre modèle.
Cette extension sert à créer des modèles. Le plugin serveur Minecraft et le
mod client s’installent séparément.

## Installer depuis un fichier

1. Récupérez `armature_blockbench.js` dans la distribution Armature Blockbench
   fournie avec vos outils.
2. Ouvrez **Plugins → Load Plugin from File** et sélectionnez le fichier
   JavaScript.
3. Laissez le plugin officiel **Cameras** activé pour l’aperçu dans l’éditeur.
4. Vérifiez que **Armature FPV Model** apparaît à la création d’un projet et
   que le menu **Armature** est disponible pour ce format.

Si vous compilez depuis les sources, `npm run verify` génère
`dist/armature_blockbench.js` et le squelette v2 par défaut. Le fichier
distribué est prêt à installer.

## Vérifier l’installation

Créez [votre premier modèle](../modele-blockbench.md). Faites un clic droit
sur un os pour vérifier la présence de **Armature → Assign Role** et de
**Physics**. L’enregistrement crée un fichier `.armature` au format v2.
Si le menu manque, vérifiez le format du projet avant de réinstaller
l’extension.

Les captures montrent les outils de création dans l’interface web de
Blockbench. Utilisez Blockbench Desktop pour installer le fichier,
enregistrer un modèle et vérifier sa réouverture.
