package de.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.app.databinding.ActivitySubmittedResultBinding
import de.app.ui.util.runActivity

class SubmittedResultActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySubmittedResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submit.setOnClickListener {
            runActivity(MainActivity::class.java)
        }

    }

}