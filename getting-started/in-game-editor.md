---
description: "In-game Bone Editor"
---

# 🎛️ In-game Bone Editor

The editor changes presentation offsets without editing your source model or
profile. Hold an item with an active profile and run `/armature editor` with
`armature.command.editor` permission.

```text
/armature editor open
/armature editor close [--confirm]
/armature editor reset [item|profile] [--confirm]
/armature editor export [item|profile]
/armature editor undo
/armature editor redo
/armature editor set <bone> <pos|rot> <x> <y> <z>
/armature editor diff [item|profile]
/armature editor mirror <left|right>
```

Bare `/armature editor` is the shortcut for `editor open`. The editor requires
a player holding an item with an active Armature profile and editable model
bones. Its Dialog exposes sliders plus a visible `temporary preview` state.
`editor set` provides exact numeric position (pixels) or rotation (degrees)
entry when a client does not offer text entry in the Dialog.

`undo` and `redo` keep a bounded (50 change) session history. `diff` prints the
authored model baseline, the unsaved `temporary preview`, and the persisted
`item-override` or `profile-override`. `mirror left` copies corresponding
right-side values into left bones; `mirror right` does the inverse and mirrors
position/rotation signs across the X axis.

`reset` prints a destructive `item-override`/`profile-override` preview and
requires `--confirm`; it removes persisted values for that scope and refreshes
live presentation. `close` blocks while the `temporary preview` is dirty;
repeat it with `--confirm` to discard those unsaved values. `export` emits a
profile-ready YAML fragment for `editor-overrides.yml`, includes the dirty
summary, and never writes source profile configuration.

All three value layers are explicit in editor messages: `temporary preview`
is live and unsaved, `item-override` is persisted for one item/profile scope,
and `profile-override` is persisted for every item resolved to that profile.
Saving an override or applying a preview changes presentation only. Source
profile files are never mutated by the editor.

Permission: `armature.command.editor`.

## Stored overrides

Operators with `armature.command.editor` can run `/armature editor` while
holding an item assigned to a profile. The Dialog selects an editable model bone
and exposes position and rotation sliders. Applying a change updates every
online player using that same item/profile scope.

Overrides are persisted separately in `plugins/Armature/editor-overrides.yml`:

```yaml
version: 2
profiles:
  m4a1:
    bones:
      root:
        position: [0.0, 1.0, 0.0]
        rotation: [0.0, 2.0, 0.0]
items:
  m4a1:
    weaponmechanics:m4a1:
      bones:
        root:
          position: [0.0, 2.0, 0.0]
          rotation: [0.0, 0.0, 0.0]
```

Profile values are inherited first, item values override them, and unsaved
live values have the highest priority. `Save item` affects only the held item;
`Save profile` affects all items resolved to the profile. These corrections
change presentation only and do not modify gameplay or inventory state.
Position values use Blockbench model units (pixels) and are converted to
model-space blocks with `/16`; rotation values use degrees in the usual
`-180..180` range.

## Verify a correction

Use `editor diff` before saving. Save to item scope for one identity or profile
scope for every matching item, then reopen the editor and confirm persistence.
Test both hands, equip/unequip and all players sharing that scope. Export is a
YAML fragment for the override file, not a saved `.armature` project.
