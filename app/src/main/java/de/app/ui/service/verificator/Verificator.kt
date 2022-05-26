package de.app.ui.service.verificator

import de.app.ui.service.data.state.FieldState

abstract class Verificator {
    abstract fun verify(name: String, value: Any): FieldState
}