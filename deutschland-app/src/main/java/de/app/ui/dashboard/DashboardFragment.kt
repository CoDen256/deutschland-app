package de.app.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.account.ServiceAccount
import de.app.api.account.CitizenServiceAccount
import de.app.api.account.CompanyServiceAccount
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
    }

    private fun fillHeader(account: ServiceAccount) {
        binding.apply {
            accountId.text = getString(R.string.account_id_dashboard, account.accountId)
            address.text = getString(
                R.string.address_dashboard,
                account.address.postalCode,
                account.address.city
            )
            welcome.text = when (account) {
                is CitizenServiceAccount ->
                    "${account.salutation} ${account.firstName} ${account.surname}"
                is CompanyServiceAccount -> account.fullName
            }
        }
    }
}