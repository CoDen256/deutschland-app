package de.app.api.dummy

import de.app.api.AdministrativeServiceRegistry
import de.app.data.model.adminservice.*
import java.util.*

class BaseAdministrativeServiceRegistry : AdministrativeServiceRegistry {
    override fun getAllProviders(): List<AdministrativeServiceProvider> {
        TODO("Not yet implemented")
    }

    override fun getAllServices(): List<AdministrativeService> {
        return listOf(
            AdministrativeService(
                UUID.randomUUID(),
                "Sell a dog",
                "This administrative service allows you to send an application to sell a dog",
                "https://sell-dog.de/api/"
            )
        )
    }

    override fun getApplicationForm(service: AdministrativeService): ApplicationForm {
        return ApplicationForm(
            "Sell a dog",
            "This administrative service allows you to send an application to sell a dog",
            service,
            ArrayList<TextField>().apply {
                for (i in 0..100){
                    add(
                        TextField(
                            name = "name",
                            label = "Name $i",
                            hint = "John"
                        )
                    )
                    add(
                        TextField(
                            name = "surname",
                            label = "Nachname $i",
                            hint = "Doe"
                        ),
                    )
                }

            }
        )
    }

    override fun sendApplicationForm(service: AdministrativeService, submittedForm: SubmittedForm) {
        TODO("Not yet implemented")
    }
}