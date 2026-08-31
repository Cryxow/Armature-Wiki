# Troubleshooting

## Plugin does not enable

Check the startup order and dependencies:

* BetterModel is installed and enabled.
* PacketEvents is installed and enabled.
* The server runs a supported Paper or Folia version with Java 25.
* The Armature jar is the current build and not an old standalone addon jar.

The startup summary reports detected item providers, action providers,
integrations, renderer flags, and resource-pack generation. Missing optional
plugins are not fatal.

## Model is invisible

Check all of the following:

1. <code>render.enabled: true</code>.
2. The profile item identity matches the actual held item.
3. <code>model.name</code> matches the <code>.bbmodel</code> filename.
4. The model is in <code>plugins/Armature/models/</code>.
5. The profile is valid and loaded by <code>/armature reload profiles</code>.
6. The client accepted the generated resource pack.

For a modern profile, the model binding must look like:

~~~yaml
match:
  item: weaponmechanics:m4a1
model:
  name: fp_rifle
~~~

## Animation does not play

Use the exact BetterModel animation id. Check the reload report for
unavailable assets. Armature rejects BetterModel's reserved names
<code>idle</code>, <code>walk</code>, <code>idle_fly</code>,
<code>walk_fly</code>, <code>spawn</code>, and <code>jump</code> in profile
definitions.

For modern rules, verify the channel and shape:

* movement rules use <code>animations.movement</code> and require a looping
  animation;
* action rules use <code>animations.actions</code> and a finite animation;
* additive rules use <code>animations.additives</code>;
* external events use a namespaced <code>trigger</code>;
* a condition's dynamic right-hand path has a <code>$</code> prefix.

Example:

~~~yaml
condition: weaponmechanics.ammo == $weaponmechanics:magazine-size
~~~

An unprefixed path-like right-hand value is a literal string.

## Profile validation warnings

The modern loader fails invalid files closed and keeps valid files active.
Common causes are:

* a profile id containing spaces or characters outside the normalized
  `[a-z0-9][a-z0-9._-]*` form;
* missing <code>match</code> or <code>model.name</code>;
* mixing structured <code>match.item</code> with
  <code>match.items</code>, <code>families</code>, or
  <code>patterns</code>;
* an unsupported root key such as legacy
  <code>definitions</code>, top-level <code>actions</code>, or
  <code>triggers</code>;
* a non-finite numeric value;
* unsupported additive interpolation such as <code>bezier</code>.

The loader reports the file and field. Fix the YAML, then run
<code>/armature reload profiles</code>.

## Console contains additive-animation warnings

Warnings such as
<code>unsupported interpolation 'bezier'</code> mean the additive catalog
could not parse that clip. Use a supported Blockbench interpolation or remove
the additive reference. A malformed numeric keyframe, for example a
non-finite <code>z</code> value, must also be corrected in the
<code>.bbmodel</code>.

These warnings do not mean every normal animation failed. The reload report
identifies unavailable configured assets.

## WeaponMechanics events do not match

Check that:

* the WeaponMechanics title is used in the profile selector;
* the provider is detected at startup;
* the action rule uses the <code>weaponmechanics:</code> namespace;
* the hand value is <code>main_hand</code> or <code>off_hand</code>;
* reload rules distinguish <code>reload-start</code>,
  <code>reload-phase</code>, <code>reload-complete</code>, and
  <code>reload-cancel</code>.

Armature emits reload complete only when the magazine reaches capacity, not
after every WeaponMechanics per-round completion callback.

## Tracker stops updating

Enable only the relevant diagnostics:

~~~yaml
debug:
  enabled: true
  tracker-lifecycle: true
  animations: true
~~~

Reproduce with one player and inspect <code>/armature status</code>. A tracker
may be rebuilding after a transient BetterModel or teleport condition; the
recovery lifecycle is intentionally guarded against duplicate sessions.
Disable debug logging after collecting the report.

## Resource-pack or shader issue

Run <code>/armature reload pack</code>, reconnect the client, and confirm that
the generated pack is accepted. Anti-clipping, FOV lock, Y lock, and optional
camera rotation depend on generated assets and client shader compatibility.
Camera rotation additionally requires the model's
<code>s_camera</code> bone and a supported Fabulous client.

## Empty hand shows the wrong item

Check <code>render.vanilla-item-mode</code>:

* <code>mask</code> uses the packet-side invisible item and is the default.
* <code>source-model</code> edits the source item model in the generated pack.

PacketEvents is required for the mask path. Enable
<code>debug.empty-hand-mask</code> only while inspecting slot and mask state.

## API or scripting operation fails

Check <code>ArmatureApi.isAvailable()</code> before optional integrations call
the API. Inspect the returned status:

* <code>NO_PROFILE</code> or <code>PROFILE_NOT_ACTIVE</code>: the held item does
  not render the requested profile;
* <code>NO_ANIMATION</code>: the asset is absent or unavailable;
* <code>PLAYER_UNAVAILABLE</code>: the player cannot be scheduled;
* <code>COOLDOWN</code> or <code>NOT_CANCELABLE</code>: runtime arbitration
  rejected the request;
* <code>HANDLE_NOT_ACTIVE</code>: a channel handle is stale.

All operations return a future and are scheduled on the player's scheduler.
Do not call Bukkit or BetterModel from an arbitrary async continuation.

## Safe report

Include Armature version, Minecraft/Paper/Folia version, Java version,
BetterModel and PacketEvents versions, detected providers, the relevant
profile, configuration, reload warnings, and debug output from one affected
player. Remove private paths, tokens, and player names before sharing.
