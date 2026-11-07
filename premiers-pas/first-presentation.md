---
description: "Votre première animation en jeu"
---

# 🚀 Votre première animation en jeu

Ce tutoriel utilise le modèle `models/default/arm_sword.bbmodel` fourni avec
Armature et ses clips `base`, `swing`, `equip` et `unequip`. Le moteur
natif par défaut lit ce modèle `.bbmodel` hérité. Vous pouvez faire le test
avec le modèle fourni, avant d’installer l’extension Blockbench.

## 1. Réserver l’objet au profil de test

Utilisez un serveur de test ou faites une sauvegarde. Le profil fourni pour
la famille des épées reconnaît aussi l’épée en diamant. Déplacez son fichier
`.yml` hors de `profiles/` pour que seul le profil du tutoriel reconnaisse
cet objet.

## 2. Écrire le profil

Enregistrez ce profil dans `plugins/Armature/profiles/wiki-sword.yml`.

```yaml
wiki_sword:
  match:
    item: minecraft:diamond_sword
  model:
    name: arm_sword
    hide-vanilla-hand: true
  animations:
    movement:
      - animation: base
    actions:
      - trigger: player:attack
        animation:
          name: swing
          cooldown: 3t
      - trigger: item:equip
        animation: equip
      - trigger: item:unequip
        animation: unequip
```

La clé racine `wiki_sword` est l’identifiant du profil. `arm_sword` est
le nom de fichier du modèle, sans dossier ni extension.

## 3. Charger le profil et distribuer le pack

Exécutez `/armature reload all`. Si un fournisseur externe gère le pack,
utilisez sa procédure de génération ou de rechargement, puis lancez
`/armature reload providers` si nécessaire. Distribuez le pack final du
fournisseur au joueur. Sans fournisseur, activez la génération du ZIP
autonome. Consultez [Packs de ressources](../configuration/resource-packs.md)
pour l’hébergement et la distribution.

## 4. Tester les animations

Prenez une épée en diamant dans la main principale. Le bras doit jouer
`base`, et une attaque doit déclencher `swing`. Quand vous prenez ou
rangez l’objet, vérifiez que les clips correspondants se lancent. Utilisez
`/armature profile inspect <player>` pour confirmer le profil sélectionné
et `/armature status` pour confirmer l’usage du moteur natif.

Si le modèle n’apparaît pas, vérifiez le profil, les diagnostics des modèles
et des clips, et le pack reçu avant de modifier les transformations. Voir
[Dépannage](../depannage.md).

Après le test, retirez le profil du tutoriel, remettez le profil d’origine et
rechargez. Poursuivez avec [Créer un modèle](modele-blockbench.md) ou les
[règles de mouvement](profils/movement.md).
