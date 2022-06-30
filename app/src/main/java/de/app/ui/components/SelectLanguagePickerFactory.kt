package de.app.ui.components

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.app.R
import de.app.data.model.Language
import de.app.databinding.ActivityMainSelectLanguageBinding
import de.app.databinding.ActivityMainSelectLanguageItemBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectLanguagePickerFactory @Inject constructor() {

    private val langs by lazy {
        listOf(
            Language("uk-UA", R.string.language_uk, R.drawable.ukraine),
            Language("de-DE", R.string.language_de, R.drawable.germany),
            Language("en-EN", R.string.language_en, R.drawable.english),
        )
    }

    fun show(activity: Activity, onSuccess: (Language) -> Unit) {
        val binding = ActivityMainSelectLanguageBinding.inflate(activity.layoutInflater)
        MaterialAlertDialogBuilder(activity)
            .setView(binding.root)
            .setTitle(activity.getString(R.string.select_language_dialog))
            .setNegativeButton(activity.getString(R.string.cancel_dialog_button), null)
            .create()
            .apply { inflate(activity, binding, onSuccess) }
            .show()
    }

    private fun AlertDialog.inflate(
        context: Context,
        binding: ActivityMainSelectLanguageBinding,
        onSuccess: (Language) -> Unit,
    ) {
        binding.root.adapter = ListViewAdapter(
            { inflater, parent -> ActivityMainSelectLanguageItemBinding.inflate(inflater, parent, false) },
            langs,) { item, itemBinding ->
            itemBinding.name.text = context.getString(item.name)
            itemBinding.icon.setImageDrawable(getDrawable(context, item.icon))
            itemBinding.root.setOnClickListener {
                onSuccess(item)
                dismiss()
            }
        }
    }
}