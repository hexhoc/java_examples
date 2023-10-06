**Криптография** - это шифрование данных путем перестановки байт в
случайном порядке. Если мы сообщим второй стороне о том как именно мы из
переставляли, то он сможет сделать отратнуд операцию.

Шифрование бывает симметричное и асимметричное

**Симметричное** - encode и decode выполняется одним ключем.

**Асимметричное** - encode выполняется публичным ключом, decode
выполняется приватным(секретным) ключём.

Ассиметричное создает высокие накладные расходы на процессор, в отличие
от симметричного ключа.

**Как происходит симметричное шифрование:**

1. У меня есть ключ которым я шифрую данные.

2. Я им шифрую и отправляю данные

3. Сам ключ я должен передать второй стороне каким либо безопасным
   способом

4. Вторая сторона при помощи этого де ключа данные расшифровывает

 

**Как происходит асимметричное шифрование:**

1. Я подключаюсь к сайту. Допустим mail.ru

2. Сайт отправляет мне публичный ключ  

3. Я публичным ключом шифрую отправляемые данные.

4. Сайт принимает зашифрованные данные и расшифровывает их приватным
   ключём  


Даже если злоумышленник перехватит публичный ключ и зашифрованные
данные, то он все равно не сможет из расшифровать без приватного ключа.

**Как происходит SSL шифрование в упрощенном виде**

SSL использует гибридное шифрование (симметричное и асимметричное)

1. Подключаемся к сайту

2. Сайт высылает нам публичный ключ

3. Мы генерируем симметричный ключ случайным образом.

4. Шифруем этот ключ и отправляем его серверу. Теперь и у клиента и у
   сервера есть симметричный ключ, который был зашифрован и перед с
   использованием асимметричного ключа

1. Дальше весь объем с сервером происходит при помощи симметричного
   ключа

Сам симметричный ключ не передается при отправке данных, поэтому
злоумышленник может получить сами зашифрованные данные, но не может
получить симметричный ключ


Однако при помощи приема "man in the middle" злоумышленник может встать
между сайтом и клиентом и играть роль прокси, перехватывая все
необходимые ему данные.


**Как происходит SSL шифрование в на самом деле:**

1. Отправляем запрос на сайт

2. Сайт предоставляет нам свой подписанный центром сертификации
   сертификат

3. В каждом браузере есть уже встроенный список центров сертификации
   которым он доверяет. Потому мы проверяем полученный сертификат в центре
   сертификации и убеждаемся в том что он действительный.

4. Далее мы используем сертификат как публичный ключ для шифрования
   симметричного ключа и отправляем его на сервер

5. Сервер расшифровывает симметричный ключ

6. Обмен данными происходит по симметричному ключу


В корпоративной сети администраторы разворачивают собственный коренной
центр сертификации и добавляют его в браузеры сотрудников


===========================================================

**Как сгенерировать свой самоподписанный сертификат.**

Самоподписанный означает что этот сертификат подписан не центром
сертификации, а лично нами, тем кто его сделал.

 

1. Мы через библиотеку openssl генерируем приватный ключ по алгоритму
   шифрования RSA. openssl genrsa -out domain.key 2048

1. Далее закрытый ключ необходимо положить в сертификат, а сам
   сертификат отправить на подпись для заверения. Но поскольку мы
   генерируем уже самоподаисанный сертификат, нам его отправлять на подпись
   никуда не нужно. Поскольку все сертификаты SSL соответствуют стандарту
   x509. Команда openssl req -key domain.key -new -x509 -days 365 -out
   domain.crt

1. Теперь у нас есть публичный сертификат domain.crt подписанный
   приватным ключём domain.key. Сертификат является публичным и мы его
   можем раздавать клиентам

1. Мы можем прочитать содержимое сертификата - openssl x509 -in
   domain.crt -text

1. Чтобы прочитать сертификат вместе с цифровой подписью - openssl x509
   -in domain.crt -text -fingerprint

1. Чтобы убедиться что мы получили корректный сертификат при подключении
   к сайту, мы сверяем цифровую подпись. Мы можем посмотреть в браузере
   рядом с адресной строкой.

 

То, что мы в просторечии называем сертификатами SSL, в действительности
должно называться сертификатами X.509

 

.pem, .crt, .cer - готовый, подписанный центром сертификации сертификат,
расширения разные, но означают одно и то же. Если совсем просто, то
сертификат, это подписанный открытый ключ, плюс немного информации о
вашей компании;

 

.der - бинарная форма сертификата PEM. Так же является подписанным
сертификатом

 

.pfx - бинарная форма сертификата используемая в windows. Как правило
pfx защищён паролем. Из него можно извлечь файлы .crt (открытый ключ) и
.key (закрытый ключ)

 

.key - закрытый или открытый ключ;

.csr - запрос на подпись сертификата, в этом файле хранится ваш открытый
ключ плюс информация, о компании и домене, которую вы указали.

 

PKCS 12 - формат архивных файлов в криптографии, который хранит в себе
сам сертификат и секретный ключ. Пример расширений .p12 и .pfx

 

.jks - это разновидность PKCS 12 для java. Делятся на keystore.jks
содержит внутри клиентские или серверные сертификаты и приватный ключ,
поэтому этот архив защищен паролем. Truststore.jks содержит публичную
сертификаты или самоподписанные.

 

Keystore.jks необходим для настройки SSL на сервере.

 

Truststore.jks необходим для подключения к защищённому SSL серверу

 

**Oauth2** - это протокол используемый для аутентификации, который позволяет аутентифицировать себя
при помощи аутентификационных сервисов третьих сторон, они называются
identity provider (idp). И в последствии использовать полученный токен (access token)
для доступа к нашим сервисам. Валидация токена выполняется так же
сервисами третьих сторон.


**Выглядить это следующим образом:**

1. Мы пытаемся получить доступ к нашим ресурсам

2. Нас просят пройти аутентификацию на google.com. мы проходим
   аутентификацию на google.com

3. Google выдаёт нам токен с которым мы пытаемся снова получить доступ к
   ресурсам

4. Наш сервис берет токен и проверяет его в google. Если токен валидный,
   мы получаем доступ к ресурсам


**OpenID Connect (OIDC)** это протокол для авторизации верхний слой протокола oauth2 который
предоставляет возможность аутентификации и предоставляет информацию о пользователе (identity token или JWT). Т.е. если
сервис поддерживает OIDC то его можно считать identity provider (idp)

**JWT** - identity token содержит всю информацию о пользователе, его правах, логин, почта, последнее время входа и т.п.

**ЗАМЕТКИ**
Ребятки, всем привет!
Подскажите, на какие факторы вы опираетесь при выборе алгоритма шифрования JWT?
Вот возьмем в пример HS256 и RS256. Как вы понимаете, что нужно выбрать "конкретный"?
Разница мне ясна примерно) По моему мнению, HS256 более "уязвимый", т.к. обязывает микросервисы знать мастер ключ

Если токены надо валидировать снаружи (почти всегда надо), то нужен асиметричный алгоритм (RSA, EC).
Если не надо - можно симметричный (HMAC). Симметричное шифрование проще, но там разница прям в 5 минут работы, а асиметричный гибче в использовании