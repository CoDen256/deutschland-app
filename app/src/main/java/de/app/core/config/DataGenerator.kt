package de.app.core.config

import de.app.api.account.CitizenAccountInfo
import de.app.api.account.CompanyAccountInfo
import de.app.api.account.SecretToken
import de.app.api.service.AdministrativeService
import de.app.api.service.form.*
import de.app.data.model.Address
import de.app.data.model.FileHeader
import org.fluttercode.datafactory.impl.DataFactory
import java.util.*
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt

val df = DataFactory()
private fun rnd() = df.randomWord

val citizens = mapOf(
    SecretToken("ua01") to CitizenAccountInfo(
        "user-alpha", df.name,
        Address("Merseburg","Germany","06217",
            "Eberhard-Leibnitz-Strasse", "1"),
        "Alpha", "Beta", "Frau"
    ),
    SecretToken("ub02") to CitizenAccountInfo(
        "user-bob", "Uncle Bob",
        Address("Halle", "Germany","06108",
            "Gottschedstraße", "10a"),
        "Uncle", "Bob", "Herr"
    ),
    SecretToken("ud03") to CitizenAccountInfo(
        "user-delta", "Delta Zeta",
        Address("Leipzig", "Germany","04103",
            "Eberhard-Leibnitz-Strasse", "4"),
        "Delta", "Zeta", ""
    )
)

val companies = mapOf(
    SecretToken("cy04") to CompanyAccountInfo(
        "comp-yota", "Yota Gmbh",
        Address("Leipzig", "Germany","04103",
            "Käthe-Kollwitz-Straße", "1"),
        "Yota Gmbh Inc."),
    SecretToken("ck05") to CompanyAccountInfo(
        "comp-kappa", "Kappa Gmbh",
        Address("Bakhmut", "Ukraine","84500",
            "Ul. Gagarina", "1"),
        "Kappa Gmbh Inc."
    )
)

val cities = listOf("Merseburg", "Berlin", "Leipzig", "Halle", "Magdeburg", "Frankfurt")
val postalCodes = listOf("06217", "04103", "10115", "06108", "39104", "60306")

val endpoints = listOf(
    "https://auslaender-behoerde.de/api/antrag",
    "https://auslaender-behoerde.de/api/verlaengerung",
    "https://baum-faellen.de/api/",
    "https://ehe.de/api/",
    "https://ehe.de/api/",
    generateEndpoint(),
    generateEndpoint(),
    generateEndpoint(),
)

val names = listOf(
    "Aufenthaltstitel für Flüchtlinge beantragen",
    "Verlängerung der Aufenthaltserlaubnis",
    "Genehmigung zum Baumfällen",
    "Eheschließung anmelden",
    "Eheurkunde beantragen",
    df.getRandomText(5, 50),
    df.getRandomText(5, 50),
    df.getRandomText(5, 50),
)

val descriptions = listOf(
    "Erteilung einer Aufenthaltserlaubnis bei anerkanntem Flüchtlingsschutz.",
    "Sie können Ihre Aufenthaltserlaubnis zur Fortsetzung eines im EU-Ausland begonnenen Studiums vor Ablauf ihrer Geltungsdauer verlängern lassen",
    "Für das Fällen von Bäumen kann aus unterschiedlichen Gründen eine Genehmigung erforderlich sein.",
    "Eine beabsichtigte Eheschließung muss beim Standesamt angemeldet werden",
    "Sie benötigen eine Eheurkunde? Ihre Eheurkunde erhalten Sie beim Standesamt, in dessen Bereich die Ehe geschlossen wurde. Das Standesamt stellt sie aus dem Eheregister aus.",
    df.getRandomText(15, 250),
    df.getRandomText(15, 250),
    df.getRandomText(15, 250),
)

val documents = listOf(
    "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
    "http://www.africau.edu/images/default/sample.pdf",
    "https://www.orimi.com/pdf-test.pdf",
    "http://www.pdf995.com/samples/pdf.pdf",
    "https://www.clickdimensions.com/links/TestPDFfile.pdf"
)

val images = listOf(
    "https://i.guim.co.uk/img/media/26392d05302e02f7bf4eb143bb84c8097d09144b/446_167_3683_2210/master/3683.jpg?width=1200&height=1200&quality=85&auto=format&fit=crop&s=49ed3252c0b2ffb49cf8b508892e452d",
    "https://picsum.photos/536/354",
    "https://source.unsplash.com/random/200x200?sig=1"
)

val mimeTypes = listOf(
    "application/pdf",
    "text/plain",
    "text/html"
)

fun generateEndpoint(): String {
    return "https://${rnd()}.${rnd()}.de/api"
}

fun generateAddresses(num: Int): List<Address> {
    return (0..num).map {
        val city = nextInt(cities.size)
        Address(
            city = cities[city], postalCode = postalCodes[city],
            street = df.streetName, country = "Germany",
            streetNumber = generateStreetNumber(30)
        )
    }
}

private fun generateStreetNumber(num: Int) = "${nextInt(num)}${(('a'..'Z') + 0.toString()).random()}"

fun generateServices(num: Int): List<AdministrativeService> {
    val addresses = generateAddresses(num)
    return (0..num).map {
        val rndInt = nextInt(names.size)
        AdministrativeService(
            UUID.randomUUID().toString(),
            names[rndInt],
            descriptions[rndInt],
            endpoints[rndInt],
            addresses[it]
        )
    }
}

fun generateDocuments(num: Int): List<FileHeader>{
    return (0..num).map{
        FileHeader(rnd(), documents.random(), rnd())
    }
}

fun generateOptions(num: Int): List<String>{
    return  (0..num).map{
         if (nextBoolean()) df.randomWord else df.randomChar + df.number.toString()
    }
}

fun generateFields(num: Int):List<Field>{
    return (0..num).map{
        fieldGenerator.random()(it)
    }
}

val fieldGenerator = listOf<(Int) -> Field>(
    { MultipleChoiceField(id="multiple-$it", required= nextBoolean(), label=rnd(), options = generateOptions(nextInt(5))) },
    { SingleChoiceField(id="single-$it", required= nextBoolean(), label=rnd(), options = generateOptions(nextInt(5)) ) },
    { RadioChoiceField(id="radio-$it", required= nextBoolean(), label=rnd(), options = generateOptions(nextInt(5)) ) },
    { AttachmentField(id="attachment-$it", required= nextBoolean(), label=rnd(), mimeType =  mimeTypes.random()) },

    { BigTextField(id="big-$it", required= nextBoolean(), label= rnd(), hint= rnd()) },
    { EmailField(id="email-$it", required= nextBoolean(), label= rnd(), hint= rnd()) },
    { TextField(id="text-$it", required= nextBoolean(), label= rnd(), hint= rnd()) },
    { NumberField(id="number-$it", required= nextBoolean(), label= rnd(), hint= rnd()) },
    { DateField(id="date-$it", required= nextBoolean(), label= rnd(), hint= rnd()) },

    { TextInfoField(text=df.getRandomText(10, 200)) },
    { DocumentInfoField(label=rnd(), documents = generateDocuments(nextInt(4)) ) },
    { ImageField(label= rnd(), imageUri = images.random() ) },
)