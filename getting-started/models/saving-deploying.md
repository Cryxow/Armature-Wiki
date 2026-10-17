---
description: "Saving & Deploying Models"
---

# 💾 Saving & Deploying Models

## Save the source

Use **File → Save / Save As** or the normal shortcut. There is no separate
Armature export/save panel. Incremental saving opens Save As. Reopen the file
and verify geometry, hierarchy, animations, roles and Physics values.

The document must identify `meta.model_format: armature_fpv`,
`armature.renderer: native` and `armature.format_version: 2`.
The model-format version is independent of the plugin release `2.0.0`.
Keep the original editable source under version control or in your asset backup.

## Install on the server

Copy `my_rifle.armature` to `plugins/Armature/models/` or a subdirectory.
Reference `model.name: my_rifle` in a profile. Model IDs derive from filenames
without extension; keep IDs unique even across different subdirectories.
Compatible `.bbmodel` assets can live beside v2 assets under the native backend.

Use `/armature diagnostics models reload` for source changes. If clips or bones
referenced by a profile changed, reload profiles too; `/armature reload all`
applies the complete Armature candidate.

## Generate and deliver

Rebuild the Armature pack, run your provider's full generation workflow and make
the player download the final pack. Optional client bundles and server renderer
assets have different transport paths; test with and without the client mod.
See [Resource packs](../../configuration/resource-packs.md).

## Common validation failures

| Diagnostic | Correction |
| --- | --- |
| Unsupported .armature version | Recreate v1 as v2; do not just rename or edit the header |
| Unknown role/non-boolean physics | Assign a supported role and use a boolean Physics value |
| Multiple camera roles | Keep a single runtime camera role |
| Missing clip or mount bone | Update the profile/reference or add the authored asset |
| Duplicate model/clip IDs | Choose unique identifiers |
| Reserved `__armature_` node name | Rename the authored node |

Library validation checks inactive variants and accessory overrides too. Fix the
whole candidate; a branch that is currently invisible still needs valid assets.
Keep the last known-good pack until the new revision has been downloaded and
visually checked.
