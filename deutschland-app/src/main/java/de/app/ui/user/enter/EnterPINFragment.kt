package de.app.ui.user.enter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.databinding.FragmentUserEnterPinBinding
import de.app.ui.MainActivity
import de.app.ui.components.SimpleFragment
import de.app.ui.util.afterTextChanged
import de.app.ui.util.observe
import de.app.ui.util.onActionDone
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EnterPINFragment : SimpleFragment<FragmentUserEnterPinBinding>() {

    @Inject
    lateinit var viewModel: EnterPINViewModel
    private val args: EnterPINFragmentArgs by navArgs()

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserEnterPinBinding.inflate(inflater, container, false)

    override fun setup() {
        lifecycleScope.launch {
            viewModel.getUserHeader(args.userId).onSuccess {
                binding.welcomeUsername.text = getString(
                    R.string.welcome_username,
                    it.displayName
                )
            }.onFailure {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }

        observe(viewModel.loginFormState) {
            binding.login.isEnabled = isDataValid

            if (passwordError != null) {
                binding.pin.error = getString(passwordError)
            }
        }

        observe(viewModel.loginResult, {
            onSuccessfulLogin(it)
        }, {
            onLoginFailed(it)
        })

        binding.pin.apply {
            afterTextChanged { viewModel.loginDataChanged(it) }
            onActionDone { submitLogin() }
        }
        binding.login.setOnClickListener { submitLogin() }

    }

    private fun submitLogin() {
        viewModel.login(args.userId, binding.pin.text.toString())
    }

    private fun onSuccessfulLogin(model: EnterPINView) {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)

        requireActivity().setResult(Activity.RESULT_OK)
        requireActivity().finish()
    }

    private fun onLoginFailed(throwable: Throwable) {
        Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_SHORT).show()
    }
}