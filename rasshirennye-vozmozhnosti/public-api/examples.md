---
description: "Примеры"
---

# 📝 Примеры

## Полный пример отдельного плагина

[Скачайте WikiIntegrationPlugin.java](../../.gitbook/assets/examples/WikiIntegrationPlugin.java). Подключите зависимость и дескриптор плагина из раздела [Обзор и подключение](setup.md). Загрузите модель `wiki_player` с клипами `base` и `inspect` из руководства по Blockbench. Вызывайте `open`, `inspectModel` и `close` из своих команд или событий в безопасном для игрока контексте. Этот класс не регистрирует игровые команды.

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

`open` создаёт постоянное представление, управляемое через API, и отправляет сигнал с данными для условия. `inspectModel` создаёт временное представление конечной длительности и затем восстанавливает отображение предмета в руке. `close` закрывает только сохранённое поколение представления. Armature освобождает представления при завершении сеанса. Удаляйте соответствующие дескрипторы и закрывайте представления, когда ваша интеграция больше ими не управляет. Удаляйте идентификаторы отключившихся игроков из таблицы, чтобы она не росла бесконечно.

## Проверка

Компилируйте с API нужного выпуска Armature и API Paper или Folia. Запустите сервер с моделью и пакетом ресурсов. Вызовите `open` и проверьте, что воспроизводятся `inspect` и затем `base`. Смените предмет в руке и убедитесь, что представление API сохранилось. Вызовите `close` и проверьте, что восстановился профиль текущего предмета. Перед добавлением игровой логики проверьте отсутствующий клип, отключившегося игрока, отклонение запроса во время защищённого действия и устаревший дескриптор.

Примеры полных классов для провайдера идентификаторов предметов и провайдера действий для совместимости приведены в разделе [Провайдеры](providers.md). Чтобы отследить завершение конечного действия, подпишитесь на события из раздела [Анимации и визуальные представления](animations.md). Принятый асинхронный результат сообщает о принятии запроса.
