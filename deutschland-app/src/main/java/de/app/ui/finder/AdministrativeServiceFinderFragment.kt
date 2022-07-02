package de.app.ui.finder

import android.R
import android.app.SearchManager.SUGGEST_COLUMN_TEXT_1
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CursorAdapter.FLAG_AUTO_REQUERY
import android.widget.SimpleCursorAdapter
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.geo.GeodataService
import de.app.api.service.AdministrativeService
import de.app.databinding.FragmentAdministrativeServiceFinderBinding
import de.app.databinding.FragmentAdministrativeServiceFinderSearchItemBinding
import de.app.ui.components.AccountAwareListFragment
import de.app.ui.util.observe
import de.app.ui.util.onClickNavigate
import javax.inject.Inject

@AndroidEntryPoint
class AdministrativeServiceFinderFragment :
    AccountAwareListFragment<FragmentAdministrativeServiceFinderBinding, FragmentAdministrativeServiceFinderSearchItemBinding, AdministrativeService>()
{
    @Inject
    lateinit var viewModel: AdministrativeServiceFinderViewModel

    @Inject
    lateinit var service: GeodataService

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
            root.onClickNavigate(navController,
                AdministrativeServiceFinderFragmentDirections.actionNavFinderToNavAdminService(item.id)
            )
        }
    }

    override fun loadItems() = listOf<AdministrativeService>()

    override fun setup() {
        viewModel.init(requireContext(), account)

        binding.serviceList.adapter = adapter

        binding.searchService.setOnQueryTextListener(ServiceQueryListener {
            viewModel.search(account, it.orEmpty(), binding.searchAddress.query.toString())
        })

        binding.searchAddress.setOnQueryTextListener(AddressQueryListener(
            service.getAllCities().map { it.city },
            binding.searchAddress,
            {
                viewModel.search(account, binding.searchService.query.toString(), it.orEmpty())
            },
            { requireActivity().runOnUiThread {
                binding.searchAddress.suggestionsAdapter.changeCursor(it)
            } }
        ))

        setupAddressSuggestions()

        observe(viewModel.currentAddress) {
            binding.searchAddress.setQuery(this, true)
        }

        observe(viewModel.currentQuery) {
            binding.searchService.setQuery(this, true)
        }

        observe(viewModel.services) {
            items.clear()
            items.addAll(this)
            adapter.notifyDataSetChanged()
        }

    }

    private fun setupAddressSuggestions() {
        binding.searchAddress.suggestionsAdapter = SimpleCursorAdapter(
            context, R.layout.simple_list_item_1, null,
            arrayOf(SUGGEST_COLUMN_TEXT_1), intArrayOf(R.id.text1),
            FLAG_AUTO_REQUERY
        )
        binding.searchAddress.setOnSuggestionListener(AddressSuggestionListener(binding.searchAddress))
    }

}