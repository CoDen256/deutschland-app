package de.app.ui.safe

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.app.R
import de.app.api.account.ServiceAccount
import de.app.api.safe.DataSafeService
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafePickerDialogBinding
import de.app.ui.components.FileViewAdapter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSafePickerFactory @Inject constructor(private val service: DataSafeService) {
    fun showPicker(activity: Activity, info: ServiceAccount, onSuccess: (FileHeader) -> Unit) {
        createShower(activity,info).show(onSuccess)
    }

    fun createShower(activity: Activity, info: ServiceAccount): DataSafePickerShower{
        val binding = FragmentDataSafePickerDialogBinding.inflate(activity.layoutInflater)
        val dialog = MaterialAlertDialogBuilder(activity)
            .setView(binding.root)
            .setTitle(activity.getString(R.string.select_data_safe_dialog))
            .setNegativeButton(activity.getString(R.string.cancel_dialog_button), null)
            .create()
        return object : DataSafePickerShower {
            override fun show(onSuccess: (FileHeader) -> Unit) {
                dialog.apply { inflate(binding, info, onSuccess, activity) }.show()
            }
        }
    }
    private fun AlertDialog.inflate(
        binding: FragmentDataSafePickerDialogBinding,
        info: ServiceAccount,
        onSuccess: (FileHeader) -> Unit,
        activity: Activity
    ) {
        val fileHeaders = service.getAllDocumentsForAccountId(info.accountId)
        binding.files.adapter = FileViewAdapter(fileHeaders, {activity}) {
            onSuccess(it)
            dismiss()
        }
    }
}

@FunctionalInterface
interface DataSafePickerShower {
    fun show(onSuccess: (FileHeader) -> Unit)
}