plugins {
    alias(libs.plugins.personalnotes.android.libraty)
}

android {
    namespace = "dev.robbik.personalnotes.core.ui"
}
dependencies {
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
}
