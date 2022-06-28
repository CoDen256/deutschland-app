package de.app.ui.signature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.safe.DataSafeService
import de.app.api.signature.SignatureService
import de.app.core.inSeparateThread
import de.app.data.model.FileHeader
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.components.AccountAwareFragment
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.safe.DataSafePickerFactory
import de.app.ui.util.openDocumentLauncher
import de.app.ui.util.toast
import de.app.ui.util.writeTo
import javax.inject.Inject


@AndroidEntryPoint
class DataSignatureFragment : AccountAwareFragment<FragmentSignatureBinding>() {

    @Inject
    lateinit var service: SignatureService

    @Inject
    lateinit var dataSafePickerFactory: DataSafePickerFactory

    @Inject
    lateinit var dataSafeService: DataSafeService

    private val files: MutableList<FileHeader> = ArrayList()
    private val adapter: OpenableFileViewAdapter =
        OpenableFileViewAdapter({requireActivity()}, files) { removeFile(it) }
    private lateinit var launcher: ActivityResultLauncher<String>

    private val downloadingQueue: MutableList<FileHeader> = ArrayList()
//    private val downloadingQueueLiveData = MutableLiveData<MutableList<FileHeader>>(ArrayList())

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignatureBinding.inflate(inflater, container, false)

    override fun setup() {
        binding.files.adapter = adapter

        val pickFileLauncher = openDocumentLauncher(requireActivity()) {
            addFile(it)
        }
        launcher = registerForActivityResult(CreateDocument("application/pdf")) {
            it ?: return@registerForActivityResult
            inSeparateThread {
                requireActivity().apply {
                    writeTo(downloadingQueue.removeFirst(), it)
                    runOnUiThread {
                        toast("Successfully written ${it.lastPathSegment}")
                    }
                    triggerDownloadNext()
                }
            }
        }

        binding.uploadFileLocal.setOnClickListener {
            pickFileLauncher.launch(arrayOf("application/pdf"))
        }

        binding.uploadFileDataSafe.setOnClickListener {
            dataSafePickerFactory.showPicker(requireActivity(), account) {
                addFile(it)
            }
        }

        binding.submitLocal.setOnClickListener {
            downloadingQueue.clear()
            downloadingQueue.addAll(files)
            triggerDownloadNext()
        }

        binding.submitDataSave.setOnClickListener {
            files.forEach { dataSafeService.upload(service.signFile(it), account.accountId) }
            requireActivity().toast("Successfully uploaded ${files.size} files")
        }
    }

    private fun triggerDownloadNext() {
        if (downloadingQueue.isEmpty()) return
        launcher.launch(service.signFile(downloadingQueue.first()).name)
    }

    private fun removeFile(it: FileHeader) {
        val index = files.indexOf(it)
        if (index != -1) {
            files.removeAt(index)
            adapter.notifyItemRemoved(index)
        }
    }

    private fun addFile(file: FileHeader) {
        files.add(0, file)
        adapter.notifyItemInserted(0)
        binding.files.apply {
            post { smoothScrollToPosition(0) }
        }
    }
}