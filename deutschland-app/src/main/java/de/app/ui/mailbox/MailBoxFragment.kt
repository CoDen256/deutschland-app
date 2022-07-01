package de.app.ui.mailbox

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import de.app.databinding.FragmentMailBoxItemBinding
import de.app.databinding.FragmentMailBoxListBinding
import de.app.ui.components.AccountAwareListFragment
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
            mailDeleteButton.setOnClickListener {
                removeMail(item)
            }
        }
    }

    private fun removeMail(item: MailMessageHeader) {
        val index = items.indexOf(item)
        if (index == -1) return
        items.removeAt(index)
        adapter.notifyItemRemoved(index)
    }

    override fun loadItems() = mailService.getAllMessagesForAccountId(account.accountId)
        .sortedBy { it.received }

    override fun setup() {
        binding.list.adapter = adapter
    }
}