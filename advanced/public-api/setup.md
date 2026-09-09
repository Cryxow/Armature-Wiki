# 🚀 Setup

This guide targets the stable
<code>io.github.cryxow:armature-api:1.3.0</code> artifact.

## 1. Add the Maven Central dependency

Armature's API is already published on Maven Central. No Armature repository
URL is needed.

{% tabs %}
{% tab title="Gradle Kotlin" %}

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.github.cryxow:armature-api:1.3.0")
}
```

{% endtab %}
{% tab title="Gradle Groovy" %}

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compileOnly 'io.github.cryxow:armature-api:1.3.0'
}
```

{% endtab %}
{% tab title="Maven" %}

```xml
<dependency>
    <groupId>io.github.cryxow</groupId>
    <artifactId>armature-api</artifactId>
    <version>1.3.0</version>
    <scope>provided</scope>
</dependency>
```

{% endtab %}
{% endtabs %}

The artifact includes the API classes, sources, and Javadocs:

* [Maven Central coordinates](https://central.sonatype.com/artifact/io.github.cryxow/armature-api/1.3.0)
* [Armature API 1.3.0 Javadocs](https://javadoc.io/doc/io.github.cryxow/armature-api/1.3.0/index.html)

## 2. Do not bundle the API

Use <code>compileOnly</code> or <code>provided</code>, never
<code>implementation</code> or a shaded copy. The server's
<code>Armature.jar</code> supplies the classes at runtime. The standalone API
artifact is a compile-time contract, not a second plugin to install.

Your integration does not need BetterModel or PacketEvents as a compile-time
dependency just to call the public API. Those are Armature's implementation
dependencies.

## 3. Declare the plugin dependency

Use a soft dependency when Armature is optional:

```yaml
name: MyWeapons
version: 1.0.0
main: com.example.myweapons.MyWeaponsPlugin
api-version: '1.21'
softdepend:
  - Armature
```

Use <code>depend</code> instead of <code>softdepend</code> when your plugin
cannot function without Armature:

```yaml
depend:
  - Armature
```

<code>softdepend</code> controls load order; it does not enable Armature and it
does not make an absent Armature safe by itself.

## 4. Check availability

For an optional integration, keep the Armature-specific code behind an
availability check. <code>tryAnimations()</code> returns an empty
<code>Optional</code> when the plugin is absent or not initialized;
<code>animations()</code> throws <code>IllegalStateException</code> in that
situation.

```java
import com.armaturemc.api.ArmatureAnimationApi;
import com.armaturemc.api.ArmatureApi;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class ArmatureHook {

    public Optional<ArmatureAnimationApi> api() {
        return ArmatureApi.tryAnimations();
    }

    public void playInspect(Player player) {
        api().ifPresent(armature ->
            armature.playAnimation(player.getUniqueId(), "inspect")
                .thenAccept(result -> {
                    if (!result.accepted()) {
                        // Use result.status() for diagnostics.
                    }
                })
        );
    }
}
```

If Armature is optional and your plugin must also run without it, load the hook
class only after checking that the Armature plugin is enabled. This prevents an
optional integration from becoming a hard class-loading dependency.

The API is also available through the Bukkit service manager while Armature is
enabled:

```java
import com.armaturemc.api.ArmatureAnimationApi;
import org.bukkit.Bukkit;

var armature = Bukkit.getServicesManager().load(ArmatureAnimationApi.class);
if (armature != null) {
    // Use the same facade as ArmatureApi.animations().
}
```

The service is removed during Armature shutdown. Reacquire it after a plugin
reload instead of keeping a reference across the lifecycle.

## 5. Match the active profile

The stable API does not create a presentation from an arbitrary model id. A
player must already have an active Armature profile. That profile selects the
item, model, and available animation assets.

Minimal profile for an M4A1 represented by a paper item with custom model data:

```yaml
m4a1:
  match:
    item:
      material: minecraft:paper
      data:
        custom-model-data: 41001
  model:
    name: fp_rifle
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: myweapons:m4a1.fire
        animation:
          name: fire
          duration: 4t
```

Put <code>fp_rifle.bbmodel</code> in
<code>plugins/Armature/models/</code>, put the profile in
<code>plugins/Armature/profiles/</code>, reload the configuration, and make
sure the client has received the generated resource pack. The animation names
must exist in the Blockbench model.

## 6. Handle the asynchronous result

Player operations return <code>CompletableFuture</code>. The request result
tells you whether Armature accepted the presentation request; it is not a
replacement for your gameplay event or weapon state.

```java
armature.playAsset(player.getUniqueId(),
        ArmatureAnimationReference.action("fire"))
    .thenAccept(result -> {
        if (result.accepted()) {
            // Armature started the visual action.
        } else {
            // Log result.status(), for example NO_PROFILE or COOLDOWN.
        }
    });
```

Completions may run outside your original event callback. Do not read or mutate
Bukkit state from an arbitrary continuation. If a follow-up must access a
player, schedule it with that player's scheduler.

## Versioning rules

* Pin <code>1.3.0</code> for the stable API.
* Do not use <code>LATEST</code>, <code>+</code>, or a development version in a
  released plugin.
* Use <code>ArmatureApi.API_MAJOR_VERSION</code> as the compatibility boundary.
* Use <code>ArmatureApi.API_VERSION</code> for diagnostics only; it is not the
  Maven coordinate.
* Compile only against <code>com.armaturemc.api</code>; implementation packages
  may change.

Preview-only standalone presentation classes are not available in the stable
<code>1.3.0</code> artifact. Use the profile-bound API documented on the next
pages.
