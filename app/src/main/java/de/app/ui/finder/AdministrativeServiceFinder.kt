package de.app.ui.finder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.app.data.model.service.AdministrativeService
import de.app.databinding.FragmentAdministrativeServiceFinderBinding

class AdministrativeServiceFinder : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel = AdministrativeServiceFinderViewModel()
    private val services = ArrayList<AdministrativeService>()
    private val adapter = ServiceInfoViewAdapter(services)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAdministrativeServiceFinderBinding.inflate(
        inflater, container, false
    ).apply {


        serviceList.adapter = adapter
        serviceList.layoutManager = LinearLayoutManager(context)

        searchService.setOnQueryTextListener(this@AdministrativeServiceFinder)

        viewModel.readData.observe(viewLifecycleOwner){
            services.clear()
            services.addAll(it)
            adapter.notifyDataSetChanged()
        }

    }.root


    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
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


}