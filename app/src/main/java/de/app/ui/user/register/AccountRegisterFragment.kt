package de.app.ui.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.core.success
import de.app.databinding.FragmentUserRegisterAccountBinding
import de.app.ui.util.afterTextChanged
import de.app.ui.util.mapFromArray
import de.app.ui.util.observe
import de.app.ui.util.onTabSelected
import javax.inject.Inject

@AndroidEntryPoint
class AccountRegisterFragment : Fragment() {
    @Inject lateinit var viewModel: AccountRegisterViewModel
    private lateinit var binding: FragmentUserRegisterAccountBinding
    private lateinit var navController: NavController

    private val tabToType = listOf(
        AccountRegisterViewModel.Type.CITIZEN,
        AccountRegisterViewModel.Type.COMPANY
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRegisterAccountBinding.inflate(inflater, container, false)

        navController = findNavController()

        observeFormState()
        observeFormResult()
        observeAccountIdView()
        observeTabs()
        observeRegisterButton()

        return binding.root
    }

    private fun observeAccountIdView() {
        binding.accountId.apply {
            afterTextChanged {
                viewModel.accountIdChanged(binding.accountId.text.toString(),)
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.register(
                            binding.accountId.text.toString(),
                            tabToType[binding.tabs.selectedTabPosition]
                        )
                }
                false
            }
        }
    }

    private fun observeRegisterButton() {
        binding.register.setOnClickListener {
            viewModel.register(
                binding.accountId.text.toString(),
                tabToType[binding.tabs.selectedTabPosition]
            )
        }
    }

    private fun observeTabs() {
        binding.tabs.onTabSelected(tabToType){ viewModel.accountTypeChanged(it) }
    }

    private fun observeFormResult() {
        observe(viewModel.formResult,
            { onSuccessfulRegister(it) },
            { onLoginFailed(it.message) }
        )
    }

    private fun observeFormState() {
        observe(viewModel.formState){
            binding.register.isEnabled = isDataValid

            if (accountIdError != null) {
                binding.accountId.error = getString(accountIdError)
            }
            if (enterIdHintText != null) {
                binding.accountId.hint = getString(enterIdHintText)
            }
        }
    }

    private fun onSuccessfulRegister(model: RegisterUserView) {
        navController.navigate(
            R.id.action_nav_register_to_result,
            bundleOf("accountSecretToken" to model.accountSecretToken.token)
        )
    }

    private fun onLoginFailed(errorString: String?) {
        navController.navigate(
            R.id.action_nav_register_to_result,
            bundleOf(
                "accountSecretToken" to null,
                "error" to errorString
            )
        )
    }
}