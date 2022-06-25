package de.app.ui.user.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.app.data.model.UserHeader
import de.app.databinding.FragmentUserSelectBinding
import de.app.databinding.FragmentUserSelectItemBinding
import de.app.ui.components.ListViewAdapter
import de.app.ui.util.onClickNavigate
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectUserFragment : Fragment() {

    private lateinit var binding: FragmentUserSelectBinding
    @Inject lateinit var viewModel: SelectUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserSelectBinding.inflate(inflater, container, false)
        val elementBinding = { i: LayoutInflater, v: ViewGroup ->
            FragmentUserSelectItemBinding.inflate(i, v, false)}

        val navController = findNavController()
        val headers = ArrayList<UserHeader>()

        val adapter = ListViewAdapter(elementBinding, headers) { element, b ->
            b.accountName.text = element.displayName
            b.root.onClickNavigate(navController,
                SelectUserFragmentDirections.actionNavSelectToEnterPin(element.userId)
            )
        }
        binding.addAccount.onClickNavigate(navController, SelectUserFragmentDirections.actionNavSelectToRegister())

        binding.accounts.adapter = adapter
        binding.accounts.layoutManager = LinearLayoutManager(context)

        initialFetch(headers, adapter)

        return binding.root
    }

    private fun initialFetch(
        headers: MutableList<UserHeader>,
        adapter: ListViewAdapter<UserHeader, FragmentUserSelectItemBinding>
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            val accounts = viewModel.getAccounts()
            if (accounts.isEmpty()) {
                binding.empty.isVisible = true
            } else {
                headers.addAll(accounts)
                adapter.notifyItemRangeInserted(0, accounts.size)
            }
        }
    }

}