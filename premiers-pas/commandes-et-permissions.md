# 📟 Commandes et permissions

## `/armature reload`

Recharge `config.yml`, tous les profils et les assets de modèles BetterModel. Les rigs actifs des joueurs sont supprimés puis synchronisés à nouveau. Si la validation des profils échoue, Armature conserve les profils actifs précédents.

Permission : `armature.admin`

## Notes opérationnelles

* Lancez le rechargement après toute modification du YAML ou des noms de modèles du resource pack.
* Un redémarrage du serveur est requis après toute modification des dépendances du plugin ou du fichier JAR.
* N’utilisez pas `/reload` ; utilisez `/armature reload` pour nettoyer l’état géré par le plugin.

***

## `/armature perf`

Affiche une bossbar contenant les statistiques de performance en temps réel du plugin.

Permission : `armature.admin`
