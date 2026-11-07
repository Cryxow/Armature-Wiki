---
description: "Conditions"
---

# 🔀 Conditions

Ce document définit le vocabulaire de conditions intégré pour les profils d’animation d’objet modernes. Le parseur et l’exécution mettent en œuvre les valeurs de base ci-dessous. Les valeurs fourniees, PlaceholderAPI, NBT et les valeurs dérivées coûteuses restent optionnelles à l’exécution si leur source n’est pas disponible. Les noms sont nommés dans un espace de noms pour éviter les collisions entre les valeurs intégrées, les valeurs fourniees et les variables définies par l’administrateur.

Les conditions utilisent `when` pour les règles de mouvement/couches additives et `condition` pour les règles d’action. Le canal d’action est toujours `replace`. Il n’y a pas de `action.mode` dans le format moderne.

## Syntaxe de base

Les conditions dans une liste `when` sont combinées avec `AND` :

```yaml
when:
  - player.on-ground == true
  - player.sneaking == false
  - player.speed < 2
```

La syntaxe de comparaison lisible devrait supporter :

```text
==       equals
!=       differs
<        less than
<=       less than or equal to
>        greater than
>=       greater than or equal to
in       belongs to a list or range
contains contains a value
```

Pour la logique groupée, une forme explicite peut être supportée :

```yaml
when:
  all:
    - player.on-ground == true
    - any:
        - player.sprinting == true
        - player.movement-direction == forward
  not:
    - player.sneaking == true
```

Le même moteur de conditions devrait être utilisable par les règles de mouvement, les événements d’action et les animations additives. Les règles de mouvement sélectionnent une animation de base. Les règles additives peuvent être actives en même temps.

## Espaces de noms

| Espace de noms | Propriété | Exemples |
| --- | --- | --- |
| `player.*` | État du joueur intégré d’Armature | `player.speed`, `player.swimming` |
| `input.*` | État de l’entrée client d’Armature | `input.forward`, `input.jump` |
| `item.*` | Objet actuel et équipement | `item.main-hand.type` |
| `world.*` | Contexte du monde et du temps | `world.is-day`, `world.weather` |
| `block.*` | Blocs autour du joueur | `block.below.type` |
| `movement.*` | État actuel du résolveur de mouvement | `movement.active`, `movement.changed` |
| `action.*` | État d’action actuel | `action.active`, `action.progress` |
| `animation.*` | État de l’animation actuel | `animation.id`, `animation.remaining` |
| `additive.*` | État de la couche additive | `additive.breathing.weight` |
| `event.*` | Événement actuel et ses arguments | `event.signal`, `event.argument.mode` |
| `variable.*` | Valeurs de profil/session/externe | `variable.stamina` |
| `<provider>.*` | Adaptateurs de plugin externe | `weaponmechanics.ammo` |
| `placeholder.*` | Valeurs PlaceholderAPI explicites | `placeholder.player-stamina` |

`<provider>.*`, `variable.*` et `placeholder.*` ne sont pas des conditions de joueur intégrées, mais ils appartiennent au même langage de condition. Le nom du fournisseur est l’espace de noms lui-même. Un préfixe générique `provider.` n’est pas exposé aux utilisateurs.

Les noms d’événement utilisent le même espace de noms source avec un séparateur de colonnes : `weaponmechanics:reload`, `player:attack` ou `block:break`. Les valeurs utilisent un point pour adresser une propriété dans cet espace de noms, tel que `weaponmechanics.ammo` ou `player.speed`.

Les règles visuelles de l’objet tenu exposent `item.using` (booléen) et `item.use-ticks` (ticks d’utilisation écoulés) pour la main rendue. Ces deux valeurs sont évaluées par main. Utilisez-les dans `model.held-item.rules[].when`.

## État du joueur

### Posture et capacités

