package de.app.ui.mailbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.app.R
import de.app.data.model.mail.MailMessageHeader

class MailMessageAdapter (private val mailMessages: List<MailMessageHeader>):
    RecyclerView.Adapter<MailMessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val mailView = inflater.inflate(R.layout.item_mailmessage, parent, false)
        return ViewHolder(mailView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val message: MailMessageHeader = mailMessages[position]
        // Set item views based on your views and data model
        val textView = holder.nameTextView
        textView.text = message.subject
        val button = holder.messageButton
    }

    override fun getItemCount(): Int {
        return mailMessages.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.mail_subject)
        val messageButton = itemView.findViewById<Button>(R.id.mail_delete_button)
    }
}
