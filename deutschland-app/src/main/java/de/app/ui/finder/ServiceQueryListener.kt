package de.app.ui.finder

import android.widget.SearchView

class ServiceQueryListener(
    private val queryChangedListener: (String?) -> Unit,
) : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        queryChangedListener(query)
        return true
    }
}