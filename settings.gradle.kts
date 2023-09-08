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
    project(":$name").projectDir = File("D:\\Programming Projects\\My Projects\\SpongeDSLTest\\$name")
}