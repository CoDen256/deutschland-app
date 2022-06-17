package de.app.ui.user.set

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.core.successOrElse
import de.app.data.model.UserHeader
import de.app.databinding.FragmentUserSetPinBinding
import de.app.ui.user.set.data.SetupUserView
import de.app.ui.util.afterTextChanged
import javax.inject.Inject
@AndroidEntryPoint
class SetPINFragment : Fragment() {

    @Inject lateinit var viewModel: SetPINViewModel
    private lateinit var binding: FragmentUserSetPinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSetPinBinding.inflate(inflater, container, false)

        val pin = binding.pin
        val login = binding.setPin
        val loading = binding.loading
        val loginAsUsername = binding.setPinMessage

        val selectedUser: UserHeader = arguments?.getString("accountId")
            .successOrElse()
            .map { viewModel.getAccountHeader(it).getOrThrow() }.getOrThrow()


        loginAsUsername.text = getString(R.string.welcome_username, selectedUser.displayName)

        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.passwordError != null) {
                pin.error = getString(loginState.passwordError)
            }
        })

        viewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                onLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                onSuccessfulLogin(loginResult.success)
            }
        })


        pin.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    selectedUser.userId,
                    pin.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.login(
                            selectedUser.userId,
                            pin.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                viewModel.login(selectedUser.userId, pin.text.toString())
            }
        }

        return binding.root
    }

    private fun onSuccessfulLogin(model: SetupUserView) {
        findNavController().navigate(
            R.id.action_nav_set_pin_to_enter_pin,
            bundleOf("accountId" to model.account.accountId)
        )
    }

    private fun onLoginFailed(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }
}