package com.betterme.betterlibs.slack

import com.betterme.betterlibs.BaseDefaultTask
import org.gradle.api.tasks.Input
import com.betterme.betterlibs.Dependency
import com.betterme.betterlibs.DependencyGraph
import com.betterme.betterlibs.gradleNotation

open class CreateSlackMessageTask : BaseDefaultTask() {

    @Input
    lateinit var channel: String
    @Input
    lateinit var token: String
    @Input
    lateinit var username: String
    @Input
    lateinit var iconUrl: String
    @Input
    lateinit var projectName: String

    init {
        description = "Create a slack message from the outdated dependencies report."
        group = "reporting"
    }

    override fun postTask(dependencyGraph: DependencyGraph) {
        val dependencies: List<Dependency> = parseGraph(dependencyGraph)
        var outDatedDependencies = ""

        dependencies.forEach {
            outDatedDependencies += "- ${it.gradleNotation()}\n"
        }
        val gradleVersion = parseGraphForGradle(dependencyGraph)
        if (gradleVersion.isNotBlank()) {
            outDatedDependencies += "\n*Gradle updates:*\n\n`$gradleVersion`\n"
        }
        if (outDatedDependencies.isBlank()) {
            return
        }
        val attachments =
            buildMessageAttachment(dependencies.size, outDatedDependencies)
        val name = if (username.isBlank()) "BetterLibs" else username
        val icon = iconUrl
        SlackClient(
            token,
            SlackMessage(
                username = name,
                channel = channel,
                iconUrl = icon,
                attachments = attachments)
        ).run()
    }

    private fun buildMessageAttachment(dependencySize: Int, outDatedDependencies: String): List<SlackMessage.Attachment> {
        val color = when {
            dependencySize <= GREEN_LIMIT -> SlackMessage.Attachment.Color.GREEN
            dependencySize <= YELLOW_LIMIT -> SlackMessage.Attachment.Color.YELLOW
            else -> SlackMessage.Attachment.Color.RED
        }
        return listOf(
            SlackMessage.Attachment(
                color = color.hexColor,
                title = "Outdated Dependencies",
                pretext = projectName,
                text = "```$outDatedDependencies```",
                footer = "Total: $dependencySize"
            )
        )
    }

    companion object {
        private const val GREEN_LIMIT = 10
        private const val YELLOW_LIMIT = 20
    }
}
