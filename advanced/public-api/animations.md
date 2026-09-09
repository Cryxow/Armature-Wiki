# 🎬 Animations

The stable animation API is profile-bound. Armature must already be rendering
the profile that matches the player's held item. A call never changes the
inventory item or mounts a second model.

The model is selected by the profile, while the animation id is resolved from
the profile or, for raw playback, from the loaded model:

| Call | Purpose | Return type |
| --- | --- | --- |
| <code>playAsset</code> | Explicit asset and channel, with a handle | <code>ArmatureOperationResult</code> |
| <code>playAnimation</code> | Finite asset compatibility call | <code>ArmatureAnimationResult</code> |
| <code>playRawAnimation</code> | Finite loaded-model asset without profile enumeration | <code>ArmatureAnimationResult</code> |
| <code>startLoop</code> | Persistent loop, built-in or configured asset | <code>ArmatureAnimationResult</code> or operation result |
| <code>startRawLoop</code> | Persistent loaded-model asset | <code>ArmatureAnimationResult</code> |
| <code>triggerAdditive</code> | Configured additive event, with a handle | <code>ArmatureOperationResult</code> |

## Channels

Use <code>ArmatureAnimationReference</code> for new integrations. It prevents
an animation name from being interpreted as the wrong kind of operation:

| Channel | Meaning | Typical weapon use |
| --- | --- | --- |
| <code>ACTION</code> | Finite, replaceable animation | fire, reload, inspect |
| <code>LOOP</code> | Persistent selected state | idle, aim, sprint |
| <code>ADDITIVE</code> | Layer over the base pose | recoil, breathing, fall |

Resolve against the profile currently rendered for the player:

```java
var api = ArmatureApi.animations();
var playerId = player.getUniqueId();

api.playAsset(playerId, ArmatureAnimationReference.action("inspect"));
api.startLoop(playerId, ArmatureAnimationReference.loop("aim"));
api.triggerAdditive(playerId, ArmatureAnimationReference.additive("recoil"));
```

Or pin the profile id explicitly:

```java
var fire = ArmatureAnimationReference.profile(
    "m4a1", "fire", ArmatureAnimationChannel.ACTION);

api.playAsset(playerId, fire);
```

An explicit profile is still checked against the active held-item profile. If
<code>m4a1</code> is not the profile currently rendered for that player, the
result is <code>PROFILE_NOT_ACTIVE</code>; Armature does not silently replace
the presentation.

## Configure the assets

For a modern profile, direct channel-aware playback resolves names from the
corresponding section. The rule names themselves are not ids; the asset names
inside <code>animation.name</code> are the ids used by the API.

```yaml
m4a1:
  match:
    item:
      material: minecraft:paper
      data:
        custom-model-data: 41001
  model:
    name: fp_rifle
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
      - when:
          - player.sneaking == true
        animation: aim

    actions:
      - trigger: myweapons:m4a1.fire
        animation:
          name: fire
          duration: 4t
          cooldown: 2t
      - trigger: myweapons:m4a1.reload
        animation:
          name: reload
          duration: 35t
      - trigger: myweapons:m4a1.inspect
        animation:
          name: inspect
          duration: 40t

    additives:
      - when:
          - player.sprinting == true
        animation: recoil
        playback: once
        group: weapon
```

For this example, <code>fp_rifle.bbmodel</code> must be loaded by Armature and
contain <code>base</code>, <code>aim</code>, <code>fire</code>,
<code>reload</code>, <code>inspect</code>, and <code>recoil</code> animations.
The profile still controls the model binding and visibility.

## Direct finite playback

Use <code>playAsset</code> when the weapon plugin owns the event and already
knows which clip should play:

```java
public void fireAnimation(Player player) {
    ArmatureApi.tryAnimations().ifPresent(api ->
        api.playAsset(player.getUniqueId(),
                ArmatureAnimationReference.action("fire"))
            .thenAccept(result -> {
                if (!result.accepted()) {
                    plugin.getLogger().fine(
                        "M4A1 visual fire rejected: " + result.status());
                }
            })
    );
}
```

The shorter compatibility calls are useful when a handle is not needed:

```java
api.playAnimation(playerId, "fire");
api.playRawAnimation(playerId, "fire");
```

<code>playAnimation</code> resolves a configured action asset in the active
profile. <code>playRawAnimation</code> uses the active rendered model and does
not require the clip to be enumerated by an action rule. Both still require an
active profile and a loaded model.

## Loops

