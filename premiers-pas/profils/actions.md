---
description: "Règles d’action"
---

# 🎬 Règles d’action

Les actions utilisent un événement `trigger` et un champ optionnel `condition` :

```yaml
animations:
  actions:
    - trigger: block:place
      condition:
        - item.main-hand.type == minecraft:stone
      animation:
        name: block-place
        duration: 8t

    - trigger: api:custom-hit
      condition:
        - event.argument.critical == true
      animation:
        name: hit
        duration: 6t
      consume: false
```

Les actions utilisent toujours le canal exclusif `replace`. Il n’y a pas de champ `mode`. `can-be-cancelled: false` protège une action active contre le remplacement. Il ne crée pas une superposition. Les règles additives correspondent au canal séparé composé par Armature.

`consume: true` a du sens pour un propriétaire d’événement qui supporte l’annulation, comme un clic droit vanilla ou une entrée fournie par un fournisseur. Pour un signal API/Skript/Denizen, il est retourné dans `ArmatureSignalResult`. L’appelant externe conserve toujours la propriété de l’annulation de jeu.

## Options d’animation

Avec `render.backend: native`, les noms d’actifs d’animation sont librement choisis, y compris `idle`, `walk`, `idle_fly`, `walk_fly`, `spawn` et `jump`. Les mouvements/actions/règles additives et leurs clips de début/fin/référence utilisent les noms explicitement configurés. Aucun clip ne démarre automatiquement en raison de son nom. La même politique s’applique aux profils legacy et aux présentations API, au démarrage et au rechargement du profil. Les noms doivent être non vides et correspondre à un clip dans le modèle. Le backend BetterModel conserve sa validation des noms réservés.

Chaque `animation` accepte soit un nom d’actif, soit une liste de variantes :

```yaml
animation:
  name: [swing, swing_1, swing_2]
  selection: random
  duration: 6t
  speed: 1.0
  cooldown: 3t
  can-be-cancelled: true
```

| Champ | Déplacement | Action | Additive | Signification |
| --- | --- | --- | --- | --- |
| `name` | oui | oui | oui | Un actif ou une liste de variantes. |
| `arms` | facultatif | facultatif | non | `one` (par défaut) ou `both`. Une action de déplacement ou d’action de la main principale peut posséder les deux squelettes de mains. |
| `selection` | oui | oui | oui | `sequence` ou `random`. Par défaut `sequence`. |
| `speed` | oui | oui | oui | Multiplicateur de lecture non nul. Les valeurs négatives jouent l’animation à l’envers. |
| `blend-in` | facultatif | facultatif | non | Durée de mélange d’entrée. Par défaut `2t` pour le déplacement et `1t` pour l’action. |
| `blend-out` | facultatif | facultatif | non | Durée de mélange de sortie. Par défaut `2t` ; `0t` le désactive. |
| `sway-rate` | oui | non | non | Déplacement optionnel surchargé. Hérite de `model.sway-rate` lorsqu’il est omis. |
| `loop` | vrai | faux/omis | n/a | Les règles de déplacement doivent être itératives. |
| `start` | facultatif | non | non | Animation unique avant une boucle de déplacement. |
| `end` | facultatif | non | non | Animation unique après avoir quitté une boucle de déplacement. |
| `duration` | non | facultatif | non | Durée de l’action de repli, en ticks ou en secondes. |
| `cooldown` | non | facultatif | non | Délai entre les envois d’actions réussis. Par défaut `3t`. |
| `can-be-cancelled` | non | facultatif | non | Indique si une autre action de remplacement peut l’interrompre. Par défaut `true`. |

Pour une action ponctuelle, la durée d’animation connue du moteur prime sur la durée de repli configurée. Avec `selection: sequence`, la sélection avance après chaque lancement réussi. Avec `random`, le choix est indépendant à chaque lancement. Toutes les variantes d’une règle partagent le même délai de récupération.

## Actions et transitions protégées

`can-be-cancelled: false` protège l’action finie active contre un remplacement ordinaire. Les changements de variante/composition en attente sont réévalués après la fin de l’action protégée. Les changements d’objet/déconnexion et le nettoyage du moteur de rendu nécessitent toujours une gestion du cycle de vie. Ce drapeau n’est pas un verrou de jeu.

Une clip de désarmement accepté utilise l’instance/posture sortante. Un cycle rapide A → B → A peut récupérer le profil/instance de modèle sortant correspondant. Un `speed` négatif non nul joue la timeline auteur en sens inverse. Une vitesse nulle est invalide.

`consume` est une décision d’envoi de fournisseur/entrée, pas une logique automatique de munitions/recharge. Certains signaux d’entrée/fournisseur annulables peuvent être consommés. Voir [WeaponMechanics](../../avance/plugins-pris-en-charge/weaponmechanics.md) pour son comportement d’annulation de l’entrée de recharge. Gardez l’autorité de l’arme dans ce plugin.

Vérifiez les entrées répétées, le refus pendant le délai de récupération, les clips interruptibles ou protégés et le passage à un objet vanilla suivi d’un retour. Le guide des [deux mains](hands.md) explique l’effet de `arms: both` sur les animations simultanées.
