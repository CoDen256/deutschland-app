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
import de.app.R
import de.app.core.AccountDataSource
import de.app.core.SessionManager
import de.app.databinding.FragmentLoginSelectAccountBinding
import de.app.ui.account.login.AccountEnterPINFragment

class SelectAccountFragment : Fragment() {

    private lateinit var binding: FragmentLoginSelectAccountBinding
    private lateinit var viewModel: SelectAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[SelectAccountViewModel::class.java]

        binding =  FragmentLoginSelectAccountBinding.inflate(inflater, container, false)

        val dataSource = AccountDataSource()
        val selectedAccount = dataSource.getAccounts()[1]

        val navController = findNavController()

        binding.select.setOnClickListener {

            navController.navigate(R.id.action_nav_select_account_to_nav_enter_pin,
                bundleOf("accountId" to selectedAccount.accountId),
                navOptions {
                    anim {
                        enter = android.R.animator.fade_in
                        exit = android.R.animator.fade_out
                    }
                })
        }

        return binding.root
    }

}