package de.app.ui.service.view

import android.view.View
import de.app.databinding.ApplicationFormDateBinding
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class DateFieldView(
    override val id: String,
    private val binding: ApplicationFormDateBinding): InputFieldView {
    override val view: View
        get() = binding.root

    override fun applyState(state: FieldState) {
        TODO("Not yet implemented")
    }

    override fun getCurrentValue(): FieldValue {
        TODO("Not yet implemented")
    }

    override fun onValueChanged(handler: () -> Unit) {
        TODO("Not yet implemented")
    }
}