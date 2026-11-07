---
description: "Обзор и подключение"
---

# 🏁 Обзор и подключение

## Зависимость

Используйте **API версии 2.0.0** как зависимость `compileOnly` или `provided`. На этапе подготовки выпуска соберите API из исходников 2.0.0 или используйте соответствующий локальный JAR. Ниже указаны координаты выпуска. Перед развёртыванием интеграции, которая получает API из Maven Central, проверьте, что версия 2.0.0 там опубликована.

```kotlin
repositories { mavenCentral() }
dependencies {
    compileOnly("io.github.cryxow:armature-api:2.0.0")
}
```

```xml
<dependency>
  <groupId>io.github.cryxow</groupId>
  <artifactId>armature-api</artifactId>
  <version>2.0.0</version>
  <scope>provided</scope>
</dependency>
```

Сборка с локальным JAR.

```kotlin
dependencies { compileOnly(files("libs/armature-api-2.0.0.jar")) }
```

Не включайте вторую копию API в свой JAR. Классы API при выполнении предоставляет серверный JAR Armature. После публикации выпуска используйте [страницу артефакта](https://central.sonatype.com/artifact/io.github.cryxow/armature-api), а также исходники и Javadoc той же версии.

## Жизненный цикл плагина

```yaml
name: WikiIntegration
version: 1.0.0
main: example.WikiIntegrationPlugin
api-version: '1.21'
depend: [Armature]
folia-supported: true
```

Укажите `depend`, если Armature обязателен. Для необязательной интеграции используйте `softdepend`. До загрузки классов, зависящих от Armature, проверьте, включён ли плагин, затем вызовите `ArmatureApi.tryAnimations()`. До инициализации и после остановки `tryAnimations()` возвращает пустой Optional, а `animations()` выбрасывает исключение. Проверка доступности не предотвращает попытку загрузки отсутствующего класса API.

```java
import com.armaturemc.api.ArmatureAnimationApi;
import com.armaturemc.api.ArmatureApi;
import java.util.Optional;

public final class ArmatureHook {
    public Optional<ArmatureAnimationApi> api() {
        return ArmatureApi.tryAnimations();
    }
}
```

Диспетчер сервисов Bukkit предоставляет `ArmatureAnimationApi`, `ProviderRegistry` и `ArmatureActionDispatcher`, пока Armature включён. После перезагрузки получите сервисы заново. Ссылки на сервисы действительны до остановки.

Для вызовов, связанных с профилем, нужен подходящий активный профиль с загруженными клипами. Для `play(playerId, modelId, animationOptions)` и `openPresentation` предмет не обязан подходить под профиль, но модель и её ресурсы уже должны быть загружены. Оба способа могут отклонить запрос, если игрок недоступен, ресурсы отсутствуют или произошла ошибка рендеринга.
