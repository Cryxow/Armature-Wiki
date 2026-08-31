# Custom armors

Armature passes player armor rendering to BetterModel. The armor texture
folder name is the BetterModel armor type.

## Custom textures for vanilla armor

Replace the BetterModel textures in:

~~~text
plugins/BetterModel/armors/armors/<vanilla_type>/
~~~

Use the expected <code>armor.png</code> and, for leggings,
<code>leggings.png</code> filenames. Keep the folder structure used by the
BetterModel installation.

## Armor supplied by an item plugin

Assume an item provider exposes:

~~~text
your_namespace:emerald_chestplate
your_namespace:emerald_leggings
~~~

Create the matching BetterModel armor folder:

~~~text
plugins/BetterModel/armors/armors/emerald/
~~~

Place the player armor textures there. The folder name
<code>emerald</code> is an arbitrary BetterModel armor type; it must match the
mapping value.

## Map the item identities

Add the custom armor item ids under <code>armor-mappings</code> in
<code>plugins/Armature/config.yml</code>:

~~~yaml
armor-mappings:
  "your_namespace:emerald_chestplate": emerald
  "your_namespace:emerald_leggings": emerald
~~~

The key is the exact item identity returned by the item provider. The value is
the BetterModel armor directory name.

Apply the mapping with:

~~~text
/armature reload all
~~~

This mapping affects presentation only. It does not create items, change armor
slots, or alter the gameplay plugin's armor state.
