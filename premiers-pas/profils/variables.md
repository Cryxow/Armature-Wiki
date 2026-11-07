---
description: "Variables partagées et PlaceholderAPI"
---

# 🔤 Variables partagées et PlaceholderAPI

Seule `plugins/Armature/variables.yml` déclare des variables YAML :

```yaml
variables:
  weapon-skin:
    type: string
    source: placeholderapi
    placeholder: "%myplugin_weapon_skin%"
    refresh: 10t
    default: classic
  charm-unlocked:
    type: boolean
    source: placeholderapi
    placeholder: "%myplugin_charm_unlocked%"
    refresh: 10t
    default: false
  player-level:
    type: number
    source: placeholderapi
    placeholder: "%player_level%"
    refresh: 10t
    default: 0
```

Les noms de placeholders ci-dessus sont des exemples, pas des expansions intégrées. Les constantes, les sources de fournisseur/API et les types `number`, `boolean`, `string`, `duration`, `enum` conservent leur syntaxe existante. Utilisez `variable.<id>` dans n’importe quel profil sans import. `placeholder.<id>` reste une alias. Les entiers utilisent `number`.

Les définitions sont partagées, les valeurs non : les caches appartiennent à un joueur et à une main/séance. Les modèles montés partagent la résolution de variables de la main propriétaire. Seules les définitions référencées sont résolues. Les valeurs de fournisseur/API utilisent le contexte actuel, y compris les arguments de l’événement actuel. Les données d’événement ne sont pas réutilisées comme valeur de variable d’un événement ultérieur. Les valeurs externes manquantes ou non convertibles utilisent la valeur par défaut déclarée, puis la valeur neutre du type. Un fichier manquant est un registre vide.

Les déclarations YAML locales sont rejetées, y compris à l’intérieur des variantes et des instances. Déplacez les anciennes déclarations manuellement dans le fichier commun, résolvez les IDs en conflit et supprimez les blocs locaux. Les déclarations communes restent propriété de l’administrateur. Les requêtes Java standalone `ArmaturePresentation` conservent leur contrat de variable en ligne existant.

## Sources et types

| Source | Déclaration/utilisation |
| --- | --- |
| `constant` | `value` fournit une valeur de type fixe |
| `placeholderapi` | `placeholder` est résolu via les expansions PlaceholderAPI/installées |
| `provider` | Fournisseur/chemin identifie une valeur de fournisseur nommée |
| `api` | Chemin lit le contexte API/signal actuel |

Les types sont `number`, `boolean`, `string`, `duration`, `enum`. Utilisez `number` aussi pour les entiers. `clamp` s’applique aux valeurs numériques/durées. Les durées acceptent des ticks, des secondes ou des ticks numériques. Les placeholders affichés ici sont des exemples, pas des expansions intégrées à Armature.

## Référence de déclaration

| Champ | Obligatoire/par défaut | Signification |
| --- | --- | --- |
| `type` | Obligatoire | `number`, `boolean`, `string`, `duration`, `enum` |
| `source` | `constant` | Une seule source dans le tableau ci-dessus |
| `value` | Obligatoire pour la valeur constante | Scalaire de type fixe. Interdit pour les autres sources |
| `placeholder` | Obligatoire pour PlaceholderAPI | Chaine de remplacement incluant `%`. Interdit pour les autres sources |
| `provider` | Obligatoire pour le fournisseur. Optionnel pour l’API | Espace de nom pour la recherche. Interdit pour les autres sources |
| `path` | ID de la variable en cas d’omission | Champ de contexte actuel pour les sources fournisseur/API |
| `refresh` | `1t`, au moins 1 tick | Durée de rafraîchissement du remplacement. Les valeurs fournisseur/API lisent le contexte actuel |
| `default` | Valeur neutre du type | Solution de repli typée en cas d’échec de recherche/conversion |
| `clamp` | Omis | `[minimum, maximum]` fini et ordonné, uniquement nombre/durée |

`enum` est un type à valeur de chaîne, pas une liste `values` déclarée séparément. Les valeurs constantes/par défaut booléennes et numériques doivent utiliser leurs types YAML. La recherche de fournisseur/API vérifie les détails de l’événement actuel, puis les valeurs du contexte actuel. Elle ne conserve pas les arguments d’un événement précédent.

```yaml
variables:
  rounds:
    type: number
    source: provider
    provider: weaponmechanics
    path: ammo
    default: 0
  input-allowed:
    type: boolean
    source: api
    path: allowed
    default: false
  mode:
    type: enum
    source: constant
    value: semi
  animation-time:
    type: duration
    source: constant
    value: 12t
    clamp: [0, 40]
```

## Un exemple de constante complet

Enregistrez ceci sous `plugins/Armature/variables.yml` :

```yaml
variables:
  rifle-enabled:
    type: boolean
    source: constant
    value: true
    default: false
  weapon-skin:
    type: string
    source: constant
    value: classic
    default: classic
  player-level:
    type: number
    source: constant
    value: 10
    default: 0
```

Utilisez `variable.rifle-enabled == true` ou `variable.player-level >= 10` depuis n’importe quel profil sans import. Rechargez les profils après avoir modifié le registre, puis inspectez la présentation sélectionnée par le joueur.

Les valeurs externes manquantes/inconvertibles utilisent `default`, puis la valeur neutre du type. Les données d’événement utilisent l’événement actuel et ne sont pas conservées pour un événement ultérieur. Modifier une variable de sélection ne reconstruit pas le pack : tous les modèles référencés doivent avoir passé la validation et déjà être présents dans les actifs publiés.
