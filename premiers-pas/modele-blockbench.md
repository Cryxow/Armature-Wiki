# 🧸 Modèle BlockBench

## Pré-requis

* Installez [BlockBench](https://www.blockbench.net/)
* Installez le plugin [Cameras](https://github.com/JannisX11/blockbench-plugins/blob/master/plugins/cameras.js) dans BlockBench

## Créer le modèle

Pour créer votre modèle, il est recommandé de dupliquer le fichier `empty_hands.bbmodel` afin d’avoir déjà la configuration correcte des bras.

Vous verrez ce modèle à l’intérieur :

<figure><img src="https://3676679775-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FDcWT3onsbt69DlAqumqZ%2Fuploads%2FU4QWjdVh8f2EbaSU83tj%2Fimage.png?alt=media&#x26;token=bd2d2383-1585-46bf-a13d-4c41d6f55233" alt="Capture d’écran BlockBench du modèle de bras par défaut."><figcaption><p>Capture d’écran BlockBench du modèle de bras par défaut.</p></figcaption></figure>

Armature utilise les préfixes de bones intégrés à [BetterModel](https://github.com/toxicity188/BetterModel/wiki/Create-player-animation#make-your-animation), notamment :

```
head (ph)
right arm (pra)
right forearm (prfa)
left arm (pla)
left forearm (plfa)
hip (phip)
waist (pw)
chest (pc)
right leg (prl)
right foreleg (prfl)
left leg (pll)
left foreleg (plfl)
left item (pli)
right item (pri)
cape (cape)
```

## Ajouter l’item

Par défaut, le modèle affiche automatiquement les items tenus via `pli_left_item` et `pri_right_item`. Vous pouvez supprimer ces bones si vous ne les voulez pas et ajouter manuellement votre item.

L’ajout manuel est utile pour les armes à feu, comme dans le modèle `fp_rifle.bbmodel`, afin d’en garder le contrôle complet.

## Animer le modèle

### Configurer la vue

Il est recommandé de diviser la vue en `Double Horizontal` pour prévisualiser l’animation depuis les yeux du joueur.

![](https://3676679775-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FDcWT3onsbt69DlAqumqZ%2Fuploads%2FQ5ACsUhcx9dU44NhmixY%2Fimage.png?alt=media\&token=39dcb3dd-ca3c-4713-8058-3bb28f1b78bd)

<figure><img src="https://3676679775-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FDcWT3onsbt69DlAqumqZ%2Fuploads%2FCKt6AbrWg7Q1jK98tu6E%2Fimage.png?alt=media&#x26;token=cd06fd41-f071-4195-b93c-0ca9b212c72c" alt=""><figcaption></figcaption></figure>

Le petit rectangle visible en haut de la vue représente l’écran du joueur.

### Animations

Quelques animations par défaut sont présentes. Supprimez-les pour repartir de zéro ou utilisez-les comme base.

Je recommande le [guide d’animation ModelEngine](https://git.lumine.io/mythiccraft/model-engine-4/-/wikis/Modeling/Animating-a-Model), bien expliqué et fonctionnant de la même manière avec BetterModel.

#### Règles de nommage des animations

<mark style="color:$danger;">**⚠️ N’UTILISEZ PAS CES NOMS EXACTS DANS VOS ANIMATIONS ⚠️**</mark>

<mark style="color:$danger;">`idle`</mark>, <mark style="color:$danger;">`walk`</mark>, <mark style="color:$danger;">`idle_fly`</mark>, <mark style="color:$danger;">`walk_fly`</mark>, <mark style="color:$danger;">`spawn`</mark>

BetterModel possède un système d’actions intégré qui joue automatiquement ces animations exactes ; Armature ne peut pas l’empêcher. Ces noms provoquent des glitches dus à l’interférence des deux plugins.

Utilisez d’autres noms, ou ajoutez un préfixe/suffixe comme `armature_idle`, `a_idle`, `base`, `idlee`…
