package de.app.ui.service.data.value

data class FormValue (
    val values: Set<FieldValue>
){
    private val idToFieldValue: Map<String, FieldValue> by lazy {
        values.groupBy { it.id }.mapValues { it.value.first() }
    }
    fun getFieldValue(id: String) = idToFieldValue[id]

}