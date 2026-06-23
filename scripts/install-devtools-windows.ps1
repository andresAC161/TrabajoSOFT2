param(
    [string]$InstallRoot = "$env:USERPROFILE\devtools",
    [string]$MavenVersion = "3.9.16"
)

$ErrorActionPreference = "Stop"

function Add-UserPathEntry {
    param([string]$PathEntry)

    $currentUserPath = [Environment]::GetEnvironmentVariable("Path", "User")
    $entries = @()
    if (-not [string]::IsNullOrWhiteSpace($currentUserPath)) {
        $entries = $currentUserPath -split ";" | Where-Object { -not [string]::IsNullOrWhiteSpace($_) }
    }

    $alreadyPresent = $entries | Where-Object { $_.TrimEnd("\") -ieq $PathEntry.TrimEnd("\") }
    if (-not $alreadyPresent) {
        $newPath = ($entries + $PathEntry) -join ";"
        [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
        Write-Host "Agregado al PATH de usuario: $PathEntry" -ForegroundColor Green
    }
    else {
        Write-Host "Ya estaba en PATH: $PathEntry" -ForegroundColor Green
    }

    if (($env:Path -split ";") -notcontains $PathEntry) {
        $env:Path = "$env:Path;$PathEntry"
    }
}

function Download-File {
    param(
        [string]$Url,
        [string]$OutputPath
    )

    Write-Host "Descargando $Url"
    Invoke-WebRequest -Uri $Url -OutFile $OutputPath
}

New-Item -ItemType Directory -Force -Path $InstallRoot | Out-Null

$mavenHome = Join-Path $InstallRoot "apache-maven-$MavenVersion"
$mavenBin = Join-Path $mavenHome "bin"
if (-not (Get-Command mvn -ErrorAction SilentlyContinue) -and -not (Test-Path (Join-Path $mavenBin "mvn.cmd"))) {
    $mavenZip = Join-Path $InstallRoot "apache-maven-$MavenVersion-bin.zip"
    $mavenUrl = "https://dlcdn.apache.org/maven/maven-3/$MavenVersion/binaries/apache-maven-$MavenVersion-bin.zip"
    Download-File -Url $mavenUrl -OutputPath $mavenZip
    Expand-Archive -Path $mavenZip -DestinationPath $InstallRoot -Force
}
Add-UserPathEntry $mavenBin
[Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "User")
$env:MAVEN_HOME = $mavenHome

$flutterHome = Join-Path $InstallRoot "flutter"
$flutterBin = Join-Path $flutterHome "bin"
if (-not (Get-Command flutter -ErrorAction SilentlyContinue) -and -not (Test-Path (Join-Path $flutterBin "flutter.bat"))) {
    if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
        Write-Host "No se encontro Git. Instala Git para Windows y vuelve a ejecutar este script." -ForegroundColor Yellow
        exit 1
    }

    Write-Host "Clonando Flutter stable desde el repositorio oficial..."
    git clone --depth 1 -b stable https://github.com/flutter/flutter.git $flutterHome
}
Add-UserPathEntry $flutterBin

Write-Host ""
Write-Host "Versiones instaladas/detectadas:"
mvn -version
flutter --version

Write-Host ""
Write-Host "Listo. Abre una nueva terminal para que Windows recargue el PATH, o sigue usando esta terminal si el script la abrio."
