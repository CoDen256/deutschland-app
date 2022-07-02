package de.app.config.common

import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import de.app.api.account.CitizenServiceAccount
import de.app.api.account.CompanyServiceAccount
import de.app.config.AssetDataSource
import de.app.data.model.Address
import de.app.data.model.FileHeader
import java.lang.reflect.Type
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileHeaderDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    AssetDataSource<FileHeaderAsset, FileHeaderAsset>(context, "origin/documents.json") {
    override fun map(origin: FileHeaderAsset): FileHeaderAsset {
        return origin
    }

    override fun getJsonType(): Type = object : TypeToken<List<FileHeaderAsset>>() {}.type
}

data class FileHeaderAsset(
    val id: Int,
    val mimeType: String,
    val name: String,
    val uri: String
){
    fun map(): FileHeader{
        return FileHeader(
            name, uri, mimeType
        )
    }
}

@Singleton
class AddressDataSource @Inject constructor(@ApplicationContext private val context: Context) :
    AssetDataSource<AddressAsset, AddressAsset>(context, "origin/addresses.json") {
    override fun map(origin: AddressAsset): AddressAsset {
        return origin
    }

    override fun getJsonType(): Type = object : TypeToken<List<AddressAsset>>() {}.type
}

data class AddressAsset(
    val id: Int,
    val address: String,
    val city: String,
    val country: String,
    val postalCode: String
){
    fun map(): Address{
        return Address(city, country, postalCode, address)
    }
}

@Singleton
class CitizenAccountDataSource @Inject constructor(
    @ApplicationContext context: Context,
    addressDataSource: AddressDataSource
    ) :
    AssetDataSource<CitizenServiceAccount, CitizenServiceAccountAsset>(context, "origin/citizens.json") {

    private val addressById = addressDataSource.data.associateBy { it.id }

    override fun map(origin: CitizenServiceAccountAsset): CitizenServiceAccount {
        return CitizenServiceAccount(
            origin.accountId,
            origin.displayName,
            addressById[origin.addressId]!!.map(),
            origin.firstName,
            origin.surname,
            origin.salutation,
            origin.dateOfBirth,
            origin.nationality,
            addressById[origin.placeOfBirthId]!!.map()
        )
    }

    override fun getJsonType(): Type = object : TypeToken<List<CitizenServiceAccountAsset>>() {}.type
}


@Singleton
class CompanyAccountDataSource @Inject constructor(
    @ApplicationContext context: Context,
    addressDataSource: AddressDataSource
) :
    AssetDataSource<CompanyServiceAccount, CompanyServiceAccountAsset>(context, "origin/companies.json") {

    private val addressById = addressDataSource.data.associateBy { it.id }

    override fun map(origin: CompanyServiceAccountAsset): CompanyServiceAccount {
        return CompanyServiceAccount(
            origin.accountId,
            origin.displayName,
            addressById[origin.addressId]!!.map(),
            origin.fullName,
            origin.foundedDate
        )
    }

    override fun getJsonType(): Type = object : TypeToken<List<CompanyServiceAccountAsset>>() {}.type
}

data class CitizenServiceAccountAsset(
    val accountId: String,
    val displayName: String,
    val addressId: Int,
    val firstName: String,
    val surname: String,
    val salutation: String,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val placeOfBirthId: Int,
)


data class CompanyServiceAccountAsset(
    val accountId: String,
    val displayName: String,
    val addressId: Int,
    val fullName: String,
    val foundedDate: LocalDate
)