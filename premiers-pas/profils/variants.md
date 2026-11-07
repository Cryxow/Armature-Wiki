---
description: "Variantes"
---

# 🎨 Variantes

Une variante sélectionne une présentation au sein d’un profil d’objet actif. La première variante éligible dans l’ordre de déclaration YAML gagne. Aucun match n’utilise la base. Les variantes ne s’empilent pas. `match.when` bloque tout le profil avant le choix de la variante.

```yaml
wiki_variant:
  match:
    item: minecraft:stick
  model:
    name: wiki_player
  variants:
    gold:
      when:
        permissions:
          any: [cosmetics.gold, cosmetics.admin]
        all:
          - variable.weapon-skin == gold
      model:
        name: wiki_player_gold
    alternate:
      when: variable.weapon-skin == alternate
      model:
        name: wiki_player_alternate
```

Déclarez `weapon-skin` globalement et fournissez les trois modèles. Les champs modèle/physique et les accessoires fusionnent par clé. Fournir une catégorie d’animation remplace toute sa liste. Son omission hérite de la liste de base. Les variantes peuvent remplacer la présentation du modèle, de la physique, des animations et des accessoires.

Les conditions de sélection utilisent un état externe/partagé, pas leurs propres résultats d’animation ou de variante/accessoire. Les conditions d’animation peuvent inspecter `variant.active` (`base` ou l’ID choisi). Les conditions de montage peuvent utiliser l’état de la variante choisie, mais ne peuvent pas dépendre du résultat actif d’un autre montage.

Les actions non annulables protégées diffèrent les changements jusqu’à leur achèvement. Puis la présentation éligible la plus récente est réévaluée. Toutes les variantes sont validées en tant qu’actifs avant la publication du pack, y compris les inactives. Les changements de variables sélectionnent donc des actifs publiés existants au lieu de reconstruire un pack.

Utilisez `/armature profile inspect <player>` pour voir la variante sélectionnée. Testez l’absence de match, les conditions superposées, les changements de permission et un changement lors du rechargement.
