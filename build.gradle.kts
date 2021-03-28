import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
val serializationVersion: String = "1.0.1"
val indyVersion: String = "1.8.2"
val ktorVersion: String = "1.5.1"
val okhttpVersion: String = "3.5.0"
val kotlinxCourutinesVersion = "1.4.2"
val uuidVersion = "0.2.3"
val junitVersion = "4.13"


plugins {
    val kotlinVersion = "1.4.30-M1"
    kotlin("multiplatform") version kotlinVersion
    id("com.android.library")
    /* TODO: Deal with : The 'kotlin-android-extensions' Gradle plugin is deprecated. Please use this migration guide (https://goo.gle/kotlin-android-extensions-deprecation) to start working with View Binding (https://developer.android.com/topic/libraries/view-binding) and the 'kotlin-parcelize' plugin.*/
    id("kotlin-android-extensions")
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("native.cocoapods") version "1.4.31"
}

group = "com.dxc"
version = "1.0-SNAPSHOT"

repositories {
    google()
    jcenter()
    mavenCentral()
    maven(url = "https://repo.sovrin.org/repository/maven-releases")
    maven { setUrl("https://dl.bintray.com/kotlin/kotlinx.html/") }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    android()

    ios {  // Replace with a target you need.
        compilations.getByName("main") {
            val indylib by cinterops.creating {
                defFile(project.file("../ssi-mobile-sdk/indylib/indylib.def"))
            }
        }
    }
    cocoapods {
        summary = "Kotlin sample project with CocoaPods dependencies"
        homepage = "https://github.com/Kotlin/kotlin-with-cocoapods-sample"
        ios.deploymentTarget = "13.5"
    }


    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("io.ktor:ktor-utils:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCourutinesVersion")
                implementation ("com.benasher44:uuid:$uuidVersion")
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.hyperledger:indy:$indyVersion") {
                    exclude(group = "net.java.dev.jna", module = "jna")
                    exclude(group = "org.slf4j", module = "slf4j-api")
                }
                implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("net.java.dev.jna:jna:4.5.1")
                implementation("org.slf4j:slf4j-simple:1.7.26")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
                implementation("org.hyperledger:indy:$indyVersion") {
                    exclude(group = "net.java.dev.jna", module = "jna")
                    exclude(group = "org.slf4j", module = "slf4j-api")
                }
                implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")

            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:$junitVersion")
                implementation ("androidx.test:runner:1.1.0")
                implementation("androidx.test:rules:1.1.0")

            }
        }
        val iosMain by getting {
            dependencies {
                implementation(files("indylib.klib"))
            }
        }
        val iosTest by getting {
            dependencies {
                implementation(files("indylib.klib"))
            }
        }
    }
}

android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

}
//TODO: check if this section is needed at all or it should be moved to JVM and Android packages
dependencies {
    implementation("junit:junit:$junitVersion")
}
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "KotlinShared")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)
