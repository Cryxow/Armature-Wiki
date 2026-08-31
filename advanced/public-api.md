# Public API

Armature exposes a presentation-only Java API for plugins that own gameplay
state. It can play a first-person animation, select a loop, route a signal, or
read presentation state. It does not own items, ammunition, damage, cooldowns,
or event cancellation.

The public API is versioned independently from BetterModel and the other
rendering internals.

## Dependency and availability

Compile against the standalone <code>armature-api</code> artifact:

~~~kotlin
dependencies {
    compileOnly("com.armaturemc:armature-api:<armature-version>")
}
~~~

The API classes are also present in the distributed <code>Armature.jar</code>;
the standalone API jar is not required at runtime. The current contract
reports:

| Constant | Value |
| --- | --- |
| <code>ArmatureApi.API_MAJOR_VERSION</code> | <code>1</code> |
| <code>ArmatureApi.API_VERSION</code> | <code>1.0.0</code> |

Optional integrations should check availability before using the API:

~~~java
if (ArmatureApi.isAvailable()) {
    ArmatureApi.animations().playAnimation(playerId, "inspect");
}

ArmatureApi.tryAnimations();
ArmatureApi.tryProviders();
ArmatureApi.tryDispatcher();
~~~

<code>animations()</code>, <code>providers()</code>, and
<code>dispatcher()</code> throw when Armature is not enabled. The
<code>try...</code> methods return an empty <code>Optional</code> instead.

Armature also exposes the animation facade and provider contracts through the
Paper service manager while the plugin is enabled. The service is removed
during shutdown.

Every operation is scheduled on the target player's scheduler and returns a
<code>CompletableFuture</code>. This keeps calls safe on Paper and Folia.
Async continuations must treat results as data; do not access Bukkit player
state, BetterModel, or Armature implementation classes from an arbitrary
async thread.

## Channel-aware playback

Use <code>ArmatureAnimationReference</code> for new integrations. It makes the
requested channel explicit:

| Channel | Meaning |
| --- | --- |
| <code>ACTION</code> | One finite, replaceable animation |
| <code>LOOP</code> | Persistent selected animation state |
| <code>ADDITIVE</code> | Layer over the base animation |

~~~java
var api = ArmatureApi.animations();

api.playAsset(playerId, ArmatureAnimationReference.action("inspect"));
api.startLoop(playerId, ArmatureAnimationReference.loop("aim"));
api.triggerAdditive(playerId, ArmatureAnimationReference.additive("recoil"));
~~~

The convenience factories resolve against the active profile:

~~~java
ArmatureAnimationReference.action("inspect");
ArmatureAnimationReference.loop("aim");
ArmatureAnimationReference.additive("recoil");
~~~

Pin a profile explicitly when needed:

~~~java
ArmatureAnimationReference.profile(
    "m4a1", "inspect", ArmatureAnimationChannel.ACTION);

api.playAsset(playerId,
    "m4a1", "inspect", ArmatureAnimationChannel.ACTION);
~~~

An explicit profile still must be the profile currently rendered for the
player's held item. Armature returns <code>PROFILE_NOT_ACTIVE</code> instead
of replacing the gameplay item's presentation.

Accepted channel-aware operations return an
<code>ArmatureOperationResult</code>. The optional handle is scoped to the
player, profile, channel, and runtime token:

~~~java
api.triggerAdditive(playerId,
    ArmatureAnimationReference.additive("recoil"))
    .thenAccept(result -> result.handle().ifPresent(handle ->
        api.stop(playerId, handle)));
~~~

Stopping an old handle cannot stop a newer replacement. A stale handle returns
<code>HANDLE_NOT_ACTIVE</code>. Accepted statuses are
<code>STARTED</code>, <code>LOOP_UPDATED</code>, and <code>STOPPED</code>.

## Compatibility playback methods

The API retains simpler overloads for existing integrations:

~~~java
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
~~~

<code>play(UUID, String)</code> is the compatibility action call.
<code>playAnimation</code> targets an exact finite asset in the active profile,
including an inline modern action asset. <code>startLoop</code> accepts a
built-in loop id or a direct loop asset id.

Raw playback requires an active rendered item profile to bind the model, but
the requested asset does not need to be listed in that profile. The loaded
model still validates the asset at playback time.

