package de.app.ui.account.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.app.R
import de.app.data.model.AccountHeader
import de.app.databinding.FragmentLoginSelectAccountBinding
import de.app.databinding.FragmentLoginSelectAccountItemBinding
import de.app.ui.util.ListViewAdapter
import de.app.ui.util.onClickNavigate
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectAccountFragment : Fragment() {

    private lateinit var binding: FragmentLoginSelectAccountBinding
    @Inject lateinit var viewModel: SelectAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginSelectAccountBinding.inflate(inflater, container, false)
        val elementBinding = { i: LayoutInflater, v: ViewGroup ->
            FragmentLoginSelectAccountItemBinding.inflate(i, v, false)}

        val navController = findNavController()
        val headers = ArrayList<AccountHeader>()

        val adapter = ListViewAdapter(elementBinding, headers) { element, b ->
            b.accountName.text = element.displayName
            b.root.onClickNavigate(navController,
                R.id.action_nav_select_to_enter_pin,
                    "accountId" to element.id
                )
        }
        binding.addAccount.onClickNavigate(navController, R.id.action_nav_select_to_register)

        binding.accounts.adapter = adapter
        binding.accounts.layoutManager = LinearLayoutManager(context)

        initialFetch(headers, adapter)

        return binding.root
    }

    private fun initialFetch(
        headers: ArrayList<AccountHeader>,
        adapter: ListViewAdapter<AccountHeader, FragmentLoginSelectAccountItemBinding>
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