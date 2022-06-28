package de.app.ui.signature

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.safe.DataSafeService
import de.app.api.signature.SignatureService
import de.app.data.model.FileHeader
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.components.AccountAwareFragment
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.safe.DataSafePickerFactory
import de.app.ui.util.FilePickerIntent
import de.app.ui.util.FileSaverIntent
import de.app.ui.util.launcher
import de.app.ui.util.toast
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

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignatureBinding.inflate(inflater, container, false)

    override fun setup() {
        binding.files.adapter = adapter

        val pickFileLauncher =
            lifecycle.launcher(FilePickerIntent(requireActivity()) { addFile(it) })
        val saveFileLauncher = lifecycle.launcher(FileSaverIntent(requireActivity()))

        binding.uploadFileLocal.setOnClickListener {
            pickFileLauncher.launch("application/pdf")
        }

        binding.uploadFileDataSafe.setOnClickListener {
            dataSafePickerFactory.showPicker(requireActivity(), account) {
                addFile(it)
            }
        }

        binding.submitLocal.setOnClickListener {
            files.forEach { saveFileLauncher.launch(service.signFile(it)) }
        }

        binding.submitDataSave.setOnClickListener {
            files.forEach {
                dataSafeService.upload(service.signFile(it), account.accountId)
            }
            requireActivity().toast("Successfully uploaded ${files.size} files")
        }

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