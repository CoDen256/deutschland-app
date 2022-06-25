package de.app.ui.signature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.signature.SignatureService
import de.app.core.config.BaseSignatureService
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.data.model.FileHeader
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.components.FileViewAdapter
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.safe.DataSafePickerDialogFragment
import de.app.ui.util.FilePickerIntentLauncher
import javax.inject.Inject

@AndroidEntryPoint
class DataSignatureFragment : Fragment(){

    @Inject
    lateinit var service: SignatureService
    lateinit var binding: FragmentSignatureBinding

    private val files: MutableList<FileHeader> = ArrayList(generateDocuments(5))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignatureBinding.inflate(inflater, container, false)

        binding.files.adapter = OpenableFileViewAdapter(requireContext(), files)

        val launcher = FilePickerIntentLauncher(requireActivity()) { addFile(it) }
        lifecycle.addObserver(launcher.getObserver())
        binding.uploadFileLocal.setOnClickListener {
            launcher.launch("application/pdf")
        }

        binding.uploadFileDataSafe.setOnClickListener {
            DataSafePickerDialogFragment { addFile(it) }
                .show(childFragmentManager, "data-safe-picker")
        }

        return binding.root
    }

    private fun addFile(file: FileHeader) {
        binding.files.adapter?.apply {
            files.add(0, file)
            notifyItemInserted(0)
        }
        binding.files.post { binding.files.smoothScrollToPosition(0) }
    }

}