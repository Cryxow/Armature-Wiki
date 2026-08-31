# Denizen

Armature's Denizen adapter is embedded in
<code>Armature.jar</code>. When Denizen is installed and Armature's public API
is available, the adapter registers one native <code>armature</code> command
and the animation lifecycle event family.

The adapter performs presentation operations only. Denizen or another gameplay
plugin remains responsible for items, ammo, damage, cooldowns, and event
cancellation.

## Command syntax

~~~text
armature [action/animation/raw_animation/loop/raw_loop/stop_action/stop_loop/signal] (<value>) (profile:<name>) (targets:<player>|...)
~~~

The command uses the script-entry player context by default. Use
<code>targets:<player>|...</code> to target one or more explicit players.
Every explicit target must be a player.

| Operation | Value | Optional arguments |
| --- | --- | --- |
| <code>action</code> | Built-in action id | none |
| <code>animation</code> or <code>anim</code> | Animation id | <code>profile:<name></code> |
| <code>raw_animation</code> | Active-model asset id | none |
| <code>loop</code> | Built-in loop id | none |
| <code>raw_loop</code> | Active-model loop asset id | none |
| <code>stop_action</code> | none | none |
| <code>stop_loop</code> | none | none |
| <code>signal</code> | Namespaced trigger | none |

The command also accepts hyphenated and compact aliases for raw and stop
operations:
<code>raw-animation</code>, <code>rawanimation</code>,
<code>raw-loop</code>, <code>rawloop</code>,
<code>stop-action</code>, <code>stopaction</code>,
<code>stop-loop</code>, <code>stoploop</code>,
<code>send_signal</code>, and <code>send-signal</code>.

Examples:

~~~text
- armature action FIRE
- armature animation inspect
- armature animation inspect profile:m4a1
- armature raw_animation fire
- armature loop AIM
- armature raw_loop aim
- armature stop_action
- armature stop_loop
- armature signal denizen:weapon.fire
~~~

<code>profile:</code> is supported for <code>animation</code> only. A value is
required for every operation except <code>stop_action</code> and
<code>stop_loop</code>.

## Lifecycle events

~~~text
on armature animation starts:
  - narrate "started <context.animation> for <context.player>"

on armature animation ends:
  - narrate "ended with <context.reason>"

on armature animation completes:
  - narrate "naturally completed <context.animation>"
~~~

Available contexts:

* <code>profile</code>
* <code>action</code>
* <code>animation</code>
* <code>reason</code>
* <code>phase</code>
* <code>token</code>
* <code>player</code>

<code>starts</code> has an empty reason. <code>ends</code> reports
<code>completed</code>, <code>cancelled</code>, <code>replaced</code>, or
<code>removed</code>. <code>completes</code> is emitted only for the natural
<code>completed</code> reason.

## Modern signal rules

The command can route a signal to an anonymous modern action rule:

~~~yaml
animations:
  actions:
    - trigger: denizen:weapon.fire
      animation:
        name: fire
        duration: 5t
~~~

~~~text
- armature signal denizen:weapon.fire
~~~

For all available trigger fields, conditions, and action selection behavior,
see [Built-in triggers](../../getting-started/profiles/triggers.md),
[Built-in conditions](../../getting-started/profiles/conditions.md), and
[Public API](../public-api.md).
