plugins {
    alias(libs.plugins.personalnotes.android.feature)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "dev.robbik.personalnotes.feature.calendar"
}
dependencies {

    implementation(project(":core:task"))

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
}
