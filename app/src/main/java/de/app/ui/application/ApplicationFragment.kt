package de.app.ui.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
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
    @Inject
    lateinit var applicationService: ApplicationService

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd. MMMM")

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentApplicationBinding.inflate(inflater, container, false)

    fun inflateItem(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentApplicationItemBinding.inflate(inflater, container, false)

    override fun setup(account: AccountInfo) {
        val elements =
            ArrayList<Application>(applicationService.getAllApplicationsByAccountId(account.accountId))

        binding.list.adapter = ListViewAdapter({ i,v -> inflateItem(i,v) }, elements) { e, b ->
            setupItem(b, e)
        }
    }

    private fun setupItem(binding: FragmentApplicationItemBinding, application: Application) {
        binding.apply {
            statusBox.background = getStatusColor(application)
            status.text = getString(
                R.string.status_placeholder,
                application.status.name.lowercase().replaceFirstChar { it.uppercase() })
            date.text = application.applicationDate.format(dateTimeFormatter)
            description.text = application.description
            name.text = application.name
        }
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