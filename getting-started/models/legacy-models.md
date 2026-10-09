---
description: "Legacy Models & BetterModel"
---

# Legacy Models & BetterModel

## Format and backend matrix

| Source | Native | BetterModel compatibility |
| --- | --- | --- |
| Strict `.armature` v2 | Supported; explicit roles and Physics | Not supported |
| `.armature` v1 | Rejected; recreate manually | Not supported |
| Compatible `.bbmodel` | Supported with legacy conventions | Supported with BetterModel conventions |

Keep the native backend for Armature FPV projects.

## Legacy bone conventions

`.bbmodel` retains names such as `pra_`/`pla_` for upper arms,
`prfa_`/`plfa_` for forearms, `pri_`/`pli_` for held items and `s_camera` for the
camera marker. Native full arms use `prfull_`/`plfull_`; native physical bones use
`phy_*`. Existing offhand adaptation/name normalization belongs to this legacy
path, not the strict v2 role system.

Use a stable authored root/pivot for legacy first-person adaptation and put
whole-model animation on a child anchor when the legacy rig needs its root
unchanged. Do not impose the old required `root` name on v2 role assignment.

## Animation names

Native `.armature` and `.bbmodel` accept ordinary names including `idle`, `walk`,
`idle_fly`, `walk_fly`, `spawn` and `jump`. Profile rules explicitly start them.
BetterModel rejects those reserved automatic names in Armature profiles;
rename clips and references when selecting that compatibility backend.

## Choosing BetterModel

Set `render.backend: bettermodel`, install BetterModel and restart. This choice
is preserved on upgrade but is not the 2.0.0 default. Model composition and
native physics need native rendering, and the client bridge needs native too.
BetterModel deprecation is planned for future versions; it remains available now.

## Recreating a legacy model

Keep a backup. For a new strict project, create Armature FPV Model, copy/rebuild
the intended geometry and animations, assign all body/item/camera roles and
Physics flags, save v2 and test. Assign v2 roles explicitly; v1 naming prefixes are not converted automatically.
Reauthor left-hand targets explicitly rather than expecting generated mirroring.
