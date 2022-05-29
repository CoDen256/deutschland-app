package de.app.ui.service.view.field

import androidx.activity.result.ActivityResult
import de.app.ui.service.data.state.FormState
import de.app.ui.service.data.value.FieldValue

interface InputFieldView: FieldView {
    fun applyState(formState: FormState)

    fun getValue(): FieldValue

    fun onValueChanged(handler: () -> Unit)

    fun onActivityResult(result: ActivityResult){}
}