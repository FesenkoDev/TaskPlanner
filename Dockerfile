# Используем OpenJDK 21
FROM openjdk:21

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR-файл в контейнер
COPY target/*.jar app.jar

# Команда для запуска приложения
CMD ["java", "-jar", "app.jar"]

