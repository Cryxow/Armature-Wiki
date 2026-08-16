# 🔭 Troubleshooting

## Plugin disables on startup

Check that BetterModel is installed and enabled before Armature. BetterModel is a hard rendering dependency.

## ModelEngine breaks

It's actually caused by BetterModel. Add `meg:` before any ModelEngine mechanic inside MythicMobs.

## Model invisible

Confirm `render.enabled: true`, the profile item identity, and the BetterModel `model` id. Check that the model asset is loaded and that the player has the resource pack applied.

## Animation does not play

Compare the profile animation name with the exact BetterModel animation name. Review startup/reload warnings for missing animations. For provider-driven actions, verify the provider plugin is enabled and its item identity resolves to the profile's `item` value.

## Motion feels too strong

Lower the relevant `*-gain`, amplitude, or `sway.maximum-rotation`. Camera-follow affects position; sway affects rotation. Tune one layer at a time.

## Tracker exists but stops updating

Enable `debug.enabled` and `debug.tracker-lifecycle`, reproduce with one player, and inspect whether the tracker is scheduled and whether an action/loop transition completed. This requires live-server evidence; a successful compile does not prove tracker health.

## Empty hand shows an invisible item or paper

Install [PacketEvents](https://modrinth.com/plugin/packetevents), enable `debug.enabled` and `debug.empty-hand-mask`, reproduce once, and inspect the selected raw slot plus server inventory mutation. `render.empty-hand-mask.repair-server-materialization` is a narrow experimental repair for the recognized Armature mask only.

## Safe report to attach

Include Armature version, Minecraft/Paper version, BetterModel version, provider list, relevant profile, `config.yml`, startup/reload warnings, and debug logs from one affected player. Redact player names if needed.
