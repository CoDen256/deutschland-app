package de.app.api.dummy

import de.app.api.AdministrativeServiceRegistry
import de.app.data.Result
import de.app.data.model.FileHeader
import de.app.data.model.service.*
import de.app.data.model.service.form.*
import de.app.data.model.service.submit.SubmittedForm
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

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

    override fun getApplicationForm(service: AdministrativeService): Form {
        return Form(
            "Sell a dog",
            "This administrative service allows you to send an application to sell a dog",
            service,
            ArrayList<Field>().apply {
                for (i in 0..10){
                    addAll(listOf(

                        AttachmentField(
                          id ="attachment$i",
                          required = true,
                          label = "Attachment",
                          mimeType = "application/pdf"
                        ),
                        DocumentInfoField(
                            label = "Documents",
                            documents = listOf(
                                FileHeader("AlphaDoc", "http://www.africau.edu/images/default/sample.pdf", "application/pdf"),
                                FileHeader("BetaDoc", "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf", "application/pdf"),
                            )
                        ),

                        ImageField(
                            label = "Image",
                            imageUri="https://i.guim.co.uk/img/media/26392d05302e02f7bf4eb143bb84c8097d09144b/446_167_3683_2210/master/3683.jpg?width=1200&height=1200&quality=85&auto=format&fit=crop&s=49ed3252c0b2ffb49cf8b508892e452d"
                        ),
                        TextInfoField(
                            text="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                        ),
                        TextField(
                            id =  "name$i",
                            label = "Name $i",
                            hint = "John",
                            required = true
                        ),
                        EmailField(
                            id = "email$i",
                            label ="Email",
                            hint="Email",
                            required = false
                        ),
                        BigTextField(
                            id =  "surname$i",
                            label = "Nachname $i",
                            hint = "Doe",
                            required = true
                        ),
                        DateField(
                            id="date$i",
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