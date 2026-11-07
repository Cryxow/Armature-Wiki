---
description: "Référence de configuration"
---

# ⚙️ Référence de configuration

Armature lit `plugins/Armature/config.yml`. Cette page reproduit les valeurs par défaut intégrées de la version 2.0.0 et décrit chaque option de feuille active. La migration de profil utilise des limites internes et ne dispose d’aucune configuration. Les valeurs des administrateurs existants sont conservées lors des mises à jour de schéma, de sorte qu’une installation en cours de fonctionnement peut différer.

## Apporter un changement

| Requis | Opération |
| --- | --- |
| Recharger | `/armature reload config` |
| Redémarrer | Arrêter complètement/démarrer le serveur. Inclut le backend, le pont client et la séparation du pack de rendu |
| Reconstruction du pack | `/armature reload pack`, puis le flux de génération du fournisseur externe |
| Rechargement client | Livraison/téléchargement du pack final modifié. Un seul rechargement de configuration serveur est insuffisant |

Exécuter `/armature config validate` pour les valeurs et exigences autorisées par le schéma actuel. Les candidats de rechargement invalides conservent l’état de fonctionnement connu bon. `config-version` appartient à Armature.

## options config-version

```yaml
config-version: 17

input:
  movement:
    vertical-stop-delay-ticks: 3
    # Horizontal distance travelled in one server tick before walk starts.
    threshold: 0.015
    # Consecutive stationary ticks required before walk/sprint returns to idle.
    stop-delay-ticks: 3

debug:
  # Master switch for the debug categories below.
  enabled: false
  teleportation: false
  tracker-lifecycle: true
  animations: true
  vanilla-hiding: false
  # Native writing/reload and CraftEngine pack preparation phase timings.
  pack-generation: false
  # Model warning summaries in the console; errors are always reported.
  # Full diagnostics remain available via /armature diagnostics native.
  native-compiler-warnings: false

render:
    # native is the primary renderer and requires no BetterModel installation.
    # bettermodel remains available; deprecation is planned for future versions.
  backend: native
  client-mod:
    # Experimental optional Fabric 1.21.8 renderer for native .armature v2 models.
    # Clients without the mod keep the configured server renderer.
    enabled: true
  enabled: true
  dynamic-items:
    # Preserve the outgoing model's items during unequip/deferred actions.
    # The incoming model receives the current items when equip is activated.
    # Applies to server rendering and the optional Fabric renderer.
    defer-until-equipped: true
  camera-rotation:
    # Client-side Fabulous post-effect driven by the optional s_camera bone.
    enabled: true
    debug-mode: 'off'
    zoom-correction:
      # Crops the image to hide missing edges during camera-bone rotations.
      # Disabled preserves the original FOV and does not apply any crop zoom.
      enabled: true
      # fixed keeps one constant zoom; dynamic adapts it to the bone rotation.
      mode: fixed
      # Fixed zoom factor, or maximum allowed factor in dynamic mode.
      maximum-zoom: 1.25
      # In fixed mode, widens the 3D source projection before applying the
      # crop, preserving the player's visible world and viewmodel FOV.
      # Breaks with Sodium.
      preserve-fov: false
    rotation-clamp:
      # Limits the total 3D rotation received from the Blockbench camera bone.
      enabled: true
      maximum-degrees: 7.0
  visibility:
    first-person-only: true
    follow-player-visibility: true
  viewmodel:
    model-adaptation: true
    anti-clipping: true
    y-lock: true
    fov:
      locked: true
      degrees: 90.0
  vanilla-hiding:
    # packet-mask | core-shaders
    # core-shaders sends a per-player GameTime channel and can conflict with
    # other resource-pack shaders that also use GameTime. core-shaders never
    # installs inventory-mask packets. PacketEvents action detection remains
    # independent.
    mode: core-shaders
  anchor:
    max-distance: 20
    sync-interval-ticks: -1
  motion:
    sway:
      enabled: true
      # Applies look/movement sway rotation to the s_camera bone.
      affects-camera-bone: true
      maximum-rotation: 10
      look:
        lerp-speed: 20.0
        yaw-gain: -0.1
        pitch-gain: -0.1
        roll-gain: -0.1
      movement:
        lerp-speed: 6.0
        yaw-gain: 0
        pitch-gain: 0
        roll-gain: -5
    bob:
      enabled: false
      cycles-per-block: 1
      horizontal-amplitude: 0.025
      vertical-amplitude: 0.03
      roll-amplitude: 0.8
      frequency: 9.0
      damping: 0.9
    camera-follow:
      enabled: false
      frequency: 20.0
      damping: 0.1
      yaw-position-gain: 0
      pitch-position-gain: 0
      maximum-offset: 0.15
      pitch-compensation: 0.3
    # Physics-driven bones: model bones named phy_* stay perpendicular to
    # the ground (their down axis follows the world, not the model's server
    # rotation) and swing with movement acceleration and player turning.
    # Only the native backend implements them; BetterModel ignores the
    # prefix. The bone pivot is the rotation center.
    physics:
      enabled: true
      acceleration-gain: 4.0
      maximum-angle: 25
      # Spring response of the swing. frequency is how fast the bone chases
      # the target, damping is how much it resists overshooting it (1.0 = no
      # overshoot at all). With damping below 1.0 the bone swings past its rest
      # pose and bounces back, which reads as a snap when the player stops:
      # at frequency 4.0 / damping 0.6 a 25 degree return overshoots by
      # ~2.1 degrees. Raise damping toward 1.0 to remove that bounce, or lower
      # frequency for a slower, heavier settle.
      frequency: 4.0
      damping: 0.6
      # Tilt added per degree/second of camera yaw turn. Keep small: mouse
      # sway while looking up or down must not fling the bone sideways.
      turn-gain: 0.25

resource-pack:
  # Optional separate renderer pack. The provider continues delivering the common pack.
  # Set the external URL, enable, restart, then rebuild and publish both packs.
  renderer-pack:
    enabled: false
    # Host plugins/Armature/renderer_pack.zip here. {sha1} can version the URL.
    url: ""
    prompt: "Armature server rendering"
  # Also write plugins/Armature/resource_pack.zip when Armature generates assets.
  generate-zip: false
  # Automatic Armature publication. Explicit pack/all reloads always generate.
  generation:
    on-startup: true
    on-reload: true
    on-provider-change: true
  # Empty keeps the backend's description. Does not rename a provider's final pack.
  description: ""
  # Filters bundled overlays; does not convert models for other client versions.
  supported-version:
    min: "1.21.4"
    max: latest
  validation:
    enable: true
    fail-on-error: false
    check-models: true
    check-textures: true
    check-atlases: true
  optimization:
    json:
      enable: false
      exclude: []
    texture:
      enable: false
      compression-level: 9
      exclude: []
  # Standalone Armature ZIP only. Path relative to plugins/Armature.
  zip:
    path: resource_pack.zip
    method: deflated
    compression-level: 6
  # Deterministic ownership when two sources publish one normalized path.
  # keep-existing preserves the first external owner; replace lets Armature
  # win when its generated source is involved; error aborts the candidate.
  collision-policy: keep-existing
  # Optional first-match rules: [{source: provider, namespace: ns, path: ns:foo/**, policy: replace}]
  collision-rules: []
  exclusions:
    # Source owner IDs are normalized lowercase and match exactly.
    sources: []
    # Namespace IDs are normalized lowercase and match assets/<namespace>/**.
    namespaces: []
    # Paths use forward slashes; * stays within one segment, ** crosses segments.
    paths: []
```

## options de version de configuration

| Option | Valeur par défaut | Valeurs acceptées | Prise en compte | Utilité |
| --- | --- | --- | --- | --- |
| `config-version` | `17` | un entier >= 0 | redémarrage | Version du schéma géré. Ne modifiez pas manuellement. |

## options d’entrée

| Option | Valeur par défaut | Valeurs acceptées | Prise en compte | Utilité |
| --- | --- | --- | --- | --- |
| `input.movement.vertical-stop-delay-ticks` | `3` | un entier >= 0 | rechargement | Ticks verticaux stationnaires avant la fin d’une transition de mouvement vertical. |
| `input.movement.threshold` | `0.015` | un nombre fini dans [0, 100] | rechargement | Distance horizontale parcourue en un tick du serveur avant le début de la marche. |
| `input.movement.stop-delay-ticks` | `3` | un entier >= 0 | rechargement | Nombre de ticks stationnaires requis avant que la marche/cours ne retourne à l’état inactif. |

## options de débogage

| Option | Valeur par défaut | Valeurs acceptées | Prise en compte | Utilité |
| --- | --- | --- | --- | --- |
| `debug.enabled` | `false` | `true` ou `false` | rechargement | Interrupteur principal pour les catégories de débogage ci-dessous. |
| `debug.teleportation` | `false` | `true` ou `false` | rechargement | Activer la catégorie de diagnostic de téléportation. L’interrupteur de débogage principal s’y applique également. |
| `debug.tracker-lifecycle` | `true` | `true` ou `false` | rechargement | Activer la catégorie de diagnostic du cycle de vie du tracker. L’interrupteur de débogage principal s’y applique également. |
| `debug.animations` | `true` | `true` ou `false` | rechargement | Activer la catégorie de diagnostic d’animations. Le interrupteur de débogage maître s’applique également. |
| `debug.vanilla-hiding` | `false` | `true` ou `false` | rechargement | Activer la catégorie de diagnostic de masquage vanilla. L’interrupteur de débogage maître s’applique également. |
| `debug.pack-generation` | `false` | `true` ou `false` | rechargement | Temps d’exécution de la phase de préparation du pack CraftEngine et du moteur de rendu natif. |
| `debug.native-compiler-warnings` | `false` | `true` ou `false` | rechargement | Résumés d’avertissements de modèle dans la console. Les erreurs sont toujours signalées. Les diagnostics complets restent disponibles via /armature diagnostics native. |

## options de rendu

| Option | Valeur par défaut | Valeurs acceptées | Prise en compte | Utilité |
| --- | --- | --- | --- | --- |
| `render.backend` | `native` | `bettermodel` ou `native` | redémarrage | Le moteur de rendu natif est le principal et ne nécessite pas d’installation de BetterModel. bettermodel reste disponible. Une dépréciation est prévue pour les futures versions. |
| `render.client-mod.enabled` | `true` | `true` ou `false` | redémarrage | Moteur de rendu Fabric 1.21.8 optionnel et expérimental pour les modèles .armature v2 natifs. Les clients sans ce mod conservent le moteur de rendu du serveur configuré. |
| `render.enabled` | `true` | `true` ou `false` | rechargement | Activer le rendu à la première personne tant que le plugin reste chargé. |
| `render.dynamic-items.defer-until-equipped` | `true` | `true` ou `false` | rechargement | Conserver les objets du modèle sortant pendant les actions de déséquipement ou différées. Le modèle entrant reçoit les objets actuels lorsque l’équipement est activé. S’applique au rendu serveur et au moteur de rendu Fabric optionnel. |
| `render.camera-rotation.enabled` | `true` | `true` ou `false` | reconstruction du pack et rechargement côté client | Effet post-Fabulous côté client piloté par l’os optionnel s_camera. |
| `render.camera-rotation.debug-mode` | `'off'` | `off`, `identity`, `signature`, `metadata`, `fixed-basis`, `uv` ou `status` | reconstruction du pack et rechargement côté client | Mode de débogage. |
| `render.camera-rotation.zoom-correction.enabled` | `true` | `true` ou `false` | reconstruction du pack et rechargement côté client | Recadre l’image pour masquer les bords manquants lors des rotations caméra-os. Désactivé conserve le champ de vision (FOV) original et n’applique aucun recadrage de zoom. |
| `render.camera-rotation.zoom-correction.mode` | `fixed` | `fixed` ou `dynamic` | reconstruction du pack et rechargement côté client | le zoom fixe conserve un facteur de zoom constant. Le dynamique l’adapte à la rotation de l’os. |
| `render.camera-rotation.zoom-correction.maximum-zoom` | `1.25` | un nombre fini dans [1, 4] | reconstruction du pack et rechargement côté client | Facteur de zoom fixe, ou facteur maximal autorisé en mode dynamique. |
| `render.camera-rotation.zoom-correction.preserve-fov` | `false` | `true` ou `false` | reconstruction du pack et rechargement côté client | En mode fixe, élargit la projection 3D de la source avant d’appliquer le recadrage, préservant le monde visible du joueur et son FOV de vue. Incompatible avec Sodium. |
| `render.camera-rotation.rotation-clamp.enabled` | `true` | `true` ou `false` | reconstruction du pack et rechargement côté client | Limite la correction de caméra animée au maximum configuré. |
| `render.camera-rotation.rotation-clamp.maximum-degrees` | `7.0` | un nombre fini dans [0, 180] | reconstruction du pack et rechargement côté client | Amplitude maximale de la correction de caméra, en degrés, lorsque la limite est activée. |
| `render.visibility.first-person-only` | `true` | `true` ou `false` | rechargement | Restreindre la présentation à la vue à la première personne. |
| `render.visibility.follow-player-visibility` | `true` | `true` ou `false` | rechargement | Suivre la visibilité/invisibilité du joueur pour décider du rendu. |
| `render.viewmodel.model-adaptation` | `true` | `true` ou `false` | reconstruction du pack | Adapter les modèles sources pris en charge à la scène à la première personne sans réécrire les fichiers. |
| `render.viewmodel.anti-clipping` | `true` | `true` ou `false` | reconstruction du pack et rechargement côté client | Générer le chemin du shader de la vue pour que la géométrie d’Armature puisse se rendre à travers les blocs. |
| `render.viewmodel.y-lock` | `true` | `true` ou `false` | rechargement | Maintenir la position verticale de la vue stable dans le chemin de shader pris en charge. |
| `render.viewmodel.fov.locked` | `true` | `true` ou `false` | reconstruction du pack et rechargement côté client | Utiliser un champ de vision de la vue fixe indépendant du champ de vision du monde. |
| `render.viewmodel.fov.degrees` | `90.0` | un nombre fini dans l’intervalle ouvert (1, 179) | reconstruction du pack et rechargement côté client | Champ de vision du modèle en degrés lorsque le verrouillage est activé. |
| `render.vanilla-hiding.mode` | `core-shaders` | `packet-mask` ou `core-shaders` | rechargement, reconstruction du pack, rechargement du client | packet-mask / core-shaders : core-shaders envoie un canal GameTime par joueur et peut entrer en conflit avec d’autres shaders de pack de ressources utilisant également GameTime. core-shaders n’installe jamais de paquets inventory-mask. La détection d’action PacketEvents reste indépendante. |
| `render.anchor.max-distance` | `20` | un nombre fini dans [0, 128] | rechargement | Distance maximale de séparation des ancres du serveur avant correction. |
| `render.anchor.sync-interval-ticks` | `-1` | un entier de -1 à 20000 inclus | rechargement | Intervalle de synchronisation des ancres ; -1 utilise la politique du backend. |
| `render.motion.sway.enabled` | `true` | `true` ou `false` | rechargement | Activer l’oscillation procédurale de la vue/déplacement. |
| `render.motion.sway.affects-camera-bone` | `true` | `true` ou `false` | rechargement | Appliquer la rotation de l’oscillation de la vue/déplacement à l’os s_camera. |
| `render.motion.sway.maximum-rotation` | `10` | un nombre fini dans [0, 180] | rechargement | Rotation maximale combinée en degrés. |
| `render.motion.sway.look.lerp-speed` | `20.0` | un nombre fini dans [0, 1000] | rechargement | Réponse de lissage du regard. Zéro supprime l’interpolation du regard. |
| `render.motion.sway.look.yaw-gain` | `-0.1` | un nombre fini dans [-1000, 1000] | rechargement | Gain de yaw signé pour cette couche de mouvement. |
| `render.motion.sway.look.pitch-gain` | `-0.1` | un nombre fini dans [-1000, 1000] | rechargement | Gain de pitch signé pour cette couche de mouvement. |
| `render.motion.sway.look.roll-gain` | `-0.1` | un nombre fini dans [-1000, 1000] | rechargement | Gain de roll signé pour cette couche de mouvement. |
| `render.motion.sway.movement.lerp-speed` | `6.0` | un nombre fini dans [0, 1000] | rechargement | Réponse de lissage du déplacement. |
| `render.motion.sway.movement.yaw-gain` | `0` | un nombre fini dans [-1000, 1000] | rechargement | Gain de rotation (yaw) signé pour cette couche de mouvement. |
| `render.motion.sway.movement.pitch-gain` | `0` | un nombre fini dans [-1000, 1000] | rechargement | Gain de rotation (pitch) signé pour cette couche de mouvement. |
| `render.motion.sway.movement.roll-gain` | `-5` | un nombre fini dans [-1000, 1000] | rechargement | Gain de rotation (roll) signé pour cette couche de mouvement. |
| `render.motion.bob.enabled` | `false` | `true` ou `false` | rechargement | Activer le balancement de marche. |
| `render.motion.bob.cycles-per-block` | `1` | un nombre fini dans [0, 1000] | rechargement | Nombre de cycles de balancement de marche par bloc parcouru. |
| `render.motion.bob.horizontal-amplitude` | `0.025` | un nombre fini dans [0, 1000] | rechargement | Amplitude du balancement horizontal. |
| `render.motion.bob.vertical-amplitude` | `0.03` | un nombre fini dans [0, 1000] | rechargement | Amplitude du bobbing vertical. |
| `render.motion.bob.roll-amplitude` | `0.8` | un nombre fini dans [0, 1000] | rechargement | Amplitude du bobbing de roulis en degrés. |
| `render.motion.bob.frequency` | `9.0` | un nombre fini dans [0, 100] | rechargement | Fréquence de réponse du ressort. Des valeurs plus élevées se stabilisent plus rapidement. |
| `render.motion.bob.damping` | `0.9` | un nombre fini dans [0, 10] | rechargement | Amortissement du ressort. Une valeur proche de 1 évite le dépassement. Sous 1, le ressort peut rebondir. |
| `render.motion.camera-follow.enabled` | `false` | `true` ou `false` | rechargement | Activer le déplacement de suivi de caméra basé sur le ressort. |
| `render.motion.camera-follow.frequency` | `20.0` | un nombre fini dans [0, 100] | rechargement | Fréquence de réponse du ressort. Des valeurs plus élevées se stabilisent plus rapidement. |
| `render.motion.camera-follow.damping` | `0.1` | un nombre fini dans [0, 10] | rechargement | Amortissement du ressort. Une valeur proche de 1 évite le dépassement. Sous 1, le ressort peut rebondir. |
| `render.motion.camera-follow.yaw-position-gain` | `0` | un nombre fini dans [-1000, 1000] | rechargement | Gain de position de la rotation pour cette couche de mouvement. |
| `render.motion.camera-follow.pitch-position-gain` | `0` | un nombre fini dans [-1000, 1000] | rechargement | Gain de position de la hauteur pour cette couche de mouvement. |
| `render.motion.camera-follow.maximum-offset` | `0.15` | un nombre fini dans [0, 100] | rechargement | Décalage maximal de suivi de la caméra en position. |
| `render.motion.camera-follow.pitch-compensation` | `0.3` | un nombre fini dans [0, 100] | rechargement | Compenser le déplacement pour la hauteur de la vue. |
| `render.motion.physics.enabled` | `true` | `true` ou `false` | rechargement | Activer la physique native pour les os marqués. Aucune simulation dans BetterModel. |
| `render.motion.physics.acceleration-gain` | `4.0` | un nombre fini dans [-1000, 1000] | rechargement | Gain de signe convertissant l’accélération du joueur en inclinaison de l’os. |
| `render.motion.physics.maximum-angle` | `25` | un nombre fini dans [0, 180] | rechargement | Limiter la rotation physique native en degrés. |
| `render.motion.physics.frequency` | `4.0` | un nombre fini dans [0, 100] | rechargement | Réponse du ressort de balancement. La fréquence détermine la vitesse à laquelle l’os rejoint sa cible. L’amortissement limite le dépassement, avec 1,0 pour le supprimer. Sous 1,0, l’os dépasse sa position de repos puis rebondit, ce qui peut se traduire par un à-coup à l’arrêt du joueur. À une fréquence de 4,0 et un amortissement de 0,6, un retour de 25 degrés dépasse la cible d’environ 2,1 degrés. Rapprochez l’amortissement de 1,0 pour retirer le rebond ou baissez la fréquence pour ralentir le retour. |
| `render.motion.physics.damping` | `0.6` | un nombre fini dans [0, 10] | rechargement | Amortissement du ressort. Une valeur proche de 1 évite le dépassement. Sous 1, le ressort peut rebondir. |
| `render.motion.physics.turn-gain` | `0.25` | un nombre fini dans [0, 1000] | rechargement | Inclinaison ajoutée par degré/second de rotation de la caméra. Gardez-le petit : la balancement de la souris en regardant vers le haut ou vers le bas ne doit pas projeter l’os de côté. |

## options du pack de ressources

| Option | Valeur par défaut | Valeurs acceptées | Prise en compte | Utilité |
| --- | --- | --- | --- | --- |
| `resource-pack.renderer-pack.enabled` | `false` | `true` ou `false` | redémarrage | Séparez les actifs du moteur de rendu natif hébergés de manière externe du pack commun. Un redémarrage est requis. |
| `resource-pack.renderer-pack.url` | `""` | une URL ZIP HTTP(S) externe | redémarrage | Hébergez ici les plugins/Armature/renderer_pack.zip. {sha1} peut versionner l’URL. |
| `resource-pack.renderer-pack.prompt` | `"Armature server rendering"` | une demande de pack de ressources | redémarrage | Demande de pack de ressources pour le pack de rendu serveur séparé. |
| `resource-pack.generate-zip` | `false` | `true` ou `false` | reconstruction du pack | Génère un ZIP autonome en plus de la sortie du répertoire. |
| `resource-pack.generation.on-startup` | `true` | `true` ou `false` | rechargement | Généré automatiquement pour cet événement de cycle de vie. La reconstruction explicite du pack reste disponible. |
| `resource-pack.generation.on-reload` | `true` | `true` ou `false` | rechargement | Générer automatiquement pour cet événement de cycle de vie. La reconstruction explicite du pack reste disponible. |
| `resource-pack.generation.on-provider-change` | `true` | `true` ou `false` | rechargement | Générer automatiquement pour cet événement de cycle de vie. La reconstruction explicite du pack reste disponible. |
| `resource-pack.description` | `""` | une description, optionnellement vide | reconstruction du pack | Un vide conserve la description du backend. Ne renomme pas le pack final d’un fournisseur. |
| `resource-pack.supported-version.min` | `"1.21.4"` | une version de client intégrée, `server` ou `latest` | reconstruction du pack | La couche additive la plus basse conservée. Cela ne convertit pas les modèles sources. |
| `resource-pack.supported-version.max` | `latest` | une version de client intégrée, `server` ou `latest` | reconstruction du pack | La couche additive la plus haute conservée. La dernière sélectionne la dernière définition empaquetée. |
| `resource-pack.validation.enable` | `true` | `true` ou `false` | reconstruction du pack | Valider le pack candidat généré. |
| `resource-pack.validation.fail-on-error` | `false` | `true` ou `false` | reconstruction du pack | Rejeter les candidats en cas d’erreur de validation si activé. |
| `resource-pack.validation.check-models` | `true` | `true` ou `false` | reconstruction du pack | Vérifier les références de modèle générées. |
| `resource-pack.validation.check-textures` | `true` | `true` ou `false` | reconstruction du pack | Vérifier les références de texture générées. |
| `resource-pack.validation.check-atlases` | `true` | `true` ou `false` | reconstruction du pack | Vérifier les références d’atlas et les actifs. |
| `resource-pack.optimization.json.enable` | `false` | `true` ou `false` | reconstruction du pack | Optimiser les JSON générés éligibles. Les chemins exclus sont conservés. |
| `resource-pack.optimization.json.exclude` | `[]` | motifs glob relatifs au pack | reconstruction du pack | Exclusions de motifs glob relatifs au pack pour cette optimisation uniquement. |
| `resource-pack.optimization.texture.enable` | `false` | `true` ou `false` | reconstruction du pack | Optimiser les textures PNG générées éligibles. Les chemins exclus sont conservés. |
| `resource-pack.optimization.texture.compression-level` | `9` | un entier de 0 à 9 inclus | reconstruction du pack | niveau de compression PNG (0-9). Ne modifie pas le gameplay. |
| `resource-pack.optimization.texture.exclude` | `[]` | motifs glob relatifs au pack | reconstruction du pack | Exclusions de motifs glob relatifs au pack pour cette optimisation uniquement. |
| `resource-pack.zip.path` | `resource_pack.zip` | un chemin ZIP relatif à l’intérieur d’Armature | reconstruction du pack | Chemin de sortie ZIP autonome relatif situé sous plugins/Armature. |
| `resource-pack.zip.method` | `deflated` | `stored` ou `deflated` | reconstruction du pack | Choisir les entrées non compressées (stockées) ou le déflatement ZIP (déflaté). |
| `resource-pack.zip.compression-level` | `6` | un entier de 0 à 9 inclus | reconstruction du pack | niveau de compression déflatement ZIP (0-9). |
| `resource-pack.collision-policy` | `keep-existing` | `keep-existing`, `replace` ou `error` | reconstruction du pack | Propriété déterministe lorsque deux sources publient un chemin normalisé. keep-existing conserve le premier propriétaire externe. Replace permet à Armature de gagner lorsque sa source générée est impliquée. Error annule le candidat. |
| `resource-pack.collision-rules` | `[]` | Règles de collision étendues | reconstruction du pack | Règles de premier match facultables : [{source: provider, namespace: ns, path: ns:foo/**, policy: replace}] |
| `resource-pack.exclusions.sources` | `[]` | Liste des identifiants de propriétaire de source en minuscules | reconstruction du pack | Les identifiants de propriétaire de source sont normalisés en minuscules et correspondent exactement. |
| `resource-pack.exclusions.namespaces` | `[]` | Liste des identifiants d’espace de noms | reconstruction du pack | Les identifiants d’espace de noms sont normalisés en minuscules et correspondent à assets/<namespace>/**. |
| `resource-pack.exclusions.paths` | `[]` | motifs glob relatifs à la racine du pack. `*` correspond à un segment et `**` à plusieurs segments | reconstruction du pack | Les chemins utilisent des barides obliques avant ; * reste dans un seul segment, ** traverse les segments. |


## Fichiers connexes

`variables.yml` déclare des variables de profil partagées ; `messages.yml` contrôle les messages administrateurs (`auto`, `en`, `fr`). Les fichiers de profil, les modèles et les textures d’armure personnalisées ont leurs propres répertoires. Cette configuration ne contient pas de règles d’animation de profil.

Continuer avec [Rendu et caméra](../configuration/rendering-camera.md), [Mouvement et physique](../avance/meilleurs-reglages.md), [Packs de ressources](../configuration/resource-packs.md), [Peaux et armure](../avance/armures-personnalisees.md), ou l'[éditeur dans le jeu](in-game-editor.md).
