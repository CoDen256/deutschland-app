package de.app.ui.finder

import android.app.SearchManager
import android.database.Cursor
import android.widget.SearchView

class AddressSuggestionListener(private val searchAddressView: SearchView)
    : SearchView.OnSuggestionListener {

    override fun onSuggestionSelect(position: Int): Boolean {
        val cursor: Cursor = searchAddressView.suggestionsAdapter.getItem(position) as Cursor
        val columnIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)
        if (columnIndex >= 0) {
            val term: String = cursor.getString(columnIndex)
            cursor.close()

            searchAddressView.setQuery(term, true)
        }
        return true
    }

    override fun onSuggestionClick(position: Int): Boolean {
        return onSuggestionSelect(position)
    }
}