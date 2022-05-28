package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.data.model.service.form.BigTextField
import de.app.data.model.service.form.Field
import de.app.data.model.service.form.TextField
import de.app.data.model.service.form.TextInfoField
import de.app.databinding.ApplicationFormBigTextBinding
import de.app.databinding.ApplicationFormInfoBinding
import de.app.databinding.ApplicationFormSubmitBinding
import de.app.databinding.ApplicationFormTextBinding

class TextInfoFieldView(
    private val binding: ApplicationFormInfoBinding
): FieldView {

    override fun getView(): View {
        return binding.root
    }

    class Inflater {
        private lateinit var binding: ApplicationFormInfoBinding

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormInfoBinding.inflate(inflater, parent, true)
        }

        fun populate(field: TextInfoField): Inflater = apply {
            binding.root.text = field.text
        }

        fun build() = TextInfoFieldView(binding)
    }
}