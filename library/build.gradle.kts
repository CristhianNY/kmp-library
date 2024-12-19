import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    id("org.jreleaser") version "1.15.0"
}

group = "io.github.cristhianny"
version = "1.0.0"

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
        }
    }
}

android {
    namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "library", version.toString())

    pom {
        name = "My library"
        description = "A library."
        inceptionYear = "2024"
        url = "https://github.com/CristhianNY/kmp-library"
        licenses {
            license {
                name = "Apache-2.0" // Corrección del nombre
                url = "https://opensource.org/licenses/Apache-2.0"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "cristhianny"
                name = "Cristhian Ariel Bonilla"
                url = "https://github.com/CristhianNY"
            }
        }
        scm {
            url = "https://github.com/CristhianNY/kmp-library"
            connection = "scm:git:https://github.com/CristhianNY/kmp-library"
            developerConnection = "scm:git:ssh://github.com/CristhianNY/kmp-library"
        }
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

// Ensure the staging directory is created before JReleaser tasks
tasks.named("jreleaserFullRelease") {
    dependsOn("prepareStagingDirectory")
}

jreleaser {
    gitRootSearch = true
    signing {
        active.set(org.jreleaser.model.Active.ALWAYS)
        armored.set(true)
    }
    deploy {
        maven {
            nexus2 {
                register("mavenCentral") {
                    active.set(org.jreleaser.model.Active.ALWAYS)
                    url.set("https://s01.oss.sonatype.org/service/local")
                    snapshotUrl.set("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    closeRepository.set(true)
                    releaseRepository.set(true)
                    stagingRepositories.add(file("build/staging-deploy").toString())
                }
            }
        }
    }
}
