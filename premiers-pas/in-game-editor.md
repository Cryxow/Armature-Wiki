---
description: "Éditeur d’os en jeu"
---

# 🎛️ Éditeur d’os en jeu

L’éditeur modifie les décalages de présentation sans modifier votre modèle source ou votre profil. Tenez un objet avec un profil actif et exécutez `/armature editor` avec la permission `armature.command.editor`.

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

`/armature editor` est le raccourci pour `editor open`. L’éditeur nécessite qu’un joueur tienne un objet avec un profil Armature actif et des os de modèle modifiables. Son dialogue expose des curseurs plus un état `temporary preview` visible. `editor set` permet une entrée numérique exacte (pixels ou degrés) lorsque le client ne propose pas de saisie de texte dans le dialogue.

`undo` et `redo` conservent un historique de session borné (50 modifications). `diff` affiche la base du modèle écrit, le `temporary preview` non sauvegardé, et le `item-override` ou le `profile-override` persisté. `mirror left` copie les valeurs correspondantes dans les os de gauche ; `mirror right` fait l’inverse et inverse les signes de position/rotation sur l’axe X.

`reset` affiche un aperçu destructif `item-override`/`profile-override` et nécessite `--confirm`. Il supprime les valeurs persistantes pour cette portée et rafraîchit la présentation en direct. `close` bloque tant que le `temporary preview` est sale. Répétez-le avec `--confirm` pour éliminer ces valeurs non sauvegardées. `export` émet un fragment YAML prêt pour le profil pour `editor-overrides.yml`, inclut le récapitulatif sale, et n’écrit jamais la configuration source du profil.

Les trois couches de valeurs sont explicites dans les messages de l’éditeur : `temporary preview` est en direct et non sauvegardé, `item-override` est persisté pour une portée objet/profil, et `profile-override` est persisté pour chaque objet résolu à ce profil. Sauvegarder une surcharge ou appliquer un aperçu modifie uniquement la présentation. Les fichiers de profil source ne sont jamais mutés par l’éditeur.

Permission : `armature.command.editor`.

## Surcharges stockées

Les opérateurs avec `armature.command.editor` peuvent exécuter `/armature editor` tout en tenant un objet assigné à un profil. Le dialogue sélectionne un os du modèle éditable et expose des curseurs de position et de rotation. L’application d’une modification met à jour tous les joueurs en ligne utilisant cet objet ou cette plage de profil.

Les surcharges sont persistées séparément dans `plugins/Armature/editor-overrides.yml` :

```yaml
version: 2
profiles:
  m4a1:
    bones:
      root:
        position: [0.0, 1.0, 0.0]
        rotation: [0.0, 2.0, 0.0]
items:
  m4a1:
    weaponmechanics:m4a1:
      bones:
        root:
          position: [0.0, 2.0, 0.0]
          rotation: [0.0, 0.0, 0.0]
```

Les valeurs de profil sont héritées en premier, les valeurs d’objet les surclassent, et les valeurs en direct non enregistrées ont la priorité la plus élevée. `Save item` affecte uniquement l’objet tenu ; `Save profile` affecte tous les objets résolus au profil. Ces corrections modifient uniquement la présentation et ne changent ni le gameplay ni l’état de l’inventaire. Les valeurs de position utilisent les unités du modèle Blockbench (pixels) et sont converties en blocs dans l’espace du modèle avec `/16`. Les valeurs de rotation utilisent des degrés dans la plage usuelle `-180..180`.

## Vérifier une correction

Utiliser `editor diff` avant de sauvegarder. Sauvegarder dans la plage d’objet pour une seule identité ou dans la plage de profil pour chaque objet correspondant, puis rouvrir l’éditeur et confirmer la persistance. Tester les deux mains, l’équipement/déséquipement et tous les joueurs partageant cette plage. L’exportation est un fragment YAML pour le fichier de surcharge, pas un projet `.armature` sauvegardé.
