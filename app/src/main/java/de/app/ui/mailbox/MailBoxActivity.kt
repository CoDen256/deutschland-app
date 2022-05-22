package de.app.ui.mailbox

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.app.R
import de.app.data.model.mail.MailMessageHeader
import de.app.databinding.ActivityMailBoxBinding
import java.time.Instant
import java.util.*
import kotlin.random.Random

class MailBoxActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMailBoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMailBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val rvMailMessages = findViewById<View>(R.id.rvMailMessages) as RecyclerView


        val mailMessages = getMails()
        rvMailMessages.adapter = MailMessageAdapter(mailMessages)
        rvMailMessages.layoutManager = LinearLayoutManager(this)

         Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateMails(rvMailMessages, mailMessages)
            }
        }, 0, 10000)
    }

    private fun updateMails(rv: RecyclerView, origin: MutableList<MailMessageHeader>) {
        val newMails = getMails()
        runOnUiThread { rv.adapter?.apply {
            val curSize = itemCount
            origin.addAll(newMails)
            notifyItemRangeChanged(curSize, newMails.size)
        } }
    }

    private fun getMails(): MutableList<MailMessageHeader> = ArrayList<MailMessageHeader>().apply {
        for (i in 0..Random.nextInt(1, 10)) {
            add(
                MailMessageHeader(
                    "Hello $i",
                    Instant.now(),
                    removed = false,
                    important = false,
                    UUID.randomUUID()
                )
            )
        }
    }


}
