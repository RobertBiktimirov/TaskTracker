package dev.robbik.personalnotes.core.ui.persent

val Number?.percent: String get() = "${this}%"

fun getFraction(top: Int, bottom: Int) = "$top / $bottom"