# WeaponMechanics

Armature detects WeaponMechanics as an optional item and action provider.
WeaponMechanics remains the owner of weapon identity, ammunition, reload
logic, fire mode, aiming, damage, and cooldowns. Armature translates those
events into first-person presentation actions and modern profile signals.

The provider id and namespace are <code>weaponmechanics</code>.

## Modern profile

Match the WeaponMechanics title with the namespaced item selector:

~~~yaml
m4a1:
  match:
    item: weaponmechanics:m4a1
  model:
    name: fp_rifle
    hide-vanilla-hand: true
  integrations:
    weaponmechanics:
      attachments:
        optic:
          bones:
            scope:
              visible: true
              offset: [0.0, 0.0, 0.0]
  animations:
    actions:
      - trigger: weaponmechanics:fire
        animation:
          name: fire
          duration: 4t
      - trigger: weaponmechanics:reload-start
        animation:
          name: reload
          duration: 20t
~~~

The WeaponMechanics item selector is the weapon title, not the display name.
The provider emits hand metadata as <code>main_hand</code> or
<code>off_hand</code>.

The optional <code>integrations.weaponmechanics.attachments</code> section
contains arbitrary attachment ids. Each attachment contains only
<code>bones</code>; each bone accepts <code>visible</code> and
<code>offset</code>. An offset is either a three-number list or an
<code>x</code>/<code>y</code>/<code>z</code> section:

~~~yaml
integrations:
  weaponmechanics:
    attachments:
      suppressor:
        bones:
          muzzle:
            visible: false
            offset:
              x: 0.0
              y: 0.0
              z: -0.1
~~~

## Provider actions and signals

The provider emits these presentation actions:

| Event | Armature action | Namespaced signal |
| --- | --- | --- |
| Weapon equipped | <code>EQUIP</code> | <code>weaponmechanics:equip</code> |
| Weapon unequipped | <code>UNEQUIP</code> | <code>weaponmechanics:unequip</code> |
| Scope entered | <code>AIM_ENTER</code>, then <code>AIM</code> | <code>weaponmechanics:aim-enter</code>, <code>weaponmechanics:aim</code> |
| Scope remains stacked | <code>AIM</code> | <code>weaponmechanics:aim</code> |
| Scope exited | <code>AIM_EXIT</code> | <code>weaponmechanics:aim-exit</code> |
| Weapon fired from hip | <code>FIRE</code> | <code>weaponmechanics:fire</code> |
| Weapon fired while aiming | <code>AIM_FIRE</code> | <code>weaponmechanics:aim-fire</code> |
| Reload input | signal only | <code>weaponmechanics:reload</code> |
| Reload cycle starts | <code>RELOAD_START</code> | <code>weaponmechanics:reload-start</code> |
| Reload iteration | <code>RELOAD_PHASE</code> | <code>weaponmechanics:reload-phase</code> |
| Reload reaches capacity | <code>RELOAD_COMPLETE</code> | <code>weaponmechanics:reload-complete</code> |
| Reload is cancelled | <code>RELOAD_CANCEL</code> | <code>weaponmechanics:reload-cancel</code> |
| Firearm state changes | <code>FIREARM_STATE</code> | <code>weaponmechanics:firearm-state-change</code> |
| Selective fire changes | <code>FIRE_MODE_CHANGE</code> | <code>weaponmechanics:fire-mode-change</code> |

Scope, fire, and reload events are main-hand presentation events. Equipment
events can identify either hand.

<code>weaponmechanics:reload</code> is sent at the low-priority input stage,
before WeaponMechanics performs its normal reload path. A modern rule with
<code>consume: true</code> cancels the WeaponMechanics reload event. This is
the one provider signal that can prevent the provider's own gameplay action;
Armature still does not implement reload logic itself.

## Event details

Every provider event includes:

| Key | Values |
| --- | --- |
| <code>weapon-title</code> | WeaponMechanics title |
| <code>hand</code> | <code>main_hand</code> or <code>off_hand</code> |

Scope events additionally include <code>zoom</code>. The exit event includes
<code>automatic-after-shot</code>; Armature adds
<code>aiming=true</code> or <code>aiming=false</code> to the corresponding
modern signal.

Fire events include <code>aiming</code>.

Reload start and phase signals include:

<table><thead><tr><th>Key</th><th>Meaning</th></tr></thead><tbody><tr><td><code>cycle</code></td><td>Armature reload-cycle id.</td></tr><tr><td><code>phase</code></td><td><code>load</code>.</td></tr><tr><td><code>ammo</code>, <code>ammo-before</code>, <code>ammo-after</code></td><td>Ammo values for the iteration.</td></tr><tr><td><code>magazine-size</code></td><td>Current magazine capacity.</td></tr><tr><td><code>initial-ammo</code>, <code>initial-magazine-size</code></td><td>Values at the start of the cycle.</td></tr><tr><td><code>ammo-per-reload</code></td><td>Rounds loaded per iteration.</td></tr><tr><td><code>round-index</code></td><td>One-based reload iteration.</td></tr><tr><td><code>rounds-needed</code>, <code>rounds-added</code>, <code>rounds-remaining</code></td><td>Cycle progress.</td></tr><tr><td><code>final-round</code></td><td>Whether this iteration reaches capacity.</td></tr><tr><td><code>reload-mode</code></td><td><code>per-round</code> or <code>bulk</code>.</td></tr><tr><td><code>reload-ticks</code>, <code>ammo-load-ticks</code></td><td>WeaponMechanics reload timings.</td></tr><tr><td><code>firearm-open-ticks</code>, <code>firearm-close-ticks</code></td><td>Firearm transition timings.</td></tr><tr><td><code>firearm-type</code></td><td>Normalized WeaponMechanics firearm type.</td></tr></tbody></table>

Reload complete reports <code>cycle</code>,
<code>rounds-needed</code>, <code>rounds-remaining=0</code>, and
<code>final-round=true</code>. WeaponMechanics can emit a completion event
after each <code>Ammo_Per_Reload</code> iteration; Armature emits
<code>RELOAD_COMPLETE</code> only when the magazine actually reaches capacity.

Reload cancel reports <code>elapsed-ticks</code> and, when a tracked cycle
exists, <code>cycle</code>, <code>rounds-needed</code>, and
<code>rounds-remaining</code>.

Firearm-state signals include:
<code>state</code>, <code>firearm-state</code>, <code>phase</code>,
<code>context</code> (<code>firearm</code>, <code>reload</code>, or
<code>shot</code>), <code>firearm-type</code>, <code>time</code>,
<code>firearm-ticks</code>, <code>firearm-open-ticks</code>, and
<code>firearm-close-ticks</code>.

Fire-mode signals include <code>mode</code>, <code>fire-mode</code>,
<code>old-mode</code>, <code>new-mode</code>,
<code>old-selective-fire</code>, and <code>new-selective-fire</code>.

## Conditions and examples

Use provider values in modern conditions. Dynamic right-hand values require
the <code>$</code> prefix:

~~~yaml
animations:
  actions:
    - trigger: weaponmechanics:fire
      condition:
        all:
          - weaponmechanics.ammo > 0
          - weaponmechanics.ammo == $weaponmechanics:magazine-size
      animation:
        name: fire
        duration: 4t
~~~

The provider namespace is also available to integrations through the
presentation API. It does not make gameplay state mutable from Armature
configuration.

For the full built-in field list, see
[Built-in conditions](../../getting-started/profiles/conditions.md) and
[Built-in triggers](../../getting-started/profiles/triggers.md).
