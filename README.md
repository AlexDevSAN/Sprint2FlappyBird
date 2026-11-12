# Flappy Bird

Аркада на Java и libGDX, проект спринта 2 курса «Разработка мобильных игр» ИТ Школы Samsung.
Птица летит между колоннами: тап — взмах вверх, без тапов она падает.

## Как играть
Меню → выбор карты → выбор птицы → выбор уровня → игра.
- Тап — взмах.
- Очко за каждую пролетевшую пару колонн.
- Врезался в колонну или вылетел за экран — конец игры.
- Restart — новая игра с теми же настройками, Main Menu — выбрать заново.

## Что есть в игре
- Две карты: город и пещера, у каждой свой фон, колонны и музыка.
- Три птицы, у каждой свой шлейф: дымок, перья, искры.
- Три уровня сложности: Easy, Medium, Hard (скорость колонн и высота просвета).
- Музыка и звуки.
- Пять достижений за одну игру: Close Shave, Hat Trick, Perfect Ten, Cave Explorer, Kamikaze.

## Как запустить
- Компьютер: `./gradlew lwjgl3:run`
- Телефон: модуль `android` в Android Studio.

## Звуки и музыка
Все под лицензией CC0:
- Juhani Junkala — «4 Chiptunes (Adventure)» и «512 Sound Effects (8-bit style)», opengameart.org;
- Wolfgang_ — «8-Bit Cave Loop», opengameart.org;
- Kenney — «Interface Sounds» и «Music Jingles», kenney.nl.
