package de.app.ui.safe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.account.AccountInfo
import de.app.api.safe.DataSafeService
import de.app.core.config.BaseDataSafeService
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.core.runWithInterval
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeBinding
import de.app.databinding.FragmentDataSafePickerDialogBinding
import de.app.ui.components.FileViewAdapter
import de.app.ui.components.OpenableFileViewAdapter
import javax.inject.Inject

@AndroidEntryPoint
class DataSafeFragment : Fragment() {

    @Inject
    lateinit var service: DataSafeService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDataSafeBinding.inflate(inflater, container, false)

        val files = getFiles()
        binding.files.adapter = OpenableFileViewAdapter(requireContext(), files)

        runWithInterval({updateFiles(binding.files, files)})

        return binding.root
    }

    private fun updateFiles(rv: RecyclerView, origin: MutableList<FileHeader>) {
        val newMails = getFiles()
        activity?.runOnUiThread {
            rv.adapter?.apply {
                val curSize = itemCount
                origin.addAll(newMails)
                notifyItemRangeChanged(curSize, newMails.size)
            }
        }
    }

    private fun getFiles(): MutableList<FileHeader> =
        ArrayList(service.getAllDocumentsForAccountId("user-alpha"))
}


