package de.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.core.db.AccountDataSource
import de.app.data.model.AccountHeader
import de.app.databinding.FragmentDashboardAppointmentItemBinding
import de.app.databinding.FragmentDashboardBinding
import de.app.databinding.FragmentDashboardSectionBinding
import de.app.notifications.Notificator
import javax.inject.Inject
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    @Inject
    lateinit var dataSource: AccountDataSource
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        val binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val account: AccountHeader = null!!//dataSource.getAccounts()[0]

        genFakeNotifications()


        binding.welcome.text = getString(R.string.welcome_dashboard,
            "${account.displayName}")

        binding.burgerId.text = getString(R.string.account_id_dashboard, account.id)
        val plz = "06217"
        val address = "Merseburg"
        binding.address.text = getString(R.string.address_dashboard, plz, address)

        inflateSection(binding.appointments, "Applications", listOf(
            "19.10.2000" to "You have an appointment at doctor",
            "20.09.2010" to "Appointment to pick up the document",
            "19.05.2022" to "Appointment to visit residence",
            "19.05.2022" to "Appointment to visit residence"
        ))
        inflateSection(binding.applications, "Applications", listOf(
            "Application #1" to "Done",
            "Application #2" to "Pending",
            "Application #3" to "Sent",
            "Application #4" to "Pending"
        ))
        inflateSection(binding.emergencies, "Emergencies", listOf(
            "Emergency #1" to "There is an emergency in your are",
            "Emergency #2" to "Flooding and strong winds in Merseburg",
            "Emergency #3" to "Hot weather in Merseburg",
            "Emergency #4" to "Too much snow"
        ))
        return root
    }

    private fun inflateSection(binding: FragmentDashboardSectionBinding,
                              sectionTitle: String,
                              cards: List<Pair<String, String>>
                              ){

        binding.sectionTitle.text = sectionTitle
        inflateCard(binding.first, cards[0])
        inflateCard(binding.second, cards[1])
        inflateCard(binding.third, cards[2])
        inflateCard(binding.fourth, cards[3])
    }

    private fun inflateCard(item: FragmentDashboardAppointmentItemBinding, itemData: Pair<String, String>){
        val (caption, body) = itemData
        item.body.text = body
        item.caption.text = caption
    }

    private fun genFakeNotifications() {
        val notificator = Notificator(requireContext())
        notificator.sendEmergencyNotification("There is an emergency in your area")
        notificator.sendLawChangeNotification("There are new laws")
        notificator.sendNewMailNotification("Your document ist abgelaufen")
    }
}