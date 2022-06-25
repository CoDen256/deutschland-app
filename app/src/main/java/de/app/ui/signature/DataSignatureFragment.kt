package de.app.ui.signature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import de.app.core.config.BaseSignatureService
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.components.FileViewAdapter
import de.app.ui.util.FilePickerIntentLauncher
import javax.inject.Inject

@AndroidEntryPoint
class DataSignatureFragment : Fragment() {

    @Inject
    lateinit var service: BaseSignatureService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignatureBinding.inflate(inflater, container, false)

        val files = ArrayList(generateDocuments(5))
        binding.files.adapter = FileViewAdapter(requireContext(), files)

        val launcher = FilePickerIntentLauncher(requireActivity()) {
            binding.files.adapter?.apply {
                files.add(0, it)
                notifyItemInserted(0)
            }
            binding.files.post { binding.files.smoothScrollToPosition(0) }
        }


        lifecycle.addObserver(launcher.getObserver())
        binding.uploadFileLocal.setOnClickListener {
            launcher.launch("application/pdf")
        }

        return binding.root
    }

}