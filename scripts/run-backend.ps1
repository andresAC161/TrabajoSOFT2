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

# Spring Boot 3.2 soporta Java 17 a 21. Si el Java por defecto es mas nuevo (p.ej. 24),
# la compilacion falla con "Unsupported class file major version". Aqui buscamos un JDK compatible.
function Get-JavaMajor([string]$javaExe) {
    if (-not (Test-Path $javaExe)) { return $null }
    $out = & $javaExe -version 2>&1 | Out-String
    if ($out -match 'version "([0-9]+)') {
        return [int]$Matches[1]
    }
    return $null
}

function Test-CompatibleJdk([string]$javaHome) {
    if ([string]::IsNullOrWhiteSpace($javaHome)) { return $false }
    $exe = Join-Path $javaHome "bin\java.exe"
    $major = Get-JavaMajor $exe
    return ($null -ne $major -and $major -ge 17 -and $major -le 21)
}

if (-not (Test-CompatibleJdk $env:JAVA_HOME)) {
    $candidates = @(
        "C:\Program Files\Android\Android Studio\jbr",
        "C:\Program Files\Eclipse Adoptium\jdk-21",
        "C:\Program Files\Eclipse Adoptium\jdk-17",
        "C:\Program Files\Java\jdk-21",
        "C:\Program Files\Java\jdk-17",
        "C:\Program Files\Microsoft\jdk-21",
        "C:\Program Files\Microsoft\jdk-17"
    )
    $found = $candidates |
        Where-Object { $_ } |
        ForEach-Object { (Get-ChildItem $_ -Directory -ErrorAction SilentlyContinue).FullName; $_ } |
        Select-Object -Unique |
        Where-Object { Test-CompatibleJdk $_ } |
        Select-Object -First 1

    if ($found) {
        $env:JAVA_HOME = $found
        $env:Path = (Join-Path $found "bin") + ";" + $env:Path
        Write-Host "Usando JDK compatible en $found" -ForegroundColor Cyan
    }
    else {
        $current = Get-JavaMajor ((Get-Command java -ErrorAction SilentlyContinue).Source)
        Write-Host "Advertencia: no se encontro un JDK 17-21. Java actual: $current." -ForegroundColor Yellow
        Write-Host "Spring Boot 3.2 puede fallar con Java 22+. Instala Temurin 17 o 21: https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Yellow
    }
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
