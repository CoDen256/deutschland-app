package de.app.ui.law

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.law.LawChangeHeader
import de.app.api.law.LawRegistryService
import de.app.databinding.FragmentLawRegistryBinding
import de.app.databinding.FragmentLawRegistryItemBinding
import de.app.ui.components.ListFragment
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class LawRegistryFragment : ListFragment<FragmentLawRegistryBinding, FragmentLawRegistryItemBinding, LawChangeHeader>() {

    @Inject
    lateinit var lawRegistry: LawRegistryService

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentLawRegistryBinding.inflate(inflater, container, false)

    override fun inflateChild(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLawRegistryItemBinding.inflate(inflater, container, false)

    override fun setupChild(binding: FragmentLawRegistryItemBinding, item: LawChangeHeader) {
        binding.apply {
            lawChangeName.text = item.name
            lawDate.text = item.date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            lawChangeDescription.text = item.shortDescription
        }
    }

    override fun loadItems(): List<LawChangeHeader> = lawRegistry.getLawChanges().sortedByDescending { it.date }

    override fun setup() {
        binding.lawChangeList.adapter = adapter
    }

}