Built-in loop actions are represented by <code>ArmatureAction</code>, for
example <code>AIM</code>, <code>IDLE</code>, <code>SPRINT</code>, or
<code>CROSSBOW_CHARGE</code>:

```java
api.startLoop(playerId, ArmatureAction.AIM);
api.stopAction(playerId); // Stop only the finite action.
api.stopLoop(playerId);   // Clear the public loop override.
```

For a custom loop asset, use the loop channel:

```java
api.startLoop(playerId, ArmatureAnimationReference.loop("aim"));
api.startRawLoop(playerId, "aim");
```

A selected loop remains active until it is stopped, the item/profile changes,
Armature is reloaded, or the runtime is disabled. Stopping the loop resumes
Armature's automatic movement selection.

## Additive layers

An additive API call references an additive event or its authored animation
name. The profile must declare the additive under
<code>animations.additives</code> (or a legacy additive section):

```java
api.triggerAdditive(playerId,
        ArmatureAnimationReference.additive("recoil"))
    .thenAccept(result -> {
        if (result.accepted()) {
            result.handle().ifPresent(handle -> {
                // Store the handle if this layer must be stopped explicitly.
            });
        }
    });
```

<code>ADDITIVE</code> is not an alternate action mode. Armature composes it
over the base pose, while finite actions remain on the exclusive action layer.

## Handles and cancellation

Channel-aware calls return an <code>ArmatureOperationResult</code>. An accepted
operation has a monotonically increasing token and an optional
<code>ArmatureAnimationHandle</code>:

```java
api.playAsset(playerId, ArmatureAnimationReference.action("reload"))
    .thenAccept(result -> result.handle().ifPresent(handle -> {
        activeReloadHandles.put(playerId, handle);
    }));

// Later, stop only the operation represented by this handle.
var handle = activeReloadHandles.remove(playerId);
if (handle != null) {
    api.stop(playerId, handle);
}
```

Handles are scoped to the player, profile, channel, and token. If a newer
operation replaced the old one, stopping the old handle returns
<code>HANDLE_NOT_ACTIVE</code> and cannot cancel the newer operation.

Compatibility calls provide simpler cancellation:

```java
api.stopAction(playerId);
api.stopLoop(playerId);
```

An action with <code>can-be-cancelled: false</code> may return
<code>NOT_CANCELABLE</code> when a replacement or stop is attempted.

## Results

All result types use <code>ArmatureAnimationResult.Status</code>. The most
useful values are:

| Status | Meaning |
| --- | --- |
| <code>STARTED</code> | A finite action or additive was accepted |
| <code>LOOP_UPDATED</code> | A loop was selected or changed |
| <code>STOPPED</code> | The requested operation was stopped |
| <code>COOLDOWN</code> | The action rule is still on cooldown |
| <code>NOT_CANCELABLE</code> | The current action rejects cancellation |
| <code>NO_PROFILE</code> | No matching active profile exists |
| <code>PROFILE_NOT_ACTIVE</code> | An explicit profile is not the active one |
| <code>NO_ANIMATION</code> | The asset is not declared or available |
| <code>NOT_RENDERED</code> | Armature cannot render the requested presentation |
| <code>PLAYER_UNAVAILABLE</code> | The player cannot be scheduled |
| <code>DISABLED</code> | Armature runtime or player presentation is disabled |
| <code>RENDERER_REJECTED</code> | The renderer rejected the request |
| <code>HANDLE_NOT_ACTIVE</code> | A handle is stale or already replaced |

Use <code>accepted()</code> instead of comparing only one status. Accepted
statuses are <code>STARTED</code>, <code>LOOP_UPDATED</code>, and
<code>STOPPED</code>.

## State and catalog

Use <code>state</code> or <code>snapshot</code> for presentation diagnostics,
never as a source of gameplay truth:

```java
api.snapshot(playerId).thenAccept(snapshot ->
    snapshot.ifPresent(current -> {
        if (current.hasActiveAction()) {
            plugin.getLogger().fine(
                "Animation: " + current.activeAnimation());
        }
    }));

api.catalog().thenAccept(catalog ->
    catalog.profile("m4a1").ifPresent(profile ->
        plugin.getLogger().info("Armature model: " + profile.modelId())));
```

The catalog is an immutable profile snapshot. It exposes action, loop,
additive, and signal ids, plus a revision number. Neither the catalog nor a
player snapshot exposes ammunition, damage, cooldowns, or other gameplay
state.
