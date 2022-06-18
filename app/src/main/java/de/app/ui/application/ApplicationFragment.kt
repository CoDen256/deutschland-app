package de.app.ui.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.applications.ApplicationService
import de.app.core.config.BaseApplicationService
import de.app.databinding.FragmentApplicationBinding
import javax.inject.Inject

@AndroidEntryPoint
class ApplicationFragment : Fragment() {

    @Inject lateinit var applicationService: ApplicationService
    private lateinit var binding: FragmentApplicationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplicationBinding.inflate(inflater, container, false)
        return binding.root
    }
}