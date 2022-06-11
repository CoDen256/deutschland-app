package de.app.ui.finder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.app.api.dummy.BaseAdministrativeServiceRegistry
import de.app.data.model.service.AdministrativeService
import de.app.databinding.FragmentAdministrativeServiceFinderBinding

class AdministrativeServiceFinder : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAdministrativeServiceFinderBinding.inflate(
        inflater, container, false
    ).apply {
        val services = getServices()
        serviceList.adapter = ServiceInfoViewAdapter(services)
        serviceList.layoutManager = LinearLayoutManager(context)
    }.root

    private fun getServices(): MutableList<AdministrativeService> {
        return ArrayList(BaseAdministrativeServiceRegistry().getAllServices())
    }


}