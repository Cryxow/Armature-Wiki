---
description: "Règles de mouvement"
---

# 🏃 Règles de mouvement

Les règles de mouvement utilisent `when`. Armature évalue les règles correspondantes, sélectionne la condition la plus spécifique et utilise l’ordre de déclaration uniquement comme dernier critère de résolution des égalités. Aucun mouvement `priority` n’est requis.

La règle de mouvement `animation.sway-rate` surseaut uniquement au multiplicateur de balancement du modèle tant que cette règle est sélectionnée. Si elle est omise, le mouvement utilise `model.sway-rate`. Une valeur explicite de `0.0` désactive le balancement procédural pour ce mouvement.

```yaml
animations:
  movement:
    - when:
        - player.climbing == true
        - player.climbing-direction == up
      animation: climb-up

    - when:
        - player.climbing == true
        - player.climbing-direction == down
      animation: climb-down

    - when:
        - player.falling == true
      animation:
        name: fall
        start: fall-start
        end: fall-end
```

`player.climbing-direction` est `up`, `down` ou `none`. Pour la chute libre, utilisez `player.falling` ou `player.vertical-speed < 0`. Pour une réponse de chute fluide, utilisez un poids additif piloté par `player.fall-distance`.

## Options d’animation

Avec `render.backend: native`, les noms des actifs d’animation sont librement choisis, y compris `idle`, `walk`, `idle_fly`, `walk_fly`, `spawn` et `jump`. Les règles de mouvement/action/additive et leurs clips de début/fin/référence utilisent les noms explicitement configurés. Aucun clip ne démarre automatiquement en raison de son nom. La même politique s’applique aux profils legacy et aux présentations API, au démarrage et au rechargement du profil. Les noms doivent être non vides et correspondre à un clip dans le modèle. Le backend BetterModel conserve sa validation des noms réservés.

Chaque `animation` accepte soit un nom d’actif, soit un mapping :

```yaml
animation:
  name: [swing, swing_1, swing_2]
  selection: random
  duration: 6t
  speed: 1.0
  cooldown: 3t
  can-be-cancelled: true
```

| Champ | Mouvement | Action | Additif | Signification |
| --- | --- | --- | --- | --- |
| `name` | oui | oui | oui | Un actif ou une liste de variantes. |
| `arms` | facultatif | facultatif | non | `one` (par défaut) ou `both`. Une action de déplacement de la main principale peut posséder les deux squelettes de mains. |
| `selection` | oui | oui | oui | `sequence` ou `random`. Par défaut `sequence`. |
| `speed` | oui | oui | oui | Multiplicateur de lecture non nul. Les valeurs négatives jouent l’animation en sens inverse. |
| `blend-in` | facultatif | facultatif | non | Durée de mélange d’entrée. Par défaut `2t` pour le déplacement et `1t` pour l’action. |
| `blend-out` | facultatif | facultatif | non | Durée de mélange de sortie. Par défaut `2t` ; `0t` le désactive. |
| `sway-rate` | oui | non | non | Surchauffe du mouvement facultative. Hérite de `model.sway-rate` si omise. |
| `loop` | vrai | faux/omise | n/a | Les règles de mouvement doivent boucler. |
| `start` | facultatif | non | non | Animation unique avant un bouclage de mouvement. |
| `end` | facultatif | non | non | Animation unique après avoir quitté un bouclage de mouvement. |
| `duration` | non | facultatif | non | Durée de l’action de repli, en ticks ou en secondes. |
| `cooldown` | non | facultatif | non | Délai entre les actions réussies. Par défaut `3t`. |
| `can-be-cancelled` | non | facultatif | non | Si une autre action de remplacement peut l’interrompre. Par défaut `true`. |

Pour les actions finies, la durée d’animation connue par le moteur de rendu prime sur la durée de repli configurée. `selection: sequence` avance après chaque action réussie ; `random` choisit indépendamment par action. Le temps de recharge est partagé entre toutes les variantes d’une même règle.

## Une règle de base et une règle spécifique

```yaml
animations:
  movement:
    - animation: base
    - when: player.sprinting == true
      animation:
        name: sprint
        speed: 1.0
        blend-in: 2t
        blend-out: 2t
```

La règle sans condition fournit une base. Les conditions de correspondance plus spécifiques prévalent. L’ordre de déclaration trancher les cas d’égalité de spécificité. Cela diffère du choix de variante, où la première variante éligible gagne. `0t` désactive ce côté. Les noms de règle ne sélectionnent pas automatiquement les clips.
