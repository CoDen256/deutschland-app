package de.app

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import de.app.api.CitizenServiceAccountRepository
import de.app.api.CompanyServiceAccountRepository
import de.app.api.dummy.BaseServiceAccountRepository

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
