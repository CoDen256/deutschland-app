package de.app.ui.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.SecretToken
import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.api.applications.ApplicationStatus
import de.app.core.SessionManager
import de.app.databinding.FragmentApplicationBinding
import de.app.databinding.FragmentApplicationItemBinding
import de.app.ui.components.ListViewAdapter
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class ApplicationFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var citizenRepo: CitizenServiceAccountRepository
    @Inject lateinit var applicationService: ApplicationService
    private lateinit var binding: FragmentApplicationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplicationBinding.inflate(inflater, container, false)

        val elementBinding = { i: LayoutInflater, v: ViewGroup ->
            FragmentApplicationItemBinding.inflate(i, v, false)}

        val elements = ArrayList<Application>()
        binding.list.adapter = ListViewAdapter(elementBinding, elements){e, b ->
            b.statusBox.background = getDrawable(requireContext(),when(e.status){
                ApplicationStatus.SENT -> R.color.sent
                ApplicationStatus.VERIFICATION -> R.color.verification
                ApplicationStatus.PROCESSING -> R.color.processing
                ApplicationStatus.DONE -> R.color.done
                ApplicationStatus.REJECTED -> R.color.sent
            })
            b.status.text = getString(R.string.status_placeholder, e.status.name.lowercase().replaceFirstChar { it.uppercase() })
            b.date.text = e.applicationDate.format(DateTimeFormatter.ofPattern("dd. MMMM"))
            b.description.text = e.description
            b.name.text = e.name
        }
        binding.list.layoutManager = LinearLayoutManager(context)


        sessionManager.currentUser?.let {
            citizenRepo.getCitizenAccount(SecretToken( it.accountSecretToken)).onSuccess { info ->
                val applications = applicationService.getAllApplicationsByAccountId(info.accountId)
                elements.addAll(applications)
            }
        }

        return binding.root
    }
}