package de.app.ui.appointment

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.databinding.FragmentAppointmentBinding
import de.app.databinding.FragmentAppointmentItemBinding
import de.app.ui.components.AccountAwareListFragment
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentFragment : AccountAwareListFragment<FragmentAppointmentBinding, FragmentAppointmentItemBinding, Appointment>() {

    @Inject
    lateinit var appointmentService: AppointmentService

    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd. MMMM")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAppointmentBinding.inflate(inflater, container, false)

    override fun inflateChild(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAppointmentItemBinding.inflate(inflater, container, false)

    override fun setupChild(binding: FragmentAppointmentItemBinding, item: Appointment) {
        binding.apply {
            additionalInfo.text = getString(R.string.note_additional_info, item.additionalInfo)
            with(item.address){
                this@apply.address.text = getString(R.string.appointment_address_format,city, postalCode, address)
            }
            date.text = item.appointment.format(dateFormatter)
            time.text = item.appointment.format(timeFormatter)
            description.text = item.description
            name.text = item.name
        }
    }

    override fun loadItems(): List<Appointment> {
        return appointmentService.getAllAppointmentsByAccountId(account.accountId)
            .sortedBy { it.appointment }
    }

    override fun setup() {
        binding.list.adapter = adapter
        binding.swipe.setOnRefreshListener {
            val new = ArrayList(loadItems()).apply {
                removeAll(items)
            }
            items.addAll(0, new)
            adapter.notifyItemRangeInserted(0, new.size)
            binding.swipe.isRefreshing = false
            binding.list.post {
                binding.list.smoothScrollToPosition(0)
            }
        }
    }
}