package de.app.api.account


/**
 * [CitizenServiceAccountRepository] repräsentiert eine API, mit der man sich
 * mit seinem Bürger-ID anmelden kann. Als Ergebnis der
 * Authentifizierung bekommt man ein Secret Token, durch das es möglich wäre
 * zusätzliche Informationen durch ein [CitizenServiceAccount] abzufragen.
 */
interface CitizenServiceAccountRepository {
    /**
     * Authentifiziert ein Bürger-Nutzer und gibt ein [SecretToken] zurück, mit dem man
     * später zusätzliche Informationen abfragen kann.
     *
     * @param accountId das Konto-ID des Bürgers
     */
    fun authenticateCitizen(accountId: String): Result<SecretToken>

    /**
     * Gibt zusätzliche in einem [CitizenServiceAccount] eingekapselte Informationen
     * bezüglich des Bürgers anhand eines Secret-Tokens
     *
     * @param secretToken das Secret Token, das man bei der Authentifizierung bekommt
     */
    fun getCitizenAccount(secretToken: SecretToken): Result<CitizenServiceAccount>
}

/**
 * [CitizenServiceAccountRepository] repräsentiert ein API, mit der man sich
 * mit seinem Bürger-ID anmelden kann. Als Ergebnis der
 * Authentifizierung bekommt man ein Secret Token, durch das es möglich wäre
 * zusätzliche Informationen durch ein [CitizenServiceAccount] abzufragen.
 */
interface CompanyServiceAccountRepository {
    /**
     * Authentifiziert ein Nutzer und gibt ein [SecretToken] zurück, mit dem man
     * später zusätzliche Informationen abfragen kann.
     *
     * @param accountId das Konto-ID des Bürgers
     */
    fun authenticateCompany(accountId: String): Result<SecretToken>

    /**
     * Get company account
     *
     * @param secretToken
     * @return
     */
    fun getCompanyAccount(secretToken: SecretToken): Result<CompanyServiceAccount>
}