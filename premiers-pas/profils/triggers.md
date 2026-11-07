---
description: "Déclencheurs"
---

# ⚡ Déclencheurs

Les déclencheurs sont des signaux instantanés nommés. L’état continu doit être géré dans `animations.movement` ou `animations.additives`. Il ne doit pas déclencher une action à chaque tour.

L’arbre moderne conserve toutes les règles dans un seul conteneur `animations`. Les règles d’action utilisent `trigger` et un `condition` optionnel. Leur canal de lecture est toujours exclusif `replace`, donc `mode` n’est pas un champ valide.

```yaml
animations:
  actions:
    - trigger: player:attack
      condition:
        - player.using-item == false
      animation:
        name: [swing, swing_1, swing_2]
        selection: random
        duration: 6t

    - trigger: weaponmechanics:reload
      condition:
        - weaponmechanics.aiming == false
      animation:
        name: inspect
        duration: 20t
      consume: true
```

Tous les arguments d’événement sont exposés lors de l’évaluation sous la forme `event.argument.<name>`. Plusieurs règles peuvent utiliser un même déclencheur. La condition de correspondance la plus spécifique prime, puis l’ordre de déclaration sert de solution de repli.

Statut : `implemented` est émis par le plugin de base ; `provider` nécessite un fournisseur optionnel nommé ; `API/Skript/Denizen` signifie qu’un appelant externe peut le publier ; `reserved` fait partie du vocabulaire mais n’a pas encore d’émetteur intégré.

## Politique d’espace de noms

| Espace de noms | Source | Exemples | État |
| --- | --- | --- | --- |
| `player:` | Événement de joueur normalisé | `player:attack` | disponible |
| `item:` | Cycle de vie de l’objet/profil | `item:equip` | disponible |
| `block:` | Interaction avec un bloc ou minage | `block:place` | disponible |
| `entity:` | Interaction/entité ou combat | `entity:hit` | disponible |
| `projectile:` | Lancement/impact de projectile | `projectile:hit` | disponible |
| `world:` | transition monde/environnement | `world:change` | disponible |
| `input:` | entrée client ou intention de paquet | `input:jump-press` | disponible |
| `animation:` | cycle de vie d’animation Armature/marqueur | `animation:complete` | disponible |
| `<provider>:` | adaptateur de plugin externe | `weaponmechanics:reload` | fournisseur |
| `<integration>:` | signal appartenant au caller externe | `skript:weapon.special` | API/Skript/Denizen |

Il n’y a pas de préfixe `armature:`. Un fournisseur ou une intégration possède son espace de noms. Armature ne résout le signal que par rapport au profil d’objet actif.

## actions et interactions du joueur

| Déclencheur | Émis quand | arguments utiles | État |
| --- | --- | --- | --- |
| `player:attack` | Attaque ou mouvement du bras de la main principale détecté. | `hand`, `target`, `critical` | disponible |
| `player:use-item` | Utilisation/clic droit initiée. | `hand`, `item`, `action` | disponible |
| `player:use-item-stop` | Utilisation d’un objet terminée avant sa complétion. | `hand`, `item`, `reason` | disponible |
| `player:use-item-complete` | Utilisation d’un objet terminée. | `hand`, `item` | disponible |
| `player:consume-item` | Le joueur consomme de la nourriture, une potion ou un autre objet consommable. | `hand`, `item` | disponible |
| `player:release-item` | Le joueur relâche un objet tenu ou chargé. | `hand`, `item`, `charge` | disponible |
| `player:swap-hand` | Échange de main principale/secondaire demandé. | `previous-main-hand`, `previous-off-hand` | disponible |
| `player:drop-item` | Joueur dépose un objet. | `hand`, `item`, `amount` | disponible |
| `player:pickup-item` | Joueur récupère un objet. | `item`, `amount` | disponible |
| `player:hotbar-change` | Emplacement de la barre d’hotbar sélectionné modifié. | `previous-slot`, `slot`, `item` | disponible |
| `player:interact` | Une interaction générique du joueur se produit. | `hand`, `action`, `target` | disponible |

