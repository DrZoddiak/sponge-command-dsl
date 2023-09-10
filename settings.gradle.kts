pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

listOf(
    "spongeDSL",
    "dslTest",
).forEach(::includeProject)

fun includeProject(name: String) {
    include(name)
    project(":$name")
}

rootProject.name = "sponge-command-dsl"