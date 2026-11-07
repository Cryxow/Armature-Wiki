---
description: "Справочник по настройкам"
---

# ⚙️ Справочник по настройкам

Armature читает `plugins/Armature/config.yml`. Эта страница воспроизводит встроенные по умолчанию значения версии 2.0.0 и описывает каждый активный параметр листа. Перемigration профилей использует внутренние ограничения и не имеет настраиваемых параметров. Существующие значения администратора сохраняются во время обновлений схемы, поэтому живая установка может отличаться.

## Применить изменение

| Требование | Операция |
| --- | --- |
| Перезагрузка | `/armature reload config` |
| Перезапуск | Полная остановка/запуск сервера. Включает бэкенд, клиентский мост и разделение пакета рендерера |
| Пересборка пакета | `/armature reload pack`, затем рабочий процесс генерации внешнего поставщика |
| Перезагрузка клиента | Доставка/загрузка финального измененного пакета. Перезагрузка конфигурации сервера сама по себе недостаточна |

Запустите `/armature config validate` для проверки допустимых значений и требований текущей схемы. Неправильные кандидаты на перезагрузку сохраняют последнее известное рабочее состояние. `config-version` относится к Armature.

## опции config-version

```yaml
config-version: 17

input:
  movement:
    vertical-stop-delay-ticks: 3
    # Horizontal distance travelled in one server tick before walk starts.
    threshold: 0.015
    # Consecutive stationary ticks required before walk/sprint returns to idle.
    stop-delay-ticks: 3

debug:
  # Master switch for the debug categories below.
  enabled: false
  teleportation: false
  tracker-lifecycle: true
  animations: true
  vanilla-hiding: false
  # Native writing/reload and CraftEngine pack preparation phase timings.
  pack-generation: false
  # Model warning summaries in the console; errors are always reported.
  # Full diagnostics remain available via /armature diagnostics native.
  native-compiler-warnings: false

render:
    # native is the primary renderer and requires no BetterModel installation.
    # bettermodel remains available; deprecation is planned for future versions.
  backend: native
  client-mod:
    # Experimental optional Fabric 1.21.8 renderer for native .armature v2 models.
    # Clients without the mod keep the configured server renderer.
    enabled: true
  enabled: true
  dynamic-items:
    # Preserve the outgoing model's items during unequip/deferred actions.
    # The incoming model receives the current items when equip is activated.
    # Applies to server rendering and the optional Fabric renderer.
    defer-until-equipped: true
  camera-rotation:
    # Client-side Fabulous post-effect driven by the optional s_camera bone.
    enabled: true
    debug-mode: 'off'
    zoom-correction:
      # Crops the image to hide missing edges during camera-bone rotations.
      # Disabled preserves the original FOV and does not apply any crop zoom.
      enabled: true
      # fixed keeps one constant zoom; dynamic adapts it to the bone rotation.
      mode: fixed
      # Fixed zoom factor, or maximum allowed factor in dynamic mode.
      maximum-zoom: 1.25
      # In fixed mode, widens the 3D source projection before applying the
      # crop, preserving the player's visible world and viewmodel FOV.
      # Breaks with Sodium.
      preserve-fov: false
    rotation-clamp:
      # Limits the total 3D rotation received from the Blockbench camera bone.
      enabled: true
      maximum-degrees: 7.0
  visibility:
    first-person-only: true
    follow-player-visibility: true
  viewmodel:
    model-adaptation: true
    anti-clipping: true
    y-lock: true
    fov:
      locked: true
      degrees: 90.0
  vanilla-hiding:
    # packet-mask | core-shaders
    # core-shaders sends a per-player GameTime channel and can conflict with
    # other resource-pack shaders that also use GameTime. core-shaders never
    # installs inventory-mask packets. PacketEvents action detection remains
    # independent.
    mode: core-shaders
  anchor:
    max-distance: 20
    sync-interval-ticks: -1
  motion:
    sway:
      enabled: true
      # Applies look/movement sway rotation to the s_camera bone.
      affects-camera-bone: true
      maximum-rotation: 10
      look:
        lerp-speed: 20.0
        yaw-gain: -0.1
        pitch-gain: -0.1
        roll-gain: -0.1
      movement:
        lerp-speed: 6.0
        yaw-gain: 0
        pitch-gain: 0
        roll-gain: -5
    bob:
      enabled: false
      cycles-per-block: 1
      horizontal-amplitude: 0.025
      vertical-amplitude: 0.03
      roll-amplitude: 0.8
      frequency: 9.0
      damping: 0.9
    camera-follow:
      enabled: false
      frequency: 20.0
      damping: 0.1
      yaw-position-gain: 0
      pitch-position-gain: 0
      maximum-offset: 0.15
      pitch-compensation: 0.3
    # Physics-driven bones: model bones named phy_* stay perpendicular to
    # the ground (their down axis follows the world, not the model's server
    # rotation) and swing with movement acceleration and player turning.
    # Only the native backend implements them; BetterModel ignores the
    # prefix. The bone pivot is the rotation center.
    physics:
      enabled: true
      acceleration-gain: 4.0
      maximum-angle: 25
      # Spring response of the swing. frequency is how fast the bone chases
      # the target, damping is how much it resists overshooting it (1.0 = no
      # overshoot at all). With damping below 1.0 the bone swings past its rest
      # pose and bounces back, which reads as a snap when the player stops:
      # at frequency 4.0 / damping 0.6 a 25 degree return overshoots by
      # ~2.1 degrees. Raise damping toward 1.0 to remove that bounce, or lower
      # frequency for a slower, heavier settle.
      frequency: 4.0
      damping: 0.6
      # Tilt added per degree/second of camera yaw turn. Keep small: mouse
      # sway while looking up or down must not fling the bone sideways.
      turn-gain: 0.25

resource-pack:
  # Optional separate renderer pack. The provider continues delivering the common pack.
  # Set the external URL, enable, restart, then rebuild and publish both packs.
  renderer-pack:
    enabled: false
    # Host plugins/Armature/renderer_pack.zip here. {sha1} can version the URL.
    url: ""
    prompt: "Armature server rendering"
  # Also write plugins/Armature/resource_pack.zip when Armature generates assets.
  generate-zip: false
  # Automatic Armature publication. Explicit pack/all reloads always generate.
  generation:
    on-startup: true
    on-reload: true
    on-provider-change: true
  # Empty keeps the backend's description. Does not rename a provider's final pack.
  description: ""
  # Filters bundled overlays; does not convert models for other client versions.
  supported-version:
    min: "1.21.4"
    max: latest
  validation:
    enable: true
    fail-on-error: false
    check-models: true
    check-textures: true
    check-atlases: true
  optimization:
    json:
      enable: false
      exclude: []
    texture:
      enable: false
      compression-level: 9
      exclude: []
  # Standalone Armature ZIP only. Path relative to plugins/Armature.
  zip:
    path: resource_pack.zip
    method: deflated
    compression-level: 6
  # Deterministic ownership when two sources publish one normalized path.
  # keep-existing preserves the first external owner; replace lets Armature
  # win when its generated source is involved; error aborts the candidate.
  collision-policy: keep-existing
  # Optional first-match rules: [{source: provider, namespace: ns, path: ns:foo/**, policy: replace}]
  collision-rules: []
  exclusions:
    # Source owner IDs are normalized lowercase and match exactly.
    sources: []
    # Namespace IDs are normalized lowercase and match assets/<namespace>/**.
    namespaces: []
    # Paths use forward slashes; * stays within one segment, ** crosses segments.
    paths: []
```

