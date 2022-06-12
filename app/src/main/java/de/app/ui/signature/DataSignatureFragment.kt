package de.app.ui.signature

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import de.app.R
import de.app.data.Result
import de.app.data.model.FileHeader
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.safe.FileViewAdapter
import de.app.ui.service.IntentLauncher
import de.app.ui.util.createFilePickerIntent
import de.app.ui.util.getFileName
import de.app.ui.util.parseFilePickerResult
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
            handleResult = {
                if (it is Result.Success) {
                    val file = FileHeader(it.data.getFileName(requireActivity().contentResolver)!!, it.data.toString(),
                    "application/pdf")
                    binding.files.adapter?.apply {
                        val curSize = itemCount
                        files.add(file)
                        notifyItemRangeInserted(curSize, 1)
                    }
                }
            }
        )

        lifecycle.addObserver(launcher.getObserver())
        binding.uploadFile.setOnClickListener {
            launcher.launch("application/pdf")
        }

        binding.files.adapter = FileViewAdapter(requireContext(), files)
        binding.files.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }

    private fun getFiles(): MutableList<FileHeader> = ArrayList<FileHeader>().apply {
        for (i in 0..Random.nextInt(2)) {
            addAll(listOf(
                FileHeader(
                    "AlphaDoc$i",
                    "http://www.africau.edu/images/default/sample.pdf",
                    "application/pdf"
                ),
                FileHeader(
                    "BetaDoc$i",
                    "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
                    "application/pdf"
                ),
            ))
        }
    }


}