package de.app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.core.SessionManager
import de.app.databinding.ActivityMainBinding
import de.app.ui.user.LoginActivity
import de.app.ui.user.enter.EnterPINView
import de.app.ui.util.runActivity
import de.app.ui.util.setLanguage
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // TODO: just ask session manager for logged in account

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main)!!
            .findNavController()


        lifecycleScope.launchWhenCreated {
            sessionManager.init()
            if (!sessionManager.isLoggedIn) {
                jumpBackToLogin()
            }
        }

        binding.switchAccount.setOnClickListener {
            lifecycleScope.launch {
                sessionManager.logout()
                jumpBackToLogin()
            }
        }

        binding.logout.setOnClickListener {
            lifecycleScope.launch {
                sessionManager.logoutAndRemoveCurrent()
                jumpBackToLogin()
            }
        }

        binding.switchLanguage.setOnClickListener {
            val languageEn = "en-EN"
            val languageDe = "de-DE"
            val current = resources.configuration.locales[0]
            if (current.toString().startsWith("de")){
                setLanguage(languageEn)
            }else {
                setLanguage(languageDe)
            }
        }

        // TODO: why is this needed?
        // (without the id, clicking on appbar will result in going back instead of
        // viewing the menu, so its kind like not top level and here is only top levels)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_dashboard, R.id.nav_geodata, R.id.nav_mailbox,
            R.id.nav_applications, R.id.nav_appointments,
            R.id.nav_data_safe, R.id.nav_finder,
            R.id.nav_signature, R.id.nav_law_registry,
            R.id.nav_admin_service
        ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun jumpBackToLogin() {
        runActivity(LoginActivity::class.java)
    }
}