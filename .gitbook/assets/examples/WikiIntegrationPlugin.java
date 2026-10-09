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
