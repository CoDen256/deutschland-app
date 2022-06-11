package de.app.ui.signature

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.R

class DataSignatureFragment : Fragment() {

    companion object {
        fun newInstance() = DataSignatureFragment()
    }

    private lateinit var viewModel: DataSignatureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data_safe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DataSignatureViewModel::class.java)
        // TODO: Use the ViewModel
    }

}