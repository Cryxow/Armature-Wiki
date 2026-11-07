---
description: "Réglages et limites connues"
---

# 🎚️ Réglages et limites connues

Avec Mod Menu, ouvrez **Mods → Armature Client → Configure**. L’écran
propose un réglage pour activer le rendu client. Sans Mod Menu, quittez
Minecraft et modifiez le fichier suivant.

```json
{
  "renderingEnabled": true
}
```

Le fichier se trouve dans `config/armature-client.json`. La valeur par
défaut est `true`. Ce réglage demande un changement de moteur. Attendez
la fin du rechargement coordonné des ressources avant de vérifier le
rendu. Le serveur gère les réglages de caméra et de profil.

## Limites de compatibilité

| Fonction | Limite actuelle |
| --- | --- |
| Modèles | Format strict `.armature` v2 et modèles `.bbmodel` compatibles. La v1 demande une conversion manuelle. |
| Skins et armures | Les skins, équipements et ornements dépendent de l’adaptateur de rendu et des ressources de la version cible. |
| Rendu des objets | Les objets tenus sont mis à jour dynamiquement. Le pack commun du fournisseur peut être nécessaire. |
| Caméra et mouvement | Le serveur choisit la caméra, le mouvement procédural et la physique. Vérifiez la parité en jeu. |
| Compositions | Les scènes avec accessoires utilisent le repli côté serveur. |
| Sodium et Iris | Testez les combinaisons en jeu. La compilation seule ne valide pas les packs de shaders. |
| Fabulous et shaders | Le masquage par shader du serveur et le rendu du mod suivent des chemins distincts. Testez le mode graphique exact. |

Sodium ne prend pas en charge le chemin de shader vanilla qui préserve
le champ de vision. Avec Iris, vérifiez le pack de shaders utilisé.
Voir [Rendu et caméra](../configuration/rendering-camera.md).

Pour signaler un problème, indiquez les versions exactes de Minecraft, Java,
Fabric Loader, Fabric API, du JAR client, du plugin serveur, de Sodium,
d’Iris et du pack de shaders. Joignez le `latest.log` du client, les
diagnostics du serveur et un modèle et un profil minimaux. Précisez si
le passage au rendu serveur change le symptôme.
