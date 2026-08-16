# 👕 Armures personnalisées

## Textures personnalisées pour les armures vanilla

Remplacez les textures `armor.png` et `leggings.png` dans `plugins/BetterModel/armors/armors/<vanilla_type>/` par vos textures personnalisées. Utilisez les mêmes noms de fichiers.

## Armures personnalisées ajoutées par un plugin

Les étapes suivantes supposent que vous avez déjà configuré un ensemble d’armure personnalisé avec un plugin comme CraftEngine, ItemsAdder ou Nexo.

Supposons que vous ayez un ensemble d’armure en émeraude avec un ID personnalisé comme `your_namespace:emerald_chestplate`, `your_namespace:emerald_leggings`, etc.

### Ajouter les textures personnalisées

Copiez les textures d’armure de votre joueur `armor.png` et `leggings.png` dans `plugins/BetterModel/armors/armors/emerald/` _(remplacez `emerald` par le nom de votre type d’armure)_.

En cas de doute, consultez les armures vanilla déjà présentes dans `plugins/BetterModel/armors/`.

### Associer les textures à l’armure

Ouvrez `/Armature/config.yml` et cherchez `armor-mappings:`. Suivez cette structure :

```yaml
armor-mappings:
  "your_namespace:emerald_chestplate": emerald
```

* `your_namespace:emerald_chestplate` : ID de votre item d’armure personnalisé.
* `emerald` : nom du dossier dans `/BetterModel/armors/armors/`.
