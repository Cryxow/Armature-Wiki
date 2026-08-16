# 🧸 Модель BlockBench

## Требования

* Установите [BlockBench](https://www.blockbench.net/)
* Установите плагин [Cameras](https://github.com/JannisX11/blockbench-plugins/blob/master/plugins/cameras.js) в BlockBench

## Создание модели

Для создания модели рекомендуется дублировать файл `empty_hands.bbmodel`, чтобы получить правильную настройку рук.

Модель будет выглядеть так:

<figure><img src="https://3676679775-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FDcWT3onsbt69LDkzkXac%2Fuploads%2FU4QWjdVh8f2EbaSU83tj%2Fimage.png?alt=media&#x26;token=bd2d2383-1585-46bf-a13d-4c41d6f55233" alt="Скриншот BlockBench с моделью рук по умолчанию."><figcaption><p>Скриншот BlockBench с моделью рук по умолчанию.</p></figcaption></figure>

Armature использует встроенные [префиксы костей BetterModel](https://github.com/toxicity188/BetterModel/wiki/Create-player-animation#make-your-animation):

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

## Добавление предмета

По умолчанию модель автоматически отображает предметы в руках через `pli_left_item` и `pri_right_item`. Эти bones можно удалить и добавить предмет вручную.

Ручное добавление полезно для оружия: модель `fp_rifle.bbmodel` даёт полный контроль.

## Анимация модели

### Настройка viewport

Рекомендуется разделить viewport через `Double Horizontal`, чтобы видеть анимацию глазами игрока.

![](https://3676679775-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FDcWT3onsbt69DlAqumqZ%2Fuploads%2FQ5ACsUhcx9dU44NhmixY%2Fimage.png?alt=media\&token=39dcb3dd-ca3c-4713-8058-3bb28f1b78bd)

<figure><img src="https://3676679775-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FDcWT3onsbt69DlAqumqZ%2Fuploads%2FCKt6AbrWg7Q1jK98tu6E%2Fimage.png?alt=media&#x26;token=cd06fd41-f071-4195-b93c-0ca9b212c72c" alt=""><figcaption></figcaption></figure>

Маленький прямоугольник в верхней части viewport — экран игрока.

### Анимации

Некоторые анимации уже есть. Их можно удалить и создать свои или использовать как основу.

Рекомендуется [гайд ModelEngine по анимации](https://git.lumine.io/mythiccraft/model-engine-4/-/wikis/Modeling/Animating-a-Model): он хорошо объясняет процесс и работает так же с BetterModel.

#### Правила имён анимаций

<mark style="color:$danger;">**⚠️ НЕ ИСПОЛЬЗУЙТЕ ЭТИ ТОЧНЫЕ ИМЕНА В АНИМАЦИЯХ ⚠️**</mark>

<mark style="color:$danger;">`idle`</mark>, <mark style="color:$danger;">`walk`</mark>, <mark style="color:$danger;">`idle_fly`</mark>, <mark style="color:$danger;">`walk_fly`</mark>, <mark style="color:$danger;">`spawn`</mark>

В BetterModel есть встроенная система actions, которая автоматически запускает эти имена; Armature не может это предотвратить. Возникают glitches из-за конфликта двух плагинов.

Используйте другие имена или добавьте prefix/suffix: `armature_idle`, `a_idle`, `base`, `idlee` и т. д.
