package de.app.ui.account.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.core.AccountDataSource
import de.app.data.model.AccountHeader
import de.app.databinding.FragmentLoginSelectAccountBinding
import javax.inject.Inject

@AndroidEntryPoint
class SelectAccountFragment : Fragment() {

    private lateinit var binding: FragmentLoginSelectAccountBinding
    @Inject lateinit var viewModel: SelectAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginSelectAccountBinding.inflate(inflater, container, false)
        val navController = findNavController()


        val headers = ArrayList<AccountHeader>()
        viewModel.getAccounts().observe(viewLifecycleOwner) {
            headers.addAll(it)
        }
        binding.accounts.adapter = AccountViewAdapter(headers) { h ->
            navController.navigate(
                R.id.action_nav_select_to_enter_pin,
                bundleOf("accountId" to h.id)
            )
        }
        binding.accounts.layoutManager = LinearLayoutManager(context)

        binding.addAccount.setOnClickListener {
            navController.navigate(R.id.action_nav_select_to_register)
        }

        return binding.root
    }

}