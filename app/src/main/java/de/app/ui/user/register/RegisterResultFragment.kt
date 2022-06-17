package de.app.ui.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.app.R
import de.app.databinding.FragmentUserRegisterResultBinding
import de.app.ui.util.onClickNavigate

class RegisterResultFragment : Fragment() {

    private val args: RegisterResultFragmentArgs by navArgs()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentUserRegisterResultBinding.inflate(inflater, container, false)
        .apply {
            navController = findNavController()
            val secretToken = args.accountSecretToken
            if (secretToken == null){
                onRegisterFailed()
            }else{
                onRegisterSuccessful(secretToken)
            }
        }.root

    private fun FragmentUserRegisterResultBinding.onRegisterSuccessful(secretToken: String) {
        next.text = getString(R.string.set_pin_for_account_register_successful)
        result.text = getString(R.string.register_successful_message)
        result.setTextColor(resources.getColor(R.color.successful))

        next.onClickNavigate(
            navController,
            RegisterResultFragmentDirections.actionNavResultToSetPin(secretToken, args.type!!)
        )
    }

    private fun FragmentUserRegisterResultBinding.onRegisterFailed() {
        next.text = getString(R.string.try_again_register_failed_button)
        result.text = getString(R.string.register_failed_message, args.error)
        result.setTextColor(resources.getColor(R.color.failed))
        next.onClickNavigate(
            navController,
            RegisterResultFragmentDirections.actionNavResultToRegister()
        )
    }

}