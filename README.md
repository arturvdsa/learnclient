# LearnClient (Fabric 1.21.11)

Учебный клиентский мод: ClickGUI на правый Shift и несколько модулей. Только для одиночной игры.

## Сборка jar на компьютере

1. Установите JDK 21 (adoptium.net). Проверка: `java -version`.
2. Распакуйте архив в папку без русских букв и пробелов в пути.
3. Добавьте Gradle wrapper: на fabricmc.net/develop выберите 1.21.11, скачайте шаблон
   и скопируйте из него в папку проекта `gradlew`, `gradlew.bat` и папку `gradle/`.
   Файлы `build.gradle`, `gradle.properties`, `settings.gradle` оставьте свои.
4. В терминале в папке проекта:
   - Windows: `gradlew.bat build`
   - Linux/Mac: `./gradlew build`
5. Готовый файл: `build/libs/learnclient-1.0.0.jar` (не тот, что с `-sources`).

Быстрая проверка без установки: `gradlew runClient`.

## Установка в игру

1. Установите Fabric Loader для 1.21.11 (fabricmc.net/use).
2. Положите в `.minecraft/mods`: Fabric API и `learnclient-1.0.0.jar`.
3. В игре откройте меню правым Shift.

## Сборка без компьютера (GitHub Actions)

Загрузите все файлы проекта (включая скрытую папку `.github`) в репозиторий на GitHub.
Во вкладке Actions запустится сборка, jar будет в Artifacts как `learnclient-jar`.

## Управление

- Правый Shift: открыть/закрыть меню
- ЛКМ по модулю: включить/выключить
- ПКМ по заголовку панели: свернуть
- Перетаскивание заголовка: переместить панель
- Поле внизу: поиск по модулям (латиница)

## Если сборка выдаёт ошибки

Версии в `gradle.properties` сверяйте с fabricmc.net/develop для 1.21.11.
Текст ошибки можно прислать Claude, он исправит код.
