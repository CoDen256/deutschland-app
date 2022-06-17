package de.app.ui.user.set

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.core.successOrElse
import de.app.data.model.UserHeader
import de.app.databinding.FragmentUserSetPinBinding
import de.app.ui.util.afterTextChanged
import de.app.ui.util.observe
import de.app.ui.util.onActionDone
import javax.inject.Inject
@AndroidEntryPoint
class SetPINFragment : Fragment() {

    @Inject lateinit var viewModel: SetPINViewModel
    private lateinit var binding: FragmentUserSetPinBinding
    private lateinit var secretToken: String
    private lateinit var navController: NavController

    val args: SetPINFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSetPinBinding.inflate(inflater, container, false)

        navController = findNavController()

        secretToken = args.accountSecretToken

        observe(viewModel.setPINFormState) {
            binding.submitPin.isEnabled = isDataValid

            if (passwordError != null) {
                binding.pin.error = getString(passwordError)
            }
        }

        observe(viewModel.setPINResult,
            { onSuccessfulLogin(it) },
            {
                binding.loading.visibility = View.GONE
                onLoginFailed(it)
            }
        )


        binding.pin.apply {
            afterTextChanged {
                viewModel.pinChanged(binding.pin.text.toString())
            }
            onActionDone { submit() }
        }

        binding.submitPin.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            submit()
        }

        return binding.root
    }

    private fun submit() {
        viewModel.setPIN(
            secretToken, binding.pin.text.toString()
        )
    }


    private fun onSuccessfulLogin(model: SetPINUserView) {
        navController.navigate(
            SetPINFragmentDirections.actionNavSetPinToEnterPin(model.user.userId)
        )
    }

    private fun onLoginFailed(error: Throwable) {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
    }
}