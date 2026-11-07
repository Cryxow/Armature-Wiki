---
description: "Structure des profils"
---

# 🗂️ Structure des profils

Les profils sont des fichiers YAML situés ci-dessous `plugins/Armature/profiles/**/*.yml`. Chaque clé racine est un identifiant de profil globalement unique. Les noms de fichiers ou de dossiers ne limitent pas la portée des références. Les fichiers modernes et legacy peuvent coexister, mais utilisez le format moderne pour les nouveaux profils.

## Profil minimal d’objet

```yaml
wiki_stick:
  match:
    item: minecraft:stick
  model:
    name: wiki_player
```

Le modèle `wiki_player` doit exister. Un profil statique peut omettre les animations. L’ajout de règles sélectionne des clips créés plutôt que de définir de nouvelles géométries de modèle.

## Forme complète

```yaml
wiki_rifle:
  match:
    item: weaponmechanics:m4a1
    when: variable.rifle-enabled == true
  model:
    name: arm_rifle
    offset: 0.0
    sway-rate: 1.0
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: weaponmechanics:fire
        animation:
          name: fire
          cooldown: 3t
    additives:
      - when: player.falling == true
        animation: fall
        playback: loop
        weight: 0.5
```

Déclarez `rifle-enabled` dans [variables.yml](variables.md), et fournissez tous les clips de modèle utilisés ci-dessus. Les noms de ressources de remplacement ne sont pas des capacités intégrées.

## Champs et présentation

| Champ | Obligatoire | Signification |
| --- | --- | --- |
| `<profile-id>` | oui | ID racine : lettres minuscules, chiffres, `.`, `_` ou `-`. |
| `match` | oui | ID d’objet exact, groupe d’objet, motif ou un matcheur structuré. |
| `model.name` | oui | ID du modèle chargé pour le moteur de rendu natif actif. |
| `model.offset` | non | Décalage vertical dans l’espace du modèle. Par défaut `0.0`. |
| `model.sway-rate` | non | Multiplicateur de balancement par profil. Par défaut `1.0`. |
| `model.hide-vanilla-hand` | non | Caché l’objet à la première personne original. Par défaut `true`. |
| `model.render-events` | non | Liste d’autorisation d’événements de rendu optionnelle pour les sessions conditionnelles. |
| `model.held-item` | non | État visuel de la copie de l’objet tenu : `static`, `source` ou `rules`. L’omission conserve le comportement existant. |
| `animations.movement[].hide-vanilla-hand` | non | Remplace la valeur du modèle lorsque cette règle de mouvement est sélectionnée. |
| `animations.actions[].hide-vanilla-hand` | non | Remplace la valeur de mouvement/modèle lorsque cette action est active. |
| `type` | non | `item` (par défaut) ou réutilisable `attachment`. Voir [composites de profils](attachments.md). |
| `animations` | non | Contient `movement`, `actions` et/ou `additives`. |
| `integrations` | non | Paramètres de présentation appartenant au fournisseur. |

`model.offset` déplace la racine du modèle dans l’espace du modèle. `sway-rate` modifie le balancement de l’aspect/mouvement sans changer le bobbing ou le suivi de la caméra.

`hide-vanilla-hand` contrôle uniquement la présentation. Armature utilise le masque de client et le chemin du pack de ressources configurés. L’objet de l’inventaire du serveur n’est jamais remplacé. Une main vide peut être sélectionnée avec `match.item: minecraft:air`.

`match.when` gère l’activation ; [variants](variants.md) choisissent une présentation conditionnelle au sein d’un profil actif, et [attachments](attachments.md) montent des instances de modèle réutilisables. Les variables YAML appartiennent exclusivement à `variables.yml`. Les présentations créées via l’API ont leur propre contrat explicite de variables en ligne.

## Charger en toute sécurité

Exécuter `/armature reload profiles`, inspecter les avertissements et utiliser `/armature profile inspect <player>`. Un fichier malformat n’est pas réparé par le rechargement. Les erreurs de liaison, telles que les profils d’accessoires inconnus, les cycles, les variables inconnues ou les conditions de sélection invalides, rejettent la bibliothèque candidate. La bibliothèque active reste en place. La validation des modèles/clip/mount couvre également les variantes inactives.

Les références de nouveaux modèles nécessitent la génération et la livraison du modèle/pack, pas seulement un rechargement de profil. Voir [Exemples complets](examples.md) et [Mise à niveau](../upgrading.md).
