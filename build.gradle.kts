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
}

group = "world.betterme.betterlibs"
version = "1.0.0"

pluginBundle {
    website = "https://github.com/betterme-dev/BetterLibs"
    vcsUrl = "https://github.com/betterme-dev/BetterLibs.git"
    tags = listOf("dependencies", "updates", "libraries", "notifications")
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

fun setupPublishingEnvironment() {
    val keyProperty = "gradle.publish.key"
    val secretProperty = "gradle.publish.secret"

    if (System.getProperty(keyProperty) == null || System.getProperty(secretProperty) == null) {
        logger.info("`$keyProperty` or `$secretProperty` were not set. Attempting to configure from environment variables")

        val key: String? = System.getenv("GRADLE_PUBLISH_KEY")
        val secret: String? = System.getenv("GRADLE_PUBLISH_SECRET")
        if (!key.isNullOrBlank() && !secret.isNullOrBlank()) {
            System.setProperty(keyProperty, key)
            System.setProperty(secretProperty, secret)
        } else {
            logger.warn("key or secret was null")
        }
    }
}

setupPublishingEnvironment()

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
