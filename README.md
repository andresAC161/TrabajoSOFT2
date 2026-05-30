## Tecnologías usadas

- Backend: Java 17 con Spring Boot 3.2 y PostgreSQL
- Frontend: Flutter para Android

## Qué necesitas instalar antes de correr el proyecto

- Java JDK 17 → https://adoptium.net (elegir JDK 17)
- Maven 3.9+ → https://maven.apache.org/download.cgi
- PostgreSQL 15 o 16 → https://www.postgresql.org/download/windows
- Flutter SDK → https://docs.flutter.dev/get-started/install/windows
- Android Studio → https://developer.android.com/studio (para el emulador)

## Paso 1 - Crear la base de datos

Esto solo se hace una vez. Abre pgAdmin o la terminal de PostgreSQL y ejecuta:

```sql
CREATE DATABASE pezcasesor;
```

Las tablas se crean solas cuando el backend inicia por primera vez.

El proyecto usa estas credenciales por defecto:
- Usuario: postgres
- Contraseña: 1234
- Puerto: 5432

Si tu contraseña es diferente, cámbiala en:
`backend/src/main/resources/application.properties`

## Paso 2 - Correr el backend

```bash
cd backend
mvn spring-boot:run
```

Cuando veas `Started PezcasesorApplication` en la consola, el backend está listo.

## Paso 3 - Correr la app Flutter

Primero inicia un emulador Android desde Android Studio, luego ejecuta:

```bash
cd frontend
flutter pub get
flutter run
```

El backend tiene que estar corriendo antes de abrir la app.
