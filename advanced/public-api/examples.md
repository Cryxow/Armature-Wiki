# 🧪 Examples

This example connects a custom M4A1 plugin to a model named
<code>fp_rifle</code>. It shows both supported styles:

* signals: the profile chooses the clip from a gameplay event;
* direct playback: the plugin chooses the exact clip.

The examples assume the model contains <code>base</code>, <code>aim</code>,
<code>run</code>, <code>fire</code>, <code>fire_aim</code>,
<code>fire_no</code>, <code>reload</code>, and <code>inspect</code>.

## Shared profile

Copy <code>fp_rifle.bbmodel</code> to
<code>plugins/Armature/models/</code>. Then create a profile under
<code>plugins/Armature/profiles/</code>:

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
      - when:
          - player.sneaking == true
        animation: aim
      - when:
          - player.sprinting == true
        animation: run
      - when:
          - player.speed >= 0
        animation: base

    actions:
      - trigger: myweapons:m4a1.fire
        animation:
          name: fire
          duration: 4t
          cooldown: 2t
      - trigger: myweapons:m4a1.fire-aim
        animation:
          name: fire_aim
          duration: 5t
          cooldown: 2t
      - trigger: myweapons:m4a1.empty
        animation:
          name: fire_no
          duration: 10t
      - trigger: myweapons:m4a1.reload
        animation:
          name: reload
          duration: 35t
      - trigger: myweapons:m4a1.inspect
        animation:
          name: inspect
          duration: 40t
```

The list entry names have no API meaning. The signal values under
<code>trigger</code> and the animation names under <code>animation</code> are
the meaningful values.

After copying the files, reload Armature and give the player the matching
paper item. The profile must be active before any API request can render.

## Example A: signal-driven M4A1

Use this style when the profile author should be able to change variants,
conditions, cooldowns, or animation names without changing the weapon code.

The gameplay plugin owns the firing decision:

```java
public void fireM4a1(Player player, Weapon weapon) {
    if (!weapon.canFire()) {
        return;
    }

    weapon.consumeRound();
    weapon.spawnProjectile();
    weapon.playSound();

    ArmatureApi.tryAnimations().ifPresent(api ->
        api.signal(player.getUniqueId(), "myweapons:m4a1.fire",
                ArmatureSignalData.of(Map.of(
                    "ammo", weapon.ammo(),
                    "mode", weapon.fireMode(),
                    "aiming", weapon.isAiming()
                )))
            .thenAccept(result -> {
                if (!result.matched()) {
                    plugin.getLogger().fine("No M4A1 fire rule matched.");
                } else if (!result.animation().accepted()) {
                    plugin.getLogger().fine(
                        "M4A1 animation rejected: "
                            + result.animation().status());
                }
            })
    );
}
```

Empty fire and reload use the same pattern:

```java
String signal;
if (weapon.ammo() == 0) {
    signal = "myweapons:m4a1.empty";
} else if (weapon.isAiming()) {
    signal = "myweapons:m4a1.fire-aim";
} else {
    signal = "myweapons:m4a1.fire";
}

ArmatureApi.tryAnimations().ifPresent(api ->
    api.sendSignal(player.getUniqueId(), signal)
);
```

If the profile needs data for a condition, send it as
<code>ArmatureSignalData</code>. Do not send the Bukkit event or the
<code>ItemStack</code> itself.

## Example B: direct animation control

Use this style when the weapon code is the source of truth for the visual
clip. The player still needs the active M4A1 profile, and the selected clip
must be available in that profile:

```java
public void playFireAnimation(Player player, Weapon weapon) {
    String animation = weapon.isAiming() ? "fire_aim" : "fire";

    ArmatureApi.tryAnimations().ifPresent(api ->
        api.playAsset(player.getUniqueId(),
                ArmatureAnimationReference.action(animation))
            .thenAccept(result -> {
                if (!result.accepted()) {
                    plugin.getLogger().fine(
                        "M4A1 direct animation rejected: "
                            + result.status());
                }
            })
    );
}
```

The profile is still needed for the model and asset binding. Direct means that
the code selects the asset; it does not mean that Armature is given an
arbitrary model or that the profile is bypassed.

Pin the profile when the integration wants an explicit guard:

```java
api.playAsset(player.getUniqueId(),
    ArmatureAnimationReference.profile(
        "m4a1", "inspect", ArmatureAnimationChannel.ACTION));
