package de.app.api.account

import de.app.data.model.Address
import java.time.LocalDate

/**
 * Das [SecretToken] wird erhalten, wenn der Nutzer
 * sich bei der Servicekonto-API authentifiziert. Dieses Token
 * wird in den nachfolgenden Abläufen verwendet, um
 * zusätzliche Informationen für den jeweiligen Nutzer durch
 * ein [ServiceAccount] abzufragen
 *
 * @property token der eindeutige Token-Wert
 */
data class SecretToken(
    val token: String
)

/**
 * Repräsentiert ein generisches Servicekonto. Es gibt nur
 * 2 mögliche Typen des Servicekontos:
 * 1) Servicekonto für Bürger [CitizenServiceAccount]
 * 2) Servicekonto für Unternehmen [CompanyServiceAccount]
 *  Enthält alle notwendige Informationen bezüglich eines Nutzers
 *  bzw. Unternehmens
 *
 * @property accountId ID des Kontos
 * @property displayName Anzeigename des Kontos
 * @property address aktuelle Wohnadresse oder Adresse des Bürgers bzw. des Unternehmens
 */
sealed interface ServiceAccount{
    val accountId: String
    val displayName: String
    val address: Address
}

/**
 * [CitizenServiceAccount] repräsentiert ein Servicekonto für Bürger
 *
 * @property accountId Konto ID (wird verwendet, um sich bei Servicekonto-API zu authentifizieren)
 * @property displayName Anzeigename des Bürgers
 * @property address Wohnadresse des Bürgers
 * @property firstName Vorname des Bürgers
 * @property surname  Nachname
 * @property salutation Anrede
 * @property dateOfBirth Geburtsdatum
 * @property nationality Staatsbürgerschaft
 * @property placeOfBirth Geburtsort
 */
data class CitizenServiceAccount(
    override val accountId: String,
    override val displayName: String,
    override val address: Address,
    val firstName: String,
    val surname: String,
    val salutation: String,
    val dateOfBirth: LocalDate = LocalDate.now(),
    val nationality: String = "Ukraine",
    val placeOfBirth: Address = address,
    ): ServiceAccount

/**
 * [CompanyServiceAccount] repräsentiert ein Servicekonto für Unternehmen
 *
 * @property accountId - Konto ID des Unternehmen (wird bei der Authentifizierung verwendet)
 * @property displayName - Anzeigename des Unternehmens (ähnlich wie bei [fullName])
 * @property address - Adresse des Unternehmens
 * @property fullName - voller Name des Unternehmens
 * @property foundedDate - Gründungsdatum des Unternehmens
 */
data class CompanyServiceAccount(
    override val accountId: String,
    override val displayName: String,
    override val address: Address,
    val fullName: String,
    val foundedDate: LocalDate = LocalDate.now()
): ServiceAccount