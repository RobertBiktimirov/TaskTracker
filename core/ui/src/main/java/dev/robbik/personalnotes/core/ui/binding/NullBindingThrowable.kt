package dev.robbik.personalnotes.core.ui.binding

internal class NullBindingThrowable : Exception("binding not must be null") {
    private fun readResolve(): Any = NullBindingThrowable()

    
}

fun bindingNullError(): Nothing = throw NullBindingThrowable()


