---
description: "Провайдеры"
---

# 🔧 Провайдеры

Большинство интеграций требуют только фасад анимации. Используйте контракты провайдеров, когда ваш плагин должен обучить Armature распознаванию его предметов или связать игровые события с диспетчером совместимости действий Armature.

Доступ к реестру возможен только в то время, когда Armature доступен:

```java
ArmatureApi.tryProviders().ifPresent(registry -> {
    registry.registerItemProvider(new MyWeaponsItemProvider(plugin));
});
```

Реестр безопасен для многопоточности, возвращает неизменяемые снимки провайдеров, обрабатывает идентификаторы без учета регистра и отклоняет дубликаты. Выберите стабильный идентификатор, принадлежащий вашему плагину, например, <code>myweapons</code>.

## Провайдеры идентификации предметов

<code>ItemIdentityProvider</code> преобразует <code>ItemStack</code> в нейтральную идентификацию предмета Armature. Он не изменяет сам предмет:

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

Затем профиль может сопоставить возвращенную провайдером идентификацию:

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

Используйте алиасы, когда несколько игровых идентификаторов должны разделять один профиль Armature:

```java
new ItemIdentity(
    new ItemKey("myweapons", "m4a1"),
    java.util.Set.of(new ItemKey("myweapons", "m4a1-silenced")),
    HeldItemCategory.FIREARM
);
```

Если ваш предмет можно сопоставить с помощью стандартных материалов, пользовательских данных модели или данных PDC, уже поддерживаемых структурированным сопоставителем, кастомный провайдер не требуется. См. [Сопоставление предметов](../../nachalo-raboty/profili/README.md).

## Кастомное маскирование от первого лица

<code>CustomItemVisualProvider</code> является опциональным расширением <code>ItemIdentityProvider</code>. Реализуйте его только тогда, когда ваш провайдер должен возвращать клиентскую копию, чья модель предмета заменяется скрытой моделью Armature. Никогда не изменяйте предоставленный стек инвентаря:

```java
public Optional<ItemStack> createFirstPersonMask(
        ItemStack original, String hiddenModelId) {
    ItemStack copy = original.clone();
    // Apply the hidden model to copy, using the provider's item API.
    return Optional.of(copy);
}
```

Возвращаемая копия представляет собой преобразование представления. Исходный игровой предмет остается в собственности вашего плагина.

## Провайдеры действий

<code>ActionProvider</code> публикует события на стороне игры через <code>ActionEventSink</code>. Он объявляет свои возможности, начинается с приемника и останавливается во время закрытия плагина:

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

Регистрируйте и запускайте провайдер явно:

```java
var provider = new MyWeaponsActionProvider();

ArmatureApi.tryProviders().ifPresent(registry -> {
    provider.start(ArmatureApi::dispatch);
    registry.registerActionProvider(provider);
});
```

Остановите его в вашем плагине <code>onDisable</code>:

```java
provider.stop();
```

Провайдер действий служит мостом совместимости. Для новой интеграции оружия предпочтительно использовать <code>ArmatureAnimationApi</code> с прямым указанием актива или именным сигналом. Эти пути делают визуальный намерение явным.

## Возможности и права собственности

Значения <code>ActionCapability</code> описывают, что может публиковать провайдер: <code>EQUIP</code>, <code>FIRE</code>, <code>AIM</code>, <code>RELOAD</code>, <code>RELOAD_PHASES</code>, <code>FIREARM_STATE</code>, <code>FIRE_MODE</code> и <code>SKIN</code>.

Провайдеры переводят состояние. Они не передают права собственности. Ваш плагин остается ответственным за:

* создание предметов и изменения инвентаря;
* патроны, состояние перезарядки, урон, снаряды и время перезарядки;
* отмену событий и разрешения игры;
* жизненный цикл любых внешних ресурсов провайдера.

Armature потребляет информацию об идентичности/действии для выбора и рендеринга представления. Она не вызывает обратный вызов к провайдеру для изменения состояния игры.


В этой спецификации нет публичного метода отписки. Отключайте внешние события ресурсов при деактивации. Не регистрируйте другой экземпляр с тем же идентификатором против неизменного реестра. Armature перестраивает свой реестр с собственным жизненным циклом. Для новых интеграций предпочтительны прямые именные сигналы, когда идентичность предмета уже выражается структурированным сопоставителем.
