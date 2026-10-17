---
description: "Installation & Requirements"
---

# 📥 Installation & Requirements

## Requirements

| Component | Requirement |
| --- | --- |
| Server | Paper/Folia using the Paper 1.21.8 API line or newer; verify the target release before deployment |
| Server Java | Java 25 |
| PacketEvents | Required; current compile/runtime integration targets 2.13.0 |
| BetterModel | Required only for `render.backend: bettermodel`; compatibility integration targets 3.4.1 |
| Resource pack | Players must accept the final pack containing Armature's generated assets |
| Client mod | Optional; exact Fabric target builds from 1.21.8 through 26.2; see the [compatibility matrix](../client-mod/installation.md) |

The pack overlay version range does not extend the server API baseline or the
Minecraft versions supported by the Fabric mod.

## Install

1. Stop the server. Install the distribution `Armature-2.0.0.jar` and PacketEvents
   in `plugins/`. Use the distribution JAR, not the `-plain` or standalone API JAR.
2. Install optional item/gameplay plugins before starting if your profiles use
   their identities or events. Install BetterModel only for that backend.
3. Start the server with Java 25. The default backend is `native`.
4. Read `/armature diagnostics startup` and `/armature status`. Resolve loading
   errors before testing profiles.
5. Generate and deliver the resource pack through your configured provider or
   host the generated standalone ZIP. A file existing on disk does not mean a
   player has downloaded it. See [Resource packs](../configuration/resource-packs.md).
6. Join with a real client, accept the pack, and follow [Your first presentation](first-presentation.md).

## Created files

```text
plugins/Armature/
  config.yml
  messages.yml
  variables.yml
  models/
  profiles/
  armors/
  resource_pack/
```

Embedded files below `default-assets/` are copied on first installation.
Administrator files are preserved. Updates can add new default files; use
`/armature config restore-defaults` to inspect the overwrite preview and repeat
with `--confirm` only when you want those defaults restored.

`internal/` contains generated caches/provider snapshots; it is not an authoring
directory. `resource_pack.zip` and `renderer_pack.zip` depend on the pack settings.

## Updates and downloads

Use the Armature release channel from which you obtained the plugin and the
matching client/Blockbench releases. Keep the previous JAR and administrator
configuration until the new combination is validated. The API artifact is for
compilation, not an additional server plugin.