<code>ArmatureAction</code> includes the built-in actions
<code>EQUIP</code>, <code>UNEQUIP</code>, <code>IDLE</code>,
<code>WALK</code>, <code>SPRINT</code>, <code>SWIM</code>,
<code>CROUCH</code>, <code>JUMP</code>, <code>LAND</code>,
<code>CLIMB</code>, <code>CLIMB_HOLD</code>, <code>CLIMB_DOWN</code>,
<code>FALL</code>, <code>SWING</code>, <code>MINE</code>, <code>PLACE</code>,
<code>USE</code>, <code>EAT</code>, <code>DRINK</code>, <code>BLOCK</code>,
<code>BOW_DRAW</code>, <code>CROSSBOW_CHARGE</code>,
<code>TRIDENT_CHARGE</code>, <code>THROW</code>, <code>CUSTOM</code>,
<code>FIRE</code>, <code>AIM</code>, <code>AIM_FIRE</code>,
<code>AIM_ENTER</code>, <code>AIM_EXIT</code>, <code>RELOAD_START</code>,
<code>RELOAD_PHASE</code>, <code>RELOAD_COMPLETE</code>,
<code>RELOAD_CANCEL</code>, <code>FIREARM_STATE</code>, and
<code>FIRE_MODE_CHANGE</code>.

The enum's persistent loop actions are
<code>IDLE</code>, <code>WALK</code>, <code>SPRINT</code>,
<code>SWIM</code>, <code>CROUCH</code>, <code>CLIMB</code>,
<code>CLIMB_HOLD</code>, <code>CLIMB_DOWN</code>, <code>FALL</code>,
<code>BLOCK</code>, <code>BOW_DRAW</code>, <code>CROSSBOW_CHARGE</code>,
<code>TRIDENT_CHARGE</code>, <code>AIM</code>, and
<code>FIREARM_STATE</code>.

## Signals and modern action rules

Use <code>signal</code>, <code>emitSignal</code>, or
<code>sendSignal</code> to route a namespaced trigger through the active
profile's modern <code>animations.actions</code> rules:

~~~yaml
animations:
  actions:
    - trigger: myplugin:weapon.fire
      condition: player.on-ground == true
      animation:
        name: pistol_fire
        duration: 6t
      consume: false
~~~

~~~java
api.sendSignal(playerId, "myplugin:weapon.fire", Map.of())
    .thenAccept(result -> {
        if (result.matched() && result.animation().accepted()) {
            // Gameplay was already handled by the owning plugin.
        }
    });
~~~

The result reports:

| Field | Meaning |
| --- | --- |
| <code>matched</code> | At least one configured trigger rule matched |
| <code>consumed</code> | The matching rule requested <code>consume: true</code> |
| <code>animation</code> | The normal accepted/failure result |

<code>consume</code> is routing metadata. It does not cancel a Bukkit event
owned by another plugin.

For typed scalar metadata:

~~~java
api.emitSignal(playerId, "myplugin:weapon.fire",
    ArmatureSignalData.of(Map.of("ammo", 7, "mode", "semi")));
~~~

<code>ArmatureSignalData</code> accepts strings, booleans, and finite numbers.
It is limited to 64 entries, 128-character keys, and 1024-character string
values. Entities, ItemStacks, Bukkit objects, nulls, and non-finite numbers
are rejected.

## Lifecycle events

Accepted finite animations fire Bukkit lifecycle events:

| Event | When | Data |
| --- | --- | --- |
| <code>ArmatureAnimationStartEvent</code> | A finite animation is accepted and started | <code>getProfileId()</code>, <code>getActionId()</code>, <code>getAnimation()</code>, <code>getToken()</code> |
| <code>ArmatureAnimationEndEvent</code> | A finite animation leaves the action layer | The same fields plus <code>getReason()</code> |

End reasons are:

* <code>COMPLETED</code>: natural end.
* <code>CANCELLED</code>: explicitly cancelled.
* <code>REPLACED</code>: another action replaced it.
* <code>REMOVED</code>: the profile, player session, or runtime was removed.

The reason's stable string id is lowercase:
<code>completed</code>, <code>cancelled</code>, <code>replaced</code>, or
<code>removed</code>. Loop transitions do not fire these finite-action events.

## State and catalog

