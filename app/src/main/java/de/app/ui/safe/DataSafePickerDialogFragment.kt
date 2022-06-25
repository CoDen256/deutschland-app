package de.app.ui.safe

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import de.app.api.account.AccountInfo
import de.app.api.safe.DataSafeService
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafePickerDialogBinding
import de.app.ui.components.FileViewAdapter

class DataSafePickerDialogFragment (
    private val service: DataSafeService,
    private val accountInfo: AccountInfo,
    private val onPositiveListener: (FileHeader) -> Unit
) : DialogFragment() {

    private lateinit var binding: FragmentDataSafePickerDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentDataSafePickerDialogBinding.inflate(requireActivity().layoutInflater)
        binding.files.adapter =
            FileViewAdapter(service.getAllDocumentsForAccountId(accountInfo.accountId)) {
                onPositiveListener(it)
                dialog?.dismiss()
            }
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Choose a file from Data Safe")
            .setNegativeButton("Cancel", null)
            .create().apply {
                setCancelable(true)
            }

    }

}