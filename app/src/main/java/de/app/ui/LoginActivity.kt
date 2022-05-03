package de.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.app.R
import de.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater   )
        val view = binding.root
        setContentView(view)


    }
}