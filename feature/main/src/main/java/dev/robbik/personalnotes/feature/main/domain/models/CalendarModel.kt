package dev.robbik.personalnotes.feature.main.domain.models

internal enum class CalendarWeek(val value: String) {
    MON("Пн"),
    TUE("Вт"),
    WED("Ср"),
    THU("Чт"),
    FRI("Пт"),
    SAT("Сб"),
    SUN("Вс"),

    ALL("←\t")
}


internal data class CalendarModel(
    val week: CalendarWeek,
    val data: Long?,
    val isSelected: Boolean
) {
    companion object {
        val init = CalendarModel(CalendarWeek.ALL, null, true)
    }
}