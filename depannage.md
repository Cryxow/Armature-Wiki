# 🔭 Dépannage

## Le plugin se désactive au démarrage

Vérifiez que BetterModel est installé et activé avant Armature. BetterModel est une dépendance de rendu obligatoire.

## ModelEngine provoque des problèmes

C’est en réalité BetterModel qui en est la cause. Ajoutez `meg:` devant toute mécanique ModelEngine dans MythicMobs.

## Modèle invisible

Vérifiez `render.enabled: true`, l’identité de l’item du profil et l’ID `model` BetterModel. Vérifiez que l’asset du modèle est chargé et que le joueur a appliqué le resource pack.

## L’animation ne se joue pas

Comparez le nom d’animation du profil avec le nom exact de l’animation BetterModel. Consultez les avertissements au démarrage et au rechargement pour les animations manquantes. Pour les actions fournies par un plugin, vérifiez que le fournisseur est activé et que son identité d’item correspond à la valeur `item` du profil.

## Le mouvement est trop fort

Réduisez le `*-gain`, l’amplitude ou `sway.maximum-rotation` concerné. Le suivi caméra agit sur la position ; le balancement agit sur la rotation. Réglez une couche à la fois.

## Le tracker existe mais cesse de se mettre à jour

Activez `debug.enabled` et `debug.tracker-lifecycle`, reproduisez avec un joueur et vérifiez si le tracker est planifié et si une transition d’action/boucle s’est terminée. Cela nécessite des preuves sur serveur en direct ; une compilation réussie ne prouve pas la santé du tracker.

## La main vide affiche un item invisible ou du papier

Installez [PacketEvents](https://modrinth.com/plugin/packetevents), activez `debug.enabled` et `debug.empty-hand-mask`, reproduisez une fois et inspectez le slot brut sélectionné ainsi que la mutation d’inventaire serveur. `render.empty-hand-mask.repair-server-materialization` est une réparation expérimentale étroite pour le masque Armature reconnu.

## Rapport à joindre

Incluez la version d’Armature, la version Minecraft/Paper, la version BetterModel, la liste des fournisseurs, le profil concerné, `config.yml`, les avertissements de démarrage/rechargement et les logs de débogage d’un joueur concerné. Masquez les noms de joueurs si nécessaire.
