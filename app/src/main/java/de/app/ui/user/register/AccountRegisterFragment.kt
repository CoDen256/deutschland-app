package de.app.ui.user.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.app.data.model.UserType
import de.app.databinding.FragmentUserRegisterAccountBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.util.afterTextChanged
import de.app.ui.util.observe
import de.app.ui.util.onActionDone
import de.app.ui.util.onTabSelected
import javax.inject.Inject

@AndroidEntryPoint
class AccountRegisterFragment : SimpleFragment<FragmentUserRegisterAccountBinding>() {
    @Inject
    lateinit var viewModel: AccountRegisterViewModel
    private val tabToType = listOf(
        UserType.CITIZEN,
        UserType.COMPANY
    )

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserRegisterAccountBinding.inflate(inflater, container, false)

    override fun setup() {
        observe(viewModel.formState) {
            binding.register.isEnabled = isDataValid

            if (accountIdError != null) {
                binding.accountId.error = getString(accountIdError)
            }
            if (enterIdHintText != null) {
                binding.accountId.hint = getString(enterIdHintText)
            }
        }
        observe(
            viewModel.formResult,
            { onSuccessfulRegister(it) },
            { onLoginFailed(it) }
        )
        binding.accountId.apply {
            afterTextChanged { viewModel.accountIdChanged(it) }
            onActionDone { submit() }
        }
        binding.tabs.onTabSelected(tabToType) { viewModel.accountTypeChanged(it) }
        binding.register.setOnClickListener { submit() }

    }

    private fun submit() {
        viewModel.register(
            binding.accountId.text.toString(),
            tabToType[binding.tabs.selectedTabPosition]
        )
    }

    private fun onSuccessfulRegister(model: RegisterView) {
        navController.navigate(
            AccountRegisterFragmentDirections.actionNavRegisterToResult(
                model.accountSecretToken.token,
                model.type.name, null
            )
        )
    }

    private fun onLoginFailed(error: Throwable) {
        navController.navigate(
            AccountRegisterFragmentDirections.actionNavRegisterToResult(null, null, error.message)
        )
    }
}