buildscript {
    repositories {
        mavenCentral()
        maven {
            name = "forge"
            url = "http://files.minecraftforge.net/maven"
        }
    }
    dependencies {
       classpath 'net.minecraftforge.gradle:ForgeGradle:1.0-SNAPSHOT'
    }
}

apply plugin: 'idea'
apply plugin: 'forge'
apply plugin: 'scala'
apply plugin: 'maven'
apply plugin: 'maven-publish'

ext.buildProps = file "build.properties"
 
buildProps.withReader {
    def prop = new Properties()
    prop.load(it)
    ext.config = new ConfigSlurper().parse prop
}
 
version = "MC${config.version.minecraft}-F${config.version.forge}_v${config.version.mod.major}.${config.version.mod.minor}${config.version.mod.revis}"
group = "minechem"
archivesBaseName = "MineChem"

minecraft {
    version = "${config.version.minecraft}-${config.version.forge}"
	
	replaceIn "ModMinechem.java"
	replace "@MAJOR@", config.version.mod.major
	replace "@MINOR@", config.version.mod.minor
	replace "@REVIS@", config.version.mod.revis
	
	if (System.getenv("BUILD_NUMBER") != null) {
		replace "@BUILD@", System.getenv("BUILD_NUMBER")
	}
	else
	{
		replace "@BUILD@", "[NIGHTLY]"
	}
}

if (System.getenv("BUILD_NUMBER") != null) {
    version += ".${System.getenv("BUILD_NUMBER")}";
}
else {
	version = "MC${config.version.minecraft}-F${config.version.forge}_[NIGHTLY]"
}

processResources {
	from 'build.properties'
}

allprojects {
    version = "${config.version.mod.major}.${config.version.mod.minor}.${config.version.mod.revis}"
    
    if (System.getenv("BUILD_NUMBER") != null)
        version += ".${System.getenv("BUILD_NUMBER")}"
        
	repositories {
        maven {
            name 'Calclavia Maven'
            url 'http://calclavia.com/maven'
        }
        maven {
            name = "forge"
            url = "http://files.minecraftforge.net/maven"
        }
    	maven { // the repo from which to get NEI and stuff
        	name 'CB Repo'
            	url "http://chickenbones.net/maven/"
    	}
	ivy {
		name 'FMP'
		artifactPattern "http://files.minecraftforge.net/[module]/[module]-dev-[revision].[ext]"
	}
        mavenCentral()
    }
    
    dependencies {
        compile group: 'universalelectricity', name: 'Universal-Electricity', version: "${rootProject.config.version.universalelectricity}", classifier: "dev"

        compile name: 'CodeChickenLib', version: "${config.version.minecraft}-${config.version.cclib}", ext: 'jar'
        compile name: 'NotEnoughItems', version: "${config.version.nei}", ext: 'jar'
        compile name: 'CodeChickenCore', version: "${config.version.cccore}", ext: 'jar'
    }
    
    jar {
		
        dependsOn ":copyBuildXml"
        destinationDir = file (rootProject.getRootDir().getPath() + '/output')
    }
	
	publishing {
		publications {
			mavenJava(MavenPublication) {
				artifact jar
			}
		}
		repositories {
			maven {
				url "file://var/www/maven"
			}
		}
	}
}
 
task copyBuildXml(type: Copy) {
    from 'build.properties'
    into 'output'
}
 
task apiZip(type: Zip) {
    classifier = 'api'
    from sourceSets*.allSource
    include 'minechem/api/**'
    destinationDir = file 'output'
}

task runClient(type: JavaExec) {
    classpath = sourceSets.main.runtimeClasspath
    main = "net.minecraft.launchwrapper.Launch"
    args("--version", "1.6",
        "--tweakClass", "cpw.mods.fml.common.launcher.FMLTweaker",
        "--accessToken", "FML")
    jvmArgs("-Xincgc",
        "-Xmx1024M",
        "-Xms1024M",
        "-Djava.library.path=${project.buildDir}/natives",
        "-Dfml.ignoreInvalidMinecraftCertificates=true")
    workingDir("eclipse")
}

task runServer(type: JavaExec) {
    classpath = sourceSets.main.runtimeClasspath
    main = "cpw.mods.fml.relauncher.ServerLaunchWrapper"
    jvmArgs("-Xincgc", "-Dfml.ignoreInvalidMinecraftCertificates=true")
}
	
artifacts {
    archives apiZip
}

build.dependsOn "apiZip", "copyBuildXml"
