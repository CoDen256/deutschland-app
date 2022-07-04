package de.app.ui.service.validator

import de.app.ui.service.data.state.FieldState
import de.app.ui.service.data.value.FieldValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class BirthdayDateFieldValidator(override val required: Boolean) : FieldValidator() {

    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun validate(value: FieldValue): FieldState {
        val string = value.value as? String
        string?.let{
            validate(it)?.let {
                if (it.isAfter(LocalDate.of(2000, 1, 1))){
                    return FieldState(value.id, error = "Invalid Date")
                }
                return FieldState(value.id, error = null)
            } ?: return FieldState(value.id, error="Invalid Date format")
        } ?: return FieldState(value.id, error=validateRequired(string != null && string.isNotBlank()))
    }

    private fun validate(string: String): LocalDate? {
        return try {
            LocalDate.parse(string, dateTimeFormatter)
        } catch (ex: DateTimeParseException) {
            null
        }
    }

}