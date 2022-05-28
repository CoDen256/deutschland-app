package de.app.ui.service.view.button

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ButtonViewFactory(
    private val inflater: LayoutInflater,
    private val parent: ViewGroup
) {

    fun createSubmitButtonView(): ButtonView{
        return SubmitButtonView.inflate(inflater, parent)
    }
}