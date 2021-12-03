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

package world.betterme.betterlibs

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.maybeCreate
import world.betterme.betterlibs.slack.CreateSlackMessageTask

open class BetterLibsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val gradlePluginVersions: DependencyUpdatesTask =
            project.tasks.maybeCreate<DependencyUpdatesTask>("dependencyUpdates")
        val jsonFile = "${gradlePluginVersions.outputDir}/${gradlePluginVersions.reportfileName}.json"
        with(gradlePluginVersions) {
            outputFormatter = "json"
            checkForGradleUpdate = true
            resolutionStrategy {
                componentSelection {
                    all {
                        val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview")
                            .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-]*") }
                            .any { it.matches(candidate.version) }
                        if (rejected) {
                            reject("Release candidate")
                        }
                    }
                }
            }
        }

        with(project) {
            val ext = extensions.create<BetterLibsExtension>(EXTENSION_NAME)
            logger.info("Creating $EXTENSION_NAME tasks...")
            tasks.register(SLACK_TASK_NAME, CreateSlackMessageTask::class.java) {
                dependsOn(GRADLE_PLUGIN_VERSION_TASK_NAME)
                jsonInputPath = jsonFile
            }
            logger.info("Finished creating $EXTENSION_NAME tasks")

            afterEvaluate {
                val slackMessage = tasks.getByName(SLACK_TASK_NAME) as CreateSlackMessageTask
                slackMessage.token = ext.slackToken
                slackMessage.channel = ext.slackChannel
                slackMessage.projectName = ext.projectName
                slackMessage.username = ext.slackName
                slackMessage.iconUrl = ext.slackIconUrl
            }
        }
    }

    companion object {

        private const val SLACK_TASK_NAME = "createSlackMessage"
        private const val GRADLE_PLUGIN_VERSION_TASK_NAME = ":dependencyUpdates"
        private const val EXTENSION_NAME = "betterlibs"
    }
}
