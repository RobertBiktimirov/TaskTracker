plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.robbik.personalnotes"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.robbik.personalnotes"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

afterEvaluate {
    android.applicationVariants.forEach { variant ->
        val copyNavFromAppToNavigationModuleTask = tasks.findByName("copyNavFromAppToNavigationModule")
        val variantCapped = variant.name.replaceFirstChar { it.uppercaseChar() }
        val generateSafeArgsTask = tasks.findByName("generateSafeArgs$variantCapped")
        val removeNavFromAppModuleTask = tasks.findByName("removeNavFromAppModule")
        val kotlinCompileTask = tasks.findByName("compile${variantCapped}Kotlin") as org.jetbrains.kotlin.gradle.tasks.KotlinCompile

        if (generateSafeArgsTask != null && copyNavFromAppToNavigationModuleTask != null) {
            generateSafeArgsTask.finalizedBy(copyNavFromAppToNavigationModuleTask)
        } else {
            println("Task generateSafeArgs$variantCapped or copyNavFromAppToNavigationModule not found")
        }

        if (copyNavFromAppToNavigationModuleTask != null) {
            kotlinCompileTask.dependsOn(copyNavFromAppToNavigationModuleTask)
            kotlinCompileTask.dependsOn(generateSafeArgsTask)
            kotlinCompileTask.finalizedBy(removeNavFromAppModuleTask)
        } else {
            println("Task compile${variantCapped}Kotlin or copyNavFromAppToNavigationModule not found")
        }
    }
}

dependencies {

    implementation(project(":feature:main"))
    implementation(project(":feature:editTask"))
    implementation(project(":feature:calendar"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":core:database"))
    implementation(project(":core:notification"))
    implementation(project(":core:task"))

    ksp(libs.dagger.compiler)
    implementation(libs.dagger)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.room.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}

tasks.register("copyNavFromAppToNavigationModule") {
    val inputArgsPath = "/build/generated/source/navigation-args"
    val outputArgsPath = "/build/generated/nav"
    val appNavigation = "${project(":app").projectDir.path}$inputArgsPath"
    val navigationPath = "${project(":core:navigation").projectDir.path}$outputArgsPath"
    val navigationPackage = "dev.robbik.personalnotes.core.navigation"
    val appPackage = "dev.robbik.personalnotes"

    doLast {
        println("Starting copyNavFromAppToNavigationModule task")

        // Очищаем папку перед копированием
        val navDir = file(navigationPath)
        if (navDir.exists()) {
            println("Cleaning directory: $navigationPath")
            navDir.deleteRecursively()
        }

        fileTree(appNavigation)
            .filter { it.isFile && it.name.contains("Directions") }
            .forEach { file ->
                if (file.exists()) {
                    val lines = file.readLines().toMutableList()
                    if ("import ${navigationPackage}.R" !in lines) {
                        lines.add(2, "import ${navigationPackage}.R;")
                    }
                    if ("package $navigationPackage" !in lines) {
                        lines.replaceAll { line ->
                            line.replace("package $appPackage", "package $navigationPackage")
                        }
                    }
                    file.writeText(lines.joinToString("\n"))
                }
            }

        val debugAppNavigation = "$appNavigation/debug"
        val releaseAppNavigation = "$appNavigation/release"
        val pathFrom = if (file(debugAppNavigation).exists()) debugAppNavigation else releaseAppNavigation

        println("Copying files from $pathFrom to $navigationPath")
        copy {
            from(pathFrom)
            into(navigationPath)
        }
    }
}

tasks.register("removeNavFromAppModule") {
    val inputArgsPath = "/build/generated/source/navigation-args"
    val appNavigation = "${project(":app").projectDir.path}$inputArgsPath"
    val inputArgsPath2 = "/build/tmp/kotlin-classes/release/ru/robbik/personalnotes/navigation"
    val appNavigation2 = "${project(":app").projectDir.path}$inputArgsPath2"

    doLast {
        println("Starting removeNavFromAppModule task")

        val folder = File(appNavigation)
        if (folder.exists()) {
            println("Deleting folder: $appNavigation")
            folder.deleteRecursively()
        } else {
            println("Folder does not exist: $appNavigation")
        }

        val folder2 = File(appNavigation2)
        if (folder2.exists()) {
            println("Deleting folder: $appNavigation2")
            folder2.deleteRecursively()
        } else {
            println("Folder does not exist: $appNavigation2")
        }
    }
}