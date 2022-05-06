package de.app.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.app.R
import de.app.databinding.ActivityLoginConfirmBinding

class LoginConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginConfirmBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}