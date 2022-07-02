package de.app.config

import com.mapbox.mapboxsdk.geometry.LatLng
import de.app.api.account.CitizenServiceAccount
import de.app.api.account.CompanyServiceAccount
import de.app.api.account.SecretToken
import de.app.api.account.ServiceAccount
import de.app.api.applications.Application
import de.app.api.applications.ApplicationStatus
import de.app.api.appointment.Appointment
import de.app.api.emergency.EmergencySeverity
import de.app.api.emergency.Emergency
import de.app.api.geo.GeoCategory
import de.app.api.geo.GeoSet
import de.app.api.mail.MailMessageHeader
import de.app.api.service.AdministrativeService
import de.app.api.service.ServiceType
import de.app.api.service.form.*
import de.app.data.model.Address
import de.app.data.model.FileHeader
import org.fluttercode.datafactory.impl.DataFactory
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt


class DataGenerator {
    companion object {
        val df = DataFactory().apply { randomize(System.currentTimeMillis().toInt()) }

        val cities = listOf("Merseburg", "Berlin", "Leipzig", "Halle", "Magdeburg", "Frankfurt")
        val postalCodes = listOf("06217", "04103", "10115", "06108", "39104", "60306")

        val endpoints = listOf(
            "https://auslaender-behoerde.de/api/antrag",
            "https://auslaender-behoerde.de/api/verlaengerung",
            "https://baum-faellen.de/api/",
            "https://ehe.de/api/",
            "https://ehe.de/api/",
        )

        val names = listOf(
            "Aufenthaltstitel für Flüchtlinge beantragen",
            "Verlängerung der Aufenthaltserlaubnis",
            "Genehmigung zum Baumfällen",
            "Eheschließung anmelden",
            "Eheurkunde beantragen",
        )

        val descriptions = listOf(
            "Erteilung einer Aufenthaltserlaubnis bei anerkanntem Flüchtlingsschutz.",
            "Sie können Ihre Aufenthaltserlaubnis zur Fortsetzung eines im EU-Ausland begonnenen Studiums vor Ablauf ihrer Geltungsdauer verlängern lassen",
            "Für das Fällen von Bäumen kann aus unterschiedlichen Gründen eine Genehmigung erforderlich sein.",
            "Eine beabsichtigte Eheschließung muss beim Standesamt angemeldet werden",
            "Sie benötigen eine Eheurkunde? Ihre Eheurkunde erhalten Sie beim Standesamt, in dessen Bereich die Ehe geschlossen wurde. Das Standesamt stellt sie aus dem Eheregister aus.",
        )

        val documents = listOf(
            "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
            "https://www.orimi.com/pdf-test.pdf",
            "https://www.clickdimensions.com/links/TestPDFfile.pdf"
        )

        val mimeTypes = listOf(
            "application/pdf",
            "text/plain",
            "text/html"
        )


        fun generateImageUri(): String {
            return "https://source.unsplash.com/random/1024x1024?sig=${nextInt()}"
        }

        fun generateText(from: Int, until: Int): String {
            return generateSequence { df.getRandomWord(5, 10) }
                .take(nextInt(from, until))
                .joinToString(separator = " ") { it }
                .replaceFirstChar { it.uppercase() }
        }

        private fun rnd() = generateText(1, 2)

        fun generateEndpoint(): String {
            return "https://${rnd()}.${rnd()}.de/api"
        }

        fun generateAddresses(num: Int): List<Address> {
            return (0..num).map {
                generateAddress()
            }
        }

        fun generateAddress(): Address {
            val city = nextInt(cities.size)
            return Address(
                city = cities[city], postalCode = postalCodes[city],
                address = df.streetName + " Str. " + generateStreetNumber(30), country = "Germany",
            )
        }

        private fun generateStreetNumber(num: Int) =
            "${nextInt(num)}${(('a'..'Z') + 0.toString()).random()}"


        fun generateDocuments(num: Int): List<FileHeader> {
            return (0..num).map {
                FileHeader(rnd() + ".pdf", documents.random(), "application/pdf")
            }
        }

        fun generateOptions(num: Int): List<String> {
            return (0..num).map {
                if (nextInt(10) <= 8) rnd() else df.getNumberText(4)
            }
        }

        fun generateFields(num: Int): List<Field> {
            return (0..num).map {
                fieldGenerator.random()(it)
            }
        }

        fun generateApplications(
            num: Int,
            services: List<AdministrativeService>
        ): List<Application> {
            return (0..num).map {
                val service = AdministrativeService(
                    "","","","", Address("","","",""),
                    ServiceType.COMPANY
                )
                Application(
                    "",
                    "mapOf<SecretToken, ServiceAccount>().values.random().accountId",
                    service.name,
                    service.description,
                    ApplicationStatus.values().random(),
                    generateLocalDateTime()
                )
            }
        }

        fun generateAppointments(
            num: Int,
            services: List<AdministrativeService>
        ): List<Appointment> {
            return (0..num).map {
                Appointment(
                    generateText(5, 8),
                    generateText(10, 25),
                    serviceId = "",
                    accountId = "mapOf<SecretToken, ServiceAccount>().values.random().accountId",
                    additionalInfo = generateText(5, 15),
                    address = generateAddress(),
                    appointment = generateLocalDateTime()
                )
            }
        }


        fun generateLocalDate(): LocalDate = LocalDate.of(
            nextInt(2022, 2024),
            nextInt(1, 13),
            nextInt(1, 28)
        )

        fun generateLocalDateTime(): LocalDateTime = LocalDateTime.of(
            nextInt(2022, 2024),
            nextInt(1, 13),
            nextInt(1, 28),
            nextInt(1, 24),
            nextInt(0, 12) * 5,
            0
        )


        val sets = listOf(
            "Energie", "Schutzgebiete", "Wasser", "Boden",
            "Acker und Wald-Boden", "Landwirtschaft", "Forstwirtschaft",
            "Strasse", "Schiene", "Flug", "Schiff"
        )
        val categories = listOf("Umwelt und Energie", "Land und Forstwirtschaft", "Verkehr und Technologie")

        fun generateCategories(num: Int, setsNum: Int, sets: List<GeoSet>): List<GeoCategory> {
            return (0..num).map {
                GeoCategory(
                    UUID.randomUUID().toString(),
                    categories.random(),
                    (0..setsNum).map { sets.random() }
                )
            }
        }

        fun generateSets(num: Int): List<GeoSet>{
            return (0..num).map {
                GeoSet(
                    UUID.randomUUID().toString(),
                    sets.random(),
                    generatePositions(nextInt(20, 40))
                )
            }
        }

        fun generatePositions(num: Int): List<LatLng> {
            return (0..num).map {
                LatLng(
                    nextDouble(48.0, 54.0),
                    nextDouble(6.5, 14.5)
                )
            }
        }


        fun generateMails(num: Int): List<MailMessageHeader> {
            return (0..num).map {
                MailMessageHeader(
                    generateText(5, 9),
                    Instant.now(),
                    UUID.randomUUID().toString(),
                    preview = generateText(9, 25),
                )
            }
        }


        val fieldGenerator = listOf<(Int) -> Field>(
            {
                MultipleChoiceField(
                    id = "multiple-$it",
                    required = nextBoolean(),
                    label = rnd(),
                    options = generateOptions(nextInt(2, 5))
                )
            },
            {
                SingleChoiceField(
                    id = "single-$it",
                    required = nextBoolean(),
                    label = rnd(),
                    options = generateOptions(nextInt(2, 5))
                )
            },
            {
                RadioChoiceField(
                    id = "radio-$it",
                    required = nextBoolean(),
                    label = rnd(),
                    options = generateOptions(nextInt(2, 5))
                )
            },
            {
                AttachmentField(
                    id = "attachment-$it",
                    required = nextBoolean(),
                    label = rnd(),
                    mimeType = mimeTypes.random()
                )
            },

            { BigTextField(id = "big-$it", required = nextBoolean(), label = rnd(), hint = rnd()) },
            {
                EmailField(
                    id = "email-$it",
                    required = nextBoolean(),
                    label = "Email of ${rnd()}",
                    hint = "Email of ${rnd()}"
                )
            },
            { TextField(id = "text-$it", required = nextBoolean(), label = rnd(), hint = rnd()) },
            {
                NumberField(
                    id = "number-$it",
                    required = nextBoolean(),
                    label = "Number of ${rnd()}",
                    hint = "Number of ${rnd()}"
                )
            },
            {
                DateField(
                    id = "date-$it",
                    required = nextBoolean(),
                    label = "Date of ${rnd()}",
                    hint = "Date of ${rnd()}"
                )
            },

            { TextInfoField(text = generateText(10, 100)) },
            {
                DocumentInfoField(
                    label = "Document of ${rnd()}",
                    documents = generateDocuments(nextInt(4))
                )
            },
            { ImageField(label = "Image of ${rnd()}", imageUri = generateImageUri()) },
        )

    }

}