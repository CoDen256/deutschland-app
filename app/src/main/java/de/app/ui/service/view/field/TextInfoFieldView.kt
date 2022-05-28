package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.data.model.service.form.BigTextField
import de.app.data.model.service.form.Field
import de.app.data.model.service.form.TextInfoField
import de.app.databinding.ApplicationFormBigTextBinding
import de.app.databinding.ApplicationFormInfoBinding
import de.app.databinding.ApplicationFormSubmitBinding

internal class TextInfoFieldView(
    private val binding: ApplicationFormInfoBinding
): FieldView {

    override fun getView(): View {
        return binding.root
    }

    internal fun populate(field: TextInfoField): FieldView {
        binding.root.text = field.text
        return this
    }

    companion object {
        fun inflate(inflater: LayoutInflater, parent: ViewGroup) = TextInfoFieldView(
            ApplicationFormInfoBinding.inflate(inflater, parent, false))
    }
}