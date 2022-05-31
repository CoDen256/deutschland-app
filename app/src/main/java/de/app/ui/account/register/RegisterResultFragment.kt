package de.app.ui.account.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.app.R
import de.app.databinding.FragmentLoginRegisterResultBinding

class RegisterResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLoginRegisterResultBinding.inflate(inflater, container, false)
        .apply {
            val accountId = arguments?.getString("accountId")
            if (accountId == null){
                this.next.text = "Try again"
                this.result.text = "Failed"
                this.result.setTextColor(resources.getColor(R.color.failed))
                this.next.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_nav_result_to_register,
                    )
                }
            }else{
                this.next.text = "Set PIN for the account"
                this.result.text = "Successful"
                this.result.setTextColor(resources.getColor(R.color.successful))
                this.next.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_nav_result_to_enter_pin,
                        bundleOf("accountId" to accountId)
                    )
                }
            }
        }.root

}