# Socks_must_flow
## Технологии:
### Основа - Spring Boot, Spring Data JPA, REST API, PostgreSQL, Spring Security.
### Сборка - Maven 
### Контроль версий - Git, Flyway
### Тесты - JUnit 5, Mockito
### Дистрибьюция - Docker (docker-compose)
### UI - Vaadin (в процессе)

Тестовое задание для Raiffeisen DGTL, которое превратилось в пет проект по обучению новым технологиям.

## Проблемы и решения
Реализация была осуществлена в соответствии с требованиями headless REST API.
Была применена методология разработки TDD, что позволило покрыть проект полностью тестами.
В задании не было указано, но я добавил список доступных цветов, что позволило также фильтровать некорректные данные по цвету при приходе носков,
а также не допустить задвоения позиций (к примеру будет введен цвет "Синий" и "синий").
Далее уже вне тестового задания, а целях обучения были добавлены:
1. миграции БД через FlyWay
2. подключен Spring Security - добавлена аутентификация по токену + роли кладовщика и начальника склада. Кладовщик может только смотреть остатки, начальник склада может добавлять новых пользователей и списывать/принимать носки.
3. Приложение запаковано в docker-compose 
4. В процессе: Vaadin - UI для регистрации/логина + работы с остатками на складе

## Запуск приложения:
скачать или скопировать файл [docker-compose.yaml](https://github.com/Habbart/socks/blob/main/src/main/docker/docker-compose.yml)

запустить командой: docker-compose up
user: user
password: password

## Требования
Реализовать приложение для автоматизации учёта носков на складе магазина. Кладовщик должен иметь возможность:
учесть приход и отпуск носков;
узнать общее количество носков определенного цвета и состава в данный момент времени.
Внешний интерфейс приложения представлен в виде HTTP API (REST, если хочется).

### Список URL HTTP-методов (стандарнтно приложение стартует на localhost:8080)
#### POST /auth
Получение токена безопасности Bearer для зарегестрированных пользователей.
Формат сообщения для получения - JSON с указанием данных пользователя.
Изначально хранится один суперюзер "admin" с ролью "Начальник", через суперюзера можно назначать новых пользователей:

    "name":"admin",
    "surname":"admin",
    "login":"admin",
    "password":"admin"
    

#### POST /register
Регистрация нового пользователя.
Данные должны быть в боди в формате JSON.
Пример:

    "name":"Petr",
    "surname":"Ivanov",
    "login":"Petr_Ivanov",
    "password":"secret_password"
    
Роль будет присвоена обычный работник. Для присвоения ролей суперпользователя необходимо делать это вручную в БД.
Для роли "Работник" доступен только метод просмотра количества носков - GET /api/socks.
Все остальные методы доступны только для роли "Начальник". 

#### POST /api/socks/income
Регистрирует приход носков на склад.

#### Параметры запроса передаются в теле запроса в виде JSON-объекта со следующими атрибутами:

color — цвет носков, строка (например, black, red, yellow);
cottonPart — процентное содержание хлопка в составе носков, целое число от 0 до 100 (например, 30, 18, 42);
quantity — количество пар носков, целое число больше 0.
Результаты:

HTTP 200 — удалось добавить приход;

HTTP 400 — параметры запроса отсутствуют или имеют некорректный формат;

HTTP 500 — произошла ошибка, не зависящая от вызывающей стороны (например, база данных недоступна).

#### POST /api/socks/outcome
Регистрирует отпуск носков со склада. Здесь параметры и результаты аналогичные, но общее количество носков указанного цвета и состава не увеличивается, а уменьшается.

#### GET /api/socks
Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса.

Параметры запроса передаются в URL:

color — цвет носков, строка;
operation — оператор сравнения значения количества хлопка в составе носков, одно значение из: moreThan, lessThan, equal;
cottonPart — значение процента хлопка в составе носков из сравнения.

Результаты:

HTTP 200 — запрос выполнен, результат в теле ответа в виде строкового представления целого числа;

HTTP 400 — параметры запроса отсутствуют или имеют некорректный формат;

HTTP 500 — произошла ошибка, не зависящая от вызывающей стороны (например, база данных недоступна).

Примеры запросов:

/api/socks?color=red&operation=moreThan&cottonPart=90 — должен вернуть общее количество красных носков с долей хлопка более 90%;

/api/socks?color=black&operation=lessThan?cottonPart=10 — должен вернуть общее количество черных носков с долей хлопка менее 10%.

Для хранения данных системы можно использовать любую реляционную базу данных. Схему БД желательно хранить в репозитории в любом удобном виде.
