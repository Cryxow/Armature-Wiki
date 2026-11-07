---
description: "Main principale et main secondaire"
---

# 👐 Main principale et main secondaire

Armature maintient un contexte de rendu pour chaque main. Les sélecteurs,
les identifiants fournis par les plugins et les conditions sur l’objet tenu
sont évalués pour la main concernée. Si le fournisseur indique la main
dans l’événement, Armature utilise cette information pour l’acheminer.

## Animer les deux bras

Les animations de mouvement et d’action acceptent `arms: one` par défaut
ou `arms: both`. Une règle de la main principale avec `both` peut prendre
en charge les deux bras pendant le mouvement ou l’action. Les couches
additives ne prennent pas en charge ce réglage.

```yaml
animations:
  actions:
    - trigger: weaponmechanics:reload
      animation:
        name: reload
        arms: both
        can-be-cancelled: false
```

Activez cette option avec un modèle et un clip conçus pour les deux bras.
En `.armature` v2, créez les os, clips et cibles additives du côté gauche.
Les anciens modèles `.bbmodel` suivent leurs propres conventions pour la
main secondaire.

## Affichage des objets lors d’un changement

`render.dynamic-items.defer-until-equipped: true` conserve les objets
du modèle sortant pendant l’animation de rangement ou une présentation
protégée. Le modèle entrant reçoit les objets actuels au lancement de
l’animation de prise en main. La sélection dans l’inventaire et les actions
de jeu restent immédiates. Cette règle s’applique aux moteurs serveur et
au rendu Fabric.

## Vérifier les deux mains

Testez séparément les objets et les actions d’attaque ou d’utilisation de
chaque main. Testez une action protégée à deux bras, des changements
rapides de case et un passage à un objet ordinaire suivi d’un retour au
profil initial. Avec le mod client, vérifiez aussi qu’il prend le relais
après avoir accepté le bundle et que les éléments de l’ancien rendu sont
supprimés. Le [guide du repli côté serveur](../../mod-client/rendering-fallback.md)
explique pourquoi certaines compositions utilisent les entités d’affichage
du serveur.
