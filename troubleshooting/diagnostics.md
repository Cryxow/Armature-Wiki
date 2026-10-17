---
description: "Diagnostics & Performance"
---

# 📊 Diagnostics & Performance

Use the command reference for exact arguments and permissions. Start with:

```text
/armature status
/armature diagnostics doctor
/armature config validate
```

The command reference includes profile/model/animation inspection, motion
diagnostics, bone editor controls and client renderer settings. Use the help
for the installed build rather than guessing an old alias.

## Collect a report

Include server software/Minecraft/Java, Armature/backend, enabled integrations,
client mod/protocol, graphics/Sodium/Iris/shaderpack, the failing hand and exact
steps. Attach startup/generation warnings, doctor/inspection output, minimal
profile plus shared variables, source model and provider pack receipt/fingerprint
when available. Remove private hosting credentials before sharing logs.

Separate four observations: parser acceptance, model/pack generation, provider
completion/delivery, and connected-client visuals. A successful build says
nothing about whether that client's cached pack was replaced.

## Performance tuning

Reduce real scene cost: visible cube parts/bones, concurrent outgoing models,
nested attachment instances, dynamic item transforms and texture resolution.
Use additive bone/channel filters instead of blending irrelevant full rigs.
Keep variable refresh intervals appropriate to their source; a placeholder
polled every tick is not necessary for static permissions or rare mode changes.

Stay within native model/entity/bundle limits and measure before increasing
budgets. Diagnose provider generation separately from render tick cost.
Cold client bundle uploads, resource reloads and steady-state rendering are
different measurements. Use the server's profiler on a representative scene,
then repeat the same scene after one change.

For composition problems inspect the selected variant and each active instance;
reusable attachment definitions do not imply shared animation/physics state.
For reload problems check matching generation receipts and final provider assets
before accepting a stale generated directory as ready.
