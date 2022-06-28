package de.app.ui.finder

import android.app.Activity
import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.widget.SearchView
import de.app.geo.GeoDataSource
import org.json.JSONArray
import java.util.concurrent.Executors

class AddressQueryListener (
    private val searchAddressView: SearchView,
    private val queryChangedListener: (String?) -> Unit,
    private val onChangeCursorListener: (Cursor) -> Unit

): SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            if (newText.length >= 2) {
                Executors.newSingleThreadExecutor().execute { searchCity(newText) }
            } else {
                searchAddressView.suggestionsAdapter.changeCursor(null)
            }
        }
        queryChangedListener(newText)
        return true
    }

    private fun searchCity(text: String) {
        val cursor = MatrixCursor(
            arrayOf(
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1
            )
        )

        GeoDataSource.requestCities()
            .filter {
                it.city.lowercase().contains(text.lowercase())
            }
            .map {
                arrayOf<Any>(it.city.hashCode(), it.city)
            }
            .forEach {
                cursor.addRow(it)
            }

        onChangeCursorListener(cursor)
    }
}