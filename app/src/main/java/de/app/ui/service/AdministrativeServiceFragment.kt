package de.app.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.app.data.model.adminservice.TextField
import de.app.databinding.ApplicationFormTextBinding
import de.app.databinding.FragmentAdministrativeServiceBinding

class AdministrativeServiceFragment : Fragment() {

    private lateinit var viewModel: AdminServiceViewModel
    private lateinit var binding: FragmentAdministrativeServiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdministrativeServiceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AdminServiceViewModel::class.java]
        val root = binding.root


        val context = requireContext()
        viewModel.applicationForm.form.forEach {
            val textBinding = ApplicationFormTextBinding.inflate(inflater, binding.layout, false)
            val textView = textBinding.root


            textView.text = (it as TextField).label


            binding.layout.addView(textView)
        }



        return root
    }

}