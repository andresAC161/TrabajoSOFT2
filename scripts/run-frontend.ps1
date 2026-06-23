param(
    [ValidateSet("windows", "android", "chrome")]
    [string]$Device = "windows",
    [string]$ApiBaseUrl = "",
    [string]$FlutterDeviceId = "",
    [int]$WebPort = 3000
)

$ErrorActionPreference = "Stop"

$env:Path = @(
    [Environment]::GetEnvironmentVariable("Path", "Machine"),
    [Environment]::GetEnvironmentVariable("Path", "User"),
    $env:Path
) -join ";"

if (-not (Get-Command flutter -ErrorAction SilentlyContinue)) {
    Write-Host "No se encontro Flutter en PATH. Instala Flutter y vuelve a abrir PowerShell." -ForegroundColor Yellow
    exit 1
}

$Root = Split-Path -Parent $PSScriptRoot
$Frontend = Join-Path $Root "frontend"

Push-Location $Frontend
try {
    if ($Device -eq "windows") {
        flutter config --enable-windows-desktop
        if (-not (Test-Path "windows")) {
            flutter create --platforms=windows .
        }
    }

    if ($Device -eq "android" -and -not (Test-Path "android")) {
        flutter create --platforms=android .
    }

    if ($Device -eq "chrome" -and -not (Test-Path "web")) {
        flutter create --platforms=web .
    }

    if ([string]::IsNullOrWhiteSpace($ApiBaseUrl)) {
        if ($Device -eq "android") {
            $ApiBaseUrl = "http://10.0.2.2:8080/api"
        }
        else {
            $ApiBaseUrl = "http://localhost:8080/api"
        }
    }

    flutter pub get

    if ($Device -eq "android" -and [string]::IsNullOrWhiteSpace($FlutterDeviceId)) {
        $devices = flutter devices --machine | ConvertFrom-Json
        $androidDevice = $devices |
            Where-Object { $_.targetPlatform -like "android-*" } |
            Select-Object -First 1

        if (-not $androidDevice) {
            Write-Host "No se encontro un emulador o dispositivo Android disponible." -ForegroundColor Yellow
            Write-Host "Abre un emulador desde Android Studio o conecta un celular con depuracion USB."
            flutter devices
            exit 1
        }

        $FlutterDeviceId = $androidDevice.id
    }

    $runArgs = @("run")
    if (-not [string]::IsNullOrWhiteSpace($FlutterDeviceId)) {
        $runArgs += @("-d", $FlutterDeviceId)
    }
    elseif ($Device -ne "android") {
        $runArgs += @("-d", $Device)
    }

    if ($Device -eq "chrome") {
        $runArgs += @("--web-hostname", "localhost", "--web-port", "$WebPort")
    }

    $runArgs += @("--dart-define", "API_BASE_URL=$ApiBaseUrl")
    flutter @runArgs
}
finally {
    Pop-Location
}
