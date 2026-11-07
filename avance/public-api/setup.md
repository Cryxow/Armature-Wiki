---
description: "Présentation et mise en place"
---

# 🏁 Présentation et mise en place

## Dépendance

Utilisez l’API **2.0.0 correspondante** comme `compileOnly`/`provided`. Lors de la préparation de la publication, compilez l’API à partir des sources 2.0.0 ou utilisez le JAR local correspondant. Les coordonnées ci-dessous sont la cible de la publication. Sa publication 2.0.0 doit être confirmée avant de déployer une intégration qui les résout depuis Maven Central.

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

Pour une compilation locale de préparation :

```kotlin
dependencies { compileOnly(files("libs/armature-api-2.0.0.jar")) }
```

Ne shadez/ne bundlez pas une seconde copie. Le JAR du serveur d’Armature fournit les classes API à l’exécution. Utilisez l’index [artifact](https://central.sonatype.com/artifact/io.github.cryxow/armature-api) publié et les sources/Javadoc correspondants une fois la publication de la release terminée.

## Cycle de vie du plugin

```yaml
name: WikiIntegration
version: 1.0.0
main: example.WikiIntegrationPlugin
api-version: '1.21'
depend: [Armature]
folia-supported: true
```

`depend` est approprié lorsque Armature est requis. Pour une intégration optionnelle, utilisez `softdepend`, vérifiez que le plugin est activé avant de charger les classes spécifiques à Armature, puis appelez `ArmatureApi.tryAnimations()`. `tryAnimations()` retourne un Optional vide avant l’initialisation ou après l’arrêt ; `animations()` lance une exception. Une méthode de disponibilité ne peut empêcher le chargement d’une classe API manquante.

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

Le gestionnaire de services de Bukkit expose également `ArmatureAnimationApi`, `ProviderRegistry` et `ArmatureActionDispatcher` lorsque Armature est activé. Reacquérez-les après rechargement. Ne conservez pas un service après l’arrêt.

Pour les appels liés à un profil, un profil actif correspondant et des clips chargés sont nécessaires. Pour `play(playerId, modelId, animationOptions)`/`openPresentation`, la correspondance d’objet n’est pas requise, mais le modèle et ses actifs de pack doivent déjà être chargés. Les deux chemins peuvent rejouer des joueurs indisponibles, des actifs manquants ou des échecs de rendu.
