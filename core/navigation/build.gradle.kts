plugins {
    alias(libs.plugins.personalnotes.android.libraty)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "dev.robbik.personalnotes.core.navigation"
    sourceSets["main"].java.srcDir(file("build/generated/nav"))
}

dependencies {
    implementation(libs.androidx.navigation.fragment.ktx)
}