# 🧩 Public API

Armature exposes a presentation-only Java API for plugins that own gameplay state. It can play a first-person animation, select a loop, route a signal, or read presentation state. It does not own items, ammunition, damage, cooldowns, or event cancellation.

The public API is versioned independently from BetterModel and the other rendering internals.

## Dependency and availability

Compile against the standalone `armature-api` artifact:

```kotlin
dependencies {
    compileOnly("com.armaturemc:armature-api:<armature-version>")
}
```

The API classes are also present in the distributed `Armature.jar`; the standalone API jar is not required at runtime. The current contract reports:

| Constant                        | Value   |
| ------------------------------- | ------- |
| `ArmatureApi.API_MAJOR_VERSION` | `1`     |
| `ArmatureApi.API_VERSION`       | `1.0.0` |

Optional integrations should check availability before using the API:

```java
if (ArmatureApi.isAvailable()) {
    ArmatureApi.animations().playAnimation(playerId, "inspect");
}

ArmatureApi.tryAnimations();
ArmatureApi.tryProviders();
ArmatureApi.tryDispatcher();
```

`animations()`, `providers()`, and `dispatcher()` throw when Armature is not enabled. The `try...` methods return an empty `Optional` instead.

Armature also exposes the animation facade and provider contracts through the Paper service manager while the plugin is enabled. The service is removed during shutdown.

Every operation is scheduled on the target player's scheduler and returns a `CompletableFuture`. This keeps calls safe on Paper and Folia. Async continuations must treat results as data; do not access Bukkit player state, BetterModel, or Armature implementation classes from an arbitrary async thread.

## Channel-aware playback

Use `ArmatureAnimationReference` for new integrations. It makes the requested channel explicit:

| Channel    | Meaning                             |
| ---------- | ----------------------------------- |
| `ACTION`   | One finite, replaceable animation   |
| `LOOP`     | Persistent selected animation state |
| `ADDITIVE` | Layer over the base animation       |

```java
var api = ArmatureApi.animations();

api.playAsset(playerId, ArmatureAnimationReference.action("inspect"));
api.startLoop(playerId, ArmatureAnimationReference.loop("aim"));
api.triggerAdditive(playerId, ArmatureAnimationReference.additive("recoil"));
```

The convenience factories resolve against the active profile:

```java
ArmatureAnimationReference.action("inspect");
ArmatureAnimationReference.loop("aim");
ArmatureAnimationReference.additive("recoil");
```

Pin a profile explicitly when needed:

```java
ArmatureAnimationReference.profile(
    "m4a1", "inspect", ArmatureAnimationChannel.ACTION);

api.playAsset(playerId,
    "m4a1", "inspect", ArmatureAnimationChannel.ACTION);
```

An explicit profile still must be the profile currently rendered for the player's held item. Armature returns `PROFILE_NOT_ACTIVE` instead of replacing the gameplay item's presentation.

Accepted channel-aware operations return an `ArmatureOperationResult`. The optional handle is scoped to the player, profile, channel, and runtime token:

```java
api.triggerAdditive(playerId,
    ArmatureAnimationReference.additive("recoil"))
    .thenAccept(result -> result.handle().ifPresent(handle ->
        api.stop(playerId, handle)));
```

Stopping an old handle cannot stop a newer replacement. A stale handle returns `HANDLE_NOT_ACTIVE`. Accepted statuses are `STARTED`, `LOOP_UPDATED`, and `STOPPED`.

## Compatibility playback methods

The API retains simpler overloads for existing integrations:

```java
api.play(playerId, "inspect");
api.playAnimation(playerId, "inspect");
api.playRawAnimation(playerId, "fire");

api.play(playerId, ArmatureAction.FIRE);
api.playAction(playerId, "FIRE");

api.startLoop(playerId, ArmatureAction.AIM);
api.startLoop(playerId, "AIM");
api.startRawLoop(playerId, "aim");
api.stopAction(playerId);
api.stopLoop(playerId);
```

`play(UUID, String)` is the compatibility action call. `playAnimation` targets an exact finite asset in the active profile, including an inline modern action asset. `startLoop` accepts a built-in loop id or a direct loop asset id.

Raw playback requires an active rendered item profile to bind the model, but the requested asset does not need to be listed in that profile. The loaded model still validates the asset at playback time.

`ArmatureAction` includes the built-in actions `EQUIP`, `UNEQUIP`, `IDLE`, `WALK`, `SPRINT`, `SWIM`, `CROUCH`, `JUMP`, `LAND`, `CLIMB`, `CLIMB_HOLD`, `CLIMB_DOWN`, `FALL`, `SWING`, `MINE`, `PLACE`, `USE`, `EAT`, `DRINK`, `BLOCK`, `BOW_DRAW`, `CROSSBOW_CHARGE`, `TRIDENT_CHARGE`, `THROW`, `CUSTOM`, `FIRE`, `AIM`, `AIM_FIRE`, `AIM_ENTER`, `AIM_EXIT`, `RELOAD_START`, `RELOAD_PHASE`, `RELOAD_COMPLETE`, `RELOAD_CANCEL`, `FIREARM_STATE`, and `FIRE_MODE_CHANGE`.

The enum's persistent loop actions are `IDLE`, `WALK`, `SPRINT`, `SWIM`, `CROUCH`, `CLIMB`, `CLIMB_HOLD`, `CLIMB_DOWN`, `FALL`, `BLOCK`, `BOW_DRAW`, `CROSSBOW_CHARGE`, `TRIDENT_CHARGE`, `AIM`, and `FIREARM_STATE`.

## Signals and modern action rules

