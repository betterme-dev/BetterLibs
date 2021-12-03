/*
 * Copyright (c) 2018. The DebLibs Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri(System.getenv().getOrDefault("NEXUS_URL", ""))
            credentials {
                username = System.getenv().getOrDefault("USER_NAME", "")
                password = System.getenv().getOrDefault("USER_PSWRD", "")
            }
        }
    }


    // Properties will be injected by Jenkins
    fun loadPropertyForKey(key: String): String {
        return System.getenv().getOrDefault(key, "")
    }

}

plugins {
    id("com.gradle.plugin-publish") version "0.18.0"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("org.gradle.kotlin.embedded-kotlin") version "2.1.4"
    id("org.gradle.kotlin.kotlin-dsl") version "2.1.4"
    `java-gradle-plugin`
    `maven-publish`
}

apply {
    plugin("kotlin")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        url = uri(System.getenv().getOrDefault("NEXUS_URL", ""))
        credentials {
            username = System.getenv().getOrDefault("USER_NAME", "")
            password = System.getenv().getOrDefault("USER_PSWRD", "")
        }
    }
}

group = "world.betterme"
version = "1.0.2"

publishing {
    repositories {
        maven {
            url = uri(System.getenv().getOrDefault("NEXUS_URL", ""))
            credentials {
                username = System.getenv().getOrDefault("USER_NAME", "")
                password = System.getenv().getOrDefault("USER_PSWRD", "")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "betterlibs"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}

gradlePlugin {
    plugins {
        register("betterlibs") {
            id = "world.betterme.betterlibs"
            displayName = "BetterLibs"
            implementationClass = "world.betterme.betterlibs.BetterLibsPlugin"
            description = "Gradle plugin reporting third party libraries update"
        }
    }
}

pluginBundle {
    website = "https://github.com/betterme-dev/BetterLibs"
    vcsUrl = "https://github.com/betterme-dev/BetterLibs.git"
    tags = listOf("dependencies", "updates", "libraries", "notifications")
}

dependencies {
    implementation("com.github.ben-manes:gradle-versions-plugin:0.39.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.squareup.moshi:moshi:1.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation("com.squareup.okio:okio:3.0.0")
    implementation(kotlin("stdlib"))
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
