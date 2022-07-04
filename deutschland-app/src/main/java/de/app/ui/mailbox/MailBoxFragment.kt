package de.app.ui.mailbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import de.app.databinding.FragmentMailBoxItemBinding
import de.app.databinding.FragmentMailBoxListBinding
import de.app.ui.components.AccountAwareListFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class MailBoxFragment : AccountAwareListFragment<FragmentMailBoxListBinding, FragmentMailBoxItemBinding, MailMessageHeader>() {

    @Inject lateinit var mailService: MailboxService

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMailBoxListBinding.inflate(inflater, container, false)

    override fun inflateChild(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMailBoxItemBinding.inflate(inflater, container, false)


    override fun setupChild(binding: FragmentMailBoxItemBinding, item: MailMessageHeader) {
        binding.apply {
            mailSubject.text = item.subject
            mailTextPreview.text = item.preview
            date.text = item.received.format(DateTimeFormatter.ofPattern("dd. MMM"))
            time.text = item.received.format(DateTimeFormatter.ofPattern("HH:mm"))
            mailSender.text = item.sender
        }
    }

    private fun removeMail(item: MailMessageHeader) {
        val index = items.indexOf(item)
        if (index == -1) return
        items.removeAt(index)
        adapter.notifyItemRemoved(index)
        mailService.removeMessagesForAccountId(account.accountId, item)
    }

    override fun loadItems() = mailService.getAllMessagesForAccountId(account.accountId)
        .filter { it.received.isBefore(LocalDateTime.now()) }
        .sortedByDescending { it.received }

    override fun setup() {
        binding.list.adapter = adapter

        binding.feedSwipe.setOnRefreshListener {
            val new = ArrayList(loadItems()).apply {
                removeAll(items)
            }
            items.addAll(0, new)
            adapter.notifyItemRangeInserted(0, new.size)
            binding.feedSwipe.isRefreshing = false
            binding.empty.isVisible = items.isEmpty()
        }

        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                removeMail(items[viewHolder.adapterPosition])
                binding.empty.isVisible = items.isEmpty()
            }
        })
        binding.empty.isVisible = items.isEmpty()
        helper.attachToRecyclerView(binding.list)
    }
}