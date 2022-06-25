package de.app.ui.signature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.app.core.config.BaseSignatureService
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.data.model.FileHeader
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.components.FileViewAdapter
import de.app.ui.util.FilePickerIntentLauncher
import de.app.ui.util.extractFileHeader
import de.app.ui.util.setFileResultListener
import javax.inject.Inject

@AndroidEntryPoint
class DataSignatureFragment : Fragment() {

    @Inject
    lateinit var service: BaseSignatureService
    lateinit var binding: FragmentSignatureBinding

    private val files: MutableList<FileHeader> = ArrayList(generateDocuments(5))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignatureBinding.inflate(inflater, container, false)

        binding.files.adapter = FileViewAdapter(requireContext(), files)

        val launcher = FilePickerIntentLauncher(requireActivity()) { addFile(it) }

        lifecycle.addObserver(launcher.getObserver())
        binding.uploadFileLocal.setOnClickListener {
            launcher.launch("application/pdf")
        }
        binding.uploadFileDataSafe.setOnClickListener {
            findNavController().navigate(DataSignatureFragmentDirections.actionNavSignatureToNavDataSafe())
        }

        parentFragmentManager.setFragmentResultListener("data-safe-file-request", viewLifecycleOwner) { key, result ->
            extractFileHeader(result).onSuccess {
                addFile(it)
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        requireActivity().setFileResultListener("data-safe-file-request", viewLifecycleOwner) {
//            addFile(it)
//        }
    }

    private fun addFile(file: FileHeader) {
        binding.files.adapter?.apply {
            files.add(0, file)
            notifyItemInserted(0)
        }
        binding.files.post { binding.files.smoothScrollToPosition(0) }
    }


}