| Condition | Type | Signification |
| --- | --- | --- |
| `player.on-ground` | booléen | Le joueur est soutenu par le sol. |
| `player.moving` | booléen | Le joueur a un mouvement horizontal significatif. |
| `player.sneaking` | booléen | Le joueur est accroupi ou se déplace accroupi. |
| `player.sprinting` | booléen | Le joueur court. |
| `player.swimming` | booléen | Le joueur est dans la posture ou l’état de nage. |
| `player.falling` | booléen | Le joueur descend dans les airs. |
| `player.jumping` | booléen | Le joueur vient de commencer un saut vers le haut. |
| `player.climbing` | booléen | Le joueur se trouve sur une surface escaladable. |
| `player.gliding` | booléen | Le joueur plane avec des élytres. |
| `player.flying` | booléen | Le joueur vole plutôt que de tomber normalement. |
| `player.riding` | booléen | Le joueur monte sur un véhicule ou une entité. |
| `player.passenger` | booléen | Le joueur est transporté en tant que passager. |
| `player.sleeping` | booléen | Le joueur dort. |
| `player.using-item` | booléen | Le joueur utilise actuellement un objet. |
| `player.blocking` | booléen | Le joueur bloque activement. |
| `player.swinging` | booléen | Le joueur joue une animation de mouvement du bras. |
| `player.dead` | booléen | Le joueur est mort ou dans l’état de mort. |
| `player.spectator` | booléen | Le joueur est en mode spectateur. |
| `player.invisible` | booléen | Le joueur est invisible. |
| `player.has-vehicle` | booléen | Le joueur possède un véhicule. |

`player.pose` peut exposer la pose sous-jacente sous forme d’énumération lorsqu’un seul booléen est insuffisant, par exemple `standing`, `crouching`, `swimming`, `fall-flying`, `sleeping`, `riding`, `spin-attack` ou `dying`.

### Valeurs de déplacement

| Condition | Type | Signification |
| --- | --- | --- |
| `player.speed` | nombre | Vitesse de déplacement horizontale normalisée. |
| `player.horizontal-speed` | nombre | Module de la vitesse horizontale. |
| `player.vertical-speed` | nombre | Vitesse verticale signée. Positif vers le haut. |
| `player.total-speed` | nombre | Module de la vitesse de déplacement totale. |
| `player.fall-distance` | nombre | Distance parcourue vers le bas depuis la prise de contact avec le sol. |
| `player.air-time` | durée | Temps écoulé depuis la prise de contact avec le sol, en ticks. |
| `player.time-since-grounded` | durée | Temps écoulé depuis le dernier état au sol, en ticks. |
| `player.jump-time` | durée | Temps écoulé depuis le début du saut actuel, en ticks. |
| `player.distance-to-ground` | nombre | Distance estimée vers le prochain sol solide. Optionnel et plus coûteux. |
| `player.acceleration` | nombre | Variation de la vitesse horizontale. Valeur dérivée optionnelle. |

`player.speed` doit avoir une unité documentée et rester stable entre les implémentations du serveur. `player.horizontal-speed` doit être disponible lorsque la vitesse exacte est requise. `player.fall-distance` et `player.vertical-speed` doivent être des valeurs indépendantes : l’une décrit la distance accumulée, l’autre la direction et l’intensité actuelles.

### Direction

La forme abrégée `player.direction` peut être supportée, avec `player.movement-direction` comme nom explicite.

```text
forward
backward
left
right
up
down
none
```

La direction est relative à la direction de regard du joueur, non aux coordonnées du monde. `up` et `down` sont significatifs pour grimper et autres mouvements verticaux. Le résolveur actuel expose le composant horizontal dominant (`forward`, `backward`, `left` ou `right`). Les booléens d’entrée restent disponibles lorsqu’une règle doit distinguer une entrée diagonale.

Deux sources doivent rester distinctes :

| Condition | Signification |
| --- | --- |
| `input.direction` | Direction vers laquelle le joueur tente de se déplacer. |
| `player.movement-direction` | Direction vers laquelle le joueur se déplace réellement. |
| `player.climbing-direction` | `up`, `down` ou `none` pendant la grimpe. |
| `player.look-direction` | Direction du regard du joueur, non de son mouvement. |

La sélection d’animation devrait normalement utiliser le mouvement réel. La direction de l’entrée est utile lorsqu’une animation doit réagir immédiatement à une touche enfoncée, même si le joueur est bloqué.

### État de l’entrée

Les conditions d’entrée sont utiles pour les actions déclenchées par un événement et pour distinguer l’intention du mouvement résultant :

