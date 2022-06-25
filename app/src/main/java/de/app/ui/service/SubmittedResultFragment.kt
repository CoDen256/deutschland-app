package de.app.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.databinding.FragmentAdministrativeServiceResultBinding

class SubmittedResultFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdministrativeServiceResultBinding.inflate(inflater, container, false)

        return binding.root
    }
}