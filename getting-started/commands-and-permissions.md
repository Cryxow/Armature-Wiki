# 📟 Commands & Permissions

## `/armature reload`

Reloads `config.yml`, all profiles and BetterModel model assets. Active player rigs are removed and synchronized again. If profile validation fails, Armature keeps the previous active profiles.

Permission: `armature.admin`

## Operational notes

* Run reload after changing YAML or resource-pack model names.
* A server restart is required when changing plugin dependencies or the plugin jar.
* Do not use `/reload`; use `/armature reload` for plugin-owned state cleanup.

***

## `/armature perf`

Shows to you a bossbar with real-time performance stats of the plugin.

Permission: `armature.admin`
