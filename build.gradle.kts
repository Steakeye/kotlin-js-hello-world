import org.jetbrains.kotlin.cli.jvm.main

plugins {
    kotlin("multiplatform") version "1.4.21"
    `maven-publish`
}

group = "me.steakeye"
version = "1.0"

repositories {
    mavenCentral()
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
    //js(BOTH) {
    js {
        //useCommonJs()
        browser {
            /*testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }*/
            webpackTask {
                output.libraryTarget = "commonjs-module"
            }
        }

        /*nodejs {
            useCommonJs()
        }*/
        //compilations.all
        //useCommonJs()
        //binaries.executable()
    }.compilations.all {
        kotlinOptions.main = "noCall"
        //kotlinOptions.outputFile = "$project.buildDir.path/js/packages/${project.name}/lib/my_lib.js"
        kotlinOptions.moduleKind = "plain"
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
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}
