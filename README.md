# Парсер отчетов избиркома

После голосования 2021 года избирком начал использовать скрамблер для усложнения парсинга результатов. Подробнее об
этом:

* https://www.facebook.com/sergey.shpilkin/posts/4392964697458799
* https://meduza.io/news/2021/09/19/na-sayte-tsik-zakodirovali-dannye-o-rezultatah-golosovaniya-teper-ih-budet-esche-trudnee-analizirovat

Для парсинга данных используется связка selenium + tesseract. После открытия нужной страницы в браузере страница
скроллится к нужному числовому значению, создается скриншот и скармливается в tesseract для распознавания числа.

## `StartUikParser.kt`

Используется для запуска логики парсинга информации (название, идентификатор, адрес) о всех УИКах с сайта изберкома.

## `StartUikPageParser.kt`

Используется для запуска парсинга статистики по каждому УИКу на основе данных, спаршеных с помощью `StartUikParser.kt`. 