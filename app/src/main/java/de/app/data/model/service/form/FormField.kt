package de.app.data.model.service.form

sealed interface FormField

data class InfoField(
    val text: String,
): FormField

data class DocumentField(
    val label: String,
    val document: String,
): FormField

data class ImageField(
    val label: String,
    val image: String,
): FormField

data class TextField(
    val name: String,
    val required: Boolean,
    val label: String,
    val hint: String?
): FormField

data class BigTextField(
    val name: String,
    val required: Boolean,
    val label: String,
    val hint: String?
): FormField

data class EmailField(
    val name: String,
    val required: Boolean,
    val label: String,
    val hint: String?
): FormField


data class NumberField(
    val name: String,
    val required: Boolean,
    val label: String,
    val hint: String?
): FormField

data class SingleChoiceField(
    val name: String,
    val required: Boolean,
    val label: String,
    val options: List<String>
): FormField

data class MultipleChoiceField(
    val name: String,
    val required: Boolean,
    val label: String,
    val options: List<String>
): FormField


data class DateField(
    val name: String,
    val required: Boolean,
    val label: String,
): FormField

data class AttachmentField(
    val name: String,
    val required: Boolean,
    val label: String,
): FormField