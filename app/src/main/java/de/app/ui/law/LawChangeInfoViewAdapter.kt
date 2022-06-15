package de.app.ui.law

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.app.api.law.LawChangeHeader
import de.app.databinding.FragmentLawRegistryItemBinding
import java.time.format.DateTimeFormatter

class LawChangeInfoViewAdapter(
    private val headers: MutableList<LawChangeHeader>
) : RecyclerView.Adapter<LawChangeInfoViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentLawRegistryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val header: LawChangeHeader = headers[position]
        holder.name.text = header.name
        holder.date.text = header.date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        holder.description.text = header.shortDescription
    }

    override fun getItemCount(): Int = headers.size

    inner class ViewHolder(binding: FragmentLawRegistryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.lawChangeName
        val description = binding.lawChangeDescription
        val date = binding.lawDate
    }

}