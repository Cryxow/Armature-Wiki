---
description: "MythicMobs"
---

# 🐉 MythicMobs

L’adaptateur MythicMobs d’Armature est intégré à `Armature.jar`. Installez `MythicMobs` à côté d’Armature. Il enregistre ses mécaniques et ses déclencheurs de cycle de vie uniquement lorsque l’API publique d’Armature et MythicMobs sont disponibles.

Toutes les mécaniques ciblent un joueur et appellent l’API de présentation d’Armature. Gardez les projectiles, les dégâts, les munitions, les temps de recharge et les autres mécaniques de jeu dans la compétence MythicMobs.

## Mécanismes

```yaml
PistolFire:
  Skills:
    - armatureaction{action=FIRE} @self
    - armatureanimation{animation=inspect} @self
    - armaturesignal{signal=mythicmobs:weapon.fire} @self

AimStart:
  Skills:
    - armatureloop{action=AIM} @self

AimStop:
  Skills:
    - armatureloop{stop=true} @self
```

| Mécanisme | Configuration principale | Effet |
| ----------------------------------------------- | ---------------------------------------------------- | ----------------------------------------------------- |
| `armatureaction` (`armatureactionplay`) | `action` ou `a` | Joue un `ArmatureAction` intégré |
| `armatureanimation` (`armatureanim`) | `animation`, ou `action`/`a`. Optionnel `profile`/`p` | Joue une animation de profil actif ou explicite |
| `armatureloop` (`armaturestartloop`) | `action`/`a`, ou `stop`/`s` | Sélectionne une boucle intégrée ou arrête la boucle sélectionnée |
| `armaturerawanimation` (`armatureanimationraw`) | `animation`/`a` | Joue un actif brut depuis le modèle rendu actif |
| `armaturerawloop` (`armatureloopraw`) | `animation`/`a`, ou `stop`/`s` | Sélectionne ou arrête une boucle brute |
| `armaturesignal` (`armatureemit`) | `signal`, `event`, ou `s` | Route un déclencheur moderne configuré |
| `armaturestopaction` (`armaturecancelaction`) | aucun | Annule l’action unique en cours |
| `armaturestoploop` (`armaturecancelloop`) | aucun | Arrête la boucle sélectionnée |

Utilisez une cible joueur telle que `@self`, `@target` ou `@trigger` lorsque cette entité est un joueur. Une cible invalide ou une configuration incorrecte produit le résultat normal d’échec de compétence MythicMobs.

## Déclencheurs de cycle de vie

L’adaptateur enregistre :

| Déclencheur canonique | Alias |
| ----------------------------- | ---------------------------------------------------------- |
| `ARMATURE_ANIMATION_START` | `armatureanimationstart`, `armature-animation-start` |
| `ARMATURE_ANIMATION_END` | `armatureanimationend`, `armature-animation-end` |
| `ARMATURE_ANIMATION_COMPLETE` | `armatureanimationcomplete`, `armature-animation-complete` |

Exemple :

```yaml
Triggers:
  - ARMATURE_ANIMATION_END
```

Les métadonnées du déclencheur contiennent :

* `armature.profile`
* `armature.action`
* `armature.animation`
* `armature.reason`
* `armature.token`

`ARMATURE_ANIMATION_END` se déclenche à chaque terminaison d’animation finie. `ARMATURE_ANIMATION_COMPLETE` se déclenche uniquement lorsque l’animation atteint sa fin naturelle.

## Règles de signalisation modernes

Utilisez `armaturesignal` lorsque le profil possède la mappage :

```yaml
WeaponFire:
  Skills:
    - armaturesignal{signal=mythicmobs:weapon.fire} @self
```

```yaml
animations:
  actions:
    - trigger: mythicmobs:weapon.fire
      condition:
        all:
          - player.on-ground == true
          - weaponmechanics.ammo > 0
      animation:
        name: fire
        duration: 5t
```

Les valeurs de la main droite dynamiques dans les conditions nécessitent le préfixe `$`. Par exemple, `weaponmechanics.ammo == $weaponmechanics.magazine-size` compare deux valeurs d’exécution. Sans `$`, une chaîne de droite de type chemin est littérale.

Voir [Déclencheurs intégrés](../../premiers-pas/profils/triggers.md), [Conditions intégrées](../../premiers-pas/profils/conditions.md) et [API publique](../public-api.md).


## Profil complet pour tester l’adaptateur

```yaml
wiki_mythicmobs:
  match:
    item: minecraft:iron_sword
  model:
    name: wiki_player
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: mythicmobs:weapon.fire
        animation:
          name: inspect
          duration: 12t
```

Créez `wiki_player` avec `base` et `inspect` en utilisant le tutoriel Blockbench. Tenez une épée en fer et envoyez le signal correspondant affiché ci-dessus depuis un script/compétence. Résultat attendu : une action `inspect`, puis `base`. Les signaux de script génériques n’annulent pas automatiquement l’événement de jeu de l’hôte. Utilisez uniquement l’événement de complétion de cycle de vie pour la complétion naturelle, et planifiez le suivi de jeu en utilisant le mécanisme normal de sécurité du joueur de l’hôte.
