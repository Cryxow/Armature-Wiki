---
description: "Diagnostics et performances"
---

# 📊 Diagnostics et performances

Utilisez la référence de commande pour les arguments exacts et les permissions. Commencez par :

```text
/armature status
/armature diagnostics doctor
/armature config validate
```

La référence de commande comprend l’inspection du profil/modèle/animation, les diagnostics de mouvement, les contrôles de l’éditeur d’os et les paramètres du moteur de rendu client. Utilisez l’aide pour la version installée plutôt que de deviner un ancien alias.

## Collecter un rapport

Inclure le logiciel serveur/Minecraft/Java, Armature/backend, les intégrations activées, le mod client/protocole, la carte graphique/Sodium/Iris/shaderpack, la main échouant et les étapes exactes. Joindre les avertissements de démarrage/génération, la sortie du docteur/inspection, le profil minimal plus les variables partagées, ainsi que le modèle source et le reçu/empreinte du pack fournisseur si disponibles. Supprimer les identifiants d’hébergement privé avant de partager les journaux.

Séparer quatre observations : l’acceptation du parseur, la génération du modèle/pack, la complétion/délivrance du fournisseur, et les visuels du client connecté. Une construction réussie ne dit rien sur le fait que le pack en cache de ce client a été remplacé.

## Ajustement des performances

Réduire le coût de la scène réelle : les parties de cube visibles/os, les modèles sortants concurrents, les instances d’attachement imbriquées, les transformations d’objet dynamiques et la résolution de texture. Utiliser les filtres d’os/canal additifs au lieu de mélanger des squelettes complets inutiles. Garder les intervalles de rafraîchissement de variables appropriés à leur source. Un placeholder interrogé à chaque tick n’est pas nécessaire pour les permissions statiques ou les changements de mode rares.

Restez dans les limites du modèle, de l’entité ou du bundle natif et mesurez avant d’augmenter les budgets. Diagnosez la génération du fournisseur séparément du coût du tick de rendu. Les téléchargements de bundles pour clients froids, les rechargements de ressources et le rendu en régime permanent sont des mesures différentes. Utilisez le profilateur du serveur sur une scène représentative, puis répétez la même scène après un seul changement.

Pour les problèmes de composition, inspectez la variante sélectionnée et chaque instance active. Les définitions d’accessoires réutilisables n’impliquent pas un état d’animation ou de physique partagé. Pour les problèmes de rechargement, vérifiez les reçus de génération correspondants et les actifs du fournisseur finaux avant d’accepter un dossier généré obsolète comme prêt.
