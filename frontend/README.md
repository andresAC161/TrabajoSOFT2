# Pezcasesor Frontend

Aplicacion Flutter del proyecto Pezcasesor.

## Windows

Desde la raiz del repositorio:

```powershell
.\scripts\run-frontend.ps1 -Device windows
```

Si prefieres hacerlo manualmente:

```powershell
cd frontend
flutter create --platforms=windows .
flutter pub get
flutter run -d windows --dart-define "API_BASE_URL=http://localhost:8080/api"
```

## Android

Para emulador Android:

```powershell
.\scripts\run-frontend.ps1 -Device android
```

El emulador usa `http://10.0.2.2:8080/api` para conectarse al backend de tu PC.

Si tienes varios dispositivos conectados, elige uno con:

```powershell
flutter devices
.\scripts\run-frontend.ps1 -Device android -FlutterDeviceId emulator-5554
```

Para celular fisico, usa la IP de tu PC:

```powershell
.\scripts\run-frontend.ps1 -Device android -ApiBaseUrl "http://192.168.1.50:8080/api"
```
