package de.app.ui.law

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.law.LawChangeHeader
import de.app.api.law.LawRegistryService
import de.app.core.runWithInterval
import de.app.databinding.FragmentLawRegistryBinding
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class LawRegistryFragment : Fragment() {

    private lateinit var listManager: LinearLayoutManager
    @Inject lateinit var lawRegistry: LawRegistryService ;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLawRegistryBinding.inflate(inflater, container ,false)


        val changes = getLawChanges()
        binding.lawChangeList.adapter = LawChangeInfoViewAdapter(changes);
         listManager = LinearLayoutManager(context)
        binding.lawChangeList.layoutManager = listManager


        runWithInterval({updateLaws(binding.lawChangeList, changes)})

        return binding.root
    }

    private fun updateLaws(rv: RecyclerView, origin: MutableList<LawChangeHeader>) {
        val newChanges = getLawChanges()
        activity?.runOnUiThread {
            rv.adapter?.apply {
                origin.addAll(0, newChanges)
                notifyItemRangeInserted(0, newChanges.size)
            }
            if (listManager.findFirstVisibleItemPosition() <= 2){
                rv.post { rv.smoothScrollToPosition(0) }
            }

        }
    }

    private fun getLawChanges(): MutableList<LawChangeHeader> = ArrayList<LawChangeHeader>().apply {
        addAll(lawRegistry.getLawChanges())
        sortByDescending { it.date }
    }

}