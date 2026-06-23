param()

$ErrorActionPreference = "Stop"

$env:Path = @(
    [Environment]::GetEnvironmentVariable("Path", "Machine"),
    [Environment]::GetEnvironmentVariable("Path", "User"),
    $env:Path
) -join ";"

$Root = Split-Path -Parent $PSScriptRoot
$Frontend = Join-Path $Root "frontend"
$Missing = $false

function Test-RequiredCommand {
    param(
        [string]$Name,
        [string]$InstallUrl
    )

    if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
        Write-Host "Falta $Name. Instalar: $InstallUrl" -ForegroundColor Yellow
        $script:Missing = $true
        return
    }

    Write-Host "OK $Name" -ForegroundColor Green
}

Test-RequiredCommand "java" "https://adoptium.net/temurin/releases/?version=17"
Test-RequiredCommand "mvn" "https://maven.apache.org/download.cgi"
Test-RequiredCommand "flutter" "https://docs.flutter.dev/get-started/install/windows"

if ($Missing) {
    Write-Host ""
    Write-Host "Instala lo faltante, cierra y abre PowerShell, y vuelve a ejecutar este script."
    exit 1
}

Write-Host ""
Write-Host "Verificando Java, Maven y Flutter..."
java -version
mvn -version
flutter doctor

Write-Host ""
Write-Host "Preparando plataformas Flutter para Windows y Android..."
Push-Location $Frontend
try {
    flutter config --enable-windows-desktop

    if (-not (Test-Path "windows")) {
        flutter create --platforms=windows .
    }

    if (-not (Test-Path "android")) {
        flutter create --platforms=android .
    }

    flutter pub get
}
finally {
    Pop-Location
}

Write-Host ""
Write-Host "Preparacion lista."
Write-Host "Crea la base de datos PostgreSQL si aun no existe:"
Write-Host "  CREATE DATABASE pezcasesor;"
Write-Host ""
Write-Host "Luego ejecuta en dos terminales:"
Write-Host "  .\scripts\run-backend.ps1"
Write-Host "  .\scripts\run-frontend.ps1 -Device windows"
