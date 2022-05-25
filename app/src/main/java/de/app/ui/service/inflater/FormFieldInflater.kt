package de.app.ui.service.inflater

import android.view.LayoutInflater
import android.view.ViewGroup
import de.app.databinding.*

class FormFieldInflater(
    private val inflater: LayoutInflater,
    private val parent: ViewGroup,
) {


    fun inflateTextView(): ApplicationFormInfoBinding {
        return ApplicationFormInfoBinding
            .inflate(inflater, parent, false)
    }

    fun inflateEditText(): ApplicationFormTextBinding {
        return ApplicationFormTextBinding
            .inflate(inflater, parent, false)
    }

    fun inflateEditTextBig(): ApplicationFormBigtextBinding {
        return ApplicationFormBigtextBinding
            .inflate(inflater, parent, false)
    }


    fun inflateDate(): ApplicationFormDateBinding {
        return ApplicationFormDateBinding
            .inflate(inflater, parent, false)
    }


    fun inflateButton(): ApplicationFormSubmitBinding{
        return ApplicationFormSubmitBinding
            .inflate(inflater, parent, false)
    }


}