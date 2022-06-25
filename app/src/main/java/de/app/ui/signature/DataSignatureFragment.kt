package de.app.ui.signature

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.data.model.FileHeader
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.util.FileViewAdapter
import de.app.ui.service.IntentLauncher
import de.app.ui.util.*
import java.util.ArrayList
import kotlin.random.Random

class DataSignatureFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignatureBinding.inflate(inflater, container, false)
        val files = getFiles()

        val launcher = IntentLauncher<String, Result<Uri>>(
            requireActivity().activityResultRegistry,
            key = "UPLOAD_FILE",
            createIntent = { _, input -> createFilePickerIntent(input) },
            parseResult = { _, intent -> parseFilePickerResult(intent) },
            handleResult = { rs ->
                rs.onSuccess {

                    val contentResolver = requireActivity().contentResolver
//                    val realPathFromURI = FileUtils.getPath(requireContext(), it.data)
                    val file = FileHeader(
                        it.getFileName(contentResolver)!!, it,
                        "application/pdf"
                    )
                    binding.files.adapter?.apply {
                        val curSize = itemCount
                        files.add(file)
                        notifyItemRangeInserted(curSize, 1)
                    }

                }
            }
        )

        lifecycle.addObserver(launcher.getObserver())
        binding.uploadFileLocal.setOnClickListener {
            launcher.launch("application/pdf")
        }

        binding.files.adapter = FileViewAdapter(requireContext(), files)
        binding.files.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }

    private fun getFiles(): MutableList<FileHeader> = ArrayList(generateDocuments(5))

}