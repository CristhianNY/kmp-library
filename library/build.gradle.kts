import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.cristhianny"
version = "1.0.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "library"
            isStatic = true
        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                // put your multiplatform dependencies here
            }
        }

        val androidMain by getting {
             dependencies {
                  implementation("androidx.startup:startup-runtime:1.2.0")
             }
        }
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
        }
    }
}

android {
    namespace = "io.github.cristhianny"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "kmp-library", version.toString())

    pom {
        name.set("My Library")
        description.set("A Kotlin Multiplatform library.")
        inceptionYear.set("2024")
        url.set("https://github.com/CristhianNY/kmp-library")
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://opensource.org/licenses/Apache-2.0")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("cristhianny")
                name.set("Cristhian Ariel Bonilla")
                url.set("https://github.com/CristhianNY")
            }
        }
        scm {
            url.set("https://github.com/CristhianNY/kmp-library")
            connection.set("scm:git:https://github.com/CristhianNY/kmp-library.git")
            developerConnection.set("scm:git:ssh://git@github.com:CristhianNY/kmp-library.git")
        }
    }

}


// Force commons-compress version
configurations.all {
    resolutionStrategy {
        force("org.apache.commons:commons-compress:1.22")
    }
}

// Task to prepare the staging directory for JReleaser
tasks.register("prepareStagingDirectory") {
    doLast {
        val stagingDir = file("build/staging-deploy")
        if (!stagingDir.exists()) {
            stagingDir.mkdirs()
        }
    }
}

