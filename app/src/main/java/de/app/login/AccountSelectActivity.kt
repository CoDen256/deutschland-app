package de.app.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import de.app.R
import de.app.databinding.ActivityAccountSelectBinding


class AccountSelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_select)
        binding = ActivityAccountSelectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//todo read from phone
        val listItems = arrayListOf("oleg", "ivan", "imput tekst")
        listItems.add("New Account")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

        binding.accountsListView.adapter = adapter



        binding.accountsListView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItemText = parent.getItemAtPosition(position)
                //textView.text = "Selected : $selectedItemText"

                Toast.makeText(applicationContext,selectedItemText.toString(), Toast.LENGTH_LONG).show()
            }
    }


}