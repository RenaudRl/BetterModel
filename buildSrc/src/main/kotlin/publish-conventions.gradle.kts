import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import kotlin.io.encoding.Base64

plugins {
    id("standard-conventions")
    id("com.vanniktech.maven.publish")
    signing
}

rootProject.dependencies.dokka(project)

val artifactBaseId = name
val artifactVersion = project.version.toString().run {
    BUILD_NUMBER?.let { substringBeforeLast("-$it") } ?: this
}

signing {
    val key = System.getenv("SIGNING_KEY")?.let {
        Base64.decode(it.toByteArray()).toString(Charsets.UTF_8)
    }
    val password = System.getenv("SIGNING_PASSWORD")
    if (!key.isNullOrEmpty() && !password.isNullOrEmpty()) {
        useInMemoryPgpKeys(
            key,
            password
        )
    } else useGpgCmd()
}

dependencies {
    api(libs.bundles.library)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

// Local BTC publishing (-PbtcLocalPublish): artifacts go to the BTC repo unsigned.
// Maven Central publishing is untouched and stays signed.
val btcLocalPublish = providers.gradleProperty("btcLocalPublish").isPresent

mavenPublishing {
    publishToMavenCentral()
    if (!btcLocalPublish) signAllPublications()
    coordinates("io.github.toxicity188", artifactBaseId, artifactVersion)
    configure(JavaLibrary(
        javadocJar = JavadocJar.Javadoc(),
        sourcesJar = SourcesJar.Sources(),
    ))
    pom {
        name = artifactBaseId
        description = "Modern Bedrock model engine for Minecraft Java Edition"
        inceptionYear = "2024"
        url = "https://github.com/toxicity188/BetterModel/"
        licenses {
            license {
                name = "MIT License"
                url = "https://mit-license.org/"
            }
        }
        developers {
            developer {
                id = "toxicity188"
                name = "toxicity188"
                url = "https://github.com/toxicity188/"
            }
        }
        scm {
            url = "https://github.com/toxicity188/BetterModel/"
            connection = "scm:git:git://github.com/toxicity188/BetterModel.git"
            developerConnection = "scm:git:ssh://git@github.com/toxicity188/BetterModel.git"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/toxicity188/${rootProject.name}")
            credentials {
                username = "toxicity188"
                password = System.getenv("PACKAGES_API_TOKEN")
            }
        }
        // BTC Studio unified static Maven repo: committed under BTCVelocity/repo and
        // uploaded as-is to https://borntocraftstudio.net/public/repo/ . Overridable via
        // -PbtcRepoDir so this fork still builds when BTCVelocity is not checked out
        // next to it.
        maven {
            name = "btcRepo"
            url = uri(
                providers.gradleProperty("btcRepoDir")
                    .getOrElse(rootProject.file("../BTCVelocity/repo").absolutePath)
            )
        }
    }
}
