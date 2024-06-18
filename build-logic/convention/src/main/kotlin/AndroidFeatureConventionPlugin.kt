import com.android.build.gradle.LibraryExtension
import dev.robbik.personalnotes.configureKotlinAndroid
import dev.robbik.personalnotes.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("personalnotes.android.library")
                apply("androidx.navigation.safeargs")
            }

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    viewBinding = true
                }
            }
            dependencies {

                add("implementation", project(":core:ui"))
                add("implementation", project(":core:di"))
                add("implementation", project(":core:navigation"))
                add("implementation", project(":core:database"))

                add("implementation", libs.findLibrary("androidx.core.ktx").get())
                add("implementation", libs.findLibrary("material").get())
                add("implementation", libs.findLibrary("androidx.constraintlayout").get())
                add("implementation", libs.findLibrary("androidx.appcompat").get())
                add("implementation", libs.findLibrary("androidx.activity").get())

                add("implementation", libs.findLibrary("androidx.navigation.ui.ktx").get())
                add("implementation", libs.findLibrary("androidx.navigation.fragment.ktx").get())

                add("implementation", libs.findLibrary("dagger").get())
                add("ksp", libs.findLibrary("dagger.compiler").get())

                add("implementation", libs.findLibrary("androidx.coordinatorlayout").get())
            }
        }
    }
}