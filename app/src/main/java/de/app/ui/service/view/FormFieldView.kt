package de.app.ui.service.view

import de.app.ui.service.data.state.FieldState

abstract class FormFieldView {

    abstract fun applyState(state: FieldState)

    abstract fun getValue(): Any
}