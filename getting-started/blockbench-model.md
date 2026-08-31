# 🧸 BlockBench Model

Armature renders BetterModel `.bbmodel` assets as first-person presentations. Put model files in `plugins/Armature/models/`. The filename becomes the BetterModel model id, so `fp_rifle.bbmodel` is referenced as `fp_rifle`.

## Requirements

* Install [Blockbench](https://www.blockbench.net/).
* Install the [Cameras plugin](https://github.com/JannisX11/blockbench-plugins/blob/master/plugins/cameras.js) if you want an in-editor first-person preview.
* Use BetterModel's model format and animation conventions.

Starting from a copy of the generated `fp_empty_hands.bbmodel` or another known-good Armature model keeps the player-arm hierarchy and camera preview ready.

## Required model structure

Use a top-level group named `root`. With `render.model-adaptation: true`, Armature adapts standard first-person models in memory before BetterModel loads them: the hierarchy is shifted for the carrier and `root` receives the first-person rotation. The source file is never rewritten.

Place the `root` pivot at the horizontal center of the model: its `X` and `Z` position must be centered, while its `Y` height can be anywhere. Keep `root` fixed during every animation; do not translate or keyframe its position. Armature uses it as the model's structural anchor.

<figure><img src="../.gitbook/assets/image (5).png" alt="" width="563"><figcaption><p>Preview of the default model inside Blockbench</p></figcaption></figure>

If an animation must move the complete model, add a separate bone directly under `root`, parent all model bones/elements to that child, and animate the child instead. This keeps `root` available as the stable anchor required by the first-person adaptation.

Disable model adaptation only when the model already uses a non-standard root hierarchy or its own completed camera transform:

```yaml
render:
  model-adaptation: false
```

Armature recognizes the conventional BetterModel player-bone prefixes:

```
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
```

## Held item bones

The conventional `pli_left_item` and `pri_right_item` bones are used for held-item presentation. Keep them when you want Armature to attach the held item automatically. Remove them when the weapon is modeled directly into the asset, as in a rifle model.

For a player-specific weapon model, bind it through a profile:

```yaml
m4a1:
  match:
    item: weaponmechanics:m4a1
  model:
    name: fp_rifle
```

The model name must match the asset filename exactly. The profile controls item matching, hand masking, animations, and optional provider attachments.

## Animation names

Do not create Armature animations with BetterModel's reserved automatic names:

* `idle`
* `walk`
* `idle_fly`
* `walk_fly`
* `spawn`
* `jump`

Armature rejects those names in profile animation definitions because BetterModel can play them automatically. Use names such as `armature_idle`, `armature_walk`, `armature_jump`, `fire`, or `reload`.

Modern profiles refer to these assets under an explicit channel:

```yaml
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
```

See [Profiles](profiles/) for the full schema and [Built-in triggers](profiles/triggers.md) for the event names.

<figure><img src="../.gitbook/assets/image (6).png" alt="" width="563"><figcaption><p>Preview of an animation inside Blockbench.</p></figcaption></figure>

<figure><img src="../.gitbook/assets/image (7).png" alt="" width="563"><figcaption><p>In-game render of the animation.</p></figcaption></figure>

## Camera marker \[NOT WORKING]

Add a bone named `s_camera` when using the optional camera-rotation pipeline. Animate this bone to define the camera basis that Armature transports through the generated marker assets. The feature is disabled by default:

```yaml
render:
  camera-rotation:
    enabled: true
    debug-mode: off
```

This is a client resource-pack path and requires a supported Fabulous client. It is independent from the default viewmodel Y-lock.

{% hint style="danger" %}
Enabling this will result in a glitching player viewport.
{% endhint %}

## Effects animator

Armature registers BetterModel script builders for sound and particle keyframes. Add an `Effects` animator in Blockbench, insert a script keyframe, and use lines such as:

```
sound{sound=block.anvil.break;locator=muzzle;volume=1;pitch=1}
particle{locator=muzzle;type=CRIT;count=8;dx=0.02;dy=0.02;dz=0.02}
```

`locator` is a named bone; `origin` uses the model origin. The default `audience=viewer` keeps a first-person effect private to the model viewer. Use `audience=nearby` or `audience=world` for effects visible to other players.

The locator follows the visible first-person model transform. The sound id and any custom sound or particle assets must also exist in the resource pack.

## Validate the asset

After adding or changing a model:

1. Run `/armature reload models` or `/armature reload all`.
2. Check the reload summary for imported models and unavailable animations.
3. Apply the generated resource pack to the client.
4. Test the held item, profile transitions, action animations, and effects with one player.

Missing model or animation references fail closed and are reported as non-fatal diagnostics; Armature does not mutate the gameplay item.