## Опции версии конфигурации

| Параметр | По умолчанию | Допустимые значения | Когда применяется | Назначение |
| --- | --- | --- | --- | --- |
| `config-version` | `17` | целое число >= 0 | перезапуск | Версия управляемой схемы. Не редактируйте вручную. |

## вводные параметры

| Параметр | По умолчанию | Допустимые значения | Когда применяется | Назначение |
| --- | --- | --- | --- | --- |
| `input.movement.vertical-stop-delay-ticks` | `3` | целое число >= 0 | перезагрузка | Стационарные вертикальные тики до завершения перехода вертикального движения. |
| `input.movement.threshold` | `0.015` | конечное число в диапазоне [0, 100] | перезагрузка | Горизонтальное расстояние, пройденное за один тик сервера до начала ходьбы. |
| `input.movement.stop-delay-ticks` | `3` | целое число >= 0 | перезагрузка | Требуемое количество последовательных неподвижных тиков перед возвратом ходьбы/беготни в режим ожидания. |

## опции отладки

| Параметр | По умолчанию | Допустимые значения | Когда применяется | Назначение |
| --- | --- | --- | --- | --- |
| `debug.enabled` | `false` | `true` или `false` | перезагрузка | Главный переключатель для категорий отладки ниже. |
| `debug.teleportation` | `false` | `true` или `false` | перезагрузка | Включить категорию диагностики телепортации. Главный переключатель отладки также применяется. |
| `debug.tracker-lifecycle` | `true` | `true` или `false` | перезагрузка | Включить категорию диагностики жизненного цикла трекера. Главный переключатель отладки также применяется. |
| `debug.animations` | `true` | `true` или `false` | перезагрузка | Включить категорию диагностики анимаций. Главный переключатель отладки также применяется. |
| `debug.vanilla-hiding` | `false` | `true` или `false` | перезагрузка | Включить категорию диагностики скрытия. Главный переключатель отладки также применяется. |
| `debug.pack-generation` | `false` | `true` или `false` | перезагрузка | Время записи и перезагрузки нативных ресурсов, а также подготовки пакета CraftEngine. |
| `debug.native-compiler-warnings` | `false` | `true` или `false` | перезагрузка | Сводка предупреждений моделей в консоли. Ошибки всегда сообщаются. Полная диагностика доступна через /armature diagnostics native. |

