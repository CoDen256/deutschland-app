package de.app.ui.service.data.state

data class FormState(
    val states: Set<FieldState>,
    val isDataValid: Boolean = false
) {
    private val idToFieldState: Map<String, FieldState> by lazy {
        states.groupBy { it.id }.mapValues { it.value.first() }
    }

    fun getFieldState(id: String): FieldState? = idToFieldState[id]

}