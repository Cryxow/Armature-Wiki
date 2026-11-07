---
description: "Signaux"
---

# 📡 Signaux

Les plugins peuvent envoyer un signal dans l’espace de noms selon les règles modernes `animations.actions`. La carte héritée `bindings` (et la carte compatible `triggers`) est toujours acceptée par le chargeur de compatibilité. La condition d’action est évaluée par Armature et sa valeur `consume` est retournée en métadonnées. Armature ne peut pas annuler un événement qui appartient à un autre plugin.

```yaml
animations:
  actions:
    - trigger: skript:weapon.pistol.fire
      condition: player.on-ground == true
      animation:
        name: pistol_fire
        duration: 6t
      consume: false
```

```java
ArmatureApi.animations()
    .sendSignal(playerId, "skript:weapon.pistol.fire", Map.of())
    .thenAccept(result -> {
        if (result.matched() && result.animation().accepted()) {
            // The gameplay plugin already performed its own firing logic.
        }
    });
```

C’est le primitif utilisé par les intégrations optionnelles Skript, MythicMobs et Denizen inclus dans `Armature.jar`. Armature enregistre leurs effets, mécaniques ou commandes uniquement lorsque le plugin hôte correspondant est présent. Il n’y a pas de syntaxe magique `emit` dans Skript natif ou Denizen.

Pour les métadonnées de signal typées, utilisez des données scalaires bornées. Les chaînes, booléens et nombres finis sont pris en charge. Les objets Bukkit, les entités, les piles d’objets et les payloads non bornés sont rejetés à la frontière de l’API :

```java
api.emitSignal(playerId, "myplugin:weapon.fire",
    ArmatureSignalData.of(Map.of("ammo", 7, "mode", "semi")));
```

La surcharge de compatibilité `Map<String, String>` est soumise aux mêmes limites : au maximum 64 entrées, des clés de 128 caractères et des chaînes de 1024 caractères.

Le temps d’exécution actuel du déclencheur reçoit une vue de chaîne de compatibilité. Les plugins de jeu restent responsables de la validation et de la mutation de leur propre munition, temps de réfrérence, dégâts et annulation d’événement.

## Alias de déclencheur aligné YAML

```java
api.trigger(playerId, "myplugin:inspect",
    ArmatureSignalData.of(Map.of("allowed", true)));
```

`event.argument.allowed` est disponible pour la condition d’action moderne correspondante. Utilisez le nom d’espace de noms exact du déclencheur dans YAML. Les signaux retournent le statut correspondant/consommé et le résultat de l’animation. Le jeu appartenant à l’appelant décide comment l’utiliser.

L’adaptateur reload-input intégré de WeaponMechanics interprète explicitement un signal `weaponmechanics:reload` consommé en annulant son propre événement fournisseur. Ce comportement spécifique de l’adaptateur n’est pas une annulation automatique d’événements arbitraires Bukkit, Skript, MythicMobs ou Denizen.

Testez les charges autorisées et interdites, les rejets de délai de recharge, les actions protégées et les joueurs indisponibles. L’envoi d’un signal sans règle correspondante n’est pas une lecture brute de clip. Consultez [Conditions](../../premiers-pas/profils/conditions.md).
