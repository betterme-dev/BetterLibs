package com.betterme.betterlibs

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.maybeCreate
import com.betterme.betterlibs.slack.CreateSlackMessageTask

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
