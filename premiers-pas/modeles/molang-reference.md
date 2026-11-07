---
description: "Référence Molang"
---

# 📖 Référence Molang

Ceci est le sous-ensemble **implémenté nativement**, partagé par les échantillonneurs serveur et client. Il ne s’agit pas d’un moteur de script Bedrock général. Les requêtes dépendent du contexte fourni par le moteur de rendu. Certains noms de compatibilité sont volontairement constants.

## Syntaxe des expressions

Les nombres, parenthèses, opérateurs unaires `!`, `-`, `+`, l’arithmétique `+ - * / %`, les comparaisons, `&&`, `||` et les conditions `condition ? yes : no` sont pris en charge. La trigonométrie utilise les degrés. Les expressions sont évaluées au moment de l’échantillonnage, y compris l’aléatoire. Les blocs d’instructions, l’exécution de scripts arbitraires et l’environnement Bedrock complet ne sont pas implicites par la prise en charge de ces expressions.

## Requêtes

| Requête/alias | Signification actuelle |
| --- | --- |
| `query.yaw` | yaw |
| `query.pitch` | pitch |
| `query.modified_distance_moved` | modifiedDistanceMoved |
| `query.modified_move_speed` | modifiedMoveSpeed |
| `query.anim_time` | animTime |
| `query.delta_time` | deltaTime |
| `query.game_time` | gameTime |
| `query.program_time` | programTime |
| `query.lifetime` | lifetime |
| `query.age` | lifetime |
| `query.is_moving` | Indicateur de contexte booléen (1 = vrai, 0 = faux). |
| `query.is_on_ground`, `query.is_onground` | Indicateur de contexte booléen (1 = vrai, 0 = faux). |
| `query.is_sneaking` | Indicateur de contexte booléen (1 = vrai, 0 = faux). |
| `query.is_sprinting` | Indicateur de contexte booléen (1 = vrai, 0 = faux). |
| `query.is_swimming` | Indicateur de contexte booléen (1 = vrai, 0 = faux). |
| `query.first_person` | Constante de compatibilité `0.0`, sans interrogation de l’état du jeu |
| `query.third_person` | Constant de compatibilité `1.0` (pas une requête de jeu en direct) |
| `query.health`, `query.max_health` | Vie actuelle du contexte. Max_health est actuellement un alias, pas une valeur maximale distincte. |
| `query.hurt_time` | hurtTime |
| `query.target_x`, `query.target_y`, `query.target_z` | Constante de compatibilité `0.0`, sans interrogation de l’état du jeu |
| `query.view_x`, `query.view_y`, `query.view_z` | Constante de compatibilité `0.0`, sans interrogation de l’état du jeu |
| `query.camera_x`, `query.camera_y`, `query.camera_z` | Constante de compatibilité `0.0`, sans interrogation de l’état du jeu |
| `query.uuid`, `query.player_uuid` | Constante de compatibilité `0.0`, sans interrogation de l’état du jeu |
| `query.day_time` | gameTime |
| `query.moon_phase` | Constante de compatibilité `0.0`, sans interrogation de l’état du jeu |

## Fonctions mathématiques

`math.sin`, `math.cos`, `math.tan`, `math.asin`, `math.acos`, `math.atan`, `math.atan2`, `math.abs`, `math.sqrt`, `math.floor`, `math.ceil`, `math.round`, `math.trunc`, `math.sign`, `math.exp`, `math.ln`, `math.degrees`, `math.radians`, `math.inversesqrt`, `math.inv_sqrt`, `math.saturate`, `math.lerp`, `math.lerprotate`, `math.lerpplace`, `math.pow`, `math.mod`, `math.min`, `math.max`, `math.clamp`, `math.step_smooth`, `math.smooth`, `math.hermite_blend`, `math.random`, `math.random_integer`, `math.randomint`, `math.die_roll`, `math.die_roll_integer`, `math.roll_integer`.

Utilisez l’arité supportée par la fonction : par exemple `math.sin(angle)`, `math.clamp(value, minimum, maximum)`, `math.lerp(start, end, amount)` et `math.random(minimum, maximum)`. Les fonctions/requêtes inconnues produisent des diagnostics. Les variables Molang non affectées se lisent comme zéro. Les variables YAML partagées ne sont pas automatiquement liées à cet espace de noms.

## Petit exemple

```text
query.is_moving ? math.sin(query.anim_time * 180) * 3 : 0
```

Utilisez ceci dans un composant numérique animé pris en charge, et non comme une chaîne YAML `when`. Vérifiez le contexte de requête dans le jeu et gardez la sélection de l’état de jeu dans [conditions de profil](../profils/conditions.md).
