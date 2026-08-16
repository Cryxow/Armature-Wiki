# 🛠️ Configuration

Armature lit `plugins/Armature/config.yml`. Les valeurs sont chargées au démarrage et par `/armature reload`.

## `config.yml` complet

```yaml
input:
  movement-threshold: 0.015
  movement-stop-delay-ticks: 3

debug:
  enabled: false
  tracker-lifecycle: true
  animations: true
  empty-hand-mask: false

render:
  enabled: true
  first-person-only: true
  model-adaptation: true
  vanilla-item-mode: mask
  empty-hand-mask:
    repair-server-materialization: true
  motion:
    sway:
      enabled: true
      maximum-rotation: 4.0
      look:
        lerp-speed: 0.0
        yaw-gain: 0.12
        pitch-gain: 0.10
        roll-gain: 0.05
      movement:
        lerp-speed: 14.0
        yaw-gain: 0.20
        pitch-gain: 0.06
        roll-gain: 0.35
    bob:
      enabled: true
      cycles-per-block: 1.8
      horizontal-amplitude: 0.025
      vertical-amplitude: 0.018
      roll-amplitude: 0.8
      frequency: 9.0
      damping: 0.9
    camera-follow:
      enabled: true
      frequency: 9.0
      damping: 0.9
      yaw-position-gain: 0.0025
      pitch-position-gain: 0.0020
      maximum-offset: 0.035
      pitch-compensation: 0.20

armor-mappings:
  "your_namespace:knight_helmet": knight
```

## Entrée

`input.*`

| Clé                         | Type   | Signification                                                                                                                                                                                                      |
| --------------------------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `movement-threshold`        | nombre | Distance horizontale par tick serveur requise avant qu’Armature considère le joueur comme marchant. Des valeurs plus basses détectent les mouvements subtils ; des valeurs plus hautes réduisent les tremblements. |
| `movement-stop-delay-ticks` | entier | Nombre de ticks immobiles avant que la boucle marche/course revienne à l’état inactif. Des valeurs plus hautes évitent les clignotements lors d’un bref arrêt.                                                     |

## Débogage

`debug.*`

| Clé                 | Type    | Signification                                                                                                                                                                        |
| ------------------- | ------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `enabled`           | booléen | Interrupteur principal des logs de diagnostic. Gardez `false` en production.                                                                                                         |
| `tracker-lifecycle` | booléen | Journalise la création, la récupération et la fermeture des trackers BetterModel lorsque le débogage est activé.                                                                     |
| `animations`        | booléen | Journalise la distribution, le remplacement, la vitesse et la fin des animations lorsque le débogage est activé.                                                                     |
| `empty-hand-mask`   | booléen | Journalise l’historique du slot sélectionné, les mutations de l’inventaire serveur et l’état du masque client. À utiliser uniquement pour diagnostiquer le masquage de la main vide. |

## Rendu

`render.*`

| Clé                                             | Type    | Signification                                                                                                                                                     |
| ----------------------------------------------- | ------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `enabled`                                       | booléen | Interrupteur global du rendu Armature. `false` retire la présentation Armature tout en laissant les plugins fournisseur/gameplay actifs.                          |
| `first-person-only`                             | booléen | `true` masque le modèle à la troisième personne.                                                                                                                  |
| `model-adaptation`                              | booléen | Adapte le modèle pour contourner les limitations en jeu. Ne modifie pas votre modèle.                                                                             |
| `vanilla-item-model`                            | option  | `source-model` modifie le vrai modèle d’item pour masquer la main ; `mask` affiche un faux item au joueur pour masquer sa main. `mask` offre plus de flexibilité. |
| `empty-hand-mask.repair-server-materialization` | booléen | Gardez cette option activée avec `mask` pour éviter les glitches d’inventaire.                                                                                    |

## Mouvement

`render.motion.*`

Tous les gains sont des multiplicateurs visuels. Les rotations sont en degrés ; les offsets sont des distances dans l’espace du modèle. Les couches de mouvement se composent sur la transformation courante du modèle.

### Balancement

`render.motion.sway.*`

| Clé                   | Signification                                                                                                                                  |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `enabled`             | Active le balancement lié à la caméra et au déplacement.                                                                                       |
| `maximum-rotation`    | Limite absolue de la rotation combinée. Empêche les angles extrêmes lorsque plusieurs gains s’additionnent.                                    |
| `look.lerp-speed`     | Lissage du balancement lié à la caméra. `0` applique directement les deltas caméra ; les valeurs supérieures suivent progressivement la cible. |
| `look.yaw-gain`       | Convertit le lacet de la vue en lacet du modèle.                                                                                               |
| `look.pitch-gain`     | Convertit le tangage de la vue en tangage du modèle.                                                                                           |
| `look.roll-gain`      | Convertit le mouvement de la vue en roulis du modèle.                                                                                          |
| `movement.lerp-speed` | Lissage du balancement lié au déplacement. Les valeurs supérieures se stabilisent plus vite.                                                   |
| `movement.yaw-gain`   | Convertit la direction de déplacement en lacet.                                                                                                |
| `movement.pitch-gain` | Convertit le déplacement en tangage.                                                                                                           |
| `movement.roll-gain`  | Convertit le déplacement en roulis.                                                                                                            |

### Balancement de marche

`render.motion.bob.*`

| Clé                    | Signification                                                                                          |
| ---------------------- | ------------------------------------------------------------------------------------------------------ |
| `enabled`              | Active le balancement de pas pendant le déplacement.                                                   |
| `cycles-per-block`     | Oscillations par bloc parcouru. Des valeurs supérieures produisent des pas plus rapprochés et rapides. |
| `horizontal-amplitude` | Distance du balancement latéral.                                                                       |
| `vertical-amplitude`   | Distance du balancement vertical.                                                                      |
| `roll-amplitude`       | Angle de roulis appliqué par le cycle.                                                                 |
| `frequency`            | Réactivité du ressort. Des valeurs supérieures réagissent plus vite.                                   |
| `damping`              | Perte d’énergie du ressort. Des valeurs supérieures réduisent le dépassement.                          |

### Suivi de caméra

`render.motion.camera-follow.*`

Le suivi de caméra est une inertie positionnelle, pas une orientation vers la caméra. L’orientation billboard reste responsable de faire face au joueur.

| Clé                   | Signification                                                         |
| --------------------- | --------------------------------------------------------------------- |
| `enabled`             | Active le suivi positionnel de caméra.                                |
| `frequency`           | Vitesse de réponse du ressort positionnel.                            |
| `damping`             | Stabilise le ressort et limite le dépassement.                        |
| `yaw-position-gain`   | Translation locale générée par le lacet de caméra.                    |
| `pitch-position-gain` | Translation locale générée par le tangage de caméra.                  |
| `maximum-offset`      | Translation maximale du suivi caméra.                                 |
| `pitch-compensation`  | Compense le placement vertical lors du regard vers le haut ou le bas. |

## Correspondances d’armures personnalisées

`armor-mappings.*`

Armature prend automatiquement en charge les armures de joueur vanilla, mais une étape est nécessaire pour les armures personnalisées ajoutées par un plugin. Consultez [Armures personnalisées](../avance/armures-personnalisees.md).

## Recettes de réglage

**Vue compétitive stable :** définissez `sway.maximum-rotation` à `2.0~3.0`, réduisez le roulis du déplacement et gardez le damping du suivi caméra près de `0.9`.

**Vue cinématique :** augmentez progressivement les gains de vue et de déplacement, augmentez les amplitudes de balancement, puis les limites seulement si le clipping reste acceptable.

**Diagnostiquer les saccades :** activez `debug.enabled`, `debug.tracker-lifecycle` et `debug.animations` ; testez avec un joueur ; désactivez ensuite ces options. Le réglage du mouvement ne répare pas un tracker qui n’est plus planifié.
