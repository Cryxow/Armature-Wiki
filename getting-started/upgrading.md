---
description: "Upgrading to 2.0.0"
---

# 🔄 Upgrading to 2.0.0

## Before replacing the JAR

Back up `plugins/Armature/`, your provider configuration/final pack, and the
previous plugin versions. Perform the upgrade on a test copy first, with the
same client and resource-pack setup you intend to publish.

## Renderer selection

New installations use `render.backend: native`. An existing explicit
`bettermodel` selection is retained. Set the option to `native` and fully restart
to switch engines; a configuration reload cannot replace the active renderer backend.
BetterModel remains available in 2.0.0, with deprecation planned for later versions.

## Configuration and commands

The current embedded schema is `config-version: 17`. Armature manages this value
and fills missing defaults while preserving administrator values. Compare the
[current reference](configuration.md), especially nested visibility/viewmodel,
vanilla-hiding, motion, client-mod and pack-generation settings. Do not copy an
old complete configuration over the new defaults or change the schema number to
avoid validation.

Use `/armature config validate` and the [current command domains](commands-and-permissions.md).
`/armature reload all` reloads Armature; it does not run the external provider's
generation command. The server-wide `/reload` is not an upgrade procedure.

## Profiles and shared variables

Legacy and modern files can coexist. New files should use anonymous movement,
action and additive rules. Compare your profiles with the complete modern
examples before enabling the new configuration.

Move **all YAML variable declarations** into `plugins/Armature/variables.yml`.
Resolve duplicate IDs yourself and remove local `variables` sections from
profiles, variants and accessory instances. Local declarations are rejected;
Armature does not overwrite your shared registry.
Java `ArmaturePresentation` requests retain their inline-variable API contract.

### Convert a legacy profile

After moving any local variables into the shared registry, use a backed-up
legacy file under `plugins/Armature/profiles/`:

```text
/armature config migrate sword.yml --output sword.migrated.yml
```

The converter runs locally and writes a validated modern YAML candidate;
the source and active runtime stay unchanged. Read the command's warnings and
manual actions, compare the output against [Profile Structure](profiles/README.md),
and verify that every model and clip exists. Source and candidate files are
limited internally to 256 KiB. No migration settings are exposed in `config.yml`;
obsolete `migration` blocks are removed on startup with a configuration backup.

Move the original outside the loaded profiles directory before activating its
replacement, so duplicate profile IDs cannot compete. Then run
`/armature profile reload` and `/armature profile inspect <player>`, equip the
matching item and test the movement/action clips. A `.migrated.yml` file is still
a loadable profile: review it before issuing any profile/full reload.

## Models

- Existing compatible `.bbmodel` assets remain supported.
- New Armature Blockbench projects save strict `.armature` **format version 2**.
- `.armature` v1 is refused. Recreate the project and assign roles explicitly;
  changing the extension or version number is not conversion.
- Native clips may use names such as `idle` and `walk`. BetterModel retains its
  reserved-name restrictions.
- `.armature` v2 does not synthesize mirrored left-hand clips or infer roles
  from bone names. See [Legacy compatibility](models/legacy-models.md).

## Complete the upgrade

Restart, load the complete model/profile library, regenerate the provider's
final pack and make the client download it. Upgrade the optional Fabric mod to
the matching protocol build. Test both hands, equip/unequip, protected actions,
skins/armor, camera, reconnect and world changes before replacing production.
The 1.3.0 documentation is preserved in the wiki repository's `wiki-v1.3.0` tag.
