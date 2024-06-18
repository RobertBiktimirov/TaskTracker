package dev.robbik.personalnotes.core.ui.state

import dev.robbik.personalnotes.core.ui.state.UiState.Failure
import dev.robbik.personalnotes.core.ui.state.UiState.Success

sealed interface UiState<out T : Any> {

    data object Loading : UiState<Nothing>

    data object Init : UiState<Nothing>

    data class Failure(val throwable: Throwable) : UiState<Nothing>

    data class Success<T : Any>(val value: T) : UiState<T>

}

fun <T : Any> Result<T>.toUiState(): UiState<T> {
    return fold(
        onSuccess = { Success(it) },
        onFailure = { Failure(it) }
    )
}

inline fun <T : Any> UiState<T>.doIfSuccess(block: (T) -> Unit) {
    if (this is Success) {
        block(value)
    }
}

inline fun <T : Any> UiState<T>.doIfFailure(block: (Throwable) -> Unit) {
    if (this is Failure) {
        block(throwable)
    }
}