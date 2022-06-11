package de.app.ui.finder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.app.R
import de.app.data.model.service.AdministrativeService
import de.app.databinding.FragmentAdministrativeServiceFinderBinding

class AdministrativeServiceFinder : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel = AdministrativeServiceFinderViewModel()
    private val services = ArrayList<AdministrativeService>()
    private val adapter = ServiceInfoViewAdapter(services) { onServiceClicked(it) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAdministrativeServiceFinderBinding.inflate(
        inflater, container, false
    ).apply {


        serviceList.adapter = adapter
        serviceList.layoutManager = LinearLayoutManager(context)

        searchService.setOnQueryTextListener(this@AdministrativeServiceFinder)

        viewModel.readData.observe(viewLifecycleOwner) {
            services.clear()
            services.addAll(it)
            adapter.notifyDataSetChanged()
        }

    }.root


    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        viewModel.search(query).observe(this) { list ->
            list.let {
                services.clear()
                services.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onServiceClicked(it: AdministrativeService) {
        findNavController().navigate(
            R.id.action_nav_finder_to_nav_admin_service,
            bundleOf("id" to it.id.toString())
        )
    }
}