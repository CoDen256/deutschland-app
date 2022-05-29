package de.app.ui.account.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.app.R
import de.app.core.AccountDataSource
import de.app.core.SessionManager
import de.app.databinding.FragmentLoginEnterPinBinding
import de.app.ui.MainActivity
import de.app.ui.account.login.data.LoggedInUserView
import de.app.ui.util.afterTextChanged

class AccountEnterPINFragment : Fragment() {

    private lateinit var viewModel: AccountEnterPINViewModel
    private lateinit var binding: FragmentLoginEnterPinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginEnterPinBinding.inflate(inflater, container, false)

        val pin = binding.pin
        val login = binding.login
        val loading = binding.loading
        val loginAsUsername = binding.loginUsername

        val dataSource = AccountDataSource()
        val accountId = arguments?.getString("accountId")!!
        val selectedAccount = dataSource.getAccounts().find { it.accountId == accountId}!!
        viewModel = AccountEnterPINViewModel(SessionManager(dataSource))

        loginAsUsername.text = getString(R.string.welcome_username, selectedAccount.name, selectedAccount.surname)

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
                    selectedAccount.accountId,
                    pin.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.login(
                            selectedAccount.accountId,
                            pin.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                viewModel.login(selectedAccount.accountId, pin.text.toString())
            }
        }

        return binding.root
    }

    private fun onSuccessfulLogin(model: LoggedInUserView) {
        val loggedInAccount = model.account

        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            putExtra("LOGGED_IN_ACCOUNT", loggedInAccount)
        }
        startActivity(intent)

        requireActivity().setResult(Activity.RESULT_OK)
        //Complete and destroy login activity once successful
        requireActivity().finish()
    }

    private fun onLoginFailed(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }
}