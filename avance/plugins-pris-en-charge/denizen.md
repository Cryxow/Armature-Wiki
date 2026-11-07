---
description: "Denizen"
---

# 🤖 Denizen

L’adaptateur Denizen d’Armature est intégré dans `Armature.jar`. Lorsque Denizen est installé et que l’API publique d’Armature est disponible, l’adaptateur enregistre une commande native `armature` et la famille des événements de cycle d’animation.

L’adaptateur effectue uniquement des opérations de présentation. Denizen ou un autre plugin de jeu reste responsable des objets, des munitions, des dégâts, des temps de recharge et de l’annulation des événements.

## Syntaxe de la commande

```
armature [action/animation/raw_animation/loop/raw_loop/stop_action/stop_loop/signal] (<value>) (profile:<name>) (targets:<player>|...)
```

La commande utilise par défaut le contexte de joueur script-entry. Utilisez `targets:|...` pour cibler un ou plusieurs joueurs explicites. Chaque cible explicite doit être un joueur.

| Opération | Valeur | Arguments optionnels |
| --------------------- | -------------------------- | ------------------ |
| `action` | ID d’action intégré | aucun |
| `animation` ou `anim` | ID d’animation | `profile:` |
| `raw_animation` | ID d’actif du modèle | aucun |
| `loop` | ID de boucle intégré | aucun |
| `raw_loop` | ID d’actif de la boucle du modèle | aucun |
| `stop_action` | aucun | aucun |
| `stop_loop` | aucun | aucun |
| `signal` | Déclencheur nommés | aucun |

La commande accepte également des alias avec tiret et des formes compactes pour les opérations raw et stop : `raw-animation`, `rawanimation`, `raw-loop`, `rawloop`, `stop-action`, `stopaction`, `stop-loop`, `stoploop`, `send_signal` et `send-signal`.

Exemples :

```
- armature action FIRE
- armature animation inspect
- armature animation inspect profile:m4a1
- armature raw_animation fire
- armature loop AIM
- armature raw_loop aim
- armature stop_action
- armature stop_loop
- armature signal denizen:weapon.fire
```

`profile:` est pris en charge uniquement pour `animation`. Une valeur est requise pour chaque opération sauf pour `stop_action` et `stop_loop`.

## Événements de cycle de vie

```
on armature animation starts:
  - narrate "started <context.animation> for <context.player>"

on armature animation ends:
  - narrate "ended with <context.reason>"

on armature animation completes:
  - narrate "naturally completed <context.animation>"
```

Contextes disponibles :

* `profile`
* `action`
* `animation`
* `reason`
* `phase`
* `token`
* `player`

`starts` possède une raison vide. `ends` signale `completed`, `cancelled`, `replaced` ou `removed`. `completes` est émis uniquement pour la raison naturelle `completed`.

## Règles de signalisation modernes

La commande peut rediriger un signal vers une règle d’action moderne anonyme :

```yaml
animations:
  actions:
    - trigger: denizen:weapon.fire
      animation:
        name: fire
        duration: 5t
```

```
- armature signal denizen:weapon.fire
```

Pour tous les champs de déclencheur, les conditions et le comportement de sélection d’action disponibles, consultez [Déclencheurs intégrés](../../premiers-pas/profils/triggers.md), [Conditions intégrées](../../premiers-pas/profils/conditions.md) et [API publique](../public-api.md).


## Complétez le profil pour tester l’adaptateur

```yaml
wiki_denizen:
  match:
    item: minecraft:iron_sword
  model:
    name: wiki_player
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: denizen:weapon.fire
        animation:
          name: inspect
          duration: 12t
```

Créez `wiki_player` avec `base` et `inspect` en utilisant le tutoriel Blockbench. Tenez une épée en fer et envoyez le signal correspondant affiché ci-dessus depuis un script/skill. Résultat attendu : une action `inspect`, puis `base`. Les signaux de script génériques ne cancelent pas automatiquement l’événement de jeu de l’hôte. Utilisez uniquement l’événement de fin de cycle de vie pour une fin naturelle, et planifiez le suivi de jeu en utilisant le mécanisme normal de sécurité du joueur de l’hôte.
