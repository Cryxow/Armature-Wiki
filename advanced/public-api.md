---
description: "Developer API Overview"
---

# Developer API Overview

Use `com.armaturemc.api` to request presentation from a plugin that owns gameplay.
No BetterModel or Armature implementation imports are required. The API is
provided by the enabled Armature plugin; requests do not create weapons, ammo,
damage or gameplay permissions.

The 2.0.0 source reports `API_VERSION = "2.0.0"` and
`API_MAJOR_VERSION = 1`. Product major version and API contract major are
different: the API retains source compatibility within contract major 1.
New standalone-presentation overloads require the matching 2.0.0 API/runtime;
they are not present in the published 1.3.0 artifact.

Start with [Setup](public-api/setup.md), then choose
[Animations & Presentations](public-api/animations.md),
[Signals](public-api/signals.md) or [Providers](public-api/providers.md).
[Examples](public-api/examples.md) supplies complete Java source.

Player operations return `CompletableFuture` and are scheduled on the player's
scheduler. A result confirms acceptance or rejection, not visual completion.
Never block the player's thread waiting on a future. Use animation lifecycle
events for finite completion; schedule gameplay follow-up on the player's
scheduler before touching Bukkit state.
