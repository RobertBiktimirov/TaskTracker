plugins {
    alias(libs.plugins.personalnotes.android.libraty)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.robbik.personalnotes.core.di"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.android)
    ksp(libs.dagger.compiler)
    implementation(libs.dagger)
}