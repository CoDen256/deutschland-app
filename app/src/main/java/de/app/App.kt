package de.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.CompanyServiceAccountRepository
import de.app.api.applications.ApplicationService
import de.app.api.appointment.AppointmentService
import de.app.api.emergency.EmergencyInfoProvider
import de.app.api.geo.GeodataService
import de.app.api.law.LawRegistryService
import de.app.api.safe.DataSafeService
import de.app.api.service.AdministrativeServiceRegistry
import de.app.api.signature.SignatureService
import de.app.core.db.UserDataSource
import de.app.core.SessionManager
import de.app.core.config.*
import de.app.core.db.AppDatabase
import javax.inject.Singleton

@HiltAndroidApp
class App: Application()

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun citizenAccountRepo(repo: BaseServiceAccountRepository) : CitizenServiceAccountRepository

    @Binds
    abstract fun companyAccountRepo(repo: BaseServiceAccountRepository) : CompanyServiceAccountRepository

    @Binds
    abstract fun appointmentService(service: BaseAppointmentService) : AppointmentService

    @Binds
    abstract fun applicationService(service: BaseApplicationService) : ApplicationService

    @Binds
    abstract fun emergencyInfoProvider(service: BaseEmergencyInfoProvider) : EmergencyInfoProvider

    @Binds
    abstract fun geodataService(service: BaseGeodataService): GeodataService

    @Binds
    abstract fun lawRegistryService(service: BaseLawRegistryService): LawRegistryService

    @Binds
    abstract fun dataSafeService(service: BaseDataSafeService): DataSafeService

    @Binds
    abstract fun administrativeServiceRegistry(service: BaseAdministrativeServiceRegistry) : AdministrativeServiceRegistry

    @Binds
    abstract fun signatureService(service: BaseSignatureService) : SignatureService
}

@Module
@InstallIn(SingletonComponent::class)
object SingletonAppModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(application: Application)
    = LocationServices.getFusedLocationProviderClient(application)

    @Singleton
    @Provides
    fun appDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "de-app-database"
        ).build()
    }

    @Singleton
    @Provides
    fun accountDataSource(db: AppDatabase): UserDataSource {
        return UserDataSource(db.accountDao())
    }

    @Singleton
    @Provides
    fun sessionManager(dataSource: UserDataSource): SessionManager{
        return SessionManager(dataSource)
    }
}
