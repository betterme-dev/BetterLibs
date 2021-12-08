package io.github.bettermedev.betterlibs

import okio.buffer
import okio.source
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class BaseDefaultTask : DefaultTask() {

    @Input
    var jsonInputPath = "build/dependencyUpdates/report.json"

    protected abstract fun postTask(dependencyGraph: DependencyGraph)

    @TaskAction
    fun taskAction() {
        val jsonInput = project.file(jsonInputPath)
        val dependencyGraph = readGraphFromJsonFile(jsonInput)
        postTask(dependencyGraph)
    }

    protected fun parseGraphForGradle(graph: DependencyGraph): String {
        val gradle = graph.gradle
        return when {
            gradle.current.version > gradle.running.version -> {
                "[${gradle.running.version} -> ${gradle.current.version}]"
            }
            gradle.releaseCandidate.version > gradle.running.version -> {
                "[${gradle.running.version} -> ${gradle.releaseCandidate.version}]"
            }
            else -> ""
        }
    }

    protected fun parseGraph(graph: DependencyGraph): List<Dependency> {
        val dependencies: List<Dependency> = graph.outdated
        return dependencies.sortedDependencies()
    }

    private fun readGraphFromJsonFile(jsonInput: File): DependencyGraph {
        return Adapters.DependencyGraph.fromJson(jsonInput.source().buffer())!!
    }

    private fun List<Dependency>.sortedDependencies(): List<Dependency> {
        return this.sortedBy { it.gradleNotation() }
    }
}
