package de.app.ui.account.select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.app.databinding.FragmentLoginSelectAccountItemBinding

class AccountViewAdapter(
    private val accounts: MutableList<AccountHeader>,
    private val onClickListener: (AccountHeader) -> Unit
) : RecyclerView.Adapter<AccountViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentLoginSelectAccountItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account: AccountHeader = accounts[position]
        holder.name.text = "%s %s".format(account.name, account.surname)
        holder.root.setOnClickListener {
            onClickListener(account)
        }
    }

    override fun getItemCount(): Int = accounts.size

    inner class ViewHolder(binding: FragmentLoginSelectAccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.accountName
        val root = binding.root
    }

}