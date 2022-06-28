package de.app.ui.finder

import android.app.SearchManager.SUGGEST_COLUMN_TEXT_1
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CursorAdapter.FLAG_AUTO_REQUERY
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.service.AdministrativeService
import de.app.databinding.FragmentAdministrativeServiceFinderBinding
import de.app.databinding.FragmentAdministrativeServiceFinderSearchItemBinding
import de.app.ui.components.ListFragment
import de.app.ui.util.editable
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AdministrativeServiceFinderFragment :
    ListFragment<FragmentAdministrativeServiceFinderBinding, FragmentAdministrativeServiceFinderSearchItemBinding, AdministrativeService>()
{
    @Inject
    lateinit var viewModel: AdministrativeServiceFinderViewModel

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdministrativeServiceFinderBinding.inflate(inflater, container, false)

    override fun inflateChild(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAdministrativeServiceFinderSearchItemBinding.inflate(inflater, container, false)

    override fun setupChild(
        binding: FragmentAdministrativeServiceFinderSearchItemBinding,
        item: AdministrativeService
    ) {
        binding.apply {
            serviceAddress.text = "%s, %s".format(item.address.postalCode, item.address.city)
            serviceName.text = item.name
            serviceDescription.text = item.description
            root.setOnClickListener {
                navController.navigate(
                    AdministrativeServiceFinderFragmentDirections.actionNavFinderToNavAdminService(item.id)
                )
            }
        }
    }

    override fun loadItems() = listOf<AdministrativeService>()

    override fun setup() {
        binding.serviceList.adapter = adapter

        binding.searchService.setOnQueryTextListener(ServiceQueryListener{
            searchDatabase(it.orEmpty(), binding.searchAddress.query.toString())
        })

        lifecycleScope.launch {
            viewModel.requestAddress(requireContext()).addOnSuccessListener { result ->
                result.onSuccess {
                    binding.searchAddress.setQuery(it.city, true)
                    searchDatabase("", it.city)
                }
            }
        }

        binding.searchAddress.suggestionsAdapter = SimpleCursorAdapter(
            context, android.R.layout.simple_list_item_1, null,
            arrayOf(SUGGEST_COLUMN_TEXT_1), intArrayOf(android.R.id.text1),
            FLAG_AUTO_REQUERY
        )

        binding.searchAddress.setOnQueryTextListener(AddressQueryListener(binding.searchAddress,
            { searchDatabase(binding.searchService.query.toString(), it.orEmpty()) },
            { requireActivity().runOnUiThread {
                binding.searchAddress.suggestionsAdapter.changeCursor(it)
                } }
        ))

        binding.searchAddress.setOnSuggestionListener(AddressSuggestionListener(binding.searchAddress))
    }

    private fun searchDatabase(query: String, address: String) {
        viewModel.search(query, address).observe(viewLifecycleOwner) { list ->
            list.let {
                items.clear()
                items.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }
}