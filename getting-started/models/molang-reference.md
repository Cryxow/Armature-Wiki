---
description: "Molang Reference"
---

# 📖 Molang Reference

This is the native **implemented subset**, shared by server and client samplers.
It is not a general Bedrock scripting engine. Queries depend on the context
populated by the renderer; some compatibility names are deliberately constant.

## Expression syntax

Numbers, parentheses, unary `!`, `-`, `+`, arithmetic `+ - * / %`, comparisons,
`&&`, `||`, and conditional `condition ? yes : no` are supported. Trigonometry
uses degrees. Expressions are evaluated at sampling time, including randomness.
Statement blocks, arbitrary script execution and the entire Bedrock environment
are not implied by support for these expressions.

## Queries

| Query/alias | Current meaning |
| --- | --- |
| `query.yaw` | yaw |
| `query.pitch` | pitch |
| `query.modified_distance_moved` | modifiedDistanceMoved |
| `query.modified_move_speed` | modifiedMoveSpeed |
| `query.anim_time` | animTime |
| `query.delta_time` | deltaTime |
| `query.game_time` | gameTime |
| `query.program_time` | programTime |
| `query.lifetime` | lifetime |
| `query.age` | lifetime |
| `query.is_moving` | Boolean context flag (1=true, 0=false). |
| `query.is_on_ground`, `query.is_onground` | Boolean context flag (1=true, 0=false). |
| `query.is_sneaking` | Boolean context flag (1=true, 0=false). |
| `query.is_sprinting` | Boolean context flag (1=true, 0=false). |
| `query.is_swimming` | Boolean context flag (1=true, 0=false). |
| `query.first_person` | Compatibility constant `0.0` (not a live gameplay query) |
| `query.third_person` | Compatibility constant `1.0` (not a live gameplay query) |
| `query.health`, `query.max_health` | Current context health; max_health is currently an alias, not a separate maximum. |
| `query.hurt_time` | hurtTime |
| `query.target_x`, `query.target_y`, `query.target_z` | Compatibility constant `0.0` (not a live gameplay query) |
| `query.view_x`, `query.view_y`, `query.view_z` | Compatibility constant `0.0` (not a live gameplay query) |
| `query.camera_x`, `query.camera_y`, `query.camera_z` | Compatibility constant `0.0` (not a live gameplay query) |
| `query.uuid`, `query.player_uuid` | Compatibility constant `0.0` (not a live gameplay query) |
| `query.day_time` | gameTime |
| `query.moon_phase` | Compatibility constant `0.0` (not a live gameplay query) |

## Math functions

`math.sin`, `math.cos`, `math.tan`, `math.asin`, `math.acos`, `math.atan`, `math.atan2`, `math.abs`, `math.sqrt`, `math.floor`, `math.ceil`, `math.round`, `math.trunc`, `math.sign`, `math.exp`, `math.ln`, `math.degrees`, `math.radians`, `math.inversesqrt`, `math.inv_sqrt`, `math.saturate`, `math.lerp`, `math.lerprotate`, `math.lerpplace`, `math.pow`, `math.mod`, `math.min`, `math.max`, `math.clamp`, `math.step_smooth`, `math.smooth`, `math.hermite_blend`, `math.random`, `math.random_integer`, `math.randomint`, `math.die_roll`, `math.die_roll_integer`, `math.roll_integer`.

Use the arity supported by the function: for example `math.sin(angle)`,
`math.clamp(value, minimum, maximum)`, `math.lerp(start, end, amount)` and
`math.random(minimum, maximum)`. Unknown functions/queries produce diagnostics.
Unassigned Molang variables read as zero; YAML shared variables are not
automatically bound into this namespace.

## Small example

```text
query.is_moving ? math.sin(query.anim_time * 180) * 3 : 0
```

Use this in a supported animated numeric component, not as a YAML `when` string.
Verify the query context in game and keep gameplay state selection in
[profile conditions](../profiles/conditions.md).
