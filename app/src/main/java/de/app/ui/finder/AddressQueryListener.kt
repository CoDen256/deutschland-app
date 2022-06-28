package de.app.ui.finder

import android.app.Activity
import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.widget.SearchView
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

        // get your search terms from the server here, ex:
        val map = mapOf("Merseburg" to 0, "Leipzig" to 1, "Berlin" to 2, "Halle" to 3)
        val terms: JSONArray = JSONArray()
        for (i in ArrayList(map.keys)) {
            if (i.lowercase().contains(text.lowercase())) {
                terms.put(i)
            }
        }

        // parse your search terms into the MatrixCursor
        for (index in 0 until terms.length()) {
            val term = terms.getString(index)
            val row = arrayOf<Any>(map[term]!!, term)

            cursor.addRow(row)

        }
        onChangeCursorListener(cursor)
    }
}