## опции рендеринга

| Параметр | По умолчанию | Допустимые значения | Когда применяется | Назначение |
| --- | --- | --- | --- | --- |
| `render.backend` | `native` | `bettermodel` или `native` | перезапуск | Нативный рендеринг является основным и не требует установки BetterModel. Мод BetterModel остаётся доступным. Его устаревание запланировано для будущих версий. |
| `render.client-mod.enabled` | `true` | `true` или `false` | перезапуск | Экспериментальный опциональный рендеринг Fabric 1.21.8 для нативных моделей .armature v2. Клиенты без модификации сохраняют настроенный рендеринг сервера. |
| `render.enabled` | `true` | `true` или `false` | перезагрузка | Включить рендеринг от первого лица, пока плагин остаётся загруженным. |
| `render.dynamic-items.defer-until-equipped` | `true` | `true` или `false` | перезагрузка | Сохраняет предметы предыдущей модели во время убирания предмета или отложенного действия. Новая модель получает текущие предметы при запуске анимации взятия. Действует на сервере и при рендеринге через Fabric. |
| `render.camera-rotation.enabled` | `true` | `true` или `false` | пересборка пакета и перезагрузка на клиенте | Клиентский постэффект графического режима Fabulous, которым управляет необязательная кость s_camera. |
| `render.camera-rotation.debug-mode` | `'off'` | `off`, `identity`, `signature`, `metadata`, `fixed-basis`, `uv` или `status` | пересборка пакета и перезагрузка на клиенте | Режим отладки. |
| `render.camera-rotation.zoom-correction.enabled` | `true` | `true` или `false` | пересборка пакета и перезагрузка на клиенте | Обрезает изображение, чтобы скрыть отсутствующие края во время поворотов камеры относительно костей. Отключение сохраняет исходный угол обзора и не применяет никаких эффектов обрезки или зума. |
| `render.camera-rotation.zoom-correction.mode` | `fixed` | `fixed` или `dynamic` | пересборка пакета и перезагрузка на клиенте | В режиме fixed масштаб постоянный. Режим dynamic меняет его в зависимости от вращения кости. |
| `render.camera-rotation.zoom-correction.maximum-zoom` | `1.25` | конечное число в диапазоне [1, 4] | пересборка пакета и перезагрузка на клиенте | Фиксированный коэффициент зума или максимальный разрешенный коэффициент в динамическом режиме. |
| `render.camera-rotation.zoom-correction.preserve-fov` | `false` | `true` или `false` | пересборка пакета и перезагрузка на клиенте | В фиксированном режиме расширяет 3D-проектирование источника перед применением обрезки, сохраняя видимый мир игрока и угол обзора viewmodel. Несовместимо с Sodium. |
| `render.camera-rotation.rotation-clamp.enabled` | `true` | `true` или `false` | пересборка пакета и перезагрузка на клиенте | Ограничивает коррекцию камеры анимации установленным максимальным значением. |
| `render.camera-rotation.rotation-clamp.maximum-degrees` | `7.0` | конечное число в диапазоне [0, 180] | пересборка пакета и перезагрузка на клиенте | Максимальная величина коррекции камеры в градусах при включенном ограничении. |
| `render.visibility.first-person-only` | `true` | `true` или `false` | перезагрузка | Ограничить представление видом от первого лица. |
| `render.visibility.follow-player-visibility` | `true` | `true` или `false` | перезагрузка | Следовать видимости/невидимости игрока при определении рендеринга. |
| `render.viewmodel.model-adaptation` | `true` | `true` или `false` | пересборка пакета | Адаптировать поддерживаемые модели источников в сцену от первого лица без переписывания файлов. |
| `render.viewmodel.anti-clipping` | `true` | `true` или `false` | пересборка пакета и перезагрузка на клиенте | Сгенерировать путь шейдера видимости модели, чтобы геометрия Armature могла рендериться сквозь блоки. |
| `render.viewmodel.y-lock` | `true` | `true` или `false` | перезагрузка | Сохранить стабильное вертикальное положение видимости модели в поддерживаемом пути шейдера. |
| `render.viewmodel.fov.locked` | `true` | `true` или `false` | пересборка пакета и перезагрузка на клиенте | Использовать фиксированное поле зрения видимости модели, независимое от поля зрения мира. |
| `render.viewmodel.fov.degrees` | `90.0` | конечное число в открытом интервале (1, 179) | пересборка пакета и перезагрузка на клиенте | Угол обзора модели в градусах при включении захвата. |
| `render.vanilla-hiding.mode` | `core-shaders` | `packet-mask` или `core-shaders` | перезагрузка, пересборка пакета, перезагрузка клиента | packet-mask или core-shaders. core-shaders отправляет канал GameTime для каждого игрока и может конфликтовать с другими шейдерами пакетов ресурсов, использующими GameTime. core-shaders никогда не устанавливает пакеты inventory-mask. Обнаружение действий PacketEvents остается независимым. |
| `render.anchor.max-distance` | `20` | конечное число в диапазоне [0, 128] | перезагрузка | Максимальное расстояние между якорями сервера перед коррекцией. |
| `render.anchor.sync-interval-ticks` | `-1` | целое число от -1 до 20000 включительно | перезагрузка | Интервал синхронизации якорей; -1 использует политику бэкенда. |
| `render.motion.sway.enabled` | `true` | `true` или `false` | перезагрузка | Включить процедурное покачивание взгляда/движения. |
| `render.motion.sway.affects-camera-bone` | `true` | `true` или `false` | перезагрузка | Применять вращение покачивания взгляда/движения к кости s_camera. |
| `render.motion.sway.maximum-rotation` | `10` | конечное число в диапазоне [0, 180] | перезагрузка | Максимальное суммарное вращение качения в градусах. |
| `render.motion.sway.look.lerp-speed` | `20.0` | конечное число в диапазоне [0, 1000] | перезагрузка | Реакция сглаживания взгляда. Ноль убирает интерполяцию взгляда. |
| `render.motion.sway.look.yaw-gain` | `-0.1` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Знаковое значение усиления поворота для этого слоя движения. |
| `render.motion.sway.look.pitch-gain` | `-0.1` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Знаковое значение усиления наклона для этого слоя движения. |
| `render.motion.sway.look.roll-gain` | `-0.1` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Знаковое значение усиления крена для этого слоя движения. |
| `render.motion.sway.movement.lerp-speed` | `6.0` | конечное число в диапазоне [0, 1000] | перезагрузка | Реакция сглаживания движения. |
| `render.motion.sway.movement.yaw-gain` | `0` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Значение поворота для этого слоя движения. |
| `render.motion.sway.movement.pitch-gain` | `0` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Значение наклона для этого слоя движения. |
| `render.motion.sway.movement.roll-gain` | `-5` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Значение крена для этого слоя движения. |
| `render.motion.bob.enabled` | `false` | `true` или `false` | перезагрузка | Включить ходьбу с подпрыгиванием. |
| `render.motion.bob.cycles-per-block` | `1` | конечное число в диапазоне [0, 1000] | перезагрузка | Количество циклов ходьбы с подпрыгиванием на пройденный блок. |
| `render.motion.bob.horizontal-amplitude` | `0.025` | конечное число в диапазоне [0, 1000] | перезагрузка | Амплитуда горизонтального подпрыгивания. |
| `render.motion.bob.vertical-amplitude` | `0.03` | конечное число в диапазоне [0, 1000] | перезагрузка | Амплитуда вертикального покачивания. |
| `render.motion.bob.roll-amplitude` | `0.8` | конечное число в диапазоне [0, 1000] | перезагрузка | Амплитуда горизонтального покачивания в градусах. |
| `render.motion.bob.frequency` | `9.0` | конечное число в диапазоне [0, 100] | перезагрузка | Частота отклика пружины. Более высокие значения быстрее стабилизируют камеру. |
| `render.motion.bob.damping` | `0.9` | конечное число в диапазоне [0, 10] | перезагрузка | Затухание пружины. Значение около 1 предотвращает превышение цели. При значении ниже 1 возможен отскок. |
| `render.motion.camera-follow.enabled` | `false` | `true` или `false` | перезагрузка | Включить смещение камеры, основанное на пружине. |
| `render.motion.camera-follow.frequency` | `20.0` | конечное число в диапазоне [0, 100] | перезагрузка | Частота отклика пружины. Более высокие значения быстрее стабилизируют камеру. |
| `render.motion.camera-follow.damping` | `0.1` | конечное число в диапазоне [0, 10] | перезагрузка | Затухание пружины. Значение около 1 предотвращает превышение цели. При значении ниже 1 возможен отскок. |
| `render.motion.camera-follow.yaw-position-gain` | `0` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Значение усиления поворота (yaw) для этого слоя движения. |
| `render.motion.camera-follow.pitch-position-gain` | `0` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Значение усиления наклона (pitch) для этого слоя движения. |
| `render.motion.camera-follow.maximum-offset` | `0.15` | конечное число в диапазоне [0, 100] | перезагрузка | Максимальное смещение камеры за игроком. |
| `render.motion.camera-follow.pitch-compensation` | `0.3` | конечное число в диапазоне [0, 100] | перезагрузка | Компенсация смещения для наклона камеры. |
| `render.motion.physics.enabled` | `true` | `true` или `false` | перезагрузка | Включить нативную физику для отмеченных костей. Без симуляции в BetterModel. |
| `render.motion.physics.acceleration-gain` | `4.0` | конечное число в диапазоне [-1000, 1000] | перезагрузка | Значение усиления, преобразующее ускорение игрока в наклон кости. |
| `render.motion.physics.maximum-angle` | `25` | конечное число в диапазоне [0, 180] | перезагрузка | Ограничение вращения нативной физики в градусах. |
| `render.motion.physics.frequency` | `4.0` | конечное число в диапазоне [0, 100] | перезагрузка | Реакция пружины раскачивания. Частота определяет скорость, с которой кость догоняет цель, а затухание — степень сопротивления перескоку (1.0 = отсутствие перескока вообще). При затухании ниже 1.0 кость раскачивается за пределы исходной позы и возвращается назад, что воспринимается как резкий рывок, когда игрок останавливается: при частоте 4.0 и затухании 0.6 возврат на 25 градусов перескакивает примерно на 2.1 градуса. Повышайте затухание до 1.0, чтобы убрать этот рывок, или снижайте частоту для более медленного и тяжелого успокоения. |
| `render.motion.physics.damping` | `0.6` | конечное число в диапазоне [0, 10] | перезагрузка | Затухание пружины. Значение около 1 предотвращает превышение цели. При значении ниже 1 возможен отскок. |
| `render.motion.physics.turn-gain` | `0.25` | конечное число в диапазоне [0, 1000] | перезагрузка | Дополнительный наклон на каждый градус/секунду поворота камеры. Сохраняйте его небольшим: покачивание мышью при взгляде вверх или вниз не должно выбрасывать кость вбок. |

## опции пакета ресурсов

| Параметр | По умолчанию | Допустимые значения | Когда применяется | Назначение |
| --- | --- | --- | --- | --- |
| `resource-pack.renderer-pack.enabled` | `false` | `true` или `false` | перезапуск | Разделение необязательных внешних ресурсов рендеринга сервера от общего пакета. Требуется перезапуск. |
| `resource-pack.renderer-pack.url` | `""` | внешняя URL-адреса ZIP (HTTP(S)) | перезапуск | Размещение здесь плагинов/Armature/renderer_pack.zip. {sha1} может версионировать URL. |
| `resource-pack.renderer-pack.prompt` | `"Armature server rendering"` | запрос на пакет ресурсов | перезапуск | Запрос клиента на пакет для отдельного серверного рендерера. |
| `resource-pack.generate-zip` | `false` | `true` или `false` | пересборка пакета | Генерация отдельного ZIP-архива в дополнение к выходу в виде директории. |
| `resource-pack.generation.on-startup` | `true` | `true` или `false` | перезагрузка | Автоматическая генерация для этого жизненного цикла событий. Явная пересборка пакета остается доступной. |
| `resource-pack.generation.on-reload` | `true` | `true` или `false` | перезагрузка | Генерировать автоматически для этого события жизненного цикла. Явное пересоздание пакета остается доступным. |
| `resource-pack.generation.on-provider-change` | `true` | `true` или `false` | перезагрузка | Генерировать автоматически для этого события жизненного цикла. Явное пересоздание пакета остается доступным. |
| `resource-pack.description` | `""` | описание, опционально пустое | пересборка пакета | Пустое сохраняет описание бэкенда. Не переименовывает финальный пакет провайдера. |
| `resource-pack.supported-version.min` | `"1.21.4"` | версия клиента из комплекта, `server` или `latest` | пересборка пакета | Сохраняется самый низкий слой наложения в пакете. Это не конвертирует исходные модели. |
| `resource-pack.supported-version.max` | `latest` | версия клиента из комплекта, `server` или `latest` | пересборка пакета | Сохраняется самый высокий слой наложения в пакете. Последняя выбирает последнее определение в пакете. |
| `resource-pack.validation.enable` | `true` | `true` или `false` | пересборка пакета | Проверить сгенерированную кандидатуру пакета. |
| `resource-pack.validation.fail-on-error` | `false` | `true` или `false` | пересборка пакета | Отклонять кандидатов при ошибках валидации, если включено. |
| `resource-pack.validation.check-models` | `true` | `true` или `false` | пересборка пакета | Проверка сгенерированных ссылок на модели. |
| `resource-pack.validation.check-textures` | `true` | `true` или `false` | пересборка пакета | Проверка сгенерированных ссылок на текстуры. |
| `resource-pack.validation.check-atlases` | `true` | `true` или `false` | пересборка пакета | Проверка ссылок на атласы и активы. |
| `resource-pack.optimization.json.enable` | `false` | `true` или `false` | пересборка пакета | Оптимизировать подходящие сгенерированные JSON. Исключенные пути сохраняются. |
| `resource-pack.optimization.json.exclude` | `[]` | паттерны глобуса, относительные к пакету | пересборка пакета | Исключения глобуса, относительные к пакету, только для этой оптимизации. |
| `resource-pack.optimization.texture.enable` | `false` | `true` или `false` | пересборка пакета | Оптимизировать генерируемые PNG-текстуры. Пути, исключенные из оптимизации, сохраняются. |
| `resource-pack.optimization.texture.compression-level` | `9` | целое число от 0 до 9 включительно | пересборка пакета | Уровень сжатия PNG (0-9). Не влияет на игровой процесс. |
| `resource-pack.optimization.texture.exclude` | `[]` | шаблоны глобальных паттернов, относительные к пакету | пересборка пакета | Исключения глобальных паттернов, относительных к пакету, для этой оптимизации только. |
| `resource-pack.zip.path` | `resource_pack.zip` | относительный путь ZIP-архива внутри Armature | пересборка пакета | Относительный путь вывода ZIP-архива, независимого от пакета, ниже папки plugins/Armature. |
| `resource-pack.zip.method` | `deflated` | `stored` или `deflated` | пересборка пакета | Выбрать несжатые записи (хранящиеся) или ZIP-deflate (сжатые). |
| `resource-pack.zip.compression-level` | `6` | целое число от 0 до 9 включительно | пересборка пакета | Уровень сжатия ZIP-deflate (0-9). |
| `resource-pack.collision-policy` | `keep-existing` | `keep-existing`, `replace` или `error` | пересборка пакета | Детерминированная владение при публикации одного нормализованного пути двумя источниками. keep-existing сохраняет первого внешнего владельца. Replace позволяет Armature выиграть, когда его сгенерированный источник задействован. Error прерывает кандидата. |
| `resource-pack.collision-rules` | `[]` | Правила столкновений с ограниченной областью | пересборка пакета | Опциональные правила первого совпадения: [{source: provider, namespace: ns, path: ns:foo/**, policy: replace}] |
| `resource-pack.exclusions.sources` | `[]` | Список идентификаторов владельцев источников в нижнем регистре | пересборка пакета | Идентификаторы владельцев источников нормализуются в нижний регистр и совпадают точно. |
| `resource-pack.exclusions.namespaces` | `[]` | Список идентификаторов пространств имен | пересборка пакета | Идентификаторы пространств имен нормализуются в нижний регистр и совпадают с assets/<namespace>/**. |
| `resource-pack.exclusions.paths` | `[]` | шаблоны glob относительно корня пакета. `*` соответствует одному сегменту, а `**` нескольким сегментам | пересборка пакета | Пути используют обратные слэши; * остается в одном сегменте, ** пересекает сегменты. |


## Связанные файлы

`variables.yml` объявляет общие переменные профиля; `messages.yml` управляет сообщениями администратора (`auto`, `en`, `fr`). Файлы профиля, модели и пользовательские текстуры брони имеют свои собственные директории. Эта конфигурация не содержит правил анимации профиля.

Продолжить с [Рендеринг и камера](../configuration/rendering-camera.md), [Движение и физика](../rasshirennye-vozmozhnosti/luchshie-nastroiki.md), [Пакеты ресурсов](../configuration/resource-packs.md), [Сkins и броня](../rasshirennye-vozmozhnosti/polzovatelskaya-bronya.md), или [редактор в игре](in-game-editor.md).
