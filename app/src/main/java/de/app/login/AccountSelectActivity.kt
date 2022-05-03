package de.app.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import de.app.R
import de.app.databinding.ActivityAccountSelectBinding
import de.app.databinding.ActivityLoginBinding

class AccountSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_select)
        binding = ActivityAccountSelectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val listItems = arrayListOf("oleg", "ivan", "imput tekst")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

        binding.accountsListView.adapter = adapter
    }
}