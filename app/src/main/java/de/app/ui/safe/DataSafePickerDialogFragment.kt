package de.app.ui.safe

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import de.app.core.config.BaseDataSafeService
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafePickerDialogBinding
import de.app.ui.components.FileViewAdapter

class DataSafePickerDialogFragment (
    private val onPositiveListener: (FileHeader) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            val binding = FragmentDataSafePickerDialogBinding.inflate(requireActivity().layoutInflater)

            val builder = AlertDialog.Builder(requireContext()).apply {

            setView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val files = BaseDataSafeService().getAllDocumentsForAccountId("user-alpha")
        binding.files.adapter = FileViewAdapter(files) {
            onPositiveListener(it)
            this.dialog?.let {
                it.
            }
        }

        return binding.root
    }

}