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
import de.app.data.model.UserHeader
import de.app.databinding.FragmentUserEnterPinBinding
import de.app.ui.MainActivity
import de.app.ui.util.afterTextChanged
import de.app.ui.util.observe
import de.app.ui.util.onActionDone
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EnterPINFragment : Fragment() {

    @Inject
    lateinit var viewModel: EnterPINViewModel
    private lateinit var binding: FragmentUserEnterPinBinding
    private val args: EnterPINFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserEnterPinBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            viewModel.getAccountHeader(args.userId).onSuccess {
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
            binding.loading.visibility = View.GONE
            onSuccessfulLogin(it)
        }, {
            binding.loading.visibility = View.GONE
            onLoginFailed(it)
        })

        binding.pin.apply {
            afterTextChanged { viewModel.loginDataChanged(it) }
            onActionDone { submitLogin() }
        }
        binding.login.setOnClickListener { submitLogin() }

        return binding.root
    }

    private fun submitLogin() {
        binding.loading.visibility = View.VISIBLE
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