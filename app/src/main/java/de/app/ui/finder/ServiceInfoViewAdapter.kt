package de.app.ui.finder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.app.api.service.AdministrativeService
import de.app.databinding.FragmentAdministrativeServiceFinderSearchItemBinding

class ServiceInfoViewAdapter(
    private val services: MutableList<AdministrativeService>,
    private val onItemClickHandler: (AdministrativeService) -> Unit
) : RecyclerView.Adapter<ServiceInfoViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentAdministrativeServiceFinderSearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service: AdministrativeService = services[position]
        holder.address.text = "%s, %s".format(service.address.postalCode, service.address.city)
        holder.name.text = service.name
        holder.desc.text = service.description
        holder.root.setOnClickListener { onItemClickHandler(service) }
    }

    override fun getItemCount(): Int = services.size

    inner class ViewHolder(binding: FragmentAdministrativeServiceFinderSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val address = binding.serviceAddress
        val desc = binding.serviceDescription
        val name = binding.serviceName
        val root = binding.root
    }

}