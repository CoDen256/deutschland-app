package de.app.api.service.form

import de.app.data.model.FileHeader

sealed interface Field
sealed interface InputField{
    val id: String
    val required: Boolean
}

data class TextInfoField(
    val text: String,
): Field

data class DocumentInfoField(
    val label: String,
    val documents: List<FileHeader>,
): Field

data class ImageField(
    val label: String,
    val imageUri: String,
): Field

data class TextField(
    override val id: String,
    override val required: Boolean,
    val label: String,
    val hint: String?
): Field, InputField

data class BigTextField(
    override val id: String,
    override val required: Boolean,
    val label: String,
    val hint: String?
): Field, InputField

data class EmailField(
    override val id: String,
    override val required: Boolean,
    val label: String,
    val hint: String?
): Field, InputField


data class NumberField(
    override val id: String,
    override val required: Boolean,
    val label: String,
    val hint: String?
): Field, InputField

data class SingleChoiceField(
    override val id: String,
    override val required: Boolean,
    val label: String,
    val hint: String,
    val options: List<String>
): Field, InputField

data class RadioChoiceField(
    override val id: String,
    override val required: Boolean,
    val label: String,
    val options: List<String>
): Field, InputField

data class MultipleChoiceField(
    override val id: String,
    override val required: Boolean,
    val label: String,
    val options: List<String>
): Field, InputField


data class DateField(
    override val id: String,
    override val required: Boolean,
    val hint: String?,
    val label: String,
): Field, InputField

data class AttachmentField(
    override val id: String,
    override val required: Boolean,
    val label: String,
    val mimeType: String,
): Field, InputField