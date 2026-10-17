---
description: "Animations & Presentations"
---

# ▶️ Animations & Presentations

## Standalone model playback and persistent presentations

These operations require the matching 2.0.0 plugin/API JAR. Earlier 1.3.0 Javadoc does not describe them.

Select a loaded model explicitly to play a finite animation without a profile
file, item matcher, or prior API call:

```java
ArmatureApi.tryAnimations().ifPresent(api ->
    api.play(playerId, "arm_rifle", Map.of(
        "name", "inspect",
        "speed", 1.25,
        "duration", "40t",
        "can-be-cancelled", true
    )).thenAccept(result -> {
        if (!result.accepted()) logger.fine("Playback: " + result.status());
    })
);
```

The call temporarily replaces the player's view through the active renderer. The model and resource-pack assets must already be loaded;
Armature discovers models independently of profile item matching. The actual
inventory is unchanged. After the finite action ends, Armature restores the
presentation appropriate to the item held **at that time**. An unknown model or
missing animation is rejected before replacing the existing view. A renderer
failure after mounting releases the temporary view and restores item matching.

The future resolves after the model is ready and the playback request is accepted,
not when the animation ends. The returned presentation handle can close that
exact generation early. Closing an old handle cannot close a newer presentation.

For persistent movement, repeated actions, additives, and conditions, open one
presentation with the exact content of a modern YAML `animations` section:

```java
var presentation = new ArmaturePresentation("arm_rifle", Map.of(
    "movement", List.of(Map.of("animation", "base")),
    "actions", List.of(Map.of(
        "trigger", "myplugin:inspect",
        "condition", List.of("event.argument.allowed == true"),
        "hide-vanilla-hand", true,
        "animation", Map.of(
            "name", "inspect",
            "duration", "40t",
            "speed", 1.25,
            "cooldown", "10t",
            "can-be-cancelled", false
        )
    ))
));

api.openPresentation(playerId, presentation).thenAccept(result -> {
    result.handle().ifPresent(handle -> {
        // Store this handle in the integration's per-player state to close later.
        presentationHandles.put(playerId, handle);
        api.trigger(playerId, "myplugin:inspect",
            ArmatureSignalData.of(Map.of("allowed", true)));
    });
});

// When the integration relinquishes this view:
var handle = presentationHandles.remove(playerId);
if (handle != null) api.closePresentation(playerId, handle);
```

The full constructor also accepts a `variables` tree, model offset, model sway
rate, and default vanilla-hand visibility. Maps/lists are deeply copied before
scheduling. The API uses the same parser and rule evaluator as YAML, including
validation of unsupported properties and invalid combinations:

| Section | Supported parameters |
| --- | --- |
| `movement` rule | `when`, `hide-vanilla-hand`, `animation` |
| Movement animation | `name` (string/list), `selection`, `speed`, `blend-in`, `blend-out`, `sway-rate`, `loop`, `start`, `end` |
| `actions` rule | `trigger`, `condition`, `consume`, `hide-vanilla-hand`, `animation` |
| Action animation | `name` (string/list), `selection`, `duration`, `speed`, `blend-in`, `blend-out`, `cooldown`, `can-be-cancelled` |
| `additives` rule | `when`, `animation`, `playback`, `group`, `priority`, `weight`, `blend-in`, `blend-out`, `bones`, `channels`, `position-gain`, `rotation-gain`, `reference` |
| Additive animation | `name` (string/list), `selection`, `speed` |
| Weight mapping | `parameter`, `input`, `output`, `easing`, `clamp` |
| Reference pose | `animation`, `time` |

Duration units and defaults match modern YAML (`t`, `s`, or numeric ticks where
that field accepts them). Omitted action duration comes from the loaded clip.
The `consume` result is a routing decision; custom gameplay still belongs to the
integration. Sending a trigger does not itself cancel an arbitrary Bukkit event.

Each opened presentation owns fresh rule state. Repeated `trigger(...)` calls in
that view share cooldowns and variant sequences. Separate `play(player, model,
options)` calls create separate contexts; their cooldowns are not shared. Use a
persistent presentation for weapon/skill systems with repeated triggers.

Only one API-owned view is active per player. It persists across item changes
until closed/replaced, but is released by player-session cleanup (death,
disconnect, world change), plugin reload, or disabling the runtime/player.
Opening a replacement respects a currently non-cancellable action; explicitly
closing a presentation tears it down. The configured profile catalog remains
read-only and does not contain these temporary views. Temporary view identifiers
in lifecycle/operation data start with `api.` and are not YAML profile IDs.

While this view is active, unpinned asset references and raw playback use its
model/rules. Built-in legacy `ArmatureAction` dispatch does not fall back to the
held item's profile. `trigger(...)` is the YAML-aligned alias for `signal(...)`;
existing signal overloads remain compatible.

## Action, loop and additive channels

New integrations should use `ArmatureAnimationReference` instead of guessing
whether a string is a finite action, a persistent loop, or an additive layer:

```java
var api = ArmatureApi.animations();

api.playAsset(playerId, ArmatureAnimationReference.action("inspect"));
api.startLoop(playerId, ArmatureAnimationReference.loop("aim"));
api.triggerAdditive(playerId, ArmatureAnimationReference.additive("recoil"));
```

Use `ArmatureAnimationReference.profile(profileId, assetId, channel)` when a
profile must be explicit. The profile still has to be the profile rendered for
the player's held item; Armature returns `PROFILE_NOT_ACTIVE` rather than
silently replacing that presentation.

Accepted operations return an `ArmatureOperationResult`. Its handle is scoped
to the player, profile, channel, and monotonically increasing runtime token:

```java
var request = api.triggerAdditive(playerId,
    ArmatureAnimationReference.additive("recoil"));
request.thenAccept(result -> result.handle().ifPresent(handle ->
    api.stop(playerId, handle)
));
```

Stopping an old handle never stops a newer replacement. A stale handle returns
`HANDLE_NOT_ACTIVE`. A direct loop remains selected until it is stopped, the
item/profile changes, Armature is reloaded, or the runtime is disabled.

`catalog()` returns an immutable profile snapshot with action, loop, additive,
and signal ids. `snapshot(playerId)` returns the presentation state and active
additive ids for one online player. Neither method exposes gameplay state.

## Profile-bound and compatibility calls

`play(UUID, String)` is the compatibility API for an administrator-defined
legacy action id in the profile of the item currently held by the player:

```java
animations.play(player.getUniqueId(), "inspect");
animations.playAction(player.getUniqueId(), ArmatureAction.FIRE);
```

`playAnimation(playerId, animationId)` targets the exact finite asset in the
active profile, including modern inline action assets. `startLoop(playerId,
assetId)` accepts either a built-in loop id or an exact direct loop asset id.
The channel-aware overload remains preferred when the caller wants a handle.

For integrations that own the animation names, use the raw methods. They
require an active item profile or API presentation to bind the rendered model; the requested clip
does not need to be listed in that profile. The renderer validates the asset
against the loaded model at playback time:

```java
animations.playRawAnimation(playerId, "fire");
animations.startRawLoop(playerId, "aim");
```

The profile still controls item matching, model id, hand masking, and
presentation policy. Raw playback does not change gameplay state or mount a
different profile.

The enum overload dispatches a built-in Armature action. `startLoop` is the
explicit state-oriented form for persistent actions such as `IDLE`, `AIM`, or
`CROSSBOW_CHARGE`; `stopLoop` clears the selected loop. `stopAction` cancels
the current one-shot and resumes the selected loop.

```java
animations.startLoop(playerId, ArmatureAction.CROSSBOW_CHARGE);
animations.stopAction(playerId);
animations.stopLoop(playerId);
```

When an integration already knows the profile id, it can make the selection
explicit:

```java
animations.playAnimation(playerId, "m4a1", "inspect");
animations.playAnimation(playerId, "m4a1", "swing");
```

The selected profile must be the profile currently rendered for the player's
held item. This prevents an animation request from silently replacing the
gameplay item's presentation. The result is `PROFILE_NOT_ACTIVE` when the
profile exists but does not match the held item.

These API playback calls do not change the item, ammo, damage or reload state, and do not cancel a caller-owned event. The gameplay plugin remains responsible for those rules.

## Lifecycle events

The API fires Bukkit events for accepted finite animations:

- `ArmatureAnimationStartEvent` exposes `getProfileId()`, `getActionId()`,
  `getAnimation()` and `getToken()`.
- `ArmatureAnimationEndEvent` exposes the same fields plus `getReason()`:
  `COMPLETED`, `CANCELLED`, `REPLACED` or `REMOVED`.

Loop transitions are internal renderer transitions and do not produce lifecycle
events. The end event is the reliable hook for starting a gameplay-owned
follow-up skill; it does not imply that Armature owns that gameplay state.

## Results and state

`ArmatureAnimationResult.Status` distinguishes accepted playback from a
missing profile/action, a cooldown, a non-cancellable active action, an
unavailable player, a stale handle, or a renderer rejection.
`ArmatureAnimationState` and `ArmatureAnimationSnapshot` expose only
presentation data: current item key, desired loop, active animation, additive
layers, and remaining ticks. They intentionally do not expose gameplay state.

```java
animations.getState(playerId).thenAccept(snapshot -> snapshot.ifPresent(state -> {
    if (state.hasActiveAction()) {
        plugin.getLogger().fine("Armature action: " + state.activeActionId()
            + " / animation: " + state.activeAnimation());
    }
}));
```

The `ArmaturePresentation` constructor permits an inline variables tree for an
API-owned ephemeral view. That does not authorize local `variables:` blocks in
YAML profiles: disk profiles use the shared `plugins/Armature/variables.yml`.