`player:use-item` et `input:secondary-press` représentent l’intention de cliquer à droite, y compris les clics dans l’air où la version originale n’a aucune action d’objet. Paper peut marquer ces `PlayerInteractEvent` comme annulés par défaut. Armature observe toujours l’entrée.

`player:attack` inclut une classification de cible calculée au moment du swing : `event.argument.target` est `air`, `block` ou `entity`. Le `event.argument.target-type` plus précis contient le type de bloc/entité nommé, ou `minecraft:air`. C’est la cible du rayon de traçage du serveur, avant qu’un autre plugin ne puisse annuler ou modifier les dégâts résultants.

```yaml
actions:
  - trigger: player:attack
    condition: event.argument.target in [air, entity]
    animation:
      name: [sword_swing_1, sword_swing_2]
      selection: sequence

  - trigger: block:damage
    animation:
      name: block_swing
```

## Transitions de mouvement

Les boucles de mouvement devraient utiliser `when`, par exemple `player.climbing == true` et `player.climbing-direction == down`. Ces signaux sont pour les animations de transition à effet unique.

| Déclencheur | Émis quand | État |
| --- | --- | --- |
| `player:move-start` | Le mouvement significatif commence. | disponible |
| `player:move-stop` | Le mouvement significatif s’arrête. | disponible |
| `player:sprint-start` | Le sprint commence. | disponible |
| `player:sprint-stop` | Le sprint s’arrête. | disponible |
| `player:sneak-start` | Le joueur commence à s’accroupir. | disponible |
| `player:sneak-stop` | Le joueur cesse de s’accroupir. | disponible |
| `player:jump` | Le saut vers le haut commence. | disponible |
| `player:land` | Le joueur revient au sol. | disponible |
| `player:fall-start` | Le mouvement vers le bas en l’air commence. | disponible |
| `player:fall-stop` | Le mouvement vers le bas s’arrête. | disponible |
| `player:climb-start` | L’escalade commence. | disponible |
| `player:climb-stop` | L’escalade se termine. | disponible |
| `player:swim-start` | La natation commence. | disponible |
| `player:swim-stop` | La natation se termine. | disponible |
| `player:glide-start` | Le joueur commence à planer avec des élytres. | disponible |
| `player:glide-stop` | Le joueur cesse de planer avec des élytres. | disponible |
| `player:mount` | Le joueur monte dans un véhicule/entité. | disponible |
| `player:dismount` | Le joueur quitte un véhicule/entité. | disponible |
| `player:direction-change` | La direction du mouvement relatif réel change. | disponible |

Les détails de la transition incluent `speed`, `vertical-speed`, `fall-distance`, `fall-time`, `direction` et `previous-direction`.

## Cycle de vie et statut du joueur

| Déclencheur | Émis quand | État |
| --- | --- | --- |
| `player:join` | Le joueur rejoint. | disponible |
| `player:ready` | Le resync de présentation est prêt pour le client. | disponible |
| `player:quit` | Le joueur quitte. | disponible |
| `player:respawn` | Le joueur réapparaît. | disponible |
| `player:death` | Le joueur entre dans l’état de mort. | disponible |
| `player:teleport` | Le téléport dans le même monde ou dans un monde différent réussit. | disponible |
| `player:world-change` | Le joueur change de monde. | disponible |
| `player:damage-taken` | Le joueur prend des dégâts. | disponible |
| `player:heal` | La santé du joueur augmente. | disponible |
| `player:effect-add` | Un effet de potion est ajouté ou modifié. | disponible |
| `player:effect-remove` | L’effet de potion est supprimé. | disponible |
| `player:effect-change` | L’action de l’effet de potion n’a pas de mappage d’ajout/suppression. | disponible |
| `player:food-change` | Le niveau de nourriture change. | disponible |
| `player:fire-start` | Le joueur prend feu. | disponible |
| `player:fire-stop` | Le joueur s’arrête de brûler. | disponible |
| `player:freeze-start` | Le gel commence. | disponible |
| `player:freeze-stop` | Le gel s’arrête/régresse. | disponible |

## Objet et équipement

| Déclencheur | Émis quand | État |
| --- | --- | --- |
| `item:equip` | Un profil moderne devient actif. | disponible |
| `item:unequip` | Un profil moderne cesse d’être actif. | disponible |
| `item:held-change` | Les changements d’identité tenus sont résolus. | disponible |
| `item:main-hand-change` | Le slot de la main principale change. | disponible |
| `item:off-hand-change` | L’objet de la main secondaire change. | disponible |
| `item:durability-change` | La durabilité de l’objet change. | disponible |
| `item:consume` | L’objet tenu est consommé/décrémenté. | disponible |
| `item:charge-start` | La charge/l’utilisation commence. | disponible |
| `item:charge-release` | Le joueur relâche la commande de charge ou d’utilisation. | disponible |

`item:equip` et `item:unequip` décrivent des sessions de présentation. Elles ne transfèrent jamais l’inventaire ou la propriété du jeu à Armature.

## Blocs, entités et projectiles

| Déclencheur | Émis quand | Arguments utiles | État |
| --- | --- | --- | --- |
| `block:break-start` | Le client commence à miner. | `phase: start` | disponible |
| `block:damage` | Bukkit signale une dégâts de bloc, généralement au début. | `block`, `tool`, `face`, `phase` | disponible |
| `block:break` | La destruction de bloc est terminée. | `block`, `tool`, `phase: complete` | disponible |
| `block:mining-start` | Une session de minage commence. | `block`, `tool`, `face`, `elapsed: 0` | disponible |
| `block:mining-stop` | Une session de minage se termine ou est annulée. | `block`, `tool`, `face`, `elapsed` | disponible |
| `block:place` | La pose de bloc réussit. | `block`, `against`, `face`, `hand`, `tool` | disponible |
| `block:interact` | Le joueur interagit avec un bloc. | `block`, `face`, `hand`, `action` | disponible |
| `entity:interact` | Le joueur interagit avec une entité. | `entity`, `hand`, `action` | disponible |
| `entity:hit` | Le joueur endommage une entité. | `target`, `damage`, `damage-type` | disponible |
| `entity:death` | La mort de l’entité provoquée par le joueur se produit. | `entity`, `killer` | disponible |
| `projectile:launch` | Le joueur lance un projectile. | `projectile`, `item` | disponible |
| `projectile:hit` | Le projectile du joueur touche une entité ou un bloc. | `projectile`, `target`, `block` | disponible |

`block:break-start` provient de PacketEvents et est un signal d’intention ; `block:break` est l’événement de complétion accepté. Cette distinction évite de jouer une animation de complétion lorsque la mineure est annulée. L’état continu est exposé sous la forme de `block.mining` et est rafraîchi avec le tick du joueur. Armature n’émet pas un nouveau signal d’action à chaque tick. En Créatif, les signaux de démarrage/déstruction répétés rafraîchissent l’état de la mineure. Il s’arrête après huit ticks sans autre impact car le client ne maintient pas la session de progression de destruction de la survie.

## Monde et environnement

| Déclencheur | Émis quand | État |
| --- | --- | --- |
| `world:change` | L’alias de changement de monde est observé. | disponible |
| `world:weather-change` | Changements d’état de pluie/tonnerre. | disponible |
| `world:time-phase-change` | Changements de phase jour/nuit. | disponible |
| `world:enter-water` | Le joueur entre dans l’eau. | disponible |
| `world:leave-water` | Le joueur quitte l’eau. | disponible |
| `world:enter-lava` | Le joueur entre dans la lave. | disponible |
| `world:leave-lava` | Le joueur quitte la lave. | disponible |
| `world:enter-powder-snow` | Le joueur entre dans la neige pulvérisée. | disponible |
| `world:leave-powder-snow` | Le joueur quitte la neige pulvérisée. | disponible |

## Entrées de bas niveau

Ces éléments décrivent l’intention du client. Ils peuvent déclencher même si un plugin de jeu bloque l’action résultante.

| Déclencheur | État |
| --- | --- |
| `input:primary-press` | disponible |
| `input:primary-release` | disponible |
| `input:secondary-press` | disponible |
| `input:secondary-release` | disponible |
| `input:forward-press` / `input:forward-release` | disponible |
| `input:backward-press` / `input:backward-release` | disponible |
| `input:left-press` / `input:left-release` | disponible |
| `input:right-press` / `input:right-release` | disponible |
| `input:jump-press` / `input:jump-release` | disponible |
| `input:sneak-press` / `input:sneak-release` | disponible |
| `input:sprint-press` / `input:sprint-release` | disponible |
| `input:slot-change` | disponible |

## Cycle de vie de l’animation

| Déclencheur | Émis quand | État |
| --- | --- | --- |
| `animation:start` | Un action/transition moderne commence. | disponible |
| `animation:complete` | Une action finie se termine. | disponible |
| `animation:cancel` | Une action est remplacée, annulée ou supprimée. | disponible |
| `animation:transition-start` | Une transition de mouvement commence. | disponible |
| `animation:transition-end` | Une transition de mouvement se termine. | disponible |
| `animation:marker` | BetterModel émet un marqueur d’animation. | disponible |
| `animation:signal` | Un signal de marqueur est transmis de manière générique. | disponible |

Ce sont des événements de session Armature, pas des détails de mise en œuvre de BetterModel.

## Fournisseur et déclencheurs personnalisés

WeaponMechanics utilise son propre espace de noms. L’adaptateur de base normalise ses événements d’action et transmet les détails du fournisseur tels que `weaponmechanics.ammo`, `weaponmechanics.aiming`, `weaponmechanics.reloading`, `weaponmechanics.rounds-remaining` et `weaponmechanics.firearm-state`.

| Famille de signaux | Exemples | État |
| --- | --- | --- |
| WeaponMechanics | `weaponmechanics:fire`, `weaponmechanics:reload`, `weaponmechanics:reload-phase`, `weaponmechanics:firearm-state-change`, `weaponmechanics:fire-mode-change`, `weaponmechanics:reload-complete` | fournisseur |
| API Java | `api:custom-hit`, `custom:parry` | API |
| Ajout Skript | `skript:weapon.special` | API/Skript |
| Denizen | `denizen:weapon.parry` | API/Denizen |
| Autre plugin | `<plugin-id>:<event>` | API |

```yaml
animations:
  actions:
    - trigger: api:custom-hit
      condition:
        - event.argument.critical == true
      animation:
        name: hit
        duration: 6t
```

Les appelants Java utilisent `ArmatureApi.animations().sendSignal(playerId, signal, details)`. Les ajouts Skript et Denizen exposent les opérations de signal équivalentes. Denizen fournit également des événements de cycle de vie natifs et des commandes de lecture brute.

Les plugins de jeu restent responsables de décider si l’événement de jeu réussit. `consume: true` n’a de sens que lorsque le propriétaire de l’événement autorise l’annulation. Pour les signaux de l’API externe, il est retourné comme métadonnées de résultat.


## Équiper et dééquiper

`item:equip` appartient à la présentation entrante. Un `item:unequip` accepté maintient l’instance de sortie/posture vivante pour ce clip, y compris les basculements rapides et le retour au même profil. Sans un clip de dééquipement accepté, il est supprimé immédiatement. `render.dynamic-items.defer-until-equipped` conserve sa capture visuelle de l’objet jusqu’à ce que la présentation entrante soit équipée. Cela ne retarde jamais la sélection de l’inventaire ou le gameplay d’arme.
