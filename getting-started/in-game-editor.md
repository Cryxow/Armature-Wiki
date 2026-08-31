# In-game bone editor

Armature 1.3.0 includes an administrator-only in-game editor for correcting
the position and rotation of BetterModel bones without reopening Blockbench.
The editor applies a live presentation correction; it does not rewrite the
<code>.bbmodel</code> file or change the item, inventory, or gameplay state.

## Requirements

Before opening the editor, make sure:

* You are running Paper or Folia on the Paper 1.21.8 API line or newer.
* You have the <code>armature.command.editor</code> permission. It is included
  in <code>armature.admin</code>; both permissions default to operators.
* The item is in your main hand and resolves to an active Armature profile.
* The active BetterModel model exposes at least one bone.

The editor is player-only. It cannot be opened from the console. It edits the
profile and item currently resolved from the main-hand item.

## Open the editor

Hold the item you want to correct, then run:

~~~text
/armature editor
~~~

<code>/arm editor</code> works as the command alias. Armature synchronizes the
held model before opening the Paper Dialog. If the item has no active profile,
or the model has no editable bones, the command reports the problem and does
not open an empty editor.

Only one administrator can edit a given profile/item scope at a time. The
scope is the active profile id combined with the resolved item identity. This
prevents two editors from overwriting each other's live corrections.

## Main dialog

The main dialog displays the current item, profile, model, and selected bone.
It contains six numeric controls:

| Control | Range | Step | Unit |
| --- | ---: | ---: | --- |
| Position X/Y/Z | <code>-64</code> to <code>64</code> | <code>0.05</code> | Blockbench model units (pixels) |
| Rotation X/Y/Z | <code>-180</code> to <code>180</code> | <code>1</code> | Degrees |

Position values use Blockbench model units. Armature converts them to
model-space blocks by dividing by <code>16</code> when applying the correction.
Rotation values remain degrees.

The selected bone is read from the active BetterModel model and sorted
alphabetically. Models with more than 32 bones use multiple selection pages.
Use the <code>Bone: ...</code> button to open the selector, then:

1. Select a bone.
2. Use <code>Previous page</code> or <code>Next page</code> when needed.
3. Press <code>Confirm</code>.
4. Adjust that bone's position or rotation in the main dialog.

Use the stable <code>root</code> bone as the model anchor. Do not use the
editor, or an animation, to translate <code>root</code>. If the complete model
needs a correction, create a child bone directly below <code>root</code>,
parent the model to that child, and edit the child instead. See
[Blockbench model](blockbench-model.md) for the required hierarchy.

## Apply, save, reset, and close

The dialog provides these actions:

| Action | Effect |
| --- | --- |
| <code>Apply</code> | Applies the current live correction to every online player using the same profile/item scope. |
| <code>Save item</code> | Persists the current live corrections for this exact resolved item and profile. |
| <code>Save profile</code> | Persists the current live corrections for the whole profile. |
| <code>Reset changes</code> | Removes unsaved live corrections for this scope and reveals the saved profile/item corrections again. |
| <code>Close</code> | Closes the editor and discards unsaved live corrections. |

<code>Apply</code> is a preview and is not persistent. Use <code>Save
item</code> for an item-specific correction, or <code>Save profile</code> for
a correction shared by every item resolved to that profile.

The dialog disables closing with Escape. Use its <code>Close</code> button.
Changing the held item or active profile also closes the editor and discards
its unsaved live corrections.

## Override priority

Armature combines corrections in this order:

1. Profile corrections provide the base pose.
2. Item corrections for the exact profile/item scope override profile values
   for the same bone.
3. Live unsaved corrections override both while the editor is open.

For example, a profile correction can position a rifle for every weapon using
the <code>m4a1</code> profile, while an item correction fine-tunes only
<code>weaponmechanics:m4a1</code>.

Saving a correction does not change the gameplay plugin's item data. It only
changes the BetterModel presentation currently rendered by Armature.

## Persistent file

Saved corrections are kept separately from profile YAML:

~~~text
plugins/Armature/editor-overrides.yml
~~~

The current file format is version <code>2</code>:

~~~yaml
version: 2
profiles:
  m4a1:
    bones:
      weapon:
        position: [0.0, 1.25, -0.5]
        rotation: [0.0, 4.0, 0.0]
items:
  m4a1:
    weaponmechanics:m4a1:
      bones:
        weapon:
          position: [0.0, 2.0, -0.25]
          rotation: [0.0, 0.0, 0.0]
~~~

Each bone requires three finite numbers in <code>position</code> and three
finite numbers in <code>rotation</code>. The file is loaded when Armature
starts. If you edit it manually, stop the server first and restart Armature
afterward; <code>/armature reload</code> does not reload this store.

<code>Reset changes</code> only removes unsaved live corrections. It does not
delete entries already saved in <code>editor-overrides.yml</code>. To remove a
persistent correction, remove its bone entry from the file while the server is
stopped, then restart.

Older override files using version <code>1</code> are migrated automatically
to Blockbench model units when loaded.

## Recommended workflow

1. Finish the model hierarchy in Blockbench. Keep <code>root</code> centered
   horizontally and fixed during animations.
2. Put the model in <code>plugins/Armature/models/</code> and bind it to an
   active profile.
3. Run <code>/armature reload models</code> or
   <code>/armature reload all</code>, then apply the generated resource pack.
4. Hold the matching item in your main hand.
5. Run <code>/armature editor</code>.
6. Select a bone and adjust its six values.
7. Press <code>Apply</code> to check the correction on all online players using
   that scope.
8. Press <code>Save item</code> or <code>Save profile</code>.
9. Press <code>Close</code> when finished.

Use the editor for static placement corrections. Use Blockbench for mesh
hierarchy, pivots, authored animation movement, and permanent model changes.
After changing the model itself, reload the model and remove obsolete saved
overrides if they should no longer apply.

## Common messages

| Message | Cause |
| --- | --- |
| <code>Hold an item assigned to an active Armature profile.</code> | Main-hand item has no active profile. |
| <code>This item is already being edited by another admin.</code> | Another administrator owns the same profile/item scope. |
| <code>The active BetterModel model exposes no editable bones.</code> | The model has no bone available to the editor. |
| <code>Editor closed because the held item or profile changed.</code> | The live editing scope changed; unsaved values were cleared. |

The editor remains presentation-only. It never replaces the held item, changes
WeaponMechanics or another gameplay provider, or cancels gameplay events.
