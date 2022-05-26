package de.app.ui.service.view

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

abstract class FieldView {
    abstract fun applyState(state: FieldState)

    abstract fun getValue(): FieldValue

    abstract fun onValueChanged(handler: () -> Unit)
}