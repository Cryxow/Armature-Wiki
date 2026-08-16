# 📟 Команды и permissions

## `/armature reload`

Перезагружает `config.yml`, все профили и assets моделей BetterModel. Активные player rigs удаляются и синхронизируются заново. При ошибке validation Armature сохраняет предыдущие активные профили.

Permission: `armature.admin`

## Операционные заметки

* Выполняйте reload после изменения YAML или имён моделей resource pack.
* После изменения зависимостей или JAR плагина нужен restart сервера.
* Не используйте `/reload`; используйте `/armature reload` для очистки состояния плагина.

***

## `/armature perf`

Показывает bossbar с performance statistics плагина в реальном времени.

Permission: `armature.admin`
