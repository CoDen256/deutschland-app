package de.app

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import de.app.databinding.ActivityLoginConfirmBinding
import de.app.databinding.ActivityMapBinding
import org.osmdroid.views.MapView


class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        var map: MapView
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy);

        val items = arrayOf("Shutzgebiete", "Umwelt", "Landwirtschaft")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        binding.dropdown.setAdapter(adapter)
    }
}