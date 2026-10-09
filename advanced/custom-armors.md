---
description: "Skins & Custom Armor"
---

# Skins & Custom Armor

## Player skins

Body-role bones use the player's actual classic/slim skin and its second layer.
Blockbench uses a neutral bundled preview skin; it is not serialized into the
project as authored cubes. Classic and slim arm widths differ, so test both.
Authored cubes remain part of `.armature` v2 and are not automatic skin helpers.

## Equipped armor with native .armature v2

| Role | Equipment layer |
| --- | --- |
| `head` | Helmet |
| `body` | Chestplate and leggings waist |
| Full arms, upper arms, forearms | Chestplate sleeves |
| Full legs, thighs, lower legs | Independent leggings and boots layers |

Equipment updates include replacement, removal and changed item metadata.
Duplicate roles have independent visible meshes. Dye and trim presentation uses
the armor assets/palettes; armor visibility does not change gameplay protection.
Split meshes follow their own bone poses and omit internal joint caps.

Legacy `.bbmodel` arm overlays keep their legacy templates. BetterModel resolves
its own armor types and textures. The optional client mod uses its client-side
equipment/render path: do not assume it can consume a custom texture known only
to the server's armor-type resolver.

## Map custom items

Add this optional section to `config.yml` (use actual provider item IDs):

```yaml
armor-mappings:
  "itemsadder:my_pack:knight_helmet": knight
  "itemsadder:my_pack:knight_chestplate": knight
  "itemsadder:my_pack:knight_leggings": knight
  "itemsadder:my_pack:knight_boots": knight
```

The value is an armor texture type. For native rendering, store
`armor.png` and `leggings.png` under
`plugins/Armature/armors/armors/knight/`. The chestplate/helmet/boots texture uses
`armor.png`; the leggings layer uses `leggings.png`. Keep the expected armor UV
layout. A mapping may target a custom type or override a vanilla type.

With the BetterModel compatibility backend, the corresponding directory is
`plugins/BetterModel/armors/armors/knight/`. Do not assume a file in one renderer's
directory is automatically installed for the other renderer.

## Apply and verify

Run `/armature config reload` after mappings change, rebuild Armature assets,
regenerate the provider final pack and download it. Test each equipment slot,
mixed leggings/boots materials, dyed leather, trims, removal and moving joints.
If the texture is missing, check both the selected identity/type and the final
pack references. [Bone roles](../getting-started/models/bone-roles.md) explains
which body layers are generated.
