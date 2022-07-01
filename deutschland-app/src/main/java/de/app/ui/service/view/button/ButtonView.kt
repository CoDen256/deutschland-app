package de.app.ui.service.view.button

import android.view.View
import de.app.ui.service.data.state.FormState

interface ButtonView {
    fun getView(): View

    fun applyState(state: FormState)

    fun setOnClickListener(handler: () -> Unit)
}