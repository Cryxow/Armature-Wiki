# 🧱 Providers

Most integrations only need the animation facade. Use the provider contracts
when your plugin must teach Armature how to identify its items or must bridge
its gameplay events into Armature's compatibility action dispatcher.

Access the registry only while Armature is available:

```java
ArmatureApi.tryProviders().ifPresent(registry -> {
    registry.registerItemProvider(new MyWeaponsItemProvider(plugin));
});
```

The registry is thread-safe, returns immutable provider snapshots, treats ids
case-insensitively, and rejects duplicate ids. Choose a stable id owned by
your plugin, such as <code>myweapons</code>.

## Item identity providers

An <code>ItemIdentityProvider</code> translates an
<code>ItemStack</code> into Armature's neutral item identity. It does not
change the item:

```java
import com.armaturemc.api.HeldItemCategory;
import com.armaturemc.api.ItemIdentity;
import com.armaturemc.api.ItemIdentityProvider;
import com.armaturemc.api.ItemKey;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

final class MyWeaponsItemProvider implements ItemIdentityProvider {
    private final NamespacedKey weaponKey;

    MyWeaponsItemProvider(MyWeaponsPlugin plugin) {
        this.weaponKey = new NamespacedKey(plugin, "weapon-id");
    }

    @Override
    public String id() {
        return "myweapons";
    }

    @Override
    public Optional<ItemIdentity> resolve(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return Optional.empty();
        }

        String weaponId = item.getItemMeta().getPersistentDataContainer()
            .get(weaponKey, PersistentDataType.STRING);
        if (!"m4a1".equals(weaponId)) {
            return Optional.empty();
        }

        return Optional.of(new ItemIdentity(
            new ItemKey("myweapons", "m4a1"),
            java.util.Set.of(),
            HeldItemCategory.FIREARM
        ));
    }
}
```

The profile can then match the identity returned by the provider:

```yaml
m4a1:
  match:
    item: myweapons:m4a1
  model:
    name: fp_rifle
  animations:
    movement:
      - animation: base
```

Use aliases when several gameplay ids should share one Armature profile:

```java
new ItemIdentity(
    new ItemKey("myweapons", "m4a1"),
    java.util.Set.of(new ItemKey("myweapons", "m4a1-silenced")),
    HeldItemCategory.FIREARM
);
```

If your item can be matched using vanilla material, custom model data, or PDC
data already supported by a structured profile matcher, a custom provider is
not necessary. See [Matching items](../../getting-started/profiles/README.md).

## Custom first-person masking

<code>CustomItemVisualProvider</code> is an optional extension of
<code>ItemIdentityProvider</code>. Implement it only when your provider must
return a client-only copy whose item model is replaced by Armature's hidden
model. Never mutate the supplied inventory stack:

```java
public Optional<ItemStack> createFirstPersonMask(
        ItemStack original, String hiddenModelId) {
    ItemStack copy = original.clone();
    // Apply the hidden model to copy, using the provider's item API.
    return Optional.of(copy);
}
```

The returned copy is a presentation transformation. The original gameplay item
remains owned by your plugin.

## Action providers

An <code>ActionProvider</code> publishes gameplay-side events through
<code>ActionEventSink</code>. It advertises its capabilities, starts with a
sink, and stops during plugin shutdown:

```java
final class MyWeaponsActionProvider implements ActionProvider {
    private ActionEventSink sink;

    @Override
    public String id() {
        return "myweapons";
    }

    @Override
    public Set<ActionCapability> capabilities() {
        return Set.of(
            ActionCapability.EQUIP,
            ActionCapability.FIRE,
            ActionCapability.AIM,
            ActionCapability.RELOAD
        );
    }

    @Override
    public void start(ActionEventSink sink) {
        this.sink = sink;
    }

    @Override
    public void stop() {
        this.sink = null;
    }

    void fire(UUID playerId, int ammo) {
        if (sink != null) {
            sink.accept(new ActionEvent(
                playerId,
                ArmatureAction.FIRE,
                new ItemKey("myweapons", "m4a1"),
                Map.of("ammo", Integer.toString(ammo))
            ));
        }
    }
}
```

Register and start the provider explicitly:

```java
var provider = new MyWeaponsActionProvider();

ArmatureApi.tryProviders().ifPresent(registry -> {
    provider.start(ArmatureApi::dispatch);
    registry.registerActionProvider(provider);
});
```

Stop it in your plugin's <code>onDisable</code>:

```java
provider.stop();
```

The action provider is a compatibility bridge. For a new weapon integration,
prefer <code>ArmatureAnimationApi</code> with a direct asset reference or a
namespaced signal; those paths make the visual intent explicit.

## Capabilities and ownership

<code>ActionCapability</code> values describe what the provider can publish:
<code>EQUIP</code>, <code>FIRE</code>, <code>AIM</code>,
<code>RELOAD</code>, <code>RELOAD_PHASES</code>,
<code>FIREARM_STATE</code>, <code>FIRE_MODE</code>, and <code>SKIN</code>.

Providers translate state; they do not transfer ownership. Your plugin remains
responsible for:

* item creation and inventory changes;
* ammunition, reload state, damage, projectiles, and cooldowns;
* event cancellation and gameplay permissions;
* the lifecycle of any external provider resources.

Armature consumes the identity/action information to select and render a
presentation. It does not call back into the provider to mutate gameplay.
