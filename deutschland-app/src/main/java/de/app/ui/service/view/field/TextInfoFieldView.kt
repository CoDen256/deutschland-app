package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import de.app.api.service.form.TextInfoField
import de.app.databinding.ApplicationFormInfoBinding

class TextInfoFieldView(
    private val binding: ApplicationFormInfoBinding
): FieldView {

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