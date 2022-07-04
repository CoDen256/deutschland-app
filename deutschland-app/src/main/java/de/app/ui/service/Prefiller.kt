package de.app.ui.service

import de.app.api.account.CitizenServiceAccount
import de.app.api.account.CompanyServiceAccount
import de.app.api.account.ServiceAccount
import java.time.format.DateTimeFormatter

class Prefiller {

    private val birthdayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val prefillPolicyCitizen =
        mapOf<String, (CitizenServiceAccount) -> String>(
            "name" to {it.firstName},
            "surname" to {it.surname},
            "birthday" to {it.dateOfBirth.format(birthdayFormatter)},
            "city" to {it.address.city},
            "postalcode" to {it.address.postalCode},
            "address" to {it.address.address}
        )
    val prefillPolicyCompany =
        mapOf<String, (CompanyServiceAccount) -> String>(
            "companyname" to {it.fullName},
            "city" to {it.address.city},
            "postalcode" to {it.address.postalCode},
            "address" to {it.address.address},
        )

    private fun prefillCitizen(id: String, account: CitizenServiceAccount): String?{
        prefillPolicyCitizen.forEach {
            if (id.contains("-") && id.split("-").last() == it.key){
                return it.value(account)
            }
        }
        return null
    }

    private fun prefillCompany(id: String, account: CompanyServiceAccount): String?{
        prefillPolicyCompany.forEach {
            if (id.endsWith(it.key)){
                return it.value(account)
            }
        }
        return null
    }

    fun prefill(id: String, account: ServiceAccount): String? = when(account){
        is CitizenServiceAccount -> prefillCitizen(id, account)
        is CompanyServiceAccount -> prefillCompany(id, account)
    }

}