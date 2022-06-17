package de.app.ui.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.app.data.model.UserType
import de.app.databinding.FragmentUserRegisterAccountBinding
import de.app.ui.util.afterTextChanged
import de.app.ui.util.observe
import de.app.ui.util.onActionDone
import de.app.ui.util.onTabSelected
import javax.inject.Inject

@AndroidEntryPoint
class AccountRegisterFragment : Fragment() {
    @Inject
    lateinit var viewModel: AccountRegisterViewModel
    private lateinit var binding: FragmentUserRegisterAccountBinding
    private lateinit var navController: NavController;

    private val tabToType = listOf(
        UserType.CITIZEN,
        UserType.COMPANY
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRegisterAccountBinding.inflate(inflater, container, false)

        navController = findNavController()
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

        return binding.root
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