package de.app.ui.finder

import android.app.SearchManager
import android.app.SearchManager.SUGGEST_COLUMN_TEXT_1
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter.FLAG_AUTO_REQUERY
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.app.R
import de.app.data.model.service.AdministrativeService
import de.app.databinding.FragmentAdministrativeServiceFinderBinding
import org.json.JSONArray
import java.util.concurrent.Executors


class AdministrativeServiceFinder : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel = AdministrativeServiceFinderViewModel()
    private val services = ArrayList<AdministrativeService>()
    private val adapter = ServiceInfoViewAdapter(services) { onServiceClicked(it) }
    private lateinit var searchCityView: SearchView;
    private lateinit var searchServiceView: SearchView;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAdministrativeServiceFinderBinding.inflate(
        inflater, container, false
    ).apply {


        serviceList.adapter = adapter
        serviceList.layoutManager = LinearLayoutManager(context)

        searchService.setOnQueryTextListener(this@AdministrativeServiceFinder)

        viewModel.readData.observe(viewLifecycleOwner) {
            services.clear()
            services.addAll(it)
            adapter.notifyDataSetChanged()
        }

        searchCity.suggestionsAdapter = SimpleCursorAdapter(
            context, android.R.layout.simple_list_item_1, null,
            arrayOf(SUGGEST_COLUMN_TEXT_1), intArrayOf(android.R.id.text1),
            FLAG_AUTO_REQUERY
        )
        searchCityView = searchCity
        searchServiceView = searchService

        searchCity.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchDatabase(searchServiceView.query.toString(), query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    if (newText.length >= 0) {
                        Executors.newSingleThreadExecutor().execute { searchCity(newText) }
                    } else {
                        searchCity.suggestionsAdapter.changeCursor(null)
                    }
                }
                return true
            }
        })

        searchCity.setOnSuggestionListener(object: SearchView.OnSuggestionListener{
            override fun onSuggestionSelect(position: Int): Boolean {
                val cursor: Cursor = searchCity.suggestionsAdapter.getItem(position) as Cursor
                val columnIndex = cursor.getColumnIndex(SUGGEST_COLUMN_TEXT_1)
                if (columnIndex >= 0){
                    val term: String = cursor.getString(columnIndex)
                    cursor.close()

                    searchCity.setQuery(term, true)
                }
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                return onSuggestionSelect(position)
            }

        })


    }.root

    private fun FragmentAdministrativeServiceFinderBinding.searchCity(text: String) {
        val cursor = MatrixCursor(
            arrayOf(
                BaseColumns._ID,
                SUGGEST_COLUMN_TEXT_1
            )
        )

        // get your search terms from the server here, ex:
        val terms: JSONArray = JSONArray()
        for (i in arrayListOf("Merseburg", "Leipzig", "Berlin", "Halle")) {
            if (i.lowercase().contains(text.lowercase())) {
                terms.put(i)
            }
        }

        // parse your search terms into the MatrixCursor
        for (index in 0 until terms.length()) {
                val term = terms.getString(index)
                val row = arrayOf<Any>(index, term)

                cursor.addRow(row)

        }

        requireActivity().runOnUiThread {
            searchCity.suggestionsAdapter.changeCursor(cursor)
        }
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        searchDatabase(query.orEmpty(), searchCityView.query.toString())
        return true
    }

    private fun searchDatabase(query: String, address: String) {
        viewModel.search(query, address).observe(this) { list ->
            list.let {
                services.clear()
                services.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onServiceClicked(it: AdministrativeService) {
        findNavController().navigate(
            R.id.action_nav_finder_to_nav_admin_service,
            bundleOf("id" to it.id.toString())
        )
    }
}