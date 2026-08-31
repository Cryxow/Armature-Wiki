# 🔭 Troubleshooting

## Plugin does not enable

Check the startup order and dependencies:

* BetterModel is installed and enabled.
* PacketEvents is installed and enabled.
* The server runs a supported Paper or Folia version with Java 25.
* The Armature jar is the current build and not an old standalone addon jar.

The startup summary reports detected item providers, action providers, integrations, renderer flags, and resource-pack generation. Missing optional plugins are not fatal.

## Model is invisible

Check all of the following:

1. `render.enabled: true`.
2. The profile item identity matches the actual held item.
3. `model.name` matches the `.bbmodel` filename.
4. The model is in `plugins/Armature/models/`.
5. The profile is valid and loaded by `/armature reload profiles`.
6. The client accepted the generated resource pack.

For a modern profile, the model binding must look like:

```yaml
match:
  item: weaponmechanics:m4a1
model:
  name: fp_rifle
```

## Animation does not play

Use the exact BetterModel animation id. Check the reload report for unavailable assets. Armature rejects BetterModel's reserved names `idle`, `walk`, `idle_fly`, `walk_fly`, `spawn`, and `jump` in profile definitions.

For modern rules, verify the channel and shape:

* movement rules use `animations.movement` and require a looping animation;
* action rules use `animations.actions` and a finite animation;
* additive rules use `animations.additives`;
* external events use a namespaced `trigger`;
* a condition's dynamic right-hand path has a `$` prefix.

Example:

```yaml
condition: weaponmechanics.ammo == $weaponmechanics:magazine-size
```

An unprefixed path-like right-hand value is a literal string.

## Profile validation warnings

The modern loader fails invalid files closed and keeps valid files active. Common causes are:

* a profile id containing spaces or characters outside the normalized `[a-z0-9][a-z0-9._-]*` form;
* missing `match` or `model.name`;
* mixing structured `match.item` with `match.items`, `families`, or `patterns`;
* an unsupported root key such as legacy `definitions`, top-level `actions`, or `triggers`;
* a non-finite numeric value;
* unsupported additive interpolation such as `bezier`.

The loader reports the file and field. Fix the YAML, then run `/armature reload profiles`.

## Console contains additive-animation warnings

Warnings such as `unsupported interpolation 'bezier'` mean the additive catalog could not parse that clip. Use a supported Blockbench interpolation or remove the additive reference. A malformed numeric keyframe, for example a non-finite `z` value, must also be corrected in the `.bbmodel`.

These warnings do not mean every normal animation failed. The reload report identifies unavailable configured assets.

## WeaponMechanics events do not match

Check that:

* the WeaponMechanics title is used in the profile selector;
* the provider is detected at startup;
* the action rule uses the `weaponmechanics:` namespace;
* the hand value is `main_hand` or `off_hand`;
* reload rules distinguish `reload-start`, `reload-phase`, `reload-complete`, and `reload-cancel`.

Armature emits reload complete only when the magazine reaches capacity, not after every WeaponMechanics per-round completion callback.

## Tracker stops updating

Enable only the relevant diagnostics:

```yaml
debug:
  enabled: true
  tracker-lifecycle: true
  animations: true
```

Reproduce with one player and inspect `/armature status`. A tracker may be rebuilding after a transient BetterModel or teleport condition; the recovery lifecycle is intentionally guarded against duplicate sessions. Disable debug logging after collecting the report.

## Resource-pack or shader issue

Run `/armature reload pack`, reconnect the client, and confirm that the generated pack is accepted. Anti-clipping, FOV lock, Y lock, and optional camera rotation depend on generated assets and client shader compatibility. Camera rotation additionally requires the model's `s_camera` bone and a supported Fabulous client.

## Empty hand shows the wrong item

Check `render.vanilla-item-mode`:

* `mask` uses the packet-side invisible item and is the default.
* `source-model` edits the source item model in the generated pack.

PacketEvents is required for the mask path. Enable `debug.empty-hand-mask` only while inspecting slot and mask state.

## API or scripting operation fails

Check `ArmatureApi.isAvailable()` before optional integrations call the API. Inspect the returned status:

* `NO_PROFILE` or `PROFILE_NOT_ACTIVE`: the held item does not render the requested profile;
* `NO_ANIMATION`: the asset is absent or unavailable;
* `PLAYER_UNAVAILABLE`: the player cannot be scheduled;
* `COOLDOWN` or `NOT_CANCELABLE`: runtime arbitration rejected the request;
* `HANDLE_NOT_ACTIVE`: a channel handle is stale.

All operations return a future and are scheduled on the player's scheduler. Do not call Bukkit or BetterModel from an arbitrary async continuation.

## Safe report

Include Armature version, Minecraft/Paper/Folia version, Java version, BetterModel and PacketEvents versions, detected providers, the relevant profile, configuration, reload warnings, and debug output from one affected player. Remove private paths, tokens, and player names before sharing.
