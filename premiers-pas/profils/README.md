# 📰 Profils

Les profils se trouvent dans `plugins/Armature/profiles/*.yml`. Chaque entrée racine est un profil d’item indépendant.

```yaml
# Profil utilisé uniquement lorsque la main principale est vide.
empty_hands:
  item: minecraft:air
  model: empty_hands
  hide-vanilla-hand: true
  sway-rate: 1.0
  #render-events: [climb, sprint, swing]

  animations:
    idle: base
    sprint: 
      start: sprint_start
      loop: sprint
    swing: 
      name: swing
      cooldown: 6
    mine: swing
    climb:
      loop: climb_up
      paused: climb_hold
      down: climb_down
    equip: equip
    unequip: unequip
    crouch: 
      #start: sneak_start
      loop: sneak
```

## Champs du profil

| Champ               | Requis | Signification                                                                                                                                  |
| ------------------- | -----: | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `<profile-id>`      |    oui | Clé YAML racine. Utilisez des lettres minuscules, chiffres, `.`, `_` ou `-`.                                                                   |
| `item`              |    oui | Identité d’item résolue, telle que `minecraft:diamond_sword` ou `weaponmechanics:m4a1`. `minecraft:air` est utilisée pour le profil main vide. |
| `model`             |    oui | ID du modèle BetterModel.                                                                                                                      |
| `model-offset`      |    non | Décalage vertical dans l’espace du modèle. Par défaut `0.0`.                                                                                   |
| `sway-rate`         |    non | Multiplicateur par profil du balancement visuel. Par défaut `1.0` ; `0` désactive sa contribution.                                             |
| `hide-vanilla-hand` |    non | Masque la main vanilla en vue à la première personne. Par défaut `true`.                                                                       |
| `animations`        |    non | Association entre noms d’actions Armature et définitions d’animations BetterModel.                                                             |

## Animations

Actions acceptées :

```
equip
unequip

idle
walk
sprint
crouch

jump
land
fall

climb
climb-hold
climb-down

swing
mine
place
use

eat
drink
throw

block
bow-draw
crossbow-charge
trident-charge

--- WeaponMechanics only ---

fire
aim
aim-start
aim-exit
aim-fire

reload-start
reload-phase
reload-complete
reload-cancel
```

| Champ      | Signification                                                                        |
| ---------- | ------------------------------------------------------------------------------------ |
| `name`     | Nom exact de l’animation BetterModel.                                                |
| `duration` | Durée forcée de l’action en ticks serveur.                                           |
| `speed`    | Multiplicateur de lecture, supérieur à `0`.                                          |
| `cooldown` | Délai minimal avant de rejouer la même animation, en ticks serveur.                  |
| `start`    | **Pour les animations en boucle.** Joue une animation avant la boucle.               |
| `loop`     | **Pour les animations en boucle.**                                                   |
| `end`      | **Pour les animations en boucle.** Joue une animation à la fin de la boucle.         |
| `paused`   | **Pour l’action d’escalade.** Animation lorsque le joueur se baisse sur une échelle. |
| `down`     | **Pour l’action d’escalade.** Animation lors de la descente d’une échelle.           |
