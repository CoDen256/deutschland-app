package de.app.ui.dashboard

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.account.ServiceAccount
import de.app.api.account.CitizenServiceAccount
import de.app.api.account.CompanyServiceAccount
import de.app.core.SessionManager
import de.app.data.model.UserHeader
import de.app.databinding.FragmentDashboardAppointmentItemBinding
import de.app.databinding.FragmentDashboardBinding
import de.app.databinding.FragmentDashboardSectionBinding
import de.app.databinding.FragmentUserSelectItemBinding
import de.app.ui.components.AccountAwareFragment
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : AccountAwareFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var sessionManager: SessionManager

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
            sessionManager.currentUser?.let {
                renderIcon(it)
            }
        }
    }

    private fun renderIcon(item: UserHeader) {
        GlideToVectorYou.init()
            .with(requireContext())
            .setPlaceHolder(R.drawable.img, R.drawable.img)
            .load(Uri.parse("https://avatars.dicebear.com/api/jdenticon/${item.userId}.svg"), binding.icon)
    }
}