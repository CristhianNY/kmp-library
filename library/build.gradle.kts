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
