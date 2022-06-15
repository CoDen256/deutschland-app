package de.app.ui.mailbox

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.app.api.mail.MailMessageHeader
import de.app.databinding.FragmentMailBoxItemBinding

class MailMessageViewAdapter(
    private val mailMessages: MutableList<MailMessageHeader>
) : RecyclerView.Adapter<MailMessageViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentMailBoxItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message: MailMessageHeader = mailMessages[position]
        val subjectView = holder.subjectView
        subjectView.text = message.subject
        holder.preview.text = message.preview
        holder.deleteButton.setOnClickListener {
            mailMessages.remove(message)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount(): Int = mailMessages.size

    inner class ViewHolder(binding: FragmentMailBoxItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val subjectView: TextView = binding.mailSubject
        val deleteButton = binding.mailDeleteButton
        val preview = binding.mailTextPreview
    }

}