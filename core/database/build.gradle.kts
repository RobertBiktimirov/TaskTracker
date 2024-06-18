plugins {
    alias(libs.plugins.personalnotes.android.libraty)
}

android {
    namespace = "dev.robbik.personalnotes.core.database"
}

dependencies {
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}