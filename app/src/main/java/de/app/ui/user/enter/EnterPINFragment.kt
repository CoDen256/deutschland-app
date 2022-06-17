package de.app.ui.user.enter

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
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.data.model.UserHeader
import de.app.databinding.FragmentUserEnterPinBinding
import de.app.ui.MainActivity
import de.app.ui.util.afterTextChanged
import javax.inject.Inject

@AndroidEntryPoint
class EnterPINFragment : Fragment() {

    @Inject lateinit var viewModel: EnterPINViewModel
    private lateinit var binding: FragmentUserEnterPinBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserEnterPinBinding.inflate(inflater, container, false)

        val pin = binding.pin
        val login = binding.login
        val loading = binding.loading
        val loginAsUsername = binding.loginUsername


        val selectedUser: UserHeader = null!!
//            arguments?.getString("accountId")
//            .successOrElse()
//            .map { viewModel.getAccountHeader(it).getOrThrow() }.getOrThrow()


        loginAsUsername.text = getString(
            R.string.welcome_username,
            selectedUser.displayName
        )

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
                    EditorInfo.IME_ACTION_DONE -> {}
//                        viewModel.login(
//                            selectedAccount.id,
//                            pin.text.toString()
//                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
//                viewModel.login(selectedAccount.id, pin.text.toString())
            }
        }

        return binding.root
    }

    private fun onSuccessfulLogin(model: EnterPINView) {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)

        requireActivity().setResult(Activity.RESULT_OK)
        //Complete and destroy login activity once successful
        requireActivity().finish()
    }

    private fun onLoginFailed(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }
}