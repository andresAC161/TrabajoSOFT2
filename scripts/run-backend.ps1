param(
    [string]$DbHost = "localhost",
    [string]$DbPort = "5432",
    [string]$DbName = "pezcasesor",
    [string]$DbUser = "postgres",
    [string]$DbPassword = "user"
)

$ErrorActionPreference = "Stop"

$env:Path = @(
    [Environment]::GetEnvironmentVariable("Path", "Machine"),
    [Environment]::GetEnvironmentVariable("Path", "User"),
    $env:Path
) -join ";"

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host "No se encontro Maven en PATH. Instala Maven 3.9+ y vuelve a abrir PowerShell." -ForegroundColor Yellow
    exit 1
}

$Root = Split-Path -Parent $PSScriptRoot
$Backend = Join-Path $Root "backend"

$env:PGHOST = $DbHost
$env:PGPORT = $DbPort
$env:PGDATABASE = $DbName
$env:PGUSER = $DbUser
$env:PGPASSWORD = $DbPassword

Write-Host "Iniciando backend en http://localhost:8080"
Push-Location $Backend
try {
    mvn spring-boot:run
}
finally {
    Pop-Location
}
