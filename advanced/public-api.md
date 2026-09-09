# 🧩 Public API

Armature exposes a stable Java API for plugins that already own gameplay. It
controls the first-person presentation only: models, animation playback,
presentation state, and the routing of animation triggers.

The API does not own item stacks, ammunition, damage, projectiles, cooldowns,
reload logic, or event cancellation. Your weapon, item, or skill plugin remains
responsible for those rules.

## Stable release

The public API is distributed as a normal Maven Central dependency:

| Property | Value |
| --- | --- |
| Group | `io.github.cryxow` |
| Artifact | `armature-api` |
| Stable version | `1.3.0` |
| Java package | `com.armaturemc.api` |

* [Open the artifact on Maven Central](https://central.sonatype.com/artifact/io.github.cryxow/armature-api/1.3.0)
* [Open the versioned Javadocs](https://javadoc.io/doc/io.github.cryxow/armature-api/1.3.0/index.html)

The dependency version is the released API artifact version. For `1.3.0`,
`ArmatureApi.API_MAJOR_VERSION` is `1` and
`ArmatureApi.API_VERSION` is the diagnostic contract string
`1.0.0`; the diagnostic string is not a Maven coordinate. Use the
artifact version in Gradle or Maven and use the major constant when checking
source compatibility.

Only classes under `com.armaturemc.api` are public API. BetterModel,
PacketEvents, Armature implementation packages, and gameplay-plugin internals
are not supported integration surfaces.

## Choose an integration style

| Need | Use |
| --- | --- |
| Your code knows the exact clip to play | [Channel-aware animation playback](public-api/animations.md) |
| Your profile should decide which clip answers an event | [Namespaced signals](public-api/signals.md) |
| Your plugin supplies item identity or gameplay events | [Provider contracts](public-api/providers.md) |
| You want a complete working weapon integration | [Concrete examples](public-api/examples.md) |

The two normal ways to connect a custom weapon are therefore:

1. Configure a profile and call `playAsset`, `startLoop`, or
   `triggerAdditive` directly.
2. Configure an `animations.actions` rule and send a namespaced signal such as
   `myweapons:m4a1.fire`.

Both paths use the same active Armature presentation. Neither path replaces
the gameplay item or duplicates the weapon system.

## Quick start

Add the dependency as `compileOnly` (Gradle) or `provided` (Maven),
declare Armature as an optional or required plugin dependency, and call the API
only after it is available:

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

```java
ArmatureApi.tryAnimations().ifPresent(api ->
    api.playAnimation(player.getUniqueId(), "inspect")
        .thenAccept(result -> {
            if (!result.accepted()) {
                getLogger().fine("Armature: " + result.status());
            }
        })
);
```

Every operation targeting a player returns a `CompletableFuture` and is
scheduled on that player's scheduler, which keeps the same code safe on Paper
and Folia. Treat the completion value as data. If a continuation must touch a
player or Bukkit state, schedule that follow-up on the player scheduler.

## Stable API boundary

The `1.3.0` artifact is profile-bound: an active item profile must already
select the player's model. The API can play an asset directly, but it does not
create an independent model view from an arbitrary model id.

The standalone presentation types visible in development builds, such as
`ArmaturePresentation`, are not part of the stable `1.3.0` artifact.
Do not use those preview-only types when compiling an integration against
`1.3.0`.

Continue with [Setup](public-api/setup.md), or jump to the [M4A1 examples](public-api/examples.md).
