package de.app.ui.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.databinding.FragmentUserRegisterAccountBinding
import de.app.ui.user.register.data.RegisteredUserView
import de.app.ui.util.afterTextChanged
import javax.inject.Inject

@AndroidEntryPoint
class AccountRegisterFragment : Fragment() {
    @Inject lateinit var viewModel: AccountRegisterViewModel
    private lateinit var binding: FragmentUserRegisterAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRegisterAccountBinding.inflate(inflater, container, false)

        viewModel.formState.observe(viewLifecycleOwner, Observer {
            val state = it ?: return@Observer

            // disable login button unless both username / password is valid
            binding.register.isEnabled = state.isDataValid

            if (state.accountIdError != null) {
                binding.accountId.error = getString(state.accountIdError)
            }
            if (state.enterIdText != null){
                binding.accountId.hint = getString(state.enterIdText)
            }
        })

        viewModel.formResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                onLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                onSuccessfulLogin(loginResult.success)
            }
        })


        binding.accountId.apply {
            afterTextChanged {
                viewModel.accountIdChanged(
                    binding.accountId.text.toString(),
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.register(
                            binding.accountId.text.toString(),
                            tabToType(binding.tabs.selectedTabPosition)
                        )
                }
                false
            }


            binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.accountTypeChanged(
                        tabToType(binding.tabs.selectedTabPosition)
                    )
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}

            })
            binding.register.setOnClickListener {
                viewModel.register(
                    binding.accountId.text.toString(),
                    tabToType(binding.tabs.selectedTabPosition)
                )
            }
        }

        return binding.root
    }

    private fun tabToType(num: Int): AccountRegisterViewModel.Type = when (num) {
        0 -> AccountRegisterViewModel.Type.CITIZEN
        1 -> AccountRegisterViewModel.Type.COMPANY
        else -> throw AssertionError()
    }

    private fun onSuccessfulLogin(model: RegisteredUserView) {
        findNavController().navigate(
            R.id.action_nav_register_to_result,
            bundleOf("accountId" to model.account.accountId)
        )
    }

    private fun onLoginFailed(errorString: String) {
        findNavController().navigate(
            R.id.action_nav_register_to_result,
            bundleOf(
                "accountId" to null,
                "error" to errorString
            )
        )
    }
}