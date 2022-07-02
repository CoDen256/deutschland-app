package de.app.config

import de.app.api.account.ServiceAccount
import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.api.applications.ApplicationStatus
import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceProvider
import de.app.api.service.AdministrativeServiceRegistry
import de.app.api.service.form.Form
import de.app.api.service.submit.SubmittedForm
import de.app.config.DataGenerator.Companion.generateFields
import de.app.core.successOrElse
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt


@Singleton
class BaseAdministrativeServiceRegistry @Inject constructor(
    private val mailboxService: MailboxService,
    private val appointmentService: AppointmentService,
    private val applicationService: ApplicationService
): AdministrativeServiceRegistry {

    companion object {
        val citizenServices = DataGenerator.generateServices(30)
        val companyServices = DataGenerator.generateServices(30)
        val services = citizenServices + companyServices
    }

    private val forms = services.map{it.id}.associateWith {
        Form(generateFields(nextInt(30)), nextBoolean())
    }

    override fun getAllProviders(): List<AdministrativeServiceProvider> {
        TODO("Not yet implemented")
    }

    override fun getAllCitizenServices(): List<AdministrativeService> {
        return citizenServices
    }

    override fun getAllCompanyServices(): List<AdministrativeService> {
        return companyServices
    }

    override fun getProviderById(id: String): Result<AdministrativeServiceProvider> {
        TODO("Not yet implemented")
    }

    override fun getServiceById(id: String): Result<AdministrativeService> {
        return services.find { id == it.id }.successOrElse()
    }

    override fun getApplicationForm(service: AdministrativeService): Result<Form> {
        return forms[service.id].successOrElse()
    }

    override fun sendApplicationForm(
        account: ServiceAccount,
        service: AdministrativeService,
        submittedForm: SubmittedForm
    ): Result<Unit> {
        dispatch(account, service)
        return Result.success(Unit)
    }

    private fun dispatch(account: ServiceAccount, service: AdministrativeService) {
        dispatchMail(account, service)
        dispatchApplication(account, service)
        dispatchAppointment(account, service)
    }

    private fun dispatchApplication(account: ServiceAccount, service: AdministrativeService) {
        applicationService.addApplicationForAccountId(
            account.accountId,
            Application(
                name = "Antrag zu `${service.name}`",
                description = "${account.displayName}, Sie haben einen Antrag für '${service.name}' gesendet",
                serviceId = service.id,
                accountId = account.accountId,
                status = ApplicationStatus.SENT,
                applicationDate = LocalDateTime.now()
            )
        )
    }

    private fun dispatchAppointment(account: ServiceAccount, service: AdministrativeService) {
        appointmentService.addAppointmentForAccountId(account.accountId, Appointment(
            name = "Termin zum ${service.name}",
            description = "${account.displayName}, Sie haben einen Termin bei ${service.name} vereinbart",
            address = service.address,
            additionalInfo = "Bringen Sie bitte alle notwendigen Unterlagen mit",
            serviceId = service.id,
            accountId = account.accountId,
            appointment = LocalDateTime.now().plus(2, ChronoUnit.DAYS)
        ))
    }

    private fun dispatchMail(account: ServiceAccount, service: AdministrativeService) {
        mailboxService.sendMessageToAccountId(
            account.accountId,
            MailMessageHeader(
                "Your application has been received",
                preview = "Thank you for sending application to ${service.name}, ${account.displayName}",
                received = Instant.now(),
                id = UUID.randomUUID().toString()
            )
        )
    }
}



@Singleton
class AdminServiceDataSource @Inject constructor(dataSource: FileHeaderAssetDataSource){
    val data by lazy {
        dataSource.data[0].`admin-service-files`
    }
}


