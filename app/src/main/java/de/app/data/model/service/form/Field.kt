package de.app.data.model.service.form

sealed interface Field

data class InfoField(
    val text: String,
): Field

data class DocumentField(
    val label: String,
    val document: String,
): Field

data class ImageField(
    val label: String,
    val image: String,
): Field

data class TextField(
    val name: String,
    val required: Boolean,
    val label: String,
    val hint: String?
): Field

data class BigTextField(
    val name: String,
    val required: Boolean,
    val label: String,
    val hint: String?
): Field

data class EmailField(
    val name: String,
    val required: Boolean,
    val label: String,
    val hint: String?
): Field


data class NumberField(
    val name: String,
    val required: Boolean,
    val label: String,
    val hint: String?
): Field

data class SingleChoiceField(
    val name: String,
    val required: Boolean,
    val label: String,
    val options: List<String>
): Field

data class MultipleChoiceField(
    val name: String,
    val required: Boolean,
    val label: String,
    val options: List<String>
): Field


data class DateField(
    val name: String,
    val required: Boolean,
    val hint: String?,
    val label: String,
): Field

data class AttachmentField(
    val name: String,
    val required: Boolean,
    val label: String,
): Field