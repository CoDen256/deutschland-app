package de.app.ui.user.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.app.data.model.UserHeader
import de.app.databinding.FragmentUserSelectBinding
import de.app.databinding.FragmentUserSelectItemBinding
import de.app.ui.components.ListFragment
import de.app.ui.components.ListViewAdapter
import de.app.ui.util.onClickNavigate
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectUserFragment :
    ListFragment<FragmentUserSelectBinding, FragmentUserSelectItemBinding, UserHeader>() {

    @Inject
    lateinit var viewModel: SelectUserViewModel


    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserSelectBinding.inflate(inflater, container, false)

    override fun inflateChild(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserSelectItemBinding.inflate(inflater, container, false)

    override fun setupChild(binding: FragmentUserSelectItemBinding, item: UserHeader) {
        binding.apply {
            accountName.text = item.displayName
            root.onClickNavigate(
                navController,
                SelectUserFragmentDirections.actionNavSelectToEnterPin(item.userId)
            )
        }
    }

    override fun loadItems() = ArrayList<UserHeader>()

    override fun setup() {
        binding.addAccount.onClickNavigate(navController,
            SelectUserFragmentDirections.actionNavSelectToRegister()
        )
        binding.accounts.adapter = adapter
        initialFetch()
    }

    private fun initialFetch() {
        viewLifecycleOwner.lifecycleScope.launch {
            val accounts = viewModel.getAccounts()
            if (accounts.isEmpty()) {
                binding.empty.isVisible = true
            } else {
                items.clear()
                items.addAll(accounts)
                adapter.notifyItemRangeInserted(0, accounts.size)
            }
        }
    }

}