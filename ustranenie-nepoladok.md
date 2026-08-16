# 🔭 Устранение неполадок

## Плагин отключается при запуске

Проверьте, что BetterModel установлен и включён до Armature. BetterModel — обязательная зависимость рендера.

## ModelEngine вызывает проблемы

На самом деле причина — BetterModel. Добавьте `meg:` перед любой механикой ModelEngine в MythicMobs.

## Модель невидима

Проверьте `render.enabled: true`, identity предмета профиля и ID `model` BetterModel. Убедитесь, что model asset загружен и игрок применил resource pack.

## Анимация не воспроизводится

Сравните имя анимации профиля с точным именем анимации BetterModel. Проверьте startup/reload warnings об отсутствующих анимациях. Для действий провайдера убедитесь, что plugin провайдера включён, а его item identity соответствует значению `item` профиля.

## Движение слишком сильное

Уменьшите соответствующий `*-gain`, amplitude или `sway.maximum-rotation`. Camera-follow влияет на позицию, sway — на rotation. Настраивайте по одному слою.

## Tracker существует, но перестал обновляться

Включите `debug.enabled` и `debug.tracker-lifecycle`, воспроизведите проблему с одним игроком и проверьте, запланирован ли tracker и завершился ли переход action/loop. Нужны данные live-сервера; успешная компиляция не доказывает исправность tracker.

## Пустая рука показывает невидимый предмет или бумагу

Установите [PacketEvents](https://modrinth.com/plugin/packetevents), включите `debug.enabled` и `debug.empty-hand-mask`, воспроизведите проблему один раз и проверьте выбранный raw slot и изменение серверного инвентаря. `render.empty-hand-mask.repair-server-materialization` — узкая экспериментальная repair-функция для распознанного Armature mask.

## Что приложить к отчёту

Укажите версию Armature, Minecraft/Paper, BetterModel, список провайдеров, проблемный профиль, `config.yml`, startup/reload warnings и debug logs одного затронутого игрока. При необходимости скройте имена игроков.
