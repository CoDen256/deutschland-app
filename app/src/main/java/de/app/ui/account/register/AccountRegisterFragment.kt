package de.app.ui.account.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import de.app.R
import de.app.databinding.FragmentLoginRegisterAccountBinding

class AccountRegisterFragment : Fragment() {


    private lateinit var viewModel: AccountRegisterViewModel
    private lateinit var binding: FragmentLoginRegisterAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[AccountRegisterViewModel::class.java]

        binding = FragmentLoginRegisterAccountBinding.inflate(inflater, container, false)

        binding.register.setOnClickListener {
            val accountId = binding.accountId.text.toString()
            findNavController().navigate(
                R.id.action_nav_register_to_successful,
                bundleOf("accountId" to accountId.ifBlank { null })
            )
        }

        return binding.root
    }
}