package de.app.ui.mailbox

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.app.R
import de.app.data.model.mail.MailMessageHeader
import de.app.databinding.ActivityMailBoxBinding
import java.time.Instant
import java.util.*

class MailBoxActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMailBoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMailBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val rvMailMessages = findViewById<View>(R.id.rvMailMessages) as RecyclerView

        rvMailMessages.adapter = MailMessageAdapter(listOf(
            MailMessageHeader("Hello", Instant.now(), removed=false, important = false, UUID.randomUUID()),
            MailMessageHeader("Hello 2", Instant.now(), removed=false, important = false, UUID.randomUUID()),
            MailMessageHeader("Hello 3", Instant.now(), removed=false, important = false, UUID.randomUUID()
        )))
        rvMailMessages.layoutManager = LinearLayoutManager(this)
    }

}