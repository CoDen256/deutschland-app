package de.app.ui

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.core.SessionManager
import de.app.databinding.ActivityMainBinding
import de.app.ui.components.SelectLanguagePickerFactory
import de.app.ui.user.LoginActivity
import de.app.ui.util.runActivity
import de.app.ui.util.setLanguage
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var factory: SelectLanguagePickerFactory

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        this.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
//            Toast.makeText(this, "YEAH:$it", LENGTH_LONG).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        setupNavController()

        jumpBackIfNotLoggedIn()
        observerSwitchAccount()
        observeLogout()
        observeLanguageSwitch()
        requestPermissions()
    }

    private fun setupNavController() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main)!!
            .findNavController()


        // TODO: why is this needed?
        // (without the id, clicking on appbar will result in going back instead of
        // viewing the menu, so its kind like not top level and here is only top levels)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_geodata, R.id.nav_mailbox,
                R.id.nav_applications, R.id.nav_appointments,
                R.id.nav_data_safe, R.id.nav_finder,
                R.id.nav_signature, R.id.nav_law_registry,
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun jumpBackIfNotLoggedIn() {
        lifecycleScope.launchWhenCreated {
            sessionManager.init()
            if (!sessionManager.isLoggedIn) {
                sessionManager.logout()
                jumpBackToLogin()
            }
        }
    }

    private fun observerSwitchAccount() {
        binding.switchAccount.setOnClickListener {
            lifecycleScope.launch {
                sessionManager.logout()
                jumpBackToLogin()
            }
        }
    }

    private fun observeLogout() {
        binding.logout.setOnClickListener {
            lifecycleScope.launch {
                sessionManager.logoutAndRemoveCurrent()
                jumpBackToLogin()
            }
        }
    }

    private fun observeLanguageSwitch() {
        binding.switchLanguage.setOnClickListener {
            factory.show(this) {
                setLanguage(it.langCode)
            }
        }
    }

    private fun requestPermissions() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun jumpBackToLogin() {
        runActivity(LoginActivity::class.java)
    }


}