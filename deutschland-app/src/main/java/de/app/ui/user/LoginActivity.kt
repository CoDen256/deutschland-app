package de.app.ui.user


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import de.app.core.SessionManager
import de.app.databinding.ActivityLoginBinding
import de.app.ui.MainActivity
import de.app.ui.util.runActivity
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenCreated {
            intent.extras?.let {
                it.getString("failed")?.let {
                    sessionManager.logout()
                }
            }
            sessionManager.init()
            if (sessionManager.isLoggedIn) {
                runActivity(MainActivity::class.java)
            }
        }

        supportActionBar?.hide()
    }

}
