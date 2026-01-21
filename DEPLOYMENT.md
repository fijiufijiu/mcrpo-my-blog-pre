# Инструкция по развертыванию

## Быстрый старт

### 1. Сборка бэкенда

```bash
cd my-blog-back-app
mvn clean package
```

Результат: `target/my-blog-back-app.war`

### 2. Развертывание в Tomcat

#### Установка Tomcat (если не установлен)

**macOS (через Homebrew):**
```bash
brew install tomcat
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install tomcat10
```

**Windows:**
Скачайте с официального сайта: https://tomcat.apache.org/download-10.cgi

#### Развертывание WAR файла

**Вариант 1: Через webapps директорию**

```bash
# Остановите Tomcat если запущен
$CATALINA_HOME/bin/shutdown.sh  # Linux/Mac
$CATALINA_HOME/bin/shutdown.bat # Windows

# Скопируйте WAR файл
cp target/my-blog-back-app.war $CATALINA_HOME/webapps/ROOT.war

# Запустите Tomcat
$CATALINA_HOME/bin/startup.sh  # Linux/Mac
$CATALINA_HOME/bin/startup.bat # Windows
```

**Вариант 2: Через Manager Web App**

1. Откройте `http://localhost:8080/manager/html`
2. Войдите с учетными данными Tomcat
3. В разделе "WAR file to deploy" выберите файл `my-blog-back-app.war`
4. Нажмите "Deploy"

#### Проверка развертывания

Откройте в браузере: `http://localhost:8080/api/posts?search=&pageNumber=1&pageSize=10`

Должен вернуться JSON с пустым списком постов:
```json
{
  "posts": [],
  "hasPrev": false,
  "hasNext": false,
  "lastPage": 1
}
```

### 3. Запуск фронтенда

```bash
cd ../my-blog-front-app
docker compose up -d
```

Проверка: `http://localhost:3000`

### 4. Использование приложения

1. Откройте браузер: `http://localhost:3000`
2. Нажмите на иконку карандаша для создания поста
3. Заполните форму и нажмите "Добавить"
4. Пост появится в ленте

## Альтернативные способы развертывания

### Jetty

```bash
# Через Maven plugin
mvn jetty:run

# Или разверните WAR в Jetty standalone
cp target/my-blog-back-app.war $JETTY_HOME/webapps/ROOT.war
java -jar $JETTY_HOME/start.jar
```

### Встроенный Tomcat (для разработки)

Можно использовать Maven плагин Tomcat:

Добавьте в `pom.xml`:
```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <port>8080</port>
        <path>/</path>
    </configuration>
</plugin>
```

Запуск:
```bash
mvn tomcat7:run
```

## Проверка работы API

### Создание поста

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Мой первый пост",
    "text": "Это тестовый пост",
    "tags": ["тест", "первый"]
  }'
```

### Получение списка постов

```bash
curl "http://localhost:8080/api/posts?search=&pageNumber=1&pageSize=10"
```

### Получение поста по ID

```bash
curl http://localhost:8080/api/posts/1
```

### Добавление лайка

```bash
curl -X POST http://localhost:8080/api/posts/1/likes
```

### Создание комментария

```bash
curl -X POST http://localhost:8080/api/posts/1/comments \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Отличный пост!",
    "postId": 1
  }'
```

## Логи

### Tomcat логи

```bash
# Логи приложения
tail -f $CATALINA_HOME/logs/catalina.out

# Логи доступа
tail -f $CATALINA_HOME/logs/localhost_access_log.*.txt
```

### Логи приложения

Логи выводятся в консоль и в Tomcat `catalina.out`. Уровень логирования настраивается в `logback.xml`.

## Troubleshooting

### Приложение не запускается

1. Проверьте логи Tomcat
2. Убедитесь, что порт 8080 свободен:
   ```bash
   lsof -i :8080  # Mac/Linux
   netstat -ano | findstr :8080  # Windows
   ```
3. Проверьте версию Java:
   ```bash
   java -version
   # Должна быть Java 21 или выше
   ```

### CORS ошибки

Убедитесь, что `CorsFilter` правильно настроен в `web.xml`.

### Ошибки базы данных

H2 база данных создается автоматически при старте. Если возникают проблемы:
1. Проверьте `schema.sql`
2. Включите H2 console для отладки (см. `application.properties`)

### Фронтенд не подключается к бэкенду

1. Проверьте, что бэкенд доступен: `curl http://localhost:8080/api/posts?search=&pageNumber=1&pageSize=10`
2. Проверьте логи браузера (F12 -> Console)
3. Убедитесь, что CORS настроен правильно

## Производственное развертывание

Для production окружения рекомендуется:

1. **Использовать PostgreSQL** вместо H2:
   - Добавьте PostgreSQL драйвер в `pom.xml`
   - Обновите `application.properties`
   - Создайте отдельную БД

2. **Настроить CORS** более строго:
   - Ограничьте разрешенные origins в `CorsFilter.java`

3. **Настроить логирование**:
   - Включите ротацию логов
   - Настройте уровни логирования для production

4. **Использовать HTTPS**:
   - Настройте SSL сертификаты в Tomcat
   - Настройте обратный прокси (nginx/Apache)

5. **Оптимизация**:
   - Настройте пул соединений БД
   - Включите кэширование
   - Настройте мониторинг

## Остановка приложения

### Tomcat
```bash
$CATALINA_HOME/bin/shutdown.sh  # Linux/Mac
$CATALINA_HOME/bin/shutdown.bat # Windows
```

### Фронтенд
```bash
cd my-blog-front-app
docker compose down
```

