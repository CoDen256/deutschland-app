package de.app.ui.safe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.core.runWithInterval
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeBinding
import de.app.ui.components.FileViewAdapter
import de.app.ui.signature.DataSignatureFragmentDirections
import de.app.ui.util.bundleFromFileHeader

class DataSafeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDataSafeBinding.inflate(inflater, container, false)

        val files = getFiles()
        binding.rvDocuments.adapter = FileViewAdapter(requireContext(), files) {
            parentFragmentManager.setFragmentResult("data-safe-file-request", bundleFromFileHeader(it))
            findNavController().navigate(DataSafeFragmentDirections.actionNavDataSafeToNavSignature())

        }


        runWithInterval({updateFiles(binding.rvDocuments, files)})

        return binding.root
    }

    private fun updateFiles(rv: RecyclerView, origin: MutableList<FileHeader>) {
        val newMails = getFiles()
        activity?.runOnUiThread {
            rv.adapter?.apply {
                val curSize = itemCount
                origin.addAll(newMails)
                notifyItemRangeChanged(curSize, newMails.size)
            }
        }
    }

    private fun getFiles(): MutableList<FileHeader> = ArrayList(generateDocuments(30))
}