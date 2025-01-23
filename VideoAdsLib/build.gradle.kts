plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish) // Apply the maven-publish plugin
}

android {
    namespace = "com.example.videoadslib"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.example.videoadslibe" // library package name
                artifactId = "VideoAdsLib" // library name
                version = "1.0.0" // library version
                artifact(tasks.getByName("bundleReleaseAar"))

                pom {
                    withXml {
                        val dependenciesNode = asNode().appendNode("dependencies")
                        configurations.api.get().dependencies.forEach { dependency ->
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", dependency.group)
                            dependencyNode.appendNode("artifactId", dependency.name)
                            dependencyNode.appendNode("version", dependency.version)
                            dependencyNode.appendNode("scope", "compile")
                        }
                        configurations.implementation.get().dependencies.forEach { dependency ->
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", dependency.group)
                            dependencyNode.appendNode("artifactId", dependency.name)
                            dependencyNode.appendNode("version", dependency.version)
                            dependencyNode.appendNode("scope", "runtime")
                        }
                    }
                }

            }
        }
    }
}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.media3.exoplayer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.11.0")

    //Gson
    api("com.squareup.retrofit2:converter-gson:2.11.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.5.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.5.1")
    implementation("androidx.media3:media3-ui:1.5.1")
}