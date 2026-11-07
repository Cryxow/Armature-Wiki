---
description: "Installation et compatibilité"
---

# 📲 Installation et compatibilité

Le mod Fabric est **facultatif**. Le moteur natif du serveur reste actif
pour les joueurs sans le mod. Utilisez le JAR client prévu pour la version
exacte de Minecraft. Le tableau présente les cibles des builds autonomes.
Vérifiez la compatibilité des versions finales du serveur 2.0.0 et du client
avant de les déployer ensemble.

| Minecraft | Java à l’exécution | Fabric API de la build | Mod Menu facultatif |
| --- | --- | --- | --- |
| 1.21.8 | 21+ | 0.136.1+1.21.8 | 15.x |
| 1.21.9 | 21+ | 0.134.1+1.21.9 | 16.x |
| 1.21.10 | 21+ | 0.138.4+1.21.10 | 16.x |
| 1.21.11 | 21+ | 0.141.6+1.21.11 | 17.x |
| 26.1 | 25+ | 0.145.1+26.1 | 18.x |
| 26.1.1 | 25+ | 0.145.4+26.1.1 | 18.x |
| 26.1.2 | 25+ | 0.155.3+26.1.2 | 18.x |
| 26.2 | 25+ | 0.161.0+26.2 | 20.x |

## Installer le mod

1. Installez **Fabric Loader 0.19.5 ou plus récent**, avec la version de Java
   indiquée dans le tableau.
2. Installez [Fabric API](https://modrinth.com/mod/fabric-api) pour cette
   version de Minecraft.
3. Téléchargez le JAR correspondant dans les [versions d’Armature Client](https://github.com/Cryxow/ArmatureClientMod/releases).
   Placez un seul JAR Armature Client dans le dossier `mods/` de l’instance.
4. Si vous le souhaitez, installez [Mod Menu](https://modrinth.com/mod/modmenu)
   dans la version indiquée.
5. Rejoignez un serveur Armature avec le moteur natif et
   `render.client-mod.enabled: true`. Acceptez son pack de ressources commun.

Les sources serveur et client correspondantes utilisent le protocole **9**.
Ce numéro définit le protocole de communication. La version du plugin et
celle du format `.armature` suivent leur propre numérotation. Vérifiez le
protocole si vous changez de build client.

## Vérifier le rendu

Prenez l’épée du premier tutoriel. Désactivez puis réactivez le rendu client
dans ses réglages. Attendez la fin de tout rechargement demandé. Vérifiez
que les deux moteurs affichent le même objet, le même état d’animation
et le même skin. Reconnectez-vous avec un cache de modèles client vide,
puis vérifiez dans les diagnostics du serveur que le client est prêt.

Le mod reste en préversion jusqu’à la validation de la matrice en jeu. Ces
builds Fabric ciblent les versions du tableau. Les snapshots et NeoForge
sont hors de ce périmètre. La prise en charge côté client ne valide pas
Paper ou Folia pour chaque version de Minecraft indiquée.
