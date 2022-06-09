package de.app.ui.account


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.app.databinding.ActivityAccountLoginBinding
import io.karn.notify.Notify

class AccountLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()
    }

}
