package de.app.ui.account


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.app.databinding.ActivityAccountLoginBinding
import de.app.notifications.Notificator
import de.app.notifications.util.createNotificationChannel

class AccountLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel("EMERGENCY", "EMERGENCY", "EMERGENCY CHANNEL")

        val notificator = Notificator(this)
        val not = notificator.buildNotification("EMERGENCY")
        notificator.sendNotification(1, not)


        supportActionBar?.hide()
    }

}
