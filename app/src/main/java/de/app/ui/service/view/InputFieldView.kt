package de.app.ui.service.view

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

interface InputFieldView: FieldView {

    val id: String

    fun applyState(state: FieldState)

    fun getCurrentValue(): FieldValue

    fun onValueChanged(handler: () -> Unit)
}