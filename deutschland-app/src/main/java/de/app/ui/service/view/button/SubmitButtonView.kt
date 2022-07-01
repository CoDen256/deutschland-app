package de.app.ui.service.view.button

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.databinding.ApplicationFormSubmitBinding
import de.app.ui.service.data.state.FormState

internal class SubmitButtonView(
    private val binding: ApplicationFormSubmitBinding
) : ButtonView {

    override fun getView(): View {
        return binding.root
    }

    override fun applyState(state: FormState) {
        binding.root.isEnabled = state.isDataValid
    }

    override fun setOnClickListener(handler: () -> Unit) {
        binding.root.setOnClickListener { handler() }
    }

    companion object {
        fun inflate(inflater: LayoutInflater, parent: ViewGroup) = SubmitButtonView(
            ApplicationFormSubmitBinding.inflate(inflater, parent, true)
        )
    }
}