package de.app.config

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.account.ServiceAccount
import de.app.api.applications.Application
import de.app.api.applications.ApplicationService
import de.app.api.applications.ApplicationStatus
import de.app.api.appointment.Appointment
import de.app.api.appointment.AppointmentService
import de.app.api.mail.MailMessageHeader
import de.app.api.mail.MailboxService
import de.app.api.service.AdministrativeService
import de.app.api.service.AdministrativeServiceRegistry
import de.app.api.service.ServiceType
import de.app.api.service.form.*
import de.app.api.service.submit.SubmittedForm
import de.app.config.common.*
import de.app.core.success
import de.app.core.successOrElse
import de.app.data.model.FileHeader
import java.io.IOException
import java.lang.reflect.Type
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BaseAdministrativeServiceRegistry @Inject constructor(
    private val serviceDataSource: ServiceByAddressDataSource,
    private val formSource: FormSource,
    private val mailboxService: MailboxService,
    private val appointmentService: AppointmentService,
    private val applicationService: ApplicationService
) : AdministrativeServiceRegistry {

    private val services: List<Pair<String, List<AdministrativeService>>> by lazy {
        serviceDataSource.data
    }

    private val forms = formSource.data.associateBy { it.serviceId }

    override fun getAllCitizenServices(): List<AdministrativeService> {
        return services.flatMap { it.second }.filter { it.type != ServiceType.COMPANY }
    }

    override fun getAllCompanyServices(): List<AdministrativeService> {
        return services.flatMap { it.second }.filter { it.type != ServiceType.CITIZEN }
    }


    override fun getServiceById(id: String): Result<AdministrativeService> {
        return services.flatMap { it.second }.find { id == it.id }.successOrElse()
    }

    override fun getApplicationForm(service: AdministrativeService): Result<Form> {
        return forms[services.find { it.second.any { it.id == service.id }}!!.first].successOrElse()
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
                description = "${account.displayName}, danke,dass Sie einen Antrag für '${service.name}' gesendet haben. Wir aktualisieren den Status, nachdem wir alle Dokumente verifizieren",
                serviceId = service.id,
                accountId = account.accountId,
                status = ApplicationStatus.SENT,
                applicationDate = LocalDateTime.now()
            )
        )
    }

    private fun dispatchAppointment(account: ServiceAccount, service: AdministrativeService) {
        appointmentService.addAppointmentForAccountId(
            account.accountId, Appointment(
                name = "Termin zu `${service.name}`",
                description = "${account.displayName}, Sie haben einen Termin bei ${service.name} vereinbart",
                address = service.address,
                additionalInfo = "Bringen Sie bitte alle notwendigen Unterlagen mit",
                serviceId = service.id,
                accountId = account.accountId,
                appointment = LocalDateTime.now().plus(2, ChronoUnit.DAYS).plus(50, ChronoUnit.MINUTES)
            )
        )
    }

    private fun dispatchMail(account: ServiceAccount, service: AdministrativeService) {
        mailboxService.sendMessageToAccountId(
            account.accountId,
            MailMessageHeader(
                "Your application has been received",
                preview = "Thank you for sending application to ${service.name}, ${account.displayName}",
                received = LocalDateTime.now(),
                id = UUID.randomUUID().toString(),
                sender = service.email
            )
        )
    }
}


@Singleton
class ServiceByAddressDataSource @Inject constructor(
    @ApplicationContext context: Context,
    addressDataSource: AddressDataSource,
    serviceDataSource: ServiceAssetDataSource,
) :
    AssetDataSource<Pair<String, List<AdministrativeService>>, ServiceByAddress>(
        context,
        "binding/service-by-address.json"
    ) {

    private val addressById = addressDataSource.data.associateBy { it.id }
    private val serviceById = serviceDataSource.data.associateBy { it.id }

    override fun map(origin: ServiceByAddress): Pair<String, List<AdministrativeService>> {
        return origin.serviceId to origin.addressIds.map {
            val serv = serviceById[origin.serviceId]!!.map(addressById[it]!!.map())
            serv.copy(
                id = serv.id + "-" + serv.address.postalCode
            )
        }
    }

    override fun getJsonType(): Type = object : TypeToken<List<ServiceByAddress>>() {}.type
}

data class ServiceByAddress(
    val serviceId: String,
    val addressIds: List<Int>
)

@Singleton
class FormSource @Inject constructor(
    @ApplicationContext context: Context,
    private val documentDataSource: FileHeaderDataSource

) {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateConverter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeConverter())
        .create()

    private val fields = listOf(
        TextInfoField::class,
        TextField::class,
        BigTextField::class,
        EmailField::class,
        NumberField::class,
        SingleChoiceField::class,
        RadioChoiceField::class,
        MultipleChoiceField::class,
        DateField::class,
        AttachmentField::class,
        ImageField::class
    )

    private val typeToField = fields.associateBy { it.simpleName.orEmpty() }
    private val documentById = documentDataSource.data.associate {
        it.id to it.map()
    }
    val data: List<Form> by lazy {
        initialFetch(context, "binding/forms.json")
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): Result<String> {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }.success()
        } catch (ioException: IOException) {
            Result.failure(ioException)
        }
    }

    private fun initialFetch(context: Context, fileName: String): List<Form> {
        return getJsonDataFromAsset(context, fileName).mapCatching {
            gson.fromJson<List<FormAsset>>(it, getJsonType())
        }.mapCatching { list ->
            mapList(list)
        }.getOrThrow()
    }

    private fun mapList(list: List<FormAsset>) = list.map { map(it) }

    private fun map(origin: FormAsset): Form {
        return Form(origin.serviceId, paymentRequired = origin.paymentRequired,
            fields = origin.fields.map { mapField(it) })
    }

    private fun mapField(origin: JsonField): Field {
        typeToField[origin.type]?.let {
            return@mapField gson.fromJson(origin.field, it.java)
        }
        return gson.fromJson(origin.field, JsonDocumentInfoField::class.java).map(documentById)
    }

    private fun getJsonType(): Type = object : TypeToken<List<FormAsset>>() {}.type
}


data class FormAsset(
    val serviceId: String,
    val paymentRequired: Boolean,
    val fields: List<JsonField>
)

data class JsonField(
    val type: String,
    val field: JsonObject
)

data class JsonDocumentInfoField(
    val label: String,
    val documents: List<Int>,
) {
    fun map(documentById: Map<Int, FileHeader>): DocumentInfoField {
        return DocumentInfoField(
            label = label,
            documents = documents.map { documentById[it]!! }
        )
    }
}