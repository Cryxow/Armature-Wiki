---
description: "Accessoires et modèles composites"
---

# 🧩 Accessoires et modèles composites

Une composition fixe des modèles indépendants sur des os d’un modèle principal. Elle diffère de l’activation d’os existants via une configuration d’accessoire pour plugin de jeu. Le moteur de rendu natif gère cette présentation. L’état du jeu reste avec WeaponMechanics ou votre propre plugin.

```yaml
charm_star:
  type: attachment
  model:
    name: charm_star
  physics:
    enabled: true
    frequency: 3.0
    damping: 0.8
  animations:
    movement:
      - animation: idle
    actions:
      - trigger: weaponmechanics:fire
        animation: flash

scope_long:
  type: attachment
  model:
    name: scope_long
  physics:
    enabled: false
```

Les accessoires statiques peuvent omettre des animations. Chaque référence crée des canaux d’animation, des temps de recharge et des ressorts physiques indépendants. Les profils réutilisables peuvent déclarer leurs propres variantes et accessoires enfants. Ils ne sont jamais sélectionnés par les objets tenus.

## Montages et variantes

```yaml
m4a1:
  match:
    item: weaponmechanics:m4a1
    when: variable.player-level >= 10
  model:
    name: arm_rifle
  attachments:
    scope:
      profile: scope_long
      attach-to: {parent: base, bone: scope_socket}
      when: weaponmechanics.has-attachment:scope_long == true
    charm:
      profile: charm_star
      attach-to: {bone: charm_socket}
      transform:
        position: [0, 0, 0]
        rotation: [0, 0, 0]
        scale: [1, 1, 1]
      when:
        permissions:
          any: [cosmetics.charm.star, cosmetics.admin]
        all:
          - variable.charm-unlocked == true
  animations:
    movement:
      - animation: base
      - when:
          - weaponmechanics.aiming == true
          - attachment.scope.active == false
        animation: aim
      - when:
          - weaponmechanics.aiming == true
          - attachment.scope.active == true
        animation: aim_scope
    actions:
      - trigger: weaponmechanics:reload
        animation:
          name: reload
          can-be-cancelled: false
  variants:
    gold:
      when:
        permissions:
          any: [cosmetics.rifle.gold, cosmetics.admin]
        all:
          - variable.weapon-skin == gold
      model: {name: arm_rifle_gold}
      attachments:
        charm:
          model: {name: charm_star_gold}
          physics: {damping: 0.9}
```

Tous les actifs nommés, os et clips dans ces exemples doivent exister dans vos modèles. `base` nomme le modèle principal du profil actuel. Le parent d’un montage peut également être une instance ID de frère. Les profils imbriqués utilisent leurs propres IDs d’instance locaux. Les diagnostics affichent des chemins tels que `charm/ribbon`. L’inactivité du parent cache les descendants. Les parents sont évalués et montés avant les enfants, indépendamment de l’ordre de déclaration.

La position utilise des unités de Blockbench (16 par bloc), la rotation utilise des degrés et l’échelle utilise trois facteurs positifs. La géométrie est montée dans ses coordonnées locales du modèle auteur, sans le décalage de scène joueur du compilateur natif. La pose finale animée/physique du parent est composée une fois avant l’animation/physique propre de l’enfant. La simulation suit les os physiques marqués de l’actif et reste un effet de ressort sans collisions. Les paramètres physiques omis héritent de la configuration globale.

`match.when` déclenche l’activation. La première variante correspondante dans l’ordre YAML gagne. Aucune correspondance n’utilise la présentation de base. Les variantes ne s’empilent pas. `variant.active` est `base` ou l’ID de la variante choisie dans le profil actuel. `attachment.<id>.active` décrit ses montages directs et est disponible pour les conditions d’animation.

Les conditions de sélection utilisent des états externes et des variables partagées. Les conditions variante/correspondance ne peuvent pas dépendre des résultats de sélection ou d’animation. Les conditions de montage peuvent utiliser le `variant.active` choisi, mais pas l’état actif d’un autre montage. Les autres conditions d’animation conservent le langage existant.

Le raccourci `permissions` accepte des listes non vides de `any`, `all`, `none`. Les groupes multiples et autres critères dans un bloc de condition sont combinés avec ET. Les expressions `player.has-permission:<permission>` existantes continuent de fonctionner.

## Surcargues et cycle de vie

Applique la base réutilisable → sa variante sélectionnée → les surcargues d’instance. Les champs modèle/physique et les entrées d’accessoire fusionnent par clé. Chaque catégorie d’animation fournie remplace cette liste entière. Les catégories omises héritent. Chaque montage nécessite `profile` et `attach-to.bone` après fusion. Les surcargues d’instance peuvent remplacer son modèle, sa physique, ses animations et ses enfants. `enabled: false` désactive un montage hérité. Modifier le `profile` d’une instance applique ses surcargues à la nouvelle référence.

Les changements attendent que la présentation contienne une action non annulable. À la fin, réévaluez les conditions et appliquez la composition demandée la plus récente. Les changements d’objet, les déconnexions, la désactivation et le rechargement de profil/modèle nettoient les instances. Les vérifications d’accessoire WeaponMechanics utilisent l’objet de la main principale propriétaire. Le jeu reste dans WM.

Toutes les références des liens sont chargées avant l’activation. Les variables/références inconnues, les ID dupliqués, les cycles de profil/monture, les champs invalides et les fonctionnalités de backend non supportées rejettent la bibliothèque. Les erreurs de rechargement conservent la bibliothèque active. La validation des actifs natifs vérifie toutes les variantes et les surcharges avant la publication du pack, y compris les os de monture, les clips et les références additives. Les conditions ne reconstruisent jamais un pack.

`/armature profile inspect <player>` inclut la variante choisie et les chemins de modèle actifs. Les contrôles automatisés doivent être suivis d’un redémarrage complet du serveur, du téléchargement du pack et de la validation par le client connecté des deux mains, de la visée, des montures enchaînées, de la physique et des modifications pendant les actions protégées.

## Compatibilité du moteur de rendu

La composition/physique par instance nécessite le backend natif. Le bundle de main actuel de Fabric n’est pas un transport de scène composite, donc une présentation composée utilise les ItemDisplays natifs du serveur et son pack de ressources requis. Supprimer la composition peut restaurer une présentation compatible pour le rendu client. Ne décrivez pas ce repli du serveur comme un bug de modèle manquant.

## Exemple d’accessoire statique

```yaml
wiki_charm:
  type: attachment
  model:
    name: wiki_charm
  physics:
    enabled: true
    frequency: 3.0
    damping: 0.9

wiki_composite:
  match:
    item: minecraft:stick
  model:
    name: wiki_player
  attachments:
    charm:
      profile: wiki_charm
      attach-to:
        bone: charm_socket
      when:
        permissions:
          any: [cosmetics.charm, cosmetics.admin]
      transform:
        position: [0, 0, 0]
        rotation: [0, 0, 0]
        scale: [1, 1, 1]
```

Fournir `wiki_charm` et l’os `charm_socket` du modèle principal. Accorder la permission, reconstruire/délivrer les actifs et inspecter le chemin d’instance actif. Mettre un drapeau Physique sur l’os de l’accessoire pour rendre son ressort activé visible.
