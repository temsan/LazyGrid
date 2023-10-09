# Используйте официальный образ PostgreSQL
FROM postgres:latest

# Устанавливаем переменные окружения для настройки БД
ENV POSTGRES_DB vaadin26
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD weekago13s

# Этот порт будет прослушиваться PostgreSQL
EXPOSE 5432