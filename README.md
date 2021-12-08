BetterLibs
-------

A Gradle plugin that creates Slack message for outdated project dependencies so they
can easily be tracked and manually upgraded.

This plugin is the simplified reincarnation of [DebLibs](https://github.com/hellofresh/deblibs-gradle-plugin)
plugin, which supports the latest Kotlin & Kapt updates. Simplified means it doesn't work with the Github and Gitlab yet.


Usage
-----

### `plugins` block:

_`Build script snippet for plugins DSL for Gradle 2.1 and later:`_

```groovy
plugins {
  id "io.github.bettermedev.betterlibs" version "$version"
}
```

_`Build script snippet for use in older Gradle versions or where dynamic configuration is required:`_
### `buildscript` block:
```groovy
apply plugin: "io.github.bettermedev.betterlibs"

buildscript {

  repositories {
    maven { url "https://plugins.gradle.org/m2/"}
  }

  dependencies {
    classpath "io.github.bettermedev.betterlibs:$version"
  }

}
```

The plugin show work with Gradle version 4.9 and above.

Configuration
-------------
The following configuration block is _required._

```groovy
betterlibs {
   projectName ="Project name"
   slackToken = "slack-token"
   slackChannel = "#slack-channel"
   slackIconUrl = "url-to-an-icon-to-be-used-by-the-slack-bot"
}

```

Tasks
----

The plugin comes with a task which performs publishing info about outdated dependencies to a Slack channel as a message. 
This is useful when you want to post outdated dependencies to slack.

To post to Slack, issue the command:

`./gradlew createSlackMessage`

Development
-----------
### Import
Import the [settings.gradle.kts](https://github.com/betterme-dev/BetterLibs/blob/master/settings.gradle.kts) file into your IDE for development.
Works well with [IntelliJ Community](https://www.jetbrains.com/de-de/idea/download) edition or IntelliJ Ultimate compared to say Android studio.

### Build

Build the plugin with: `./gradlew build`

Publish to a local maven repository for testing with: `./gradlew publishToMavenLocal`

License
-------

    Copyright (C) 2021 The BetterLibs Authors

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
