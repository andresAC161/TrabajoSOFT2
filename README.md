# Pezcasesor

Proyecto con backend Spring Boot y frontend Flutter. Estos pasos estan pensados para correrlo en Windows.

## Requisitos

- Java JDK 17 recomendado: https://adoptium.net/temurin/releases/?version=17
- Maven 3.9 o superior: https://maven.apache.org/download.cgi
- PostgreSQL 15 o 16: https://www.postgresql.org/download/windows
- Flutter SDK para Windows: https://docs.flutter.dev/get-started/install/windows
- Visual Studio con "Desktop development with C++" si quieres correr Flutter en Windows Desktop.
- Android Studio si quieres correr Flutter en emulador Android.

Despues de instalar Java, Maven o Flutter, cierra y abre PowerShell para que Windows recargue el PATH.

## Preparar el proyecto

Desde la raiz del proyecto:

```powershell
.\scripts\setup-windows.ps1
```

Este script verifica Java, Maven y Flutter, ejecuta `flutter doctor`, agrega las carpetas Flutter que falten para `windows` y `android`, y descarga las dependencias Dart.

## Crear la base de datos

En pgAdmin o psql:

```sql
CREATE DATABASE pezcasesor;
```

Credenciales por defecto del backend en esta PC:

- Host: `localhost`
- Puerto: `5432`
- Base de datos: `pezcasesor`
- Usuario: `postgres`
- Password: `user`

Si tus credenciales son diferentes puedes pasarlas al script del backend:

```powershell
.\scripts\run-backend.ps1 -DbUser postgres -DbPassword TU_PASSWORD
```

## Correr el backend

En una terminal:

```powershell
.\scripts\run-backend.ps1
```

Cuando veas `Started PezcasesorApplication`, el API queda disponible en:

```text
http://localhost:8080/api
```

## Correr el frontend

En otra terminal, para app Windows:

```powershell
.\scripts\run-frontend.ps1 -Device windows
```

Para emulador Android:

```powershell
.\scripts\run-frontend.ps1 -Device android
```

Si tienes varios dispositivos Android, mira los ids con `flutter devices` y pasa el que quieres usar:

```powershell
.\scripts\run-frontend.ps1 -Device android -FlutterDeviceId emulator-5554
```

Para navegador Chrome:

```powershell
.\scripts\run-frontend.ps1 -Device chrome
```

Si corres Android en un celular fisico, reemplaza la URL del API por la IP de tu PC en la misma red:

```powershell
.\scripts\run-frontend.ps1 -Device android -ApiBaseUrl "http://192.168.1.50:8080/api"
```

## Comandos manuales equivalentes

Backend:

```powershell
cd backend
mvn spring-boot:run
```

Frontend:

```powershell
cd frontend
flutter create --platforms=windows,android .
flutter pub get
flutter run -d windows --dart-define "API_BASE_URL=http://localhost:8080/api"
```
