---
description: "Installing the Armature Blockbench Plugin"
---

# 🔌 Installing the Armature Blockbench Plugin

Use **Blockbench Desktop 5.0+**, the official **Cameras** plugin, and the Armature
Blockbench extension matching your model-format workflow. The extension is a
model authoring tool; it is separate from the Minecraft server plugin and client mod.

## Install from file

1. Obtain the current `armature_blockbench.js` distribution from the Armature
   Blockbench release supplied with your toolchain.
2. Open **Plugins → Load Plugin from File** and select that JavaScript file.
3. Keep the official **Cameras** plugin enabled for the editor preview.
4. Check that **Armature FPV Model** is available when creating a project and
   the **Armature** menu appears for that format.

For source builds, `npm run verify` creates `dist/armature_blockbench.js` and
the v2 default rig. Building the extension is not required to use its distribution.

## Check the installation

Create [your first model](../blockbench-model.md), right-click a bone and confirm
**Armature → Assign Role** plus **Physics** are available. Saving should create
a `.armature` document with format version 2. If the menu is absent, verify the
current project format before reinstalling.

The documentation screenshots use the current plugin in Blockbench's web UI
to show the authoring controls. Desktop remains the target for file-based
installation and save/reopen validation.
