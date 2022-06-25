package de.app.ui.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.SecretToken
import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.core.SessionManager
import de.app.databinding.FragmentAppointmentBinding
import de.app.databinding.FragmentAppointmentItemBinding
import de.app.ui.components.ListViewAdapter
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentFragment : Fragment() {

    @Inject
    lateinit var appointmentService: AppointmentService
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var citizenRepo: CitizenServiceAccountRepository
    private lateinit var binding: FragmentAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        val elementBinding = { i: LayoutInflater, v: ViewGroup ->
            FragmentAppointmentItemBinding.inflate(i, v, false)}

        val elements = ArrayList<Appointment>()
        binding.list.adapter = ListViewAdapter(elementBinding, elements){e, b ->
            b.additionalInfo.text = getString(R.string.note_additional_info, e.additionalInfo)
            b.address.text = "${e.address.city}, ${e.address.postalCode}, ${e.address}"
            b.date.text = e.appointment.format(DateTimeFormatter.ofPattern("EEEE, dd. MMMM"))
            b.time.text = e.appointment.format(DateTimeFormatter.ofPattern("HH:mm"))
            b.description.text = e.description
            b.name.text = e.name
        }
        binding.list.layoutManager = LinearLayoutManager(context)


        sessionManager.currentUser?.let {
            citizenRepo.getCitizenAccount(SecretToken( it.accountSecretToken)).onSuccess { info ->
                    val appointments = appointmentService.getAllAppointmentsByAccountId(info.accountId)
                    elements.addAll(appointments)
            }
        }

        return binding.root
    }
}