---
description: "Settings & Known Limitations"
---

# 🎚️ Settings & Known Limitations

With Mod Menu, open **Mods → Armature Client → Configure**. The current settings
screen exposes one preference: client rendering enabled. Without Mod Menu,
change this file while Minecraft is stopped:

```json
{
  "renderingEnabled": true
}
```

Path: `config/armature-client.json`. The default is `true`. The preference
requests a renderer switch; wait for a coordinated pack/resource reload before
judging the result. Camera and profile settings remain server-owned.

## Compatibility boundaries

| Feature | Current boundary |
| --- | --- |
| Models | Strict `.armature` v2 and compatible `.bbmodel`; no v1 auto-conversion |
| Skins/armor | Skin, equipment and trims depend on the target's rendering adapter and assets |
| Item rendering | Dynamic held-item state; common provider pack may still be required |
| Camera/motion | Server-selected camera, procedural motion and physics; verify parity in game |
| Compositions | Server fallback; arbitrary attachment scenes are not transported to the client |
| Sodium/Iris | Final combinations need runtime testing; compilation does not prove shaderpack compatibility |
| Fabulous/shader effects | Server shader masking and mod rendering are different rendering paths; test the exact graphics mode |

The vanilla preserve-FOV shader route has an explicitly unsupported Sodium
combination. Installing Iris does not establish compatibility with every shader
pack. See [Rendering & Camera](../configuration/rendering-camera.md).

When reporting an issue include the exact Minecraft, Java, Fabric Loader,
Fabric API, client JAR, server plugin, Sodium/Iris and shaderpack versions.
Attach client `latest.log`, server diagnostics, the minimal model/profile and
whether toggling to server fallback changes the symptom.
