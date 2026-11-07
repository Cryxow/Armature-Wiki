---
description: "Commandes et permissions"
---

# ⌨️ Commandes et permissions

Armature enregistre un Paper `BasicCommand` tout au long du cycle de vie du registrar de commandes. La commande racine est `/armature`. Paper expose également `/arm` et `/armatureadmin`. `plugin.yml` conserve uniquement les métadonnées de commande Bukkit et la chaîne d’utilisation, de sorte qu’il ne déclare pas d’alias concurrents.

Les en-têtes d’administrateur utilisent `plugins/Armature/messages.yml`. `locale: auto`, `en` et `fr` sont pris en charge. Les codes de diagnostic restent indépendants du langage. Chaque commande nécessite `armature.command` au niveau racine, puis sa permission de domaine. Les envoyeurs de console sont pris en charge sauf si une commande exige explicitement un contexte de joueur.

## Commandes principales

```text
/armature help [domain]
/armature status
/armature reload [all|config|profiles|models|pack|providers]
/armature enable
/armature disable
/armature config ...
/armature profile ...
/armature animation ...
/armature editor
/armature diagnostics ...
/armature debug ...
```

Les raccourcis racines `help`, `status`, `reload`, `enable`, `disable` et `editor` nus restent disponibles. `/armature help` liste uniquement les domaines autorisés pour l’expéditeur. Le domaine `debug` est omis sauf si l’expéditeur possède `armature.command.native-probe`.

`help` accepte un domaine pour afficher l’utilisation contextuelle, par exemple `/armature profile help`. Les suggestions sont filtrées par permission et par le texte déjà tapé. Les complétions joueur, profil, modèle, animation, migration-file et native-probe sont fournies là où applicable.

## `/armature status`

Affiche le backend actif, les interrupteurs de runtime et de rendu, les nombres de modèles/profils configurés, les animations configurées indisponibles, les nombres de fournisseurs et les métriques du moteur de rendu. Les détails du catalogue natif sont inclus lorsque le backend natif a chargé un catalogue.

Permission : `armature.command.status`.

## Commandes de rechargement

Tous les rechargements utilisent `/armature reload <target>` :

| Cible | Opération |
| --- | --- |
| `config` | Recharger et valider `config.yml` uniquement. |
| `profiles` | Recharger uniquement les fichiers de profil. |
| `models` | Recharger uniquement les actifs de modèle de moteur de rendu. |
| `pack` | Reconstruire le pack de ressources d’Armature. |
| `providers` | Demander la génération d’un fournisseur de pack de ressources externe sans reconstruire Armature. |
| `all` | Recharger la configuration, les profils, les modèles et le pack d’Armature. |

Sans cible, `/armature reload` recharge la configuration, les profils et les modèles. La génération du pack suit `resource-pack.generation.on-reload`. Des cibles explicites `pack` et `all` génèrent le pack d’Armature même lorsque les déclencheurs automatiques sont désactivés.

`all` reconstruit les actifs d’Armature. Les ponts de fournisseur peuvent régénérer leurs packs lorsque ces actifs changent. `/armature reload providers` demande explicitement la génération via les ponts CraftEngine et Nexo supportés sans reconstruire Armature. ItemsAdder conserve son propre flux de génération de pack complet. Chaque opération signale les étapes écoulées et conserve l’état de temps d’exécution connu bon lorsque son candidat échoue. `/armature help reload` explique les cibles. L’autocomplétion les liste.

Les opérations de recharge ne sont plus disponibles sous `config`, `profile` ou `diagnostics`. `/armature diagnostics startup` signale l’état et le temps de l’opération ; `/armature diagnostics pack` signale l’état du pack sans le modifier.

Permission : `armature.command.reload`.

## `/armature config`

```text
/armature config validate
/armature config migrate <file> [--output <file>] [--local|--ai]
/armature config restore-defaults [--confirm]
```

`validate` signale les résultats de la configuration sans appliquer les modifications.

`migrate` lit un profil legacy par rapport à `plugins/Armature/profiles/` et écrit un candidat validé séparé, par défaut `<name>.migrated.yml`. Il ne remplace jamais la source ni ne recharge la présentation active. `--local` et `--ai` sont des drapeaux de compatibilité : les deux utilisent le convertisseur déterministe local, sans appels réseau. Les fichiers source et candidat ont une limite interne fixe de 256 KiB. Il n’y a pas d’options de migration dans `config.yml`. Consultez [Mise à niveau vers 2.0.0](upgrading.md) avant d’activer la sortie.

`restore-defaults` affiche d’abord une prévisualisation. Il écrase les fichiers correspondants provenant de `default-assets/` intégré uniquement après que l’expéditeur identique ait répété la commande avec `--confirm`. Exécutez ensuite la commande de recharge explicite correspondant aux fichiers modifiés.

Autorisation : `armature.command.reload` pour `validate` et `restore-defaults` ; `armature.command.migrate-config` pour `migrate`.

## `/armature profile`

```text
/armature profile list [player]
/armature profile inspect <player>
/armature profile enable <player> <profile>
/armature profile disable <player> <profile>
```

Sans joueur, `list` affiche les identifiants de profil configurés. Avec un joueur en ligne, il affiche les exclusions de profil gérées par commande. `inspect` est en lecture seule et signale la résolution en direct observée par le serveur. Il ne prétend pas que le client connecté a rendu le résultat.

`enable` et `disable` gèrent l’exclusion de profil basée sur l’UUID persistante. Les exclusions basées sur les autorisations restent prioritaires par rapport à l’état géré par commande.

Autorisation : `armature.command.profile`.

## `/armature animation`

```text
/armature animation play <player> <model> <animation>
/armature animation stop <player>
```

`play` accepte tout modèle chargé par le moteur de rendu sélectionné (`.bbmodel` ou natif `.armature`, en utilisant son ID de modèle) et un nom de clip exact. Le joueur cible doit être en ligne. Ni un modèle actif correspondant ni un profil pour le modèle demandé n’est requis. Pendant la lecture, le modèle demandé remplace temporairement la présentation de l’objet tenu, conservant l’offset de modèle, le taux de balancement et la visibilité de la main principale du profil. Sans profil d’objet tenu, les présentations par défaut s’appliquent. La vue temporaire utilise uniquement le clip demandé, donc les clips, conditions et couches additives du modèle précédent ne peuvent pas bloquer la lecture. Les modèles ou clips manquants sont rejetés avant de remplacer la vue actuelle. Une fois l’animation terminée, Armature restaure le profil pour l’objet actuellement tenu, y compris lorsque le joueur a changé d’objets pendant la lecture. `stop` annule l’action active et la boucle directe du cible, puis permet à l’état de présentation normal de reprendre.

Autorisation : `armature.command.play`.

## `/armature editor`

```text
/armature editor open
/armature editor close [--confirm]
/armature editor reset [item|profile] [--confirm]
/armature editor export [item|profile]
/armature editor undo
/armature editor redo
/armature editor set <bone> <pos|rot> <x> <y> <z>
/armature editor diff [item|profile]
/armature editor mirror <left|right>
```

Bare `/armature editor` est le raccourci pour `editor open`. L’éditeur exige qu’un joueur tienne un objet avec un profil Armature actif et des os de modèle modifiables. Son dialogue expose des curseurs ainsi qu’un état `temporary preview` visible. `editor set` permet une entrée numérique exacte (pixels ou degrés) lorsque le client ne propose pas de saisie de texte dans le dialogue.

`undo` et `redo` conservent un historique de session limité (50 modifications). `diff` imprime la base de modèle autorisée, le `temporary preview` non sauvegardé et le `item-override` ou `profile-override` persisté. `mirror left` copie les valeurs correspondantes dans les os de gauche ; `mirror right` fait l’inverse et miroite les signes de position/rotation sur l’axe X.

`reset` imprime un aperçu destructif `item-override`/`profile-override` et nécessite `--confirm`. Il supprime les valeurs persistantes pour cette portée et rafraîchit la présentation en direct. `close` bloque tant que le `temporary preview` est sale. Répétez-le avec `--confirm` pour éliminer ces valeurs non sauvegardées. `export` émet un fragment YAML prêt pour le profil pour `editor-overrides.yml`, inclut le récapitulatif sale et n’écrat jamais la configuration du profil source.

Les trois couches de valeur sont explicites dans les messages de l’éditeur : `temporary preview` est en direct et non sauvegardé, `item-override` est persisté pour une portée objet/profil, et `profile-override` est persisté pour chaque objet résolu à ce profil. Sauvegarder une surcharge ou appliquer un aperçu ne modifie que la présentation. Les fichiers de profil source ne sont jamais mutés par l’éditeur.

Permission : `armature.command.editor`.

## `/armature diagnostics`

```text
/armature diagnostics doctor
/armature diagnostics performance
/armature diagnostics pack
/armature diagnostics startup
/armature diagnostics export
```

`doctor` génère un rapport de prévol avec des codes de découverte stables lorsque disponibles. `performance` conserve la barre de chef de performance en direct existante limitée aux joueurs. `pack` signale l’état du pack généré et de la propriété sans reconstruire ou synchroniser les actifs. Utilisez `/armature reload pack`, `/armature reload models` ou `/armature reload providers` pour ces opérations. `startup` signale l’état de l’opération en direct et les temps de stage. `export` programme immédiatement un bundle de support anonymisé et borné hors du thread, puis signale le chemin relatif `plugins/Armature/diagnostics/armature-support-<timestamp>.zip` lorsque le déplacement de l’archive atomique est terminé. Les valeurs de configuration contenant des identifiants ou des points de terminaison, des identifiants de joueur, des adresses IP et des lignes de journal non pertinentes sont masquées. L’exportation ne jamais effectue de I/O d’archive synchrone dans le gestionnaire de commande.

Permission: `armature.command.status` pour `doctor`, `pack` et `export` ; `armature.command.performance` pour `performance`.

## `/armature debug`

```text
/armature debug native-probe <model> [animation]
/armature debug stress <entity-count> <model-name> <animation>
/armature debug stress clear
```

Exécute l’explorateur de modèle statique natif généré existant. La mise en œuvre conserve également son comportement de préparation du catalogue lorsque aucun modèle n’est fourni, afin que les flux de travail des opérateurs existants ne régressent pas. Les noms de modèle natif et d’animation sont complétés à partir du catalogue chargé.

`stress` génère une grille carrée de jusqu’à 1 024 modèles natifs temporaires devant le joueur qui lance la commande. Chaque modèle boucle l’animation sélectionnée et est visible uniquement par ce joueur. Une grille expire après dix minutes, est effacée lorsque son propriétaire se déconnecte, ou peut être supprimée immédiatement avec `stress clear`. Cette action de débogage nécessite le moteur de rendu natif.

Permission: `armature.command.native-probe`.

## Formes de commande de compatibilité

Les formes obsolètes exécutent toujours leur opération existante, puis émettent un avertissement indiquant la forme canonique. Leurs arguments sont conservés dans l’avertissement.

| Forme obsolète | Forme canonique |
| --- | --- |
| `/armature perf` | `/armature diagnostics performance` |
| `/armature regenerate-defaults` | `/armature config restore-defaults` |
| `/armature migrate-config <file> [--output <file>]` | `/armature config migrate <file> [--output <file>]` |
| `/armature play <player> <model> <animation>` | `/armature animation play <player> <model> <animation>` |
| `/armature native-probe <model> [animation]` | `/armature debug native-probe <model> [animation]` |

`performance` reste accepté comme une orthographe supplémentaire de la forme obsolète `perf` pour la compatibilité avec les habitudes des opérateurs plus anciens.

## Permissions de désactivation par joueur

Les commandes globales `enable` et `disable` affectent le rendu en cours d’exécution. Pour exclure un joueur sans modifier l’état global du rendu, accordez l’une de ces permissions via un plugin de gestion des permissions :

- `armature.disabled` désactive la présentation Armature pour chaque profil ;
- `armature.disabled.<profile-id>` désactive uniquement le profil correspondant.

Par exemple :

```text
/lp user Alice permission set armature.disabled.m4a1 true
```

Les exclusions basées sur les permissions prévalent sur `/armature profile enable` et sont prises en compte au prochain cycle de joueur. Elles ne modifient ni les objets tenus ni l’état du jeu.

## Récapitulatif des permissions

| Permission | Portée | Valeur par défaut |
| --- | --- | --- |
| `armature.command` | Accès à la commande racine | op |
| `armature.command.status` | État, médecin, pack, exportation des diagnostics | op |
| `armature.command.performance` | Performance de la barre de boss | op |
| `armature.command.reload` | Opérations explicites de configuration/profil/schéma/pack/fournisseur, validation de la configuration, restauration des valeurs par défaut | op |
| `armature.command.toggle` | Activer/désactiver le temps d’exécution | op |
| `armature.command.play` | Lecture/arrêt de l’animation | op |
| `armature.command.editor` | Opérations d’édition | op |
| `armature.command.profile` | Opérations de profil | op |
| `armature.command.migrate-config` | Migration du profil hérité | op |
| `armature.command.native-probe` | Commandes de débogage d’Armature | op |
| `armature.disabled` | Refus du joueur | false |


Contrôle de la génération du pack : `/armature reload pack` et `/armature reload all` génèrent explicitement même lorsque les déclencheurs automatiques sont désactivés. Les rechargements de configuration/profil/squelette respectent `resource-pack.generation.on-reload`. `/armature diagnostics pack` signale les options de génération publiées, les conclusions de la validation, la taille et la durée. `/armature reload providers` demande la synchronisation avec le fournisseur existant. Il ne contourne pas les paramètres de génération d’Armature.
