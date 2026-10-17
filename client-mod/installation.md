---
description: "Installation & Compatibility"
---

# 📲 Installation & Compatibility

The Fabric mod is **optional**. Players without it keep the native server
renderer. Use the client JAR built for the exact Minecraft release in this
table; a single universal JAR is not provided. These are the standalone source
build targets. Final 2.0.0 release pairings must be checked before publication.

| Minecraft | Runtime Java | Fabric API used for the build | Optional Mod Menu |
| --- | --- | --- | --- |
| 1.21.8 | 21+ | 0.136.1+1.21.8 | 15.x |
| 1.21.9 | 21+ | 0.134.1+1.21.9 | 16.x |
| 1.21.10 | 21+ | 0.138.4+1.21.10 | 16.x |
| 1.21.11 | 21+ | 0.141.6+1.21.11 | 17.x |
| 26.1 | 25+ | 0.145.1+26.1 | 18.x |
| 26.1.1 | 25+ | 0.145.4+26.1.1 | 18.x |
| 26.1.2 | 25+ | 0.155.3+26.1.2 | 18.x |
| 26.2 | 25+ | 0.161.0+26.2 | 20.x |

## Install

1. Install Fabric Loader **0.19.5 or later**, using the Java version above.
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) for that release.
3. Download the matching JAR from [Armature Client releases](https://github.com/Cryxow/ArmatureClientMod/releases).
   Put exactly one Armature Client JAR in the instance's `mods/` directory.
4. Optionally install [Mod Menu](https://modrinth.com/mod/modmenu), using the table.
5. Join an Armature server with the native backend selected and
   `render.client-mod.enabled: true`. Accept its required common resource pack.

The current matching server/client sources use protocol **9**. Protocol number
is a transport contract, not the plugin version or `.armature` format version.
A client built for another protocol is not automatically compatible.

## Verify

Equip the first-presentation sword, then toggle the client renderer off and on
in its settings. Wait for any requested resource reload. Both paths should show
the same held item, equip/action state and skin. Repeat after reconnecting with
an empty client model cache. Check server diagnostics for client readiness.

The mod remains preview software until the final release has passed the runtime
matrix. Snapshots, NeoForge and Minecraft releases outside the table are not
supported by these Fabric builds. Client support does not certify a Paper/Folia
server build for every corresponding Minecraft release.