```

If the clip exists in the loaded model but is not enumerated by a profile
action rule, use raw playback:

```java
api.playRawAnimation(player.getUniqueId(), "inspect");
api.startRawLoop(player.getUniqueId(), "aim");
```

Raw playback still uses the active profile to find the player's presentation
and model. It does not create a standalone model session.

## Example C: loop and recoil handle

Select a persistent aim loop, then start an additive recoil and keep its handle
if the integration must stop that exact operation:

```java
api.startLoop(player.getUniqueId(),
        ArmatureAnimationReference.loop("aim"))
    .thenAccept(loop -> {
        if (!loop.accepted()) {
            plugin.getLogger().fine("Aim loop: " + loop.status());
        }
    });

api.triggerAdditive(player.getUniqueId(),
        ArmatureAnimationReference.additive("recoil"))
    .thenAccept(operation -> operation.handle().ifPresent(handle -> {
        activeRecoil.put(player.getUniqueId(), handle);
    }));
```

Stop the recoil without affecting a newer recoil operation:

```java
var handle = activeRecoil.remove(player.getUniqueId());
if (handle != null) {
    api.stop(player.getUniqueId(), handle);
}
```

The profile must declare the additive event or authored additive animation
name. Additives are layered over the base pose; they are not replacements for
the finite action layer.

## Example D: observe lifecycle events

Use Bukkit events when the gameplay plugin needs to observe accepted visual
playback. The event is an observation hook, not ownership transfer:

```java
@EventHandler
public void onArmatureAnimationEnd(ArmatureAnimationEndEvent event) {
    if (!"reload".equals(event.getAnimation())) {
        return;
    }

    if (event.getReason() == ArmatureAnimationEndEvent.Reason.COMPLETED) {
        // Apply gameplay-side reload state in the weapon plugin.
    }
}
```

<code>ArmatureAnimationStartEvent</code> fires when a finite action is
accepted. <code>ArmatureAnimationEndEvent</code> reports
<code>COMPLETED</code>, <code>CANCELLED</code>, <code>REPLACED</code>, or
<code>REMOVED</code>. Loop transitions do not fire these finite-action events.

## Example E: optional integration skeleton

Minimal descriptor:

```yaml
name: MyWeapons
version: 1.0.0
main: com.example.myweapons.MyWeaponsPlugin
api-version: '1.21'
softdepend:
  - Armature
```

Minimal enable/disable lifecycle:

```java
public final class MyWeaponsPlugin extends JavaPlugin {
    private ArmatureHook armature;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("Armature") != null) {
            armature = new ArmatureHook(this);
        }
    }

    @Override
    public void onDisable() {
        // Clear your per-player handles and stop your own provider resources.
        armature = null;
    }
}
```

For a required integration, replace <code>softdepend</code> with
<code>depend</code> and fail enable if Armature is unavailable.

## Troubleshooting this example

| Symptom | Check |
| --- | --- |
| <code>NO_PROFILE</code> | The held item does not match <code>m4a1</code> |
| <code>NO_ANIMATION</code> | The clip is missing from the profile or model |
| <code>PROFILE_NOT_ACTIVE</code> | The explicit profile is not rendered for that player |
| <code>COOLDOWN</code> | The signal rule's cooldown has not elapsed |
| <code>NOT_RENDERED</code> | Model, resource pack, allow-list, or renderer state prevents presentation |
| No compile dependency | Check that the coordinate is <code>io.github.cryxow:armature-api:1.3.0</code> |

For the complete signatures and constructors, use the
[Armature API 1.3.0 Javadocs](https://javadoc.io/doc/io.github.cryxow/armature-api/1.3.0/index.html).
