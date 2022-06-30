package de.app.ui.signature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.safe.DataSafeService
import de.app.api.signature.SignatureService
import de.app.data.model.FileHeader
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.components.AccountAwareFragment
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.safe.DataSafePickerFactory
import de.app.ui.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DataSignatureFragment : AccountAwareFragment<FragmentSignatureBinding>() {

    @Inject
    lateinit var service: SignatureService

    @Inject
    lateinit var dataSafePickerFactory: DataSafePickerFactory

    @Inject
    lateinit var dataSafeService: DataSafeService

    private lateinit var createFileLauncher: ActivityResultLauncher<String>
    private val files: MutableList<FileHeader> = ArrayList()
    private val adapter: OpenableFileViewAdapter =
        OpenableFileViewAdapter({requireActivity()}, files,
            onRemoved = {removeFile(it)},
        )


    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignatureBinding.inflate(inflater, container, false)

    override fun setup() {
        binding.files.adapter = adapter

        val writer = IterativeFileWriter(requireActivity()) { createFileLauncher.launch(it.name) }

        val pickFileLauncher = openDocumentLauncher {
            addFile(it)
        }
        createFileLauncher = createDocumentLauncher("application/pdf") {
            writer.saveNextTo(it)
        }

        binding.uploadFrom.uploadFileLocal.setOnClickListener {
            pickFileLauncher.launch(arrayOf("application/pdf"))
        }

        binding.uploadFrom.uploadFileDataSafe.setOnClickListener {
            dataSafePickerFactory.showPicker(requireActivity(), account) {
                addFile(it)
            }
        }

        binding.submitLocal.setOnClickListener {
            writer.push(files)
        }

        binding.submitDataSave.setOnClickListener {
            files.forEach { dataSafeService.upload(service.signFile(it), account.accountId) }
            requireActivity().toast("Successfully uploaded ${files.size} files")
        }
    }


    private fun removeFile(it: FileHeader) {
        val index = files.indexOf(it)
        if (index == -1) return
        files.removeAt(index)
        adapter.notifyItemRemoved(index)
    }

    private fun addFile(file: FileHeader) {
        files.add(0, file)
        adapter.notifyItemInserted(0)
        binding.files.apply {
            post { smoothScrollToPosition(0) }
        }
    }
}