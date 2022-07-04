package de.app.ui.service.view.field

import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue

interface InputFieldView: FieldView {
    fun applyState(formState: FormState)

    fun getValue(): FieldValue

    fun setOnValueChangedListener(handler: () -> Unit)

    fun prefill(prefiller: (String) -> String?){}
}