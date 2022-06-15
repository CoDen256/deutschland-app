package de.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.CompanyServiceAccountRepository
import de.app.core.config.BaseServiceAccountRepository
import de.app.core.AccountDataSource
import de.app.core.SessionManager
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
}

@Module
@InstallIn(SingletonComponent::class)
object SingletonAppModule {

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
    fun accountDataSource(db: AppDatabase): AccountDataSource {
        return AccountDataSource(db.accountDao())
    }

    @Singleton
    @Provides
    fun sessionManager(dataSource: AccountDataSource): SessionManager{
        return SessionManager(dataSource)
    }
}
