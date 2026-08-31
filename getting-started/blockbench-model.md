# Blockbench model

Armature renders BetterModel <code>.bbmodel</code> assets as first-person
presentations. Put model files in
<code>plugins/Armature/models/</code>. The filename becomes the BetterModel
model id, so <code>fp_rifle.bbmodel</code> is referenced as
<code>fp_rifle</code>.

## Requirements

* Install [Blockbench](https://www.blockbench.net/).
* Install the [Cameras plugin](https://github.com/JannisX11/blockbench-plugins/blob/master/plugins/cameras.js) if you want an in-editor first-person preview.
* Use BetterModel's model format and animation conventions.

Starting from a copy of the generated <code>fp_empty_hands.bbmodel</code> or
another known-good Armature model keeps the player-arm hierarchy and camera
preview ready.

## Required model structure

Use a top-level group named <code>root</code>. With
<code>render.model-adaptation: true</code>, Armature adapts standard
first-person models in memory before BetterModel loads them: the hierarchy is
shifted for the carrier and <code>root</code> receives the first-person
rotation. The source file is never rewritten.

Place the <code>root</code> pivot at the horizontal center of the model: its
<code>X</code> and <code>Z</code> position must be centered, while its
<code>Y</code> height can be anywhere. Keep <code>root</code> fixed during every
animation; do not translate or keyframe its position. Armature uses it as the
model's structural anchor.

If an animation must move the complete model, add a separate bone directly
under <code>root</code>, parent all model bones/elements to that child, and
animate the child instead. This keeps <code>root</code> available as the stable
anchor required by the first-person adaptation.

Disable model adaptation only when the model already uses a non-standard root
hierarchy or its own completed camera transform:

~~~yaml
render:
  model-adaptation: false
~~~

Armature recognizes the conventional BetterModel player-bone prefixes:

~~~text
head (ph)
right arm (pra)
right forearm (prfa)
left arm (pla)
left forearm (plfa)
hip (phip)
waist (pw)
chest (pc)
right leg (prl)
right foreleg (prfl)
left leg (pll)
left foreleg (plfl)
left item (pli)
right item (pri)
cape (cape)
~~~

## Held item bones

The conventional <code>pli_left_item</code> and
<code>pri_right_item</code> bones are used for held-item presentation. Keep
them when you want Armature to attach the held item automatically. Remove them
when the weapon is modeled directly into the asset, as in a rifle model.

For a player-specific weapon model, bind it through a profile:

~~~yaml
m4a1:
  match:
    item: weaponmechanics:m4a1
  model:
    name: fp_rifle
~~~

The model name must match the asset filename exactly. The profile controls
item matching, hand masking, animations, and optional provider attachments.

## Animation names

Do not create Armature animations with BetterModel's reserved automatic names:

* <code>idle</code>
* <code>walk</code>
* <code>idle_fly</code>
* <code>walk_fly</code>
* <code>spawn</code>
* <code>jump</code>

Armature rejects those names in profile animation definitions because
BetterModel can play them automatically. Use names such as
<code>armature_idle</code>, <code>armature_walk</code>,
<code>armature_jump</code>, <code>fire</code>, or
<code>reload</code>.

Modern profiles refer to these assets under an explicit channel:

~~~yaml
animations:
  movement:
    - when: player.moving == true
      animation:
        name: armature_walk
        loop: true
  actions:
    - trigger: weaponmechanics:fire
      animation:
        name: fire
        duration: 4t
~~~

See [Profiles](profiles/README.md) for the full schema and
[Built-in triggers](profiles/triggers.md) for the event names.

## Camera marker

Add a zero-size bone named <code>s_camera</code> when using the optional
camera-rotation pipeline. Animate this bone to define the camera basis that
Armature transports through the generated marker assets. The feature is
disabled by default:

~~~yaml
render:
  camera-rotation:
    enabled: true
    debug-mode: off
~~~

This is a client resource-pack path and requires a supported Fabulous client.
It is independent from the default viewmodel Y-lock.

## Effects animator

Armature registers BetterModel script builders for sound and particle
keyframes. Add an <code>Effects</code> animator in Blockbench, insert a script
keyframe, and use lines such as:

~~~text
sound{sound=block.anvil.break;locator=muzzle;volume=1;pitch=1}
particle{locator=muzzle;type=CRIT;count=8;dx=0.02;dy=0.02;dz=0.02}
~~~

<code>locator</code> is a named bone; <code>origin</code> uses the model
origin. The default <code>audience=viewer</code> keeps a first-person effect
private to the model viewer. Use <code>audience=nearby</code> or
<code>audience=world</code> for effects visible to other players.

The locator follows the visible first-person model transform. The sound id and
any custom sound or particle assets must also exist in the resource pack.

## Validate the asset

After adding or changing a model:

1. Run <code>/armature reload models</code> or
   <code>/armature reload all</code>.
2. Check the reload summary for imported models and unavailable animations.
3. Apply the generated resource pack to the client.
4. Test the held item, profile transitions, action animations, and effects
   with one player.

Missing model or animation references fail closed and are reported as
non-fatal diagnostics; Armature does not mutate the gameplay item.
