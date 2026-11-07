---
description: "Fournisseurs"
---

# 🔧 Fournisseurs

La plupart des intégrations ne nécessitent que la façade d’animation. Utilisez les contrats de fournisseur lorsque votre plugin doit enseigner à Armature comment identifier ses objets ou connecter ses événements de jeu au dispatcher d’actions de compatibilité d’Armature.

Accédez uniquement au registre lorsque Armature est disponible :

```java
ArmatureApi.tryProviders().ifPresent(registry -> {
    registry.registerItemProvider(new MyWeaponsItemProvider(plugin));
});
```

Le registre est sécurisé par fil d’exécution, retourne des instantanés de fournisseur immuables, traite les identifiants de manière insensible à la casse et rejette les identifiants dupliqués. Choisissez un identifiant stable appartenant à votre plugin, tel que <code>myweapons</code>.

## Fournisseurs d’identité d’objet

Un <code>ItemIdentityProvider</code> traduit un <code>ItemStack</code> en l’identité d’objet neutre d’Armature. Il ne modifie pas l’objet :

```java
import com.armaturemc.api.HeldItemCategory;
import com.armaturemc.api.ItemIdentity;
import com.armaturemc.api.ItemIdentityProvider;
import com.armaturemc.api.ItemKey;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

final class MyWeaponsItemProvider implements ItemIdentityProvider {
    private final NamespacedKey weaponKey;

    MyWeaponsItemProvider(org.bukkit.plugin.Plugin plugin) {
        this.weaponKey = new NamespacedKey(plugin, "weapon-id");
    }

    @Override
    public String id() {
        return "myweapons";
    }

    @Override
    public Optional<ItemIdentity> resolve(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return Optional.empty();
        }

        String weaponId = item.getItemMeta().getPersistentDataContainer()
            .get(weaponKey, PersistentDataType.STRING);
        if (!"m4a1".equals(weaponId)) {
            return Optional.empty();
        }

        return Optional.of(new ItemIdentity(
            new ItemKey("myweapons", "m4a1"),
            java.util.Set.of(),
            HeldItemCategory.FIREARM
        ));
    }
}
```

Le profil peut ensuite correspondre à l’identité retournée par le fournisseur :

```yaml
m4a1:
  match:
    item: myweapons:m4a1
  model:
    name: arm_rifle
  animations:
    movement:
      - animation: base
```

Utilisez des alias lorsque plusieurs identifiants de jeu doivent partager un seul profil d’Armature :

```java
new ItemIdentity(
    new ItemKey("myweapons", "m4a1"),
    java.util.Set.of(new ItemKey("myweapons", "m4a1-silenced")),
    HeldItemCategory.FIREARM
);
```

Si votre objet peut être identifié via un matériau vanilla, des données de modèle personnalisées ou des données PDC déjà supportées par un matcheur de profil structuré, un fournisseur personnalisé n’est pas nécessaire. Consultez [Correspondance des objets](../../premiers-pas/profils/README.md).

## Masquage personnalisé à la première personne

<code>CustomItemVisualProvider</code> est une extension optionnelle de <code>ItemIdentityProvider</code>. Implémentez-le uniquement lorsque votre fournisseur doit retourner une copie exclusive au client dont le modèle d’objet est remplacé par le modèle caché d’Armature. Ne modifiez jamais la pile d’inventaire fournie :

```java
public Optional<ItemStack> createFirstPersonMask(
        ItemStack original, String hiddenModelId) {
    ItemStack copy = original.clone();
    // Apply the hidden model to copy, using the provider's item API.
    return Optional.of(copy);
}
```

La copie retournée est une transformation de présentation. L’objet de jeu original reste propriété de votre plugin.

## Fournisseurs d’actions

Un <code>ActionProvider</code> publie des événements du côté jeu via <code>ActionEventSink</code>. Il annonce ses capacités, démarre avec un écouisseur et s’arrête lors de l’arrêt du plugin :

```java
final class MyWeaponsActionProvider implements ActionProvider {
    private ActionEventSink sink;

    @Override
    public String id() {
        return "myweapons";
    }

    @Override
    public Set<ActionCapability> capabilities() {
        return Set.of(
            ActionCapability.EQUIP,
            ActionCapability.FIRE,
            ActionCapability.AIM,
            ActionCapability.RELOAD
        );
    }

    @Override
    public void start(ActionEventSink sink) {
        this.sink = sink;
    }

    @Override
    public void stop() {
        this.sink = null;
    }

    void fire(UUID playerId, int ammo) {
        if (sink != null) {
            sink.accept(new ActionEvent(
                playerId,
                ArmatureAction.FIRE,
                new ItemKey("myweapons", "m4a1"),
                Map.of("ammo", Integer.toString(ammo))
            ));
        }
    }
}
```

Enregistrez et démarrez le fournisseur explicitement :

```java
var provider = new MyWeaponsActionProvider();

ArmatureApi.tryProviders().ifPresent(registry -> {
    provider.start(ArmatureApi::dispatch);
    registry.registerActionProvider(provider);
});
```

Arrêtez-le dans votre <code>onDisable</code> de plugin :

```java
provider.stop();
```

Le fournisseur d’action est un pont de compatibilité. Pour une nouvelle intégration d’arme, préférez <code>ArmatureAnimationApi</code> avec une référence d’actif directe ou un signal namespaced. Ces chemins rendent l’intention visuelle explicite.

## Capacités et propriété

Les valeurs <code>ActionCapability</code> décrivent ce que le fournisseur peut publier : <code>EQUIP</code>, <code>FIRE</code>, <code>AIM</code>, <code>RELOAD</code>, <code>RELOAD_PHASES</code>, <code>FIREARM_STATE</code>, <code>FIRE_MODE</code> et <code>SKIN</code>.

Les fournisseurs traduisent l’état. Ils ne transfèrent pas la propriété. Votre plugin reste responsable de :

* la création d’objets et les modifications d’inventaire ;
* les munitions, l’état de rechargement, les dégâts, les projectiles et les temps de recharge ;
* l’annulation d’événements et les permissions de jeu ;
* le cycle de vie de toute ressource externe fournie.

Armature consomme les informations d’identité/action pour sélectionner et afficher une présentation. Elle ne fait pas appel au fournisseur pour modifier le jeu.


Il n’existe pas de méthode d’enregistrement public dans ce contrat. Arrêtez les ressources d’événements externes lors de la désactivation. N’enregistrez pas une autre instance avec le même identifiant contre un registre inchangé. Armature reconstruit son registre avec son propre cycle de vie. Préférez les signaux namespaced directs pour de nouvelles intégrations lorsque l’identité de l’objet est déjà exprimable avec le matcheur structuré.
