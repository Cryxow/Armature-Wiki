---
description: "Examples"
---

# Examples

## Complete standalone integration

[Download WikiIntegrationPlugin.java](../../.gitbook/assets/examples/WikiIntegrationPlugin.java).
Use the dependency/plugin descriptor from [Setup](setup.md). The model
`wiki_player` must already be loaded and contain `base` and `inspect`, as created
in the Blockbench tutorial. Invoke `open`, `inspectModel` and `close` from your
own player-safe command/event path; this class does not register a gameplay
command itself.

```java
package example;

import com.armaturemc.api.ArmatureApi;
import com.armaturemc.api.ArmaturePresentation;
import com.armaturemc.api.ArmaturePresentationHandle;
import com.armaturemc.api.ArmatureSignalData;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class WikiIntegrationPlugin extends JavaPlugin {
    private final Map<UUID, ArmaturePresentationHandle> views = new ConcurrentHashMap<>();

    public void open(Player player) {
        UUID id = player.getUniqueId(); // Call from the player's safe context.
        ArmatureApi.tryAnimations().ifPresent(api -> {
            var definition = new ArmaturePresentation("wiki_player", Map.of(
                "movement", List.of(Map.of("animation", "base")),
                "actions", List.of(Map.of(
                    "trigger", "wiki:inspect",
                    "condition", "event.argument.allowed == true",
                    "animation", Map.of("name", "inspect", "duration", "12t")
                ))
            ));
            api.openPresentation(id, definition).thenAccept(result -> {
                result.handle().ifPresent(handle -> {
                    // Store handles only; do not access Bukkit state here.
                    views.put(id, handle);
                    api.trigger(id, "wiki:inspect",
                        ArmatureSignalData.of(Map.of("allowed", true)));
                });
                if (!result.accepted()) getLogger().fine("Armature: " + result.status());
            });
        });
    }

    public void inspectModel(Player player) {
        UUID id = player.getUniqueId();
        ArmatureApi.tryAnimations().ifPresent(api -> api.play(id, "wiki_player",
            Map.of("name", "inspect", "duration", "12t", "speed", 1.0)));
    }

    public void close(Player player) {
        UUID id = player.getUniqueId();
        ArmaturePresentationHandle handle = views.remove(id);
        if (handle != null) ArmatureApi.tryAnimations().ifPresent(api ->
            api.closePresentation(id, handle));
    }

    @Override public void onDisable() {
        ArmatureApi.tryAnimations().ifPresent(api -> views.forEach(api::closePresentation));
        views.clear();
    }
}
```

`open` creates a persistent API-owned view and sends a condition-bearing signal.
`inspectModel` creates a finite temporary view and restores the current held
item afterwards. `close` only closes the stored generation. Armature releases
views during session cleanup; integrations should discard corresponding handles
and close views when relinquishing ownership. Do not keep an unbounded map of
disconnected player IDs in a production integration.

## Verify

Build against the exact release API and Paper/Folia API. Load a server with the
model/pack, call `open`, observe `inspect` then `base`, change held item and
confirm the API-owned view persists, then call `close` and confirm restoration
of the current item's profile. Test missing clip, offline player, protected
action rejection and a stale handle before adding gameplay.

For a custom item identity provider and compatibility action provider, see the
complete class examples in [Providers](providers.md). For finite action
completion, register the lifecycle events described in
[Animations & Presentations](animations.md); an accepted future is not an
animation-completion notification.
