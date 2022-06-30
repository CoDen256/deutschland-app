package de.app.ui.user.select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import de.app.data.model.UserHeader
import de.app.databinding.FragmentUserSelectBinding
import de.app.databinding.FragmentUserSelectItemBinding
import de.app.ui.components.ListFragment
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

        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                removeAccount(viewHolder.adapterPosition)
            }
        })

        helper.attachToRecyclerView(binding.accounts)
    }

    private fun removeAccount(index: Int) {
        val item = items[index]
        items.removeAt(index)
        adapter.notifyItemRemoved(index)
        if (items.isEmpty()) binding.empty.isVisible = true
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.removeAccount(item)
        }
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