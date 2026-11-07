---
description: "Exemples complets"
---

# 🧪 Exemples complets

Chaque exemple est un fichier de profil complet, pas un fragment pour `config.yml`. Activez un seul exemple correspondant à la fois et supprimez les profils par défaut ou de tutoriel qui se chevauchent. Les IDs racine doivent être uniques dans votre bibliothèque active.

| Exemple | Modèles/clip requis |
| --- | --- |
| Épée | Livré avec `arm_sword` : base, frappe, équipement, déséquipement |
| Les mains vides | Livré avec `arm_empty` : base |
| Arc | Auteur de `wiki_player` : base, vise, os rôle d’objet |
| Bouclier | Auteur de `wiki_player` : base, blocage, os rôle d’objet |
| Mécanique d’arme/bras double | `arm_rifle` avec base, vise, tir, rechargement comme utilisé. Titre réel de l’arme WM `m4a1` |
| Accessoire | `wiki_player` avec charm_socket, `wiki_charm` avec un os Physics, autorisation |

Le [modèle de tutoriel](../../.gitbook/assets/examples/wiki_player.armature) contient ces clips/déclencheurs de démonstration. Pour votre propre modèle, le tutoriel explique l’auteur ; `aim`/`block`/`charm_socket` doivent exister si vous utilisez ces exemples. Un analyseur acceptant YAML ne crée pas ses clips ou os manquants. Le titre d’arme fournisseur est un exemple, pas une arme de jeu fournie. Les conditions et définitions de pack se résolvent par la main propriétaire.

## Épée

[Télécharger le profil](../../.gitbook/assets/examples/sword.yml)

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

## Les mains vides

[Télécharger le profil](../../.gitbook/assets/examples/empty-hands.yml)

```yaml
wiki_empty:
  match: {item: minecraft:air}
  model: {name: arm_empty, hide-vanilla-hand: true}
  animations:
    movement:
      - animation: base
```

## Arc

[Télécharger le profil](../../.gitbook/assets/examples/bow.yml)

```yaml
wiki_bow:
  match: {item: minecraft:bow}
  model:
    name: wiki_player
    held-item: {mode: source}
  animations:
    movement:
      - animation: base
      - when: item.using == true
        animation: aim
```

## Écu

[Télécharger le profil](../../.gitbook/assets/examples/shield.yml)

```yaml
wiki_shield:
  match: {item: minecraft:shield}
  model:
    name: wiki_player
    held-item: {mode: static}
  animations:
    movement:
      - animation: base
      - when: item.using == true
        animation: block
```

## Mécaniques des armes

[Télécharger le profil](../../.gitbook/assets/examples/weaponmechanics.yml)

```yaml
wiki_rifle:
  match: {item: weaponmechanics:m4a1}
  model: {name: arm_rifle}
  animations:
    movement:
      - animation: base
      - when: weaponmechanics.aiming == true
        animation: aim
    actions:
      - trigger: weaponmechanics:fire
        animation: {name: fire, cooldown: 3t}
      - trigger: weaponmechanics:reload-start
        animation: {name: reload, can-be-cancelled: false}
```

## Deux bras

[Télécharger le profil](../../.gitbook/assets/examples/two-arms.yml)

```yaml
wiki_two_arms:
  match: {item: weaponmechanics:m4a1}
  model: {name: arm_rifle}
  animations:
    movement:
      - animation: {name: base, arms: both}
    actions:
      - trigger: weaponmechanics:reload-start
        animation: {name: reload, arms: both, can-be-cancelled: false}
```

## Accessoire

[Télécharger le profil](../../.gitbook/assets/examples/accessory.yml)

```yaml
wiki_charm:
  type: attachment
  model: {name: wiki_charm}
  physics: {enabled: true, frequency: 3.0, damping: 0.9}

wiki_composite:
  match: {item: minecraft:stick}
  model: {name: wiki_player}
  attachments:
    charm:
      profile: wiki_charm
      attach-to: {bone: charm_socket}
      when:
        permissions:
          any: [cosmetics.charm, cosmetics.admin]
```

[Téléchargez le modèle statique du pendentif avec physique](../../.gitbook/assets/examples/wiki_charm.armature) pour l’exemple d’accessoire. Copiez-le à côté de `wiki_player.armature` et accordez `cosmetics.charm` au joueur de test. Le serveur simule l’os marqué du pendentif. Ce mouvement ne s’affiche pas dans Blockbench.

## Appliquer et vérifier

Enregistrer sous `plugins/Armature/profiles/`, recharger les profils et les modèles, reconstruire les actifs d’Armature et livrer le pack final du fournisseur. Utilisez l’inspection de profil pour confirmer l’identité, la variante et les instances choisies. Testez les actions ordinaires, le rechargement protégé, l’équipement/déséquipement et les deux mains avant d’adapter le fichier.
