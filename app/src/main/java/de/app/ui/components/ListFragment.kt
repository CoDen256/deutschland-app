package de.app.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class ListFragment<B: ViewBinding, I: ViewBinding, M> :SimpleFragment<B>() {
    protected lateinit var adapter: ListViewAdapter<M, I>
    protected val items: MutableList<M> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container)
        adapter = inflateAndSetupChildren()

        items.addAll(loadItems())
        adapter.notifyItemRangeInserted(0, items.size)

        setup()

        return root
    }

    abstract fun inflateChild(inflater: LayoutInflater, container: ViewGroup?): I
    abstract fun setupChild(binding: I, item: M)
    abstract fun loadItems(): List<M>

    open fun inflateAndSetupChildren() =
        ListViewAdapter({l, v -> inflateChild(l,v)}, items) { item, binding -> setupChild(binding, item)
    }
}