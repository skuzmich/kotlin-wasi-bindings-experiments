import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    kotlin("multiplatform") version "1.8.255-SNAPSHOT"
}

group="org.jetbrains.kotlinx"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    wasm {
        nodejs {
            testTask {
                nodeJsArgs += "--experimental-wasi-unstable-preview1"
            }
        }
    }
    sourceSets {
        val wasmMain by getting
        val wasmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

rootProject.plugins.withType(NodeJsRootPlugin::class.java) {
    rootProject.the<NodeJsRootExtension>().apply {
        nodeVersion = "20.0.0-v8-canary2022112061c569ba0d"
        nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlin.wasm.unsafe.UnsafeWasmMemoryApi"
    }
}

tasks.withType<KotlinNpmInstallTask> {
    // Node with canary V8 version is not parsed correctly by NPM, producing errors like
    //
    //          error typescript@4.7.4: The engine "node" is incompatible with this module.
    //                                  Expected version ">=4.2.0". Got "20.0.0-v8-canary***"
    //
    args.add("--ignore-engines")
}