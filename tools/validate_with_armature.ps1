param(
    [Parameter(Mandatory=$true)][string]$ArmatureSource,
    [string]$Python = 'python',
    [string]$Java = 'java',
    [string]$Javac = 'javac',
    [string]$BlockbenchDefaultModel
)
$ErrorActionPreference = 'Stop'
$sourceRoot = (Resolve-Path -LiteralPath $ArmatureSource).Path
$wikiRoot = Split-Path -Parent $PSScriptRoot
$scratchRoot = Join-Path ([System.IO.Path]::GetTempPath()) ('armature-wiki-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $scratchRoot | Out-Null
$initPath = Join-Path $scratchRoot 'classpath.init.gradle'
@'
allprojects {
    afterEvaluate { p ->
        if (p.name == 'armature-plugin') tasks.register('wikiValidationClasspath') {
            doLast {
                new File(System.getProperty('armature.wiki.validationDir'), 'classpath.txt').text = p.sourceSets.test.runtimeClasspath.asPath
            }
        }
    }
}
'@ | Set-Content -LiteralPath $initPath -Encoding utf8
Push-Location $sourceRoot
try {
    & .\gradlew.bat :armature-plugin:testClasses :armature-plugin:wikiValidationClasspath -I $initPath "-Darmature.wiki.validationDir=$scratchRoot" --console=plain
    if ($LASTEXITCODE -ne 0) { throw 'Armature classpath/classes preparation failed' }
} finally { Pop-Location }
& $Python (Join-Path $PSScriptRoot 'validate_wiki.py') --output $scratchRoot
if ($LASTEXITCODE -ne 0) { throw 'GitBook structural validation failed' }
$classpath = Get-Content -LiteralPath (Join-Path $scratchRoot 'classpath.txt') -Raw
$classes = Join-Path $scratchRoot 'classes'
& $Javac -cp $classpath -d $classes (Join-Path $PSScriptRoot 'WikiValidation.java')
if ($LASTEXITCODE -ne 0) { throw 'Parser runner compilation failed' }
$models = @(
    (Join-Path $wikiRoot '.gitbook/assets/examples/wiki_player.armature'),
    (Join-Path $wikiRoot '.gitbook/assets/examples/wiki_charm.armature'),
    (Join-Path $sourceRoot 'armature-plugin/src/main/resources/models/default/arm_sword.bbmodel'),
    (Join-Path $sourceRoot 'armature-plugin/src/main/resources/models/default/arm_empty.bbmodel')
)
if ($BlockbenchDefaultModel) { $models += (Resolve-Path -LiteralPath $BlockbenchDefaultModel).Path }
& $Java -cp "$classes;$classpath" com.armaturemc.plugin.WikiValidation (Join-Path $scratchRoot 'wiki-yaml.json') $wikiRoot (Join-Path $scratchRoot 'fixtures') @models
if ($LASTEXITCODE -ne 0) { throw 'Armature YAML/profile/model validation failed' }
$javaSources = Join-Path $scratchRoot 'java'
& $Python (Join-Path $PSScriptRoot 'extract_java.py') (Join-Path $scratchRoot 'wiki-java.json') $javaSources
if ($LASTEXITCODE -ne 0) { throw 'Java source extraction failed' }
$files = @(Get-ChildItem -LiteralPath $javaSources -Filter '*.java' | ForEach-Object FullName)
& $Javac -cp $classpath -d (Join-Path $scratchRoot 'compiled-examples') @files
if ($LASTEXITCODE -ne 0) { throw 'Documented Java compilation failed' }
Write-Output "Automated checks complete. Evidence and fixtures retained in $scratchRoot. Live Paper registry/visual checks remain separate."
