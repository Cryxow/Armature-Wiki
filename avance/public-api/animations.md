---
description: "Animations et présentations"
---

# ▶️ Animations et présentations

## Lecture de modèles autonome et présentations persistantes

Ces opérations nécessitent le JAR de plugin/API 2.0.0 correspondant. La Javadoc 1.3.0 antérieure ne les décrit pas.

Sélectionnez un modèle chargé explicitement pour jouer une animation finie sans fichier de profil, sans correspondeur d’objet ni appel API précédent :

```java
ArmatureApi.tryAnimations().ifPresent(api ->
    api.play(playerId, "arm_rifle", Map.of(
        "name", "inspect",
        "speed", 1.25,
        "duration", "40t",
        "can-be-cancelled", true
    )).thenAccept(result -> {
        if (!result.accepted()) logger.fine("Playback: " + result.status());
    })
);
```

L’appel remplace temporairement la vue du joueur via le moteur de rendu actif. Le modèle et les actifs du pack de ressources doivent déjà être chargés. Armature découvre les modèles indépendamment de la correspondance d’objet du profil. L’inventaire réel reste inchangé. Après la fin de l’action finie, Armature restaure la présentation appropriée à l’objet tenu **à ce moment-là**. Un modèle inconnu ou une animation manquante est rejeté avant de remplacer la vue existante. Une échec du moteur de rendu après le montage libère la vue temporaire et restaure la correspondance d’objet.

La future se résout lorsque le modèle est prêt et que la demande de lecture est acceptée, et non lorsque l’animation se termine. La poignée de présentation retournée peut fermer cette génération exacte précocement. Fermer une ancienne poignée ne peut pas fermer une présentation plus récente.

Pour les déplacements persistants, les actions répétées, les additifs et les conditions, ouvrez une présentation avec le contenu exact d’une section YAML moderne `animations` :

```java
var presentation = new ArmaturePresentation("arm_rifle", Map.of(
    "movement", List.of(Map.of("animation", "base")),
    "actions", List.of(Map.of(
        "trigger", "myplugin:inspect",
        "condition", List.of("event.argument.allowed == true"),
        "hide-vanilla-hand", true,
        "animation", Map.of(
            "name", "inspect",
            "duration", "40t",
            "speed", 1.25,
            "cooldown", "10t",
            "can-be-cancelled", false
        )
    ))
));

api.openPresentation(playerId, presentation).thenAccept(result -> {
    result.handle().ifPresent(handle -> {
        // Store this handle in the integration's per-player state to close later.
        presentationHandles.put(playerId, handle);
        api.trigger(playerId, "myplugin:inspect",
            ArmatureSignalData.of(Map.of("allowed", true)));
    });
});

// When the integration relinquishes this view:
var handle = presentationHandles.remove(playerId);
if (handle != null) api.closePresentation(playerId, handle);
```

Le constructeur complet accepte également un arbre `variables`, un décalage de modèle, un taux de balancement de modèle et la visibilité de la main principale par défaut. Les tableaux et listes sont profondément copiés avant le planning. L’API utilise le même analyseur et l’évaluateur de règles que YAML, y compris la validation des propriétés non supportées et des combinaisons invalides :

| Section | Paramètres pris en charge |
| --- | --- |
| Règle `movement` | `when`, `hide-vanilla-hand`, `animation` |
| Animation de déplacement | `name` (chaîne/liste), `selection`, `speed`, `blend-in`, `blend-out`, `sway-rate`, `loop`, `start`, `end` |
| Règle `actions` | `trigger`, `condition`, `consume`, `hide-vanilla-hand`, `animation` |
| Animation d’action | `name` (chaîne/liste), `selection`, `duration`, `speed`, `blend-in`, `blend-out`, `cooldown`, `can-be-cancelled` |
| Règle `additives` | `when`, `animation`, `playback`, `group`, `priority`, `weight`, `blend-in`, `blend-out`, `bones`, `channels`, `position-gain`, `rotation-gain`, `reference` |
| Animation additive | `name` (chaîne/liste), `selection`, `speed` |
| Cartographie des poids | `parameter`, `input`, `output`, `easing`, `clamp` |
| Posture de référence | `animation`, `time` |

