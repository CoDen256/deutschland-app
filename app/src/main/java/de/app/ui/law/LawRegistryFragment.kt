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
import de.app.core.applyIf
import de.app.core.runWithInterval
import de.app.databinding.FragmentLawRegistryBinding
import de.app.databinding.FragmentLawRegistryItemBinding
import de.app.ui.components.ListViewAdapter
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LawRegistryFragment : Fragment() {

    private lateinit var binding: FragmentLawRegistryBinding
    @Inject
    lateinit var lawRegistry: LawRegistryService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLawRegistryBinding.inflate(inflater, container, false)
        val elementBinding = { i: LayoutInflater, p: ViewGroup -> FragmentLawRegistryItemBinding.inflate(i, p, false) }

        val changes = ArrayList(lawRegistry.getLawChanges().sortedByDescending { it.date })

        binding.lawChangeList.adapter = ListViewAdapter(elementBinding, changes) { header, binding ->
            binding.lawChangeName.text = header.name
            binding.lawDate.text = header.date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            binding.lawChangeDescription.text = header.shortDescription
        }

        runWithInterval({ updateLaws(binding.lawChangeList, changes) })

        return binding.root
    }

    private fun updateLaws(rv: RecyclerView, origin: MutableList<LawChangeHeader>) {
        val newChanges = lawRegistry.getLawChanges().sortedByDescending { it.date }
        activity?.runOnUiThread {
            rv.adapter?.apply {
                origin.addAll(0, newChanges)
                notifyItemRangeInserted(0, newChanges.size)
            }
            binding.lawChangeList.layoutManager.applyIf<RecyclerView.LayoutManager, LinearLayoutManager> {
                if (findFirstVisibleItemPosition() <= 2){
                    rv.post { rv.smoothScrollToPosition(0) }
                }
            }
        }
    }

}