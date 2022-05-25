package de.app.api.dummy

import de.app.api.AdministrativeServiceRegistry
import de.app.data.Result
import de.app.data.model.service.*
import de.app.data.model.service.form.*
import de.app.data.model.service.submit.SubmittedForm
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
            ArrayList<FormField>().apply {
                for (i in 0..0){
                    addAll(listOf(
                        InfoField(
                            text="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                        ),
                        TextField(
                            name = "name",
                            label = "Name $i",
                            hint = "John",
                            required = true
                        ),
                        BigTextField(
                            name = "surname",
                            label = "Nachname $i",
                            hint = "Doe",
                            required = true
                        ),
                        DateField(
                            name="date",
                            label="Birthday $i",
                            required = false,
                            hint = "19.10.2000"
                        )
                    ))
                }

            }
        )
    }

    override fun sendApplicationForm(service: AdministrativeService, submittedForm: SubmittedForm): Result<Any> {
        return Result.Success("")
    }
}