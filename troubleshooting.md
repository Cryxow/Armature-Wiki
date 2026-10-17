---
description: "Troubleshooting"
---

# 🩺 Troubleshooting

Start with a minimal profile and a known loaded model. Keep the exact model,
profile, server/client versions and final provider pack that reproduce the
problem. Change one variable at a time.

| Symptom | Check first | Next step |
| --- | --- | --- |
| No presentation | Runtime/player enabled, item identity, selected profile, loaded model, accepted pack | Profile inspection, model inspection, doctor |
| Missing/pink texture | Actual final provider ZIP, namespace/path and target pack definitions | [Resource Packs](configuration/resource-packs.md) |
| Wrong animation | Exact case-sensitive clip name, rule condition/trigger, protected action and cooldown | [Action Rules](getting-started/profiles/actions.md) |
| Wrong movement | Most-specific matching rule, declaration tie order, loop/start/end assets | [Movement Rules](getting-started/profiles/movement.md) |
| Item/arm displaced | Role assignment, pivots, mirrored hand clip, saved bone override | [Bone Roles](getting-started/models/bone-roles.md), [editor](getting-started/in-game-editor.md) |
| Model disappears inside blocks | Anti-clipping and exact shader/graphics path | [Rendering & Camera](configuration/rendering-camera.md) |
| Camera rotation absent | Camera role/element, enabled camera rotation and supported client graphics | Rendering & Camera |
| Skin/armor incorrect | Classic/slim role, player equipment, armor mapping and texture path | [Skins & Custom Armor](advanced/custom-armors.md) |
| Attachment missing | Native backend, referenced attachment ID/bone, condition, depth/entity budget | [Attachments](getting-started/profiles/attachments.md) |
| Renderer changes unexpectedly | Client preference/readiness, composite fallback, pending pack reload | [Client Rendering & Fallback](client-mod/rendering-fallback.md) |
| `.armature` rejected | Strict v2, normal plugin Save, duplicate/invalid roles, exporter diagnostics | [Saving & Deploying](getting-started/models/saving-deploying.md) |

## A short reproducible check

1. Run `/armature diagnostics doctor`; fix errors before adjusting cosmetics.
2. Validate config without applying it: `/armature config validate`.
3. Inspect the held item/profile and model using the commands in
   [Diagnostics](troubleshooting/diagnostics.md).
4. Rebuild Armature/provider assets and accept the provider's final pack.
5. Test after a full server restart where required. A Bukkit `/reload` is not
   proof of a clean startup or renderer/dependency change.
6. Retest with optional client rendering disabled. Record which path fails.

Do not fix a pack failure by changing an already-correct item matcher. Do not
remove a protected action merely to hide a lost reload/end signal. Find the
first incorrect step in item identity → selection → model → pack → rendering.
