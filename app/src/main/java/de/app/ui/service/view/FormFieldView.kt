package de.app.ui.service.view

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.verificator.Verificator

abstract class FormFieldView(val verificator: Verificator) {

    abstract fun applyState(state: FieldState)

    abstract fun getValue(): Any

    abstract fun onValueChanged(handler: () -> Unit)
}