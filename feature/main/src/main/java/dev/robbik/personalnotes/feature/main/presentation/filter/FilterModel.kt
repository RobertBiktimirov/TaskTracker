package dev.robbik.personalnotes.feature.main.presentation.filter

data class FilterModel(
    val type: FilterType,
    val isSelected: Boolean
) {

    companion object {
        fun getInitList(): List<FilterModel> {
            return FilterType.entries.toList().map {
                FilterModel(
                    type = it,
                    isSelected = it == FilterType.ALL
                )
            }
        }
    }
}

enum class FilterType(val value: String) {

    ALL("Все"),
    TODAY("Сегодня"),
    OPEN("Открытые"),
    COMPLETED("Закрытые"),
//    WITH_DEADLINE("С дедлайном"),
//    NOT_WITH_DEADLINE("Бед дедлайна"),
    WITH_SUBTASK("С подзадачами"),
    NOT_WITH_SUBTASK("Без подзадач"),
}
