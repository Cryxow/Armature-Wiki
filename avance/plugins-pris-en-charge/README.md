---
description: "Plugins pris en charge"
---

# 🔗 Plugins pris en charge

Les intégrations sont facultatives. Armature présente un objet. Son plugin propriétaire fournit l’identité, le gameplay et, le cas échéant, la livraison finale du pack de ressources.

| Intégration | Identité de l’objet | Événements/état | Pont de pack | Adaptateur de script/API |
| --- | --- | --- | --- | --- |
| [ItemsAdder](itemsadder.md) | `itemsadder:namespace:id` | Identité. Déclencheurs génériques d’objet/vanilla | Oui, ItemsAdder gère la livraison | — |
| [Nexo](nexo.md) | `nexo:id` | Identité. Déclencheurs génériques d’objet/vanilla | Oui, Nexo gère la livraison | — |
| [CraftEngine](craftengine.md) | `craftengine:namespace:id` | Identité. Déclencheurs génériques d’objet/vanilla | Oui, CraftEngine gère la livraison | — |
| [WeaponMechanics](weaponmechanics.md) | `weaponmechanics:weapon_title` | Tir, visée, rechargement, mode d’arme/arme et état de l’arme | Non, pas un fournisseur de pack | Événements de présentation et surcharges d’accessoires |
| [Skript](skript.md) | Utiliser l’identité existante de l’objet | Signaux émis par le script, cycle de vie de l’animation | Non | Effets/expressions/événements intégrés |
| [MythicMobs](mythicmobs.md) | Utiliser l’identité existante de l’objet | Signaux de compétence, cycle de vie de l’animation | Non | Mécanismes/déclencheurs intégrés |
| [Denizen](denizen.md) | Utiliser l’identité existante de l’objet | Signaux de script, cycle de vie de l’animation | Non | Commandes/événements intégrés |

[PlaceholderAPI](../../premiers-pas/profils/variables.md) fournit des valeurs pour les variables partagées. Ce plugin ne fournit ni objets ni packs de ressources.

Utilisez un sélecteur qui inclut l’espace de noms de l’objet. Créez les objets d’exemple et les armes dans leur plugin. Après la mise à jour d’un fournisseur, vérifiez les identifiants, les intégrations actives, les journaux et les ressources générées.

Pour les fournisseurs de packs, terminez le flux de travail complet de génération de fournisseur et livrez le pack résultant. Reconstruire Armature seul ne prouve pas que le joueur a reçu le ZIP de fournisseur mis à jour.
