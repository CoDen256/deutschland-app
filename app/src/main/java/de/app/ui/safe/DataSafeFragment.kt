package de.app.ui.safe

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.R

class DataSafeFragment : Fragment() {

    companion object {
        fun newInstance() = DataSafeFragment()
    }

    private lateinit var viewModel: DataSafeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data_safe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DataSafeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}