package de.app.ui.user.set

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import de.app.data.model.UserType
import de.app.databinding.FragmentUserSetPinBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.util.afterTextChanged
import de.app.ui.util.observe
import de.app.ui.util.onActionDone
import javax.inject.Inject

@AndroidEntryPoint
class SetPINFragment : SimpleFragment<FragmentUserSetPinBinding>() {
    private val args: SetPINFragmentArgs by navArgs()

    @Inject lateinit var viewModel: SetPINViewModel
    private lateinit var secretToken: String
    private lateinit var userType: UserType

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserSetPinBinding.inflate(inflater, container, false)

    override fun setup() {
        secretToken = args.accountSecretToken
        userType = UserType.valueOf(args.type)

        observe(viewModel.setPINFormState) {
            binding.submitPin.isEnabled = isDataValid

            if (passwordError != null) {
                binding.pin.error = getString(passwordError)
            }
        }

        observe(viewModel.setPINResult,
            { onSuccessfulLogin(it) },
            {
                onLoginFailed(it)
            }
        )

        binding.pin.apply {
            afterTextChanged {
                viewModel.pinChanged(binding.pin.text.toString())
            }
            onActionDone { submit() }
        }

        binding.submitPin.setOnClickListener { submit() }

    }

    private fun submit() {
        viewModel.setPIN(
            secretToken, userType, binding.pin.text.toString()
        )
    }

    private fun onSuccessfulLogin(model: SetPINView) {
        navController.navigate(
            SetPINFragmentDirections.actionNavSetPinToEnterPin(model.user.userId)
        )
    }

    private fun onLoginFailed(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
    }
}