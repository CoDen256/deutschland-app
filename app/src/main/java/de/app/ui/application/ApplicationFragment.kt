package de.app.ui.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.account.AccountInfo
import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.api.applications.ApplicationStatus
import de.app.databinding.FragmentApplicationBinding
import de.app.databinding.FragmentApplicationItemBinding
import de.app.ui.components.AccountAwareFragment
import de.app.ui.components.ListViewAdapter
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class ApplicationFragment : AccountAwareFragment<FragmentApplicationBinding>() {

    @Inject lateinit var applicationService: ApplicationService

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentApplicationBinding.inflate(inflater, container, false)

    override fun setup(account: AccountInfo) {
        val elementBinding = { i: LayoutInflater, v: ViewGroup ->
            FragmentApplicationItemBinding.inflate(i, v, false)}

        val elements =
            ArrayList<Application>(applicationService.getAllApplicationsByAccountId(account.accountId))

        setupList(elementBinding, elements)
    }

    private fun setupList(
        elementBinding: (LayoutInflater, ViewGroup) -> FragmentApplicationItemBinding,
        elements: List<Application>
    ) {
        binding.list.adapter = ListViewAdapter(elementBinding, elements) { e, b ->
            setupItem(b, e)
        }
    }

    private fun setupItem(b: FragmentApplicationItemBinding, e: Application) {
        b.statusBox.background = getDrawable(
            requireContext(), when (e.status) {
                ApplicationStatus.SENT -> R.color.sent
                ApplicationStatus.VERIFICATION -> R.color.verification
                ApplicationStatus.PROCESSING -> R.color.processing
                ApplicationStatus.DONE -> R.color.done
                ApplicationStatus.REJECTED -> R.color.sent
            }
        )
        b.status.text = getString(
            R.string.status_placeholder,
            e.status.name.lowercase().replaceFirstChar { it.uppercase() })
        b.date.text = e.applicationDate.format(DateTimeFormatter.ofPattern("dd. MMMM"))
        b.description.text = e.description
        b.name.text = e.name
    }
}