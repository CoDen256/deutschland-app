package de.app.ui.law

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.R

class LawRegistryFragment : Fragment() {

    companion object {
        fun newInstance() = LawRegistryFragment()
    }

    private lateinit var viewModel: LawRegistryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_law_registry, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LawRegistryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}