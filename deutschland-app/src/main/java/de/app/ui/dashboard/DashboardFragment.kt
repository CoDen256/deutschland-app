package de.app.ui.dashboard

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.account.CitizenServiceAccount
import de.app.api.account.CompanyServiceAccount
import de.app.api.account.ServiceAccount
import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.api.emergency.Emergency
import de.app.api.emergency.EmergencyInfoProvider
import de.app.core.SessionManager
import de.app.data.model.UserHeader
import de.app.databinding.FragmentDashboardBinding
import de.app.databinding.FragmentDashboardFeedItemBinding
import de.app.ui.components.AccountAwareListFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.reflect.KClass

@AndroidEntryPoint
class DashboardFragment : AccountAwareListFragment<FragmentDashboardBinding, FragmentDashboardFeedItemBinding, FeedItem>() {

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var emergencyInfoProvider: EmergencyInfoProvider
    @Inject
    lateinit var applicationService: ApplicationService
    @Inject
    lateinit var appointmentService: AppointmentService

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)


    override fun inflateChild(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardFeedItemBinding.inflate(inflater, container, false)

    override fun setupChild(binding: FragmentDashboardFeedItemBinding, item: FeedItem) {
        binding.apply {
            date.text = DateTimeFormatter.ISO_DATE.format(item.dateTime)
            time.text = DateTimeFormatter.ofPattern("HH:mm").format(item.dateTime)
            shortInfo.text = item.shortInfo
            description.text = item.description
            name.text = item.name
            type.setBackgroundColor(requireActivity().getColor(when(item.type){
                Application::class -> R.color.application
                Emergency::class -> R.color.emergency
                Appointment::class -> R.color.appointment
                else -> R.color.black
            }))
        }
    }

    override fun loadItems(): List<FeedItem> {
        val loaded = ArrayList<FeedItem>()

        val cityEmergencies =
            emergencyInfoProvider.getAllEmergenciesForCity(account.address.city)
                .sortedByDescending { it.dateTime }
                .take(2)


        val countryEmergencies =
            emergencyInfoProvider.getAllEmergenciesForCountry(account.address.country)
                .sortedByDescending { it.dateTime }
                .take(2)

        val appointments = appointmentService.getAllAppointmentsByAccountId(account.accountId)
            .sortedByDescending { it.appointment }
            .take(2)

        val applications = applicationService.getAllApplicationsByAccountId(account.accountId)
            .sortedByDescending { it.applicationDate }
            .take(2)


        loaded.addAll(cityEmergencies.map { extractFeedItem(it) })
        loaded.addAll(countryEmergencies.map { extractFeedItem(it) })
        loaded.addAll(appointments.map { extractFeedItem(it) })
        loaded.addAll(applications.map { extractFeedItem(it) })

        return loaded
    }

    private fun extractFeedItem(emergency: Emergency): FeedItem{
        return FeedItem(
            Emergency::class,
            emergency.name,
            emergency.city,
            emergency.description,
            emergency.dateTime
        )
    }

    private fun extractFeedItem(appointment: Appointment): FeedItem{
        return FeedItem(
            Appointment::class,
            appointment.name,
            appointment.address.city + " " + appointment.address.address,
            appointment.description,
            appointment.appointment
        )
    }

    private fun extractFeedItem(application: Application): FeedItem{
        return FeedItem(
            Application::class,
            application.name,
            application.status.toString(),
            application.description,
            application.applicationDate
        )
    }

    override fun setup() {
        fillHeader(account)
        binding.feed.adapter = adapter
        binding.feedSwipe.setOnRefreshListener {
            items.clear()
            items.addAll(loadItems())
            adapter.notifyDataSetChanged()
            binding.feedSwipe.isRefreshing = false
        }
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
            .load(
                Uri.parse("https://avatars.dicebear.com/api/jdenticon/${item.userId}.svg"),
                binding.icon
            )
    }

}


data class FeedItem(
    val type: KClass<*>,
    val name: String,
    val shortInfo: String,
    val description: String,
    val dateTime: LocalDateTime
)