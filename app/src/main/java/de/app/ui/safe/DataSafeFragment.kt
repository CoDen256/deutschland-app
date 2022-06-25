package de.app.ui.safe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.app.core.config.DataGenerator
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.core.runWithInterval
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeBinding
import de.app.ui.util.FileViewAdapter
import kotlin.random.Random

class DataSafeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDataSafeBinding.inflate(inflater, container, false)

        val files = getFiles()
        binding.rvDocuments.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = FileViewAdapter(context, files)
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