---
description: "Overview & Setup"
---

# 🏁 Overview & Setup

## Dependency

Use the **matching 2.0.0 API** as `compileOnly`/`provided`. During release
preparation, build the API from the 2.0.0 sources or use the matching local JAR.
The coordinates below are the release target; their 2.0.0 publication must be
confirmed before deploying an integration that resolves them from Maven Central.

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

For a local preparation build:

```kotlin
dependencies { compileOnly(files("libs/armature-api-2.0.0.jar")) }
```

Do not shade/bundle a second copy. Armature's server JAR supplies API classes at
runtime. Use the published [artifact index](https://central.sonatype.com/artifact/io.github.cryxow/armature-api)
and matching source/Javadoc when release publication is complete.

## Plugin lifecycle

```yaml
name: WikiIntegration
version: 1.0.0
main: example.WikiIntegrationPlugin
api-version: '1.21'
depend: [Armature]
folia-supported: true
```

`depend` is appropriate when Armature is required. For an optional integration,
use `softdepend`, check the plugin is enabled before loading Armature-specific
classes, then call `ArmatureApi.tryAnimations()`. `tryAnimations()` returns an
empty Optional before initialization/after shutdown; `animations()` throws.
An availability method cannot prevent a missing API class from being loaded.

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

Bukkit's service manager also exposes `ArmatureAnimationApi`, `ProviderRegistry`
and `ArmatureActionDispatcher` while Armature is enabled. Reacquire after reload;
do not retain a service across shutdown.

For profile-bound calls, a matching active profile and loaded clips are needed.
For `play(playerId, modelId, animationOptions)`/`openPresentation`, item matching
is not required, but the model and its pack assets must already be loaded.
Both paths can reject unavailable players, missing assets or renderer failures.
