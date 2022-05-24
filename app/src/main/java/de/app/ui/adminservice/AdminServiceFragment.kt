package de.app.ui.adminservice

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.app.R

class AdminServiceFragment : Fragment() {

    companion object {
        fun newInstance() = AdminServiceFragment()
    }

    private lateinit var viewModel: AdminServiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_service, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminServiceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}