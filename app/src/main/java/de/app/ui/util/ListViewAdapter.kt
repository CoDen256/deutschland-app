package de.app.ui.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class ListViewAdapter<T, B: ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup) -> B,
    private val elements: List<T>,
    private val bindElement: (T, B) -> Unit
) : RecyclerView.Adapter<ListViewAdapter<T, B>.ViewHolder<B>>(){

    inner class ViewHolder<out B: ViewBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
        return ViewHolder(inflate(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        val element: T = elements[position]
        bindElement(element, holder.binding)
    }

    override fun getItemCount() =  elements.size
}
