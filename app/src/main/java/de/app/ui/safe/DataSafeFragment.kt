package de.app.ui.safe

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.app.core.runWithInterval
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeBinding
import java.util.*
import kotlin.random.Random

class DataSafeFragment : Fragment() {


    private lateinit var viewModel: DataSafeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDataSafeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(DataSafeViewModel::class.java)

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

    private fun getFiles(): MutableList<FileHeader> = ArrayList<FileHeader>().apply {
        for (i in 0..Random.nextInt(1, 10)) {
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