Use `signal`, `emitSignal`, or `sendSignal` to route a namespaced trigger through the active profile's modern `animations.actions` rules:

```yaml
animations:
  actions:
    - trigger: myplugin:weapon.fire
      condition: player.on-ground == true
      animation:
        name: pistol_fire
        duration: 6t
      consume: false
```

```java
api.sendSignal(playerId, "myplugin:weapon.fire", Map.of())
    .thenAccept(result -> {
        if (result.matched() && result.animation().accepted()) {
            // Gameplay was already handled by the owning plugin.
        }
    });
```

The result reports:

| Field       | Meaning                                      |
| ----------- | -------------------------------------------- |
| `matched`   | At least one configured trigger rule matched |
| `consumed`  | The matching rule requested `consume: true`  |
| `animation` | The normal accepted/failure result           |

`consume` is routing metadata. It does not cancel a Bukkit event owned by another plugin.

For typed scalar metadata:

```java
api.emitSignal(playerId, "myplugin:weapon.fire",
    ArmatureSignalData.of(Map.of("ammo", 7, "mode", "semi")));
```

`ArmatureSignalData` accepts strings, booleans, and finite numbers. It is limited to 64 entries, 128-character keys, and 1024-character string values. Entities, ItemStacks, Bukkit objects, nulls, and non-finite numbers are rejected.

## Lifecycle events

Accepted finite animations fire Bukkit lifecycle events:

| Event                         | When                                       | Data                                                              |
| ----------------------------- | ------------------------------------------ | ----------------------------------------------------------------- |
| `ArmatureAnimationStartEvent` | A finite animation is accepted and started | `getProfileId()`, `getActionId()`, `getAnimation()`, `getToken()` |
| `ArmatureAnimationEndEvent`   | A finite animation leaves the action layer | The same fields plus `getReason()`                                |

End reasons are:

* `COMPLETED`: natural end.
* `CANCELLED`: explicitly cancelled.
* `REPLACED`: another action replaced it.
* `REMOVED`: the profile, player session, or runtime was removed.

The reason's stable string id is lowercase: `completed`, `cancelled`, `replaced`, or `removed`. Loop transitions do not fire these finite-action events.

## State and catalog

`state(playerId)` and `getState(playerId)` return an optional `ArmatureAnimationState` with the current item, selected loop, active action, active asset, and remaining ticks.

`snapshot(playerId)` additionally includes the active profile id and the set of active additive ids. These objects expose presentation state only.

`catalog()` returns an immutable `ArmatureProfileCatalog` snapshot. Each `ArmatureProfileDescriptor` contains the profile id, model id, action ids, loop ids, additive ids, signal ids, and a `modern` flag. The catalog also exposes a monotonically increasing revision.

```java
api.catalog().thenAccept(catalog ->
    catalog.profile("m4a1").ifPresent(profile ->
        plugin.getLogger().info("Model: " + profile.modelId())));

api.getState(playerId).thenAccept(state ->
    state.ifPresent(current -> {
        if (current.hasActiveAction()) {
            plugin.getLogger().info(current.activeAnimation());
        }
    }));
```

## Result statuses

All result types use `ArmatureAnimationResult.Status`:

| Status               | Meaning                                                   |
| -------------------- | --------------------------------------------------------- |
| `STARTED`            | Finite action accepted.                                   |
| `LOOP_UPDATED`       | Loop selected or updated.                                 |
| `STOPPED`            | Requested action or loop stopped.                         |
| `COOLDOWN`           | Action cooldown has not elapsed.                          |
| `NOT_CANCELABLE`     | Current action does not allow cancellation.               |
| `NO_PROFILE`         | No matching rendered profile.                             |
| `PROFILE_NOT_ACTIVE` | The explicit profile is not the active held-item profile. |
| `NO_ANIMATION`       | Requested asset is not available.                         |
| `PLAYER_UNAVAILABLE` | Player is offline or cannot be scheduled.                 |
| `DISABLED`           | Armature is disabled.                                     |
| `NOT_RENDERED`       | No active presentation exists for the player.             |
| `NOT_ACTIVE`         | Nothing is active for the requested stop operation.       |
| `HANDLE_NOT_ACTIVE`  | The channel handle is stale or already replaced.          |
| `INVALID_ACTION`     | String is not a built-in action.                          |
| `RENDERER_REJECTED`  | Renderer rejected playback.                               |
| `NOT_CONFIGURED`     | Armature API is not configured or enabled.                |

## Providers and ownership boundary

Use `ArmatureApi.providers()` to access the thread-safe `ProviderRegistry`. Providers register an `ItemIdentityProvider` or an `ActionProvider`; ids are case-insensitive and duplicate ids are rejected.

An item provider resolves an `ItemStack` to an `ItemIdentity`. An action provider publishes an `ActionEvent` containing:

* `playerId`
* an `ArmatureAction`
* the resolved `ItemKey`
* immutable string `details`

Action-provider capabilities are `EQUIP`, `FIRE`, `AIM`, `RELOAD`, `RELOAD_PHASES`, `FIREARM_STATE`, `FIRE_MODE`, and `SKIN`. Use `ArmatureActionDispatcher` or `ArmatureApi.dispatch(ActionEvent)` only for the compatibility provider event contract; normal integrations should prefer `ArmatureAnimationApi` and namespaced signals.

Provider integrations translate gameplay-plugin events into presentation actions. They must not move ammunition, apply damage, replace items, or duplicate Armature's model lifecycle. WeaponMechanics, ItemsAdder, QualityArmory, CraftEngine, and Nexo remain the owners of their gameplay/item state.

For supported scripting adapters, see [Skript, MythicMobs, and Denizen](supported-plugins/).
