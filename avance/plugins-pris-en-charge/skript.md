---
description: "Skript"
---

# 📜 Skript

L’adaptateur Skript d’Armature est intégré dans la distribution `Armature.jar`. Installez `Skript` à côté d’Armature. La syntaxe est enregistrée uniquement lorsque l’API publique d’Armature et Skript sont disponibles.

L’adaptateur appelle l’API de présentation publique. Il ne valide pas les armes, ne consomme pas les munitions, n’applique pas de dégâts, ni ne cancelle l’événement de jeu qui a déclenché un effet Skript.

## Effets

```skript
play armature animation "inspect" for player
play armature action "FIRE" for player
start armature loop "AIM" for player
stop armature action for player
stop armature loop for player
send armature signal "myplugin:weapon.fire" to player
play armature animation "inspect" from profile "m4a1" for player
play raw armature animation "fire" for player
start raw armature loop "aim" for player
```

Les effets correspondent à ces opérations API :

| Syntaxe | Opération API |
| ----------------------------- | ------------------------------------------- |
| `play armature animation` | `playAnimation(player, animation)` |
| `play armature action` | Parse un `ArmatureAction` intégré |
| `start armature loop` | Sélectionne une action de boucle intégrée |
| `stop armature action` | `stopAction(player)` |
| `stop armature loop` | `stopLoop(player)` |
| `send armature signal` | `sendSignal(player, signal)` |
| `... from profile ...` | `playAnimation(player, profile, animation)` |
| `play raw armature animation` | `playRawAnimation(player, animation)` |
| `start raw armature loop` | `startRawLoop(player, animation)` |

Utilisez un signal namespaced lorsque le profil contient une règle d’action moderne :

```yaml
animations:
  actions:
    - trigger: skript:weapon.fire
      animation:
        name: pistol_fire
        duration: 6t
```

```skript
send armature signal "skript:weapon.fire" to player
```

La règle YAML peut inspecter les détails de l’événement et définir `consume: true`, mais cette valeur est retournée comme métadonnées de routage. Elle ne cancelle pas automatiquement un événement Skript.

## Événements de cycle de vie

```skript
on armature animation start:
    broadcast "started [armature animation name]"

on armature animation end:
    broadcast "ended [armature animation name]: [armature animation end reason]"

on armature animation complete:
    # This is only a natural finite-animation completion.
    broadcast "completed [armature animation name]"
```

L’adaptateur expose ces expressions :

* `armature animation profile`
* `armature animation action`
* `armature animation name`
* `armature animation end reason`

La raison de la fin est disponible sur `animation end` et `animation complete`. Elle est l’une de `completed`, `cancelled`, `replaced` ou `removed`. L’événement complet est émis uniquement pour `completed`.

## Propriété et échecs

Gardez la logique de jeu dans le code Skript propriétaire :

```skript
on arm swing:
    # Check the weapon, ammo and cooldown in the gameplay script.
    play armature action "FIRE" for player
```

Si le joueur est hors ligne, Armature n’est pas disponible, ou si le profil ou l’asset demandé est manquant, l’adaptateur n’a tout simplement aucune opération de présentation à appliquer. Pour les statuts de résultat et le routage de signal moderne, consultez [API publique](../public-api.md) et [Conditions intégrées](../../premiers-pas/profils/conditions.md).


## Profil complet pour tester l’adaptateur

```yaml
wiki_skript:
  match:
    item: minecraft:iron_sword
  model:
    name: wiki_player
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: skript:weapon.fire
        animation:
          name: inspect
          duration: 12t
```

Créez `wiki_player` avec `base` et `inspect` en suivant le tutoriel Blockbench. Tenez une épée en fer et envoyez le signal correspondant affiché ci-dessus depuis un script ou une compétence. Résultat attendu : une action `inspect`, puis `base`. Les signaux de script génériques ne cancelent pas automatiquement l’événement de jeu de l’hôte. Utilisez uniquement l’événement de fin de cycle de vie pour une fin naturelle, et planifiez le suivi du jeu en utilisant le mécanisme normal de sécurité du joueur de l’hôte.
