package de.app.ui.account.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast

import de.app.R
import de.app.core.AccountDataSource
import de.app.core.SessionManager
import de.app.databinding.ActivityAccountLoginBinding
import de.app.ui.MainActivity
import de.app.ui.account.login.data.LoggedInUserView
import de.app.ui.util.afterTextChanged

const val LOGGED_IN_ACCOUNT = "de.app.ui.account.login.ACCOUNT"
// TODO: nav graph between login screens, make as fragment
class AccountLoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityAccountLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pin = binding.pin
        val login = binding.login
        val loading = binding.loading
        val loginAsUsername = binding.loginUsername

        val dataSource = AccountDataSource()
        val selectedAccount = dataSource.getAccounts()[0]
        loginViewModel = LoginViewModel(SessionManager(dataSource))


        loginAsUsername.text = getString(R.string.welcome_username, selectedAccount.name, selectedAccount.surname)

        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.passwordError != null) {
                pin.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this, Observer {
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
                loginViewModel.loginDataChanged(
                    selectedAccount.accountId,
                    pin.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            selectedAccount.accountId,
                            pin.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(selectedAccount.accountId, pin.text.toString())
            }
        }
    }

    private fun onSuccessfulLogin(model: LoggedInUserView) {
        val loggedInAccount = model.account

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(LOGGED_IN_ACCOUNT, loggedInAccount)
        }
        startActivity(intent)

        setResult(Activity.RESULT_OK)
        //Complete and destroy login activity once successful
        finish()
    }

    private fun onLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