Les unités de durée et les valeurs par défaut correspondent au YAML moderne (`t`, `s`, ou des ticks numériques si le champ les accepte). La durée d’action omise provient du clip chargé. Le résultat `consume` est une décision de routage. Le gameplay personnalisé reste intégré. L’envoi d’un déclencheur ne supprime pas lui-même un événement Bukkit arbitraire.

Chaque présentation ouverte possède un état de règle frais. Les appels répétés de `trigger(...)` dans cette vue partagent les temps de réfréquence et les séquences de variantes. Les appels séparés de `play(player, model, options)` créent des contextes distincts. Leurs temps de réfréquence ne sont pas partagés. Utilisez une présentation persistante pour les systèmes d’armes ou de compétences avec des déclencheurs répétés.

Une seule vue appartenant à l’API est active par joueur. Elle persiste jusqu’à ce qu’elle soit fermée ou remplacée, mais est libérée lors du nettoyage de la session du joueur (mort, déconnexion, changement de monde), du rechargement de la plugin, ou de la désactivation du runtime/joueur. L’ouverture d’un remplacement respecte une action actuellement non annulable. La fermeture explicite d’une présentation la détruit. Le catalogue de profils configuré reste en lecture seule et ne contient pas ces vues temporaires. Les identifiants de vues temporaires dans les données de cycle/opération commencent par `api.` et ne sont pas des IDs de profil YAML.

Pendant cette vue, les références d’actifs non fixées et la lecture brute utilisent son modèle/règles. Le dispatch hérité intégré `ArmatureAction` ne fait pas appel au profil de l’objet tenu. `trigger(...)` est l’alias aligné YAML pour `signal(...)`. Les surcharges de signal existantes restent compatibles.

## Canaux d’action, boucle et additive

Les nouvelles intégrations devraient utiliser `ArmatureAnimationReference` au lieu de deviner si une chaîne est une action finie, une boucle persistante ou une couche additive :

```java
var api = ArmatureApi.animations();

api.playAsset(playerId, ArmatureAnimationReference.action("inspect"));
api.startLoop(playerId, ArmatureAnimationReference.loop("aim"));
api.triggerAdditive(playerId, ArmatureAnimationReference.additive("recoil"));
```

Utilisez `ArmatureAnimationReference.profile(profileId, assetId, channel)` lorsqu’un profil doit être explicite. Le profil doit toujours être celui rendu pour l’objet tenu du joueur. Armature retourne `PROFILE_NOT_ACTIVE` plutôt que de remplacer silencieusement cette présentation.

Les opérations acceptées retournent un `ArmatureOperationResult`. Sa poignée est limitée au joueur, au profil, au canal et à un jeton de temps de fonctionnement monotone :

```java
var request = api.triggerAdditive(playerId,
    ArmatureAnimationReference.additive("recoil"));
request.thenAccept(result -> result.handle().ifPresent(handle ->
    api.stop(playerId, handle)
));
```

Arrêter une poignée ancienne ne jamais arrête un remplacement plus récent. Une poignée obsolète retourne `HANDLE_NOT_ACTIVE`. Une boucle directe reste sélectionnée jusqu’à ce qu’elle soit arrêtée, que l’objet/profil change, que Armature soit rechargé, ou que le temps de fonctionnement soit désactivé.

`catalog()` retourne une capture de profil immuable avec les ids d’action, de boucle, additive et signal. `snapshot(playerId)` retourne l’état de présentation et les ids additifs actifs pour un joueur en ligne. Aucune méthode n’expose l’état de jeu.

## Appels liés au profil et de compatibilité

`play(UUID, String)` est l’API de compatibilité pour un id d’action hérité défini par un administrateur dans le profil de l’objet actuellement tenu par le joueur :

```java
animations.play(player.getUniqueId(), "inspect");
animations.playAction(player.getUniqueId(), ArmatureAction.FIRE);
```

`playAnimation(playerId, animationId)` cible l’actif fini exact du profil actif, y compris les actifs d’action en ligne modernes. `startLoop(playerId, assetId)` accepte soit un identifiant de boucle intégré, soit un identifiant d’actif de boucle direct exact. La surcharge sensible au canal reste préférée lorsque l’appelant souhaite un gestionnaire.

Pour les intégrations qui possèdent les noms d’animation, utilisez les méthodes brutes. Elles nécessitent un profil d’objet actif ou une présentation API pour lier le modèle rendu. La boucle demandée n’a pas besoin d’être listée dans ce profil. Le moteur de rendu valide l’actif contre le modèle chargé au moment de la lecture :

```java
animations.playRawAnimation(playerId, "fire");
animations.startRawLoop(playerId, "aim");
```

Le profil contrôle toujours la correspondance d’objet, l’identifiant de modèle, le masquage de la main principale et la politique de présentation. La lecture brute ne modifie pas l’état de jeu ni ne monte un profil différent.

La surcharge par énumération déclenche une action Armature intégrée. `startLoop` est la forme explicite orientée état pour les actions persistantes telles que `IDLE`, `AIM` ou `CROSSBOW_CHARGE` ; `stopLoop` efface la boucle sélectionnée. `stopAction` annule la boucle unique actuelle et reprend la boucle sélectionnée.

```java
animations.startLoop(playerId, ArmatureAction.CROSSBOW_CHARGE);
animations.stopAction(playerId);
animations.stopLoop(playerId);
```

Lorsqu’une intégration connaît déjà l’identifiant de profil, elle peut rendre le choix explicite :

```java
animations.playAnimation(playerId, "m4a1", "inspect");
animations.playAnimation(playerId, "m4a1", "swing");
```

Le profil sélectionné doit être le profil actuellement rendu pour l’objet tenu du joueur. Cela empêche une demande d’animation de remplacer silencieusement la présentation de l’objet de jeu. Le résultat est `PROFILE_NOT_ACTIVE` lorsque le profil existe mais ne correspond pas à l’objet tenu.

Ces appels de lecture API ne modifient pas l’objet, les munitions, les dégâts ou l’état de rechargement, et n’annulent pas un événement appartenant à l’appelant. Le plugin de jeu reste responsable de ces règles.

## Événements de cycle de vie

L’API déclenche des événements Bukkit pour les animations finies acceptées :

- `ArmatureAnimationStartEvent` expose `getProfileId()`, `getActionId()`, `getAnimation()` et `getToken()`.
- `ArmatureAnimationEndEvent` expose les mêmes champs plus `getReason()` : `COMPLETED`, `CANCELLED`, `REPLACED` ou `REMOVED`.

Les transitions de boucle sont des transitions du moteur de rendu natif et ne produisent pas d’événements de cycle de vie. L’événement de fin est l’accroche fiable pour démarrer une compétence de suivi appartenant au jeu. Cela n’implique pas que Armature possède cet état de jeu.

## Résultats et état

`ArmatureAnimationResult.Status` distingue la lecture acceptée d’un profil/action manquant, d’un délai de recharge, d’une action active non annulable, d’un joueur indisponible, d’une poignée obsolète ou d’un rejet du moteur de rendu natif. `ArmatureAnimationState` et `ArmatureAnimationSnapshot` exposent uniquement les données de présentation : clé de l’objet actuel, boucle souhaitée, animation active, couches additives et ticks restants. Elles n’exposent intentionnellement pas l’état de jeu.

```java
animations.getState(playerId).thenAccept(snapshot -> snapshot.ifPresent(state -> {
    if (state.hasActiveAction()) {
        plugin.getLogger().fine("Armature action: " + state.activeActionId()
            + " / animation: " + state.activeAnimation());
    }
}));
```

Le constructeur de `ArmaturePresentation` autorise un arbre de variables en ligne pour une vue éphémère appartenant à l’API. Cela n’autorise pas les blocs `variables:` locaux dans les profils YAML : les profils disque utilisent le `plugins/Armature/variables.yml` partagé.
