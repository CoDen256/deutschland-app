package de.app.ui.safe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.safe.DataSafeService
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeBinding
import de.app.ui.components.AccountAwareFragment
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.util.IterativeFileWriter
import de.app.ui.util.createDocumentLauncher
import de.app.ui.util.openDocumentLauncher
import javax.inject.Inject

@AndroidEntryPoint
class DataSafeFragment : AccountAwareFragment<FragmentDataSafeBinding>() {

    @Inject
    lateinit var service: DataSafeService

    private lateinit var createFileLauncher: ActivityResultLauncher<String>
    private lateinit var adapter: OpenableFileViewAdapter

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDataSafeBinding.inflate(inflater, container, false)

    override fun setup() {
        val files: MutableList<FileHeader> =
            ArrayList(service.getAllDocumentsForAccountId(account.accountId))

        val writer = IterativeFileWriter(requireActivity()) { createFileLauncher.launch(it.name) }

        createFileLauncher = createDocumentLauncher( "application/pdf") {
            writer.saveNextTo(it)
        }
        adapter = OpenableFileViewAdapter({ requireActivity() }, files,
            onRemoved = {
                val index = files.indexOf(it)
                if (index != -1){
                    files.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    service.remove(it, account.accountId)
                }
            },
            onDownloaded = {writer.push(listOf(it))}
            )
        binding.files.adapter = adapter

        val pickFileLauncher = openDocumentLauncher {
            files.add(0, it)
            adapter.notifyItemInserted(0)
            service.upload(it, account.accountId)
        }

        binding.addFile.setOnClickListener {
            pickFileLauncher.launch(arrayOf("application/pdf"))
        }
    }
}


