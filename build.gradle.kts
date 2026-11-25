plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.1.0"
    id("com.gradleup.shadow") version "9.3.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.codemc.io/repository/maven-releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.74-stable")

    compileOnly("org.projectlombok:lombok:1.18.48")
    annotationProcessor("org.projectlombok:lombok:1.18.48")

    implementation("com.mysql:mysql-connector-j:26.10.0")

    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    implementation("com.github.cryptomorin:XSeries:14.0.0")

    implementation("com.github.retrooper:packetevents-spigot:2.14.0")

    implementation("com.google.code.gson:gson:2.15.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    runServer {
        minecraftVersion("26.1.2")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        relocate("co.aikar.commands", "io.github.tomthebuddhacat.libs.acf")
        relocate("co.aikar.locales", "io.github.tomthebuddhacat.libs.locales")

        relocate("com.cryptomorin.xseries", "io.github.tomthebuddhacat.utils")

        relocate("com.github.retrooper", "io.github.tomthebuddhacat.libs.packetevents")
        relocate("io.github.retrooper", "io.github.tomthebuddhacat.libs.packetevents")

        relocate("com.google.gson", "io.github.tomthebuddhacat.libs.gson")

        archiveClassifier.set("")
    }

    jar {
        archiveFileName.set("Wizard.jar")
    }
}
