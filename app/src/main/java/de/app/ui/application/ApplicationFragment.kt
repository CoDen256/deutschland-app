package de.app.ui.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.api.applications.ApplicationStatus
import de.app.databinding.FragmentApplicationBinding
import de.app.databinding.FragmentApplicationItemBinding
import de.app.ui.components.AccountAwareListFragment
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class ApplicationFragment : AccountAwareListFragment<FragmentApplicationBinding, FragmentApplicationItemBinding, Application>() {
    @Inject
    lateinit var applicationService: ApplicationService

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy")

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentApplicationBinding.inflate(inflater, container, false)

    override fun inflateChild(inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentApplicationItemBinding.inflate(inflater, container, false)

    override fun setupChild(binding: FragmentApplicationItemBinding, item: Application) {
        binding.apply {
            statusBox.background = getStatusColor(item)
            status.text = getString(
                R.string.status_placeholder,
                item.status.name.lowercase().replaceFirstChar { it.uppercase() })
            date.text = item.applicationDate.format(dateTimeFormatter)
            description.text = item.description
            name.text = item.name
        }
    }

    override fun setup() {
        binding.list.adapter = adapter
    }

    override fun loadItems(): List<Application> {
        return applicationService.getAllApplicationsByAccountId(account.accountId)
            .sortedByDescending { it.applicationDate }
    }

    private fun getStatusColor(application: Application) = getDrawable(
        requireContext(), when (application.status) {
            ApplicationStatus.SENT -> R.color.sent
            ApplicationStatus.VERIFICATION -> R.color.verification
            ApplicationStatus.PROCESSING -> R.color.processing
            ApplicationStatus.DONE -> R.color.done
            ApplicationStatus.REJECTED -> R.color.sent
        }
    )


}