package de.app.ui.service.view

import android.view.View
import de.app.databinding.ApplicationFormInfoBinding

class InfoFieldView(private val binding: ApplicationFormInfoBinding) : FieldView {
    override val view: View get() = binding.root
}