| Condition | Type | Signification |
| --- | --- | --- |
| `input.forward` | booléen | L’entrée vers l’avant est maintenue. |
| `input.backward` | booléen | L’entrée vers l’arrière est maintenue. |
| `input.left` | booléen | L’entrée vers la gauche est maintenue. |
| `input.right` | booléen | L’entrée vers la droite est maintenue. |
| `input.jump` | booléen | L’entrée de saut est maintenue ou active. |
| `input.sneak` | booléen | La touche d’accroupissement est maintenue. |
| `input.sprint` | booléen | La touche de course est maintenue. |
| `input.just-pressed.<name>` | booléen | Une touche a été pressée cette mise à jour. |
| `input.just-released.<name>` | booléen | Une touche a été relâchée cette mise à jour. |

## Environnement et emplacement

### Emplacement du joueur

| Condition | Type | Signification |
| --- | --- | --- |
| `player.world` | chaîne | Nom du monde actuel. |
| `player.dimension` | énumération | Dimension/environnement actuel. |
| `player.biome` | chaîne/énumération | Biome actuel. |
| `player.x` | nombre | Coordonnée X actuelle. |
| `player.y` | nombre | Coordonnée Y actuelle. |
| `player.z` | nombre | Coordonnée Z actuelle. |
| `player.light-level` | nombre | Niveau de luminosité au niveau du joueur. |
| `player.in-water` | booléen | Le joueur est dans l’eau. |
| `player.in-lava` | booléen | Le joueur est dans la lave. |
| `player.in-powder-snow` | booléen | Le joueur est dans la neige pulvérulente. |
| `player.underwater` | booléen | Les yeux du joueur sont sous l’eau. |

### Contexte du monde.

| Condition | Type | Signification |
| --- | --- | --- |
| `world.time` | nombre | L’heure actuelle du monde. |
| `world.is-day` | booléen | Il est jour. |
| `world.is-night` | booléen | Il est nuit. |
| `world.weather` | énumération | `clear`, `rain` ou `thunder`. |
| `world.raining` | booléen | La pluie est active. |
| `world.thundering` | booléen | L’éclair est actif. |
| `world.moon-phase` | enum/number | La phase actuelle de la lune. |
| `world.game-time` | nombre | L’heure de jeu du monde. |

### Les blocs proches

Les conditions de bloc doivent être limitées à des positions fixes autour du joueur et ne doivent pas effectuer de recherches illimitées :

| Condition | Type | Signification |
| --- | --- | --- |
| `block.below.type` | matériau | Bloc situé directement sous le joueur. |
| `block.above.type` | matériau | Bloc situé directement au-dessus du joueur. |
| `block.current.type` | matériau | Bloc situé à la position du joueur. |
| `block.below.solid` | booléen | Le bloc situé en dessous est solide. |
| `block.current.liquid` | booléen | Le bloc actuel est liquide. |
| `block.below.slipperiness` | nombre | La glissabilité de la surface en dessous du joueur. |
| `block.below.climbable` | booléen | Le bloc situé en dessous ou à côté du joueur est escaladable. |

Les régions, les revendications, les permissions et les recherches arbitraires d’entités proches doivent être fournies par des intégrations dédiées plutôt que par l’ajout au vocabulaire central.

## Statut du joueur et équipement

### Valeurs de statut

| Condition | Type | Signification |
| --- | --- | --- |
| `player.health` | nombre | Santé actuelle. |
| `player.max-health` | nombre | Santé maximale. |
| `player.health-percent` | nombre | Santé actuelle en pourcentage, de `0` à `100`. |
| `player.food-level` | nombre | Niveau de nourriture actuel. |
| `player.saturation` | nombre | Saturation actuelle. |
| `player.air` | nombre | Valeur d’air/respire restante. |
| `player.experience-percent` | nombre | Progression vers le niveau d’expérience suivant, de `0` à `100`. |
| `player.level` | nombre | Niveau d’expérience actuel. |
| `player.on-fire` | booléen | Le joueur brûle. |
| `player.freezing` | booléen | Le joueur gèle. |
| `player.has-effect` | paramétré | Le joueur possède un effet de statut nommé. |
| `player.effect-amplifier` | nombre paramétré | Amplificateur d’un effet de statut nommé. |
| `player.effect-remaining` | durée paramétrée | Durée restante d’un effet nommé. |
| `player.has-permission` | booléen paramétré | Le joueur possède une autorisation nommée. |

Exemples :

```yaml
when:
  - player.health-percent < 25
  - player.has-effect:speed == true
  - player.has-permission:armature.debug == true
```

Pour désactiver le joueur sur le profil, utilisez les autorisations d’exécution `armature.disabled` ou `armature.disabled.<profile-id>` au lieu de répéter une condition d’autorisation dans chaque règle. `player.has-permission` reste utile pour les conditions d’animation spécifiques à la règle.

### Objet actuel et équipement

Le profil correspondant reste le sélecteur d’objet principal. Ces conditions sont destinées à des vérifications supplémentaires pendant l’exécution :

| Condition | Type | Signification |
| --- | --- | --- |
| `item.main-hand.type` | ID de l’objet | Type ou identité de l’objet tenu dans la main principale. |
| `item.off-hand.type` | ID de l’objet | Type ou identité de l’objet tenu dans la main secondaire. |
| `item.main-hand.amount` | nombre | Quantité empilée dans la main principale. |
| `item.main-hand.durability` | nombre | Durabilité actuelle ou valeur de dégâts. |
| `item.main-hand.durability-percent` | nombre | Pourcentage de durabilité restante. |
| `item.main-hand.uses` | nombre | Nombre d’utilisations restantes ou consommées si disponible. |
| `item.main-hand.has-enchantment` | booléen paramétré | Si un enchantement nommé existe. |
| `item.main-hand.has-tag` | booléen paramétré | Si une étiquette personnalisée existe. |
| `item.main-hand.has-pdc` | booléen paramétré | Si une valeur PDC nommée existe. |
| `player.held-slot` | nombre | Emplacement de la barre d’outils actuel. |
| `player.held-hand` | énumération | `main-hand` ou `off-hand`. |
| `player.has-off-hand-item` | booléen | La main secondaire n’est pas vide. |
| `player.armor-piece` | objet paramétré | l’objet d’un emplacement de protection nommé. |

## Animation et état de session

Ces valeurs permettent aux événements et aux règles d’animation de réagir à l’état d’exécution d’Armature sans lire les détails internes du moteur de rendu natif.

### État de déplacement

| Condition | Type | Signification |
| --- | --- | --- |
| `movement.active` | booléen | Une règle de déplacement correspond actuellement. |
| `movement.id` | chaîne | Identifiant de la règle de déplacement sélectionnée. |
| `movement.previous` | chaîne | Règle de déplacement précédemment sélectionnée. |
| `movement.changed` | booléen | Le déplacement sélectionné a changé lors de cette mise à jour. |
| `movement.entered` | booléen paramétré | Un déplacement nommé a été entré. |
| `movement.left` | booléen paramétré | Un déplacement nommé a été quitté. |
| `movement.transitioning` | booléen | Une transition de déplacement est en cours. |
| `movement.transition-progress` | nombre | Progression de la transition de `0.0` à `1.0`. |

### État d’action

| Condition | Type | Signification |
| --- | --- | --- |
| `action.active` | booléen | Une action est actuellement en cours d’exécution. |
| `action.id` | chaîne | Identifiant de l’action actuelle. |
| `action.progress` | nombre | Progression de l’action actuelle de `0.0` à `1.0`. |
| `action.elapsed` | durée | Temps écoulé depuis le début de l’action. |
| `action.remaining` | durée | Temps restant dans l’action. |
| `action.completed` | booléen | L’action actuelle a été terminée lors de cette mise à jour. |
| `action.cancelled` | booléen | L’action actuelle a été annulée lors de cette mise à jour. |
| `action.interruptible` | booléen | L’action actuelle peut être remplacée. |
| `action.cooldown-ready` | booléen paramétré | Une action nommée peut être déclenchée à nouveau. |

Les actions modernes utilisent toujours le canal `replace` exclusif. Les règles additives fournissent le canal de composition continue séparé. Elles ne définissent pas un mode d’action.

### État de minage

| Condition | Type | Signification |
| --- | --- | --- |
| `block.mining` | booléen | Une session de minage à la main principale est active. |
| `block.mining.elapsed` | durée | Nombre de ticks écoulés depuis le début du minage. |
| `block.mining.block` | chaîne | Bloc namspace en cours de minage. |
| `block.mining.tool` | chaîne | Objet namspace tenu à la main principale. |
| `block.mining.face` | énumération | Face du bloc en cours de minage. |
| `block.mining.instant` | booléen | La dernière détection de dégâts de bloc indique une rupture instantanée. |

`block.mining.elapsed` représente le temps écoulé, non un pourcentage : les événements publics Bukkit n’exposent pas le pourcentage interne de rupture de bloc du serveur. Utilisez l’état booléen pour une boucle et les signaux `block:mining-start`/`block:mining-stop` pour les animations de démarrage et de fin à un seul coup. Le mode créatif n’a pas de session de progression de minière de survie, mais les signaux répétés de démarrage et de rupture de bloc maintiennent `block.mining` actif tant que le joueur continue de frapper des blocs. Il expire après huit ticks sans autre impact. L’ID du dernier bloc non-air est conservé lorsque la fonction de rappel de paquet programmée voit un bloc déjà supprimé par la rupture.

### Animation et état additive

| Condition | Type | Signification |
| --- | --- | --- |
| `animation.id` | chaîne | Identificateur d’animation rendu actuel. |
| `animation.progress` | nombre | Progression de l’animation actuelle. |
| `animation.elapsed` | durée | Temps écoulé depuis le début de l’animation. |
| `animation.remaining` | durée | Temps restant. |
| `animation.marker` | booléen paramétré | Un marqueur nommé a été atteint cette mise à jour. |
| `animation.loop-index` | nombre | Itération actuelle de la boucle. |
| `additive.active` | booléen | Au moins une couche additive est active. |
| `additive.<id>.active` | booléen | Une couche additive nommée est active. |
| `additive.<id>.weight` | nombre | Poids actuel d’une couche additive nommée. |

## Contexte d’événement et de temporisation

Ces conditions n’ont de sens que lors de l’évaluation d’un événement ou d’un appel de retour :

| Condition | Type | Signification |
| --- | --- | --- |
| `event.type` | chaîne | Type d’événement normalisé. |
| `event.signal` | Chaîne nommée dans un espace de noms. | Signal externe ou fourni par un fournisseur. |
| `event.source` | énumération | Intégré, fourni par un fournisseur, Skript, Denizen, API, ou marqueur. |
| `event.argument.<name>` | Valeur saisie. | Temps écoulé depuis l’événement déclencheur. |
| `event.elapsed` | durée | Temps écoulé depuis le début de la session du profil. |
| `session.age` | durée | Il s’agit de la première activation de la session. |
| `session.first-equip` | booléen | Un délai de recharge nommé est prêt. |
| `cooldown.<id>.ready` | booléen | Temps de délai de recharge restant. |
| `cooldown.<id>.remaining` | durée | Valeur personnalisée et externe |

## Valeurs personnalisées et externes

Les conditions intégrées doivent rester stables et peu coûteuses. Les valeurs personnalisées peuvent étendre la même syntaxe. Déclarez des variables partagées dans `plugins/Armature/variables.yml` :

```yaml
variables:
  stamina:
    type: number
    source: placeholderapi
    placeholder: "%player_stamina%"
    refresh: 2t
```

Puis référez-vous à elles dans un profil :

```yaml
animations:
  additives:
    - animation: breathing
      weight:
        parameter: variable.stamina
        input: [0, 100]
        output: [1.0, 0.15]
```

Déclarez les valeurs PlaceholderAPI avec un type et un intervalle d’actualisation. Évitez de les analyser séparément pour chaque condition à chaque tick.

L’état de jeu spécifique au fournisseur reste dans un espace de noms :

```yaml
when:
  - weaponmechanics.ammo == weaponmechanics.magazine-size
  - weaponmechanics.aiming == false
```

Armature doit exposer les valeurs du fournisseur via des adaptateurs, mais ne doit pas inventer de conditions intégrées génériques pour les munitions, les dégâts, les rechargements, la mana ou d’autres états de jeu.



## Conditions spécifiques au choix

Le choix `match.when` et ses variantes utilisent un état externe ou des variables partagées, pas les résultats de l’animation ou des variantes/accessoires sélectionnés. Les conditions de montage peuvent utiliser le `variant.active` sélectionné du profil, mais pas l’état actif d’un autre montage. Les règles d’animation peuvent utiliser `variant.active` et `attachment.<id>.active`.

```yaml
when:
  permissions:
    any: [cosmetics.gold, cosmetics.admin]
    none: [cosmetics.disabled]
  all:
    - variable.weapon-skin == gold
```

Les critères dans un bloc de condition structuré se combinent avec ET. Une liste de permissions `any` fournit des alternatives. [Variables](variables.md) explique les valeurs manquantes, les types et le rafraîchissement. La disponibilité des requêtes dépend du contexte d’exécution réel ou du fournisseur. Une source optionnelle doit être installée et configurée avant de s’en fier.
