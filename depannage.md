---
description: "Dépannage"
---

# 🩺 Dépannage

Commencez par un profil minimaliste et un modèle connu chargé. Gardez le modèle exact, le profil, les versions serveur/client et le pack de ressources fournisseur final qui reproduisent le problème. Modifiez une seule variable à la fois.

| Symptôme | Vérifier d’abord | Étape suivante |
| --- | --- | --- |
| Aucune présentation | Runtime/joueur activé, identité de l’objet, profil sélectionné, modèle chargé, pack accepté | Inspection du profil, inspection du modèle, médecin |
| Texture manquante/rose | ZIP fournisseur final exact, définitions du chemin/namespaces et du pack cible | [Packs de ressources](configuration/resource-packs.md) |
| Animation incorrecte | Nom exact du clip sensible à la casse, condition/règle de déclencheur, action protégée et délai de recharge | [Règles d’action](premiers-pas/profils/actions.md) |
| Mouvement incorrect | Règle de correspondance la plus spécifique, ordre de liaison des déclarations, actifs de boucle/début/fin | [Règles de mouvement](premiers-pas/profils/movement.md) |
| Objet/bras déplacé | Affectation de rôle, pivots, clip de main miroir, surcharge d’os sauvegardée | [Rôles d’os](premiers-pas/modeles/bone-roles.md), [éditeur](premiers-pas/in-game-editor.md) |
| Le modèle disparaît à l’intérieur des blocs | Anti-cassure et chemin exact du shader/graphismes | [Rendu et Caméra](configuration/rendering-camera.md) |
| Rotation de la caméra absente | Rôle/élément de la caméra, rotation de la caméra activée et graphismes du client pris en charge | Rendu et Caméra |
| Peau/armure incorrecte | Rôle classique/maigre, équipement du joueur, mappage de l’armure et chemin de texture | [Peaux et Armure personnalisée](avance/armures-personnalisees.md) |
| Accessoire manquant | Backend natif, ID d’accessoire/os référencé, condition, profondeur/budget d’entités | [Accessoires](premiers-pas/profils/attachments.md) |
| Le rendu du client change de manière inattendue | Préférence/état de préparation du client, solution de repli composite, rechargement du pack en attente | [Rendu du client et solution de repli](mod-client/rendering-fallback.md) |
| `.armature` rejeté | Strict v2, sauvegarde du plugin normale, rôles dupliqués/invalides, diagnostics de l’exportateur | [Sauvegarde & Déploiement](premiers-pas/modeles/saving-deploying.md) |

## Un test court et reproductible

1. Exécutez `/armature diagnostics doctor`. Corrigez les erreurs avant d’ajuster les cosmétiques.
2. Validez la configuration sans l’appliquer : `/armature config validate`.
3. Inspectez l’objet tenu/profil et le modèle en utilisant les commandes dans [Diagnostics](depannage/diagnostics.md).
4. Reconstruisez les actifs Armature/fournisseur et acceptez le pack final du fournisseur.
5. Testez après un redémarrage complet du serveur si nécessaire. Un Bukkit `/reload` ne prouve pas un démarrage propre ou un changement de moteur de rendu/dépendance.
6. Retestez avec le rendu client désactivé si possible. Notez quel chemin échoue.

Ne corrigez pas un échec de pack en modifiant un matcheur d’objet déjà correct. Ne supprimez pas une action protégée simplement pour cacher un signal de rechargement/fin perdu. Repérez la première étape incorrecte dans l’identité de l’objet → sélection → modèle → pack → rendu.
