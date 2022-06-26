package de.app.ui.mailbox

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.R
import de.app.api.mail.MailMessageHeader
import de.app.core.config.DataGenerator.Companion.generateMails
import de.app.core.runWithInterval
import java.time.Instant
import java.util.*
import kotlin.random.Random

/**
 * A fragment representing a list of Items.
 */
class MailBoxFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =
            inflater.inflate(R.layout.fragment_mail_box_list, container, false) as RecyclerView

        val mailMessages = getMails()
        with(view) {
            layoutManager = LinearLayoutManager(context)
            adapter = MailMessageViewAdapter(mailMessages)
        }

        runWithInterval({updateMails(view, mailMessages)})

        return view
    }

    private fun updateMails(rv: RecyclerView, origin: MutableList<MailMessageHeader>) {
        val newMails = getMails()
        activity?.runOnUiThread {
            rv.adapter?.apply {
                val curSize = itemCount
                origin.addAll(newMails)
                notifyItemRangeChanged(curSize, newMails.size)
            }
        }
    }

    private fun getMails(): MutableList<MailMessageHeader> = ArrayList<MailMessageHeader>(
        generateMails(30)
    )
}