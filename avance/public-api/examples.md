---
description: "Exemples"
---

# 📝 Exemples

## Intégration autonome complète

[Téléchargez WikiIntegrationPlugin.java](../../.gitbook/assets/examples/WikiIntegrationPlugin.java). Utilisez la dépendance et le descripteur décrits dans [Mise en place](setup.md). Le modèle `wiki_player` doit être chargé et contenir `base` et `inspect`, comme dans le tutoriel Blockbench. Appelez `open`, `inspectModel` et `close` depuis vos commandes ou événements dans un contexte adapté au joueur. Cette classe ne déclare aucune commande de jeu.

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

`open` crée un rendu persistant géré par l’API et émet un signal associé à une condition. `inspectModel` crée un rendu temporaire de durée finie, puis rétablit l’affichage de l’objet tenu. `close` ferme uniquement la génération mémorisée. Armature libère les rendus à la fin de la session. Supprimez alors les handles associés et fermez les rendus lorsque votre intégration en cède le contrôle. En production, retirez de votre table les identifiants des joueurs déconnectés.

## Vérification

Compilez avec l’API de la version exacte d’Armature et celle de Paper ou Folia. Sur un serveur avec le modèle et le pack, appelez `open` et vérifiez que `inspect` puis `base` se jouent. Changez d’objet tenu pour vérifier que le rendu géré par l’API persiste. Appelez `close` et vérifiez que le profil de l’objet actuel revient. Testez aussi un clip absent, un joueur déconnecté, le refus pendant une action protégée et un handle périmé avant d’ajouter la logique de jeu.

Les classes de la page [Fournisseurs](providers.md) montrent comment fournir une identité d’objet personnalisée et des actions via l’API de compatibilité. Pour détecter la fin d’une action ponctuelle, abonnez-vous aux événements décrits dans [Animations et présentations](animations.md). Un résultat asynchrone accepté indique que la demande a été acceptée.
