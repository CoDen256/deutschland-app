package de.app.ui.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import de.app.R
import de.app.databinding.ActivityAccountLoginBinding

class AccountLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController: NavController = findNavController(R.id.nav_host_fragment_login_content)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_enter_pin
        ), null)

        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )
    }

}
