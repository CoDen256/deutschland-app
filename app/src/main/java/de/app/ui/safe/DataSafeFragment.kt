package de.app.ui.safe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.safe.DataSafeService
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeBinding
import de.app.ui.components.AccountAwareFragment
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.util.FilePickerIntent
import de.app.ui.util.FileSaverIntent
import de.app.ui.util.launcher
import javax.inject.Inject

@AndroidEntryPoint
class DataSafeFragment : AccountAwareFragment<FragmentDataSafeBinding>() {

    @Inject
    lateinit var service: DataSafeService

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDataSafeBinding.inflate(inflater, container, false)

    override fun setup() {
        val files: MutableList<FileHeader> =
            ArrayList(service.getAllDocumentsForAccountId(account.accountId))

        val saveFileLauncher = lifecycle.launcher(FileSaverIntent(requireActivity()))

        val adapter = OpenableFileViewAdapter({ requireActivity() }, files) {
            saveFileLauncher.launch(it)
        }
        binding.files.adapter = adapter

        val pickFileLauncher = lifecycle.launcher(FilePickerIntent(requireActivity()) {
            files.add(0, it)
            adapter.notifyItemInserted(0)
            service.upload(it, account.accountId)
        })

        binding.addFile.setOnClickListener {
            pickFileLauncher.launch("application/pdf")
        }
    }
}


