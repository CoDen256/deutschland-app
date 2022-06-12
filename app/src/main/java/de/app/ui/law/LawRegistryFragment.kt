package de.app.ui.law

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.app.data.model.law.LawChangeHeader
import de.app.databinding.FragmentLawRegistryBinding
import de.app.ui.util.runWithInterval
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

class LawRegistryFragment : Fragment() {

    private lateinit var listManager: LinearLayoutManager

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
        for (i in 1..Random.nextInt(1, 12)) {
            add(
                LawChangeHeader(
                    UUID.randomUUID(),
                    "Änderung des $i. Gesetzes",
                    shortDescription = "Das $i. Gesetz vom 3. Mai 2013 (BGBl. I S. 1084), das zuletzt durch Artikel 7 des Gesetzes vom 15. Januar 2021 (BGBl. I S. 530) geändert worden ist",
                    date= LocalDate.of(2022, i, Random.nextInt(1, 28))
                )
            )
        }
        sortByDescending { it.date }
    }

}