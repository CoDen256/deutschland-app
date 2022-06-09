package de.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.app.R
import de.app.core.AccountDataSource
import de.app.core.SessionManager
import de.app.databinding.FragmentDashboardBinding
import de.app.notifications.Notificator

class DashboardFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        val binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val account = AccountDataSource().getAccounts()[0]

        genFakeNotifications()


        binding.welcome.text = getString(R.string.welcome_dashboard,
            "${account.name} ${account.surname}")

        binding.burgerId.text = getString(R.string.account_id_dashboard, account.accountId)
        val plz = "06217"
        val address = "Merseburg"
        binding.address.text = getString(R.string.address_dashboard, plz, address)

        return root
    }

    private fun genFakeNotifications() {
        val notificator = Notificator(requireContext())
        notificator.sendEmergencyNotification("There is an emergency in your area")
        notificator.sendLawChangeNotification("There are new laws")
        notificator.sendNewMailNotification("Your document ist abgelaufen")
    }
}