---
description: "Rendu côté client et repli côté serveur"
---

# 🖥️ Rendu côté client et repli côté serveur

Le serveur choisit les profils, variantes, clips, conditions, objets à
afficher, règles de visibilité et moments de déclenchement. Le client
dessine le rendu sélectionné à sa fréquence d’images. Le serveur reste
responsable du gameplay des armes et de la sélection des profils YAML.

## Passage au rendu client

1. Le client annonce son protocole et sa préférence de rendu.
2. Le serveur vérifie que le client est prêt et transmet le bundle du
   modèle et son état.
3. Le client charge les textures et la géométrie. Le premier chargement
   peut prendre du temps.
4. Le changement de moteur est coordonné avec le pack de repli.
   L’animation de prise en main attend que le modèle soit prêt à être
   dessiné.

Les bundles de modèles natifs sont transmis séparément des ressources
du pack commun. Les textures des objets personnalisés, les sons et les
polices restent dans le pack du fournisseur. Voir
[Packs de ressources](../configuration/resource-packs.md) pour la livraison
des packs séparés.

## Repli côté serveur

Le moteur du serveur reste actif si le mod est absent, si le protocole
est incompatible, si la préférence est désactivée ou si le rendu client
est indisponible. Un changement de moteur peut attendre le rechargement
des ressources requises. Gardez le pack de rendu serveur disponible,
même sur un serveur qui accepte le mod.

Le protocole actuel gère plusieurs canaux pour les mains et les rendus
sortants. Les scènes composites avec accessoires restent côté serveur.
Une connexion réussie du mod ne garantit donc pas le rendu client de
tous les profils.

Les générations de rendu de prise en main et de rangement conservent
leur état. Une réponse tardive ou un ancien ordre d’arrêt ne doit pas
supprimer une génération plus récente. Testez les changements rapides
de case, les deux mains et le changement de moteur pendant une action.

## Vérifier le repli

Connectez-vous avec le mod activé, avec sa préférence désactivée, puis
sans le mod. Testez un profil simple et un profil avec accessoire. Vérifiez
le modèle, les objets tenus, le skin, l’armure, l’acceptation du pack et
la fin des actions. Si des textures communes manquent malgré le chargement
du bundle de modèle, vérifiez la distribution du pack.