<code>state(playerId)</code> and <code>getState(playerId)</code> return an
optional <code>ArmatureAnimationState</code> with the current item,
selected loop, active action, active asset, and remaining ticks.

<code>snapshot(playerId)</code> additionally includes the active profile id and
the set of active additive ids. These objects expose presentation state only.

<code>catalog()</code> returns an immutable
<code>ArmatureProfileCatalog</code> snapshot. Each
<code>ArmatureProfileDescriptor</code> contains the profile id, model id,
action ids, loop ids, additive ids, signal ids, and a <code>modern</code>
flag. The catalog also exposes a monotonically increasing revision.

~~~java
api.catalog().thenAccept(catalog ->
    catalog.profile("m4a1").ifPresent(profile ->
        plugin.getLogger().info("Model: " + profile.modelId())));

api.getState(playerId).thenAccept(state ->
    state.ifPresent(current -> {
        if (current.hasActiveAction()) {
            plugin.getLogger().info(current.activeAnimation());
        }
    }));
~~~

## Result statuses

All result types use <code>ArmatureAnimationResult.Status</code>:

<table><thead><tr><th>Status</th><th>Meaning</th></tr></thead><tbody><tr><td><code>STARTED</code></td><td>Finite action accepted.</td></tr><tr><td><code>LOOP_UPDATED</code></td><td>Loop selected or updated.</td></tr><tr><td><code>STOPPED</code></td><td>Requested action or loop stopped.</td></tr><tr><td><code>COOLDOWN</code></td><td>Action cooldown has not elapsed.</td></tr><tr><td><code>NOT_CANCELABLE</code></td><td>Current action does not allow cancellation.</td></tr><tr><td><code>NO_PROFILE</code></td><td>No matching rendered profile.</td></tr><tr><td><code>PROFILE_NOT_ACTIVE</code></td><td>The explicit profile is not the active held-item profile.</td></tr><tr><td><code>NO_ANIMATION</code></td><td>Requested asset is not available.</td></tr><tr><td><code>PLAYER_UNAVAILABLE</code></td><td>Player is offline or cannot be scheduled.</td></tr><tr><td><code>DISABLED</code></td><td>Armature is disabled.</td></tr><tr><td><code>NOT_RENDERED</code></td><td>No active presentation exists for the player.</td></tr><tr><td><code>NOT_ACTIVE</code></td><td>Nothing is active for the requested stop operation.</td></tr><tr><td><code>HANDLE_NOT_ACTIVE</code></td><td>The channel handle is stale or already replaced.</td></tr><tr><td><code>INVALID_ACTION</code></td><td>String is not a built-in action.</td></tr><tr><td><code>RENDERER_REJECTED</code></td><td>Renderer rejected playback.</td></tr><tr><td><code>NOT_CONFIGURED</code></td><td>Armature API is not configured or enabled.</td></tr></tbody></table>

## Providers and ownership boundary

Use <code>ArmatureApi.providers()</code> to access the thread-safe
<code>ProviderRegistry</code>. Providers register an
<code>ItemIdentityProvider</code> or an <code>ActionProvider</code>; ids are
case-insensitive and duplicate ids are rejected.

An item provider resolves an <code>ItemStack</code> to an
<code>ItemIdentity</code>. An action provider publishes an
<code>ActionEvent</code> containing:

* <code>playerId</code>
* an <code>ArmatureAction</code>
* the resolved <code>ItemKey</code>
* immutable string <code>details</code>

Action-provider capabilities are <code>EQUIP</code>, <code>FIRE</code>,
<code>AIM</code>, <code>RELOAD</code>, <code>RELOAD_PHASES</code>,
<code>FIREARM_STATE</code>, <code>FIRE_MODE</code>, and <code>SKIN</code>.
Use <code>ArmatureActionDispatcher</code> or
<code>ArmatureApi.dispatch(ActionEvent)</code> only for the compatibility
provider event contract; normal integrations should prefer
<code>ArmatureAnimationApi</code> and namespaced signals.

Provider integrations translate gameplay-plugin events into presentation
actions. They must not move ammunition, apply damage, replace items, or
duplicate Armature's model lifecycle. WeaponMechanics, ItemsAdder,
QualityArmory, CraftEngine, and Nexo remain the owners of their gameplay/item
state.

For supported scripting adapters, see
[Skript, MythicMobs, and Denizen](supported-plugins/README.md).
