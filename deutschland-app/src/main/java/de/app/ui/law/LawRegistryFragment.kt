package de.app.ui.law

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.law.LawChange
import de.app.api.law.LawRegistryService
import de.app.databinding.FragmentLawRegistryBinding
import de.app.databinding.FragmentLawRegistryItemBinding
import de.app.ui.components.ListFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class LawRegistryFragment : ListFragment<FragmentLawRegistryBinding, FragmentLawRegistryItemBinding, LawChange>() {

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

    override fun setupChild(binding: FragmentLawRegistryItemBinding, item: LawChange) {
        binding.apply {
            lawChangeName.text = item.name
            lawDate.text = item.date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            lawChangeDescription.text = item.description
        }
    }

    override fun loadItems() = lawRegistry.getLawChanges(to=LocalDateTime.now()).sortedByDescending { it.date }

    override fun setup() {
        binding.lawChangeList.adapter = adapter
    }

}