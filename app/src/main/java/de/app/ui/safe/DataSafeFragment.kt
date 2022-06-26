package de.app.ui.safe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.safe.DataSafeService
import de.app.core.runWithInterval
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeBinding
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.util.FilePickerIntent
import de.app.ui.util.launcher
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
        binding.files.adapter = OpenableFileViewAdapter(requireActivity(), files)

        runWithInterval({updateFiles(binding.files, files)})

        val pickFileLauncher = lifecycle.launcher(FilePickerIntent(requireActivity()) {
            addFiles(binding.files, files, listOf(it))
        })

        binding.addFile.setOnClickListener {
            pickFileLauncher.launch("application/pdf")
        }

        return binding.root
    }

    private fun updateFiles(rv: RecyclerView, origin: MutableList<FileHeader>) {
        val newMails = getFiles()
        activity?.runOnUiThread {
            addFiles(rv, origin, newMails)
        }
    }

    private fun addFiles(
        rv: RecyclerView,
        origin: MutableList<FileHeader>,
        newMails: List<FileHeader>
    ) {
        rv.adapter?.apply {
            origin.addAll(0, newMails)
            notifyItemRangeInserted(0, newMails.size)
        }
    }

    private fun getFiles(): MutableList<FileHeader> =
        ArrayList(service.getAllDocumentsForAccountId("user-alpha"))
}


