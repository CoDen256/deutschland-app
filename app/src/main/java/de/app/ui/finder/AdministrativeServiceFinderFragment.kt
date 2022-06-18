package de.app.ui.finder

import android.app.SearchManager.SUGGEST_COLUMN_TEXT_1
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.api.service.AdministrativeService
import de.app.databinding.FragmentAdministrativeServiceFinderBinding
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class AdministrativeServiceFinderFragment : Fragment(), SearchView.OnQueryTextListener {

    @Inject lateinit var viewModel: AdministrativeServiceFinderViewModel
    private val services = ArrayList<AdministrativeService>()
    private val adapter = ServiceInfoViewAdapter(services) { onServiceClicked(it) }
    private lateinit var searchCityView: SearchView;
    private lateinit var searchServiceView: SearchView;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAdministrativeServiceFinderBinding.inflate(inflater, container, false)

        searchCityView = binding.searchCity
        searchServiceView = binding.searchService

        binding.serviceList.adapter = adapter
        binding.serviceList.layoutManager = LinearLayoutManager(context)

        searchServiceView.setOnQueryTextListener(this@AdministrativeServiceFinderFragment)

        lifecycleScope.launch {
            viewModel.getAddress().onSuccess {
                searchCityView.setQuery(it.city, true)
                searchDatabase("", it.city)
            }
        }

        searchCityView.suggestionsAdapter = SimpleCursorAdapter(
            context, android.R.layout.simple_list_item_1, null,
            arrayOf(SUGGEST_COLUMN_TEXT_1), intArrayOf(android.R.id.text1),
            FLAG_AUTO_REQUERY
        )

        searchCityView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.length >= 2) {
                        Executors.newSingleThreadExecutor().execute { searchCity(newText) }
                    } else {
                        searchCityView.suggestionsAdapter.changeCursor(null)
                    }
                }
                searchDatabase(searchServiceView.query.toString(), newText.orEmpty())
                return true
            }
        })

        searchCityView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                val cursor: Cursor = searchCityView.suggestionsAdapter.getItem(position) as Cursor
                val columnIndex = cursor.getColumnIndex(SUGGEST_COLUMN_TEXT_1)
                if (columnIndex >= 0) {
                    val term: String = cursor.getString(columnIndex)
                    cursor.close()

                    searchCityView.setQuery(term, true)
                }
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                return onSuggestionSelect(position)
            }

        })

        return binding.root
    }

    private fun searchCity(text: String) {
        val cursor = MatrixCursor(
            arrayOf(
                BaseColumns._ID,
                SUGGEST_COLUMN_TEXT_1
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

        requireActivity().runOnUiThread {
            searchCityView.suggestionsAdapter.changeCursor(cursor)
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
        viewModel.search(query, address).observe(viewLifecycleOwner) { list ->
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