package de.app.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.account.AccountInfo
import de.app.api.account.CitizenAccountInfo
import de.app.api.account.CompanyAccountInfo
import de.app.databinding.FragmentDashboardAppointmentItemBinding
import de.app.databinding.FragmentDashboardBinding
import de.app.databinding.FragmentDashboardSectionBinding
import de.app.ui.components.AccountAwareFragment

@AndroidEntryPoint
class DashboardFragment : AccountAwareFragment<FragmentDashboardBinding>() {

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun setup() {
        fillHeader(account)
        fillDashboardInfo()
    }

    private fun fillHeader(account: AccountInfo) {
        binding.apply {
            accountId.text = getString(R.string.account_id_dashboard, account.accountId)
            address.text = getString(
                R.string.address_dashboard,
                account.address.postalCode,
                account.address.city
            )
            welcome.text = getString(
                R.string.welcome_dashboard, when (account) {
                    is CitizenAccountInfo ->
                        "${account.formOfAddress} ${account.firstName} ${account.surname}"
                    is CompanyAccountInfo -> account.fullName
                }
            )
        }
    }

    private fun fillDashboardInfo() {
        inflateSection(
            binding.appointments, "Applications", listOf(
                "19.10.2000" to "You have an appointment at doctor",
                "20.09.2010" to "Appointment to pick up the document",
                "19.05.2022" to "Appointment to visit residence",
                "19.05.2022" to "Appointment to visit residence"
            )
        )
        inflateSection(
            binding.applications, "Applications", listOf(
                "Application #1" to "Done",
                "Application #2" to "Pending",
                "Application #3" to "Sent",
                "Application #4" to "Pending"
            )
        )
        inflateSection(
            binding.emergencies, "Emergencies", listOf(
                "Emergency #1" to "There is an emergency in your are",
                "Emergency #2" to "Flooding and strong winds in Merseburg",
                "Emergency #3" to "Hot weather in Merseburg",
                "Emergency #4" to "Too much snow"
            )
        )
    }

    private fun inflateSection(
        binding: FragmentDashboardSectionBinding,
        sectionTitle: String,
        cards: List<Pair<String, String>>
    ) {

        binding.sectionTitle.text = sectionTitle
        inflateCard(binding.first, cards[0])
        inflateCard(binding.second, cards[1])
        inflateCard(binding.third, cards[2])
        inflateCard(binding.fourth, cards[3])
    }

    private fun inflateCard(
        item: FragmentDashboardAppointmentItemBinding,
        itemData: Pair<String, String>
    ) {
        val (caption, body) = itemData
        item.body.text = body
        item.caption.text = caption
    }
}