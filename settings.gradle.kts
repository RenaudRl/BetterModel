plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "BetterModel"

include(
    "api",
    "core",
    "purpur",
    "plugin:paper",
    "nms:v1_21_R7",
    "test-plugin"
)
