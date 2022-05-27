package de.app.ui.service.view

import android.view.View
import de.app.databinding.ApplicationFormBigTextBinding
import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue

class BigTextFieldView(
    override val id: String,
    private val binding: ApplicationFormBigTextBinding): InputFieldView {
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