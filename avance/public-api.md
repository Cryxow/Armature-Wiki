---
description: "Présentation de l’API développeur"
---

# 💻 Présentation de l’API développeur

Utilisez `com.armaturemc.api` pour demander une présentation à un plugin qui gère le gameplay. Aucune importation de BetterModel ou d’Armature n’est requise. L’API est fournie par le plugin Armature activé. Les demandes ne créent pas d’armes, de munitions, de dégâts ou de permissions de gameplay.

La source 2.0.0 signale `API_VERSION = "2.0.0"` et `API_MAJOR_VERSION = 1`. La version majeure du produit et la version majeure du contrat API sont différentes : l’API conserve la compatibilité de source au sein de la version majeure 1 du contrat. Les surcharges de présentation autonome nécessitent l’API/runtime 2.0.0 correspondantes. Elles ne figurent pas dans l’artifact publié 1.3.0.

Commencez par [Setup](public-api/setup.md), puis choisissez [Animations & Presentations](public-api/animations.md), [Signals](public-api/signals.md) ou [Providers](public-api/providers.md). [Examples](public-api/examples.md) fournit le code source Java complet.

Les opérations de joueur retournent `CompletableFuture` et sont planifiées sur le planificateur du joueur. Un résultat confirme l’acceptation ou le rejet, pas la complétion visuelle. Ne bloquez jamais le thread du joueur en attendant une future. Utilisez les événements du cycle de vie de l’animation pour la complétion finie. Planifiez le suivi de gameplay sur le planificateur du joueur avant de toucher à l’état Bukkit.
