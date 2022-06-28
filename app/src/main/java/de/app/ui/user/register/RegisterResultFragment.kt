package de.app.ui.user.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import de.app.R
import de.app.databinding.FragmentUserRegisterResultBinding
import de.app.ui.components.SimpleFragment
import de.app.ui.util.onClickNavigate

class RegisterResultFragment : SimpleFragment<FragmentUserRegisterResultBinding>() {

    private val args: RegisterResultFragmentArgs by navArgs()

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserRegisterResultBinding.inflate(inflater, container, false)

    override fun setup() {
        val secretToken = args.accountSecretToken
        if (secretToken == null){
            binding.onRegisterFailed()
        }else{
            binding.onRegisterSuccessful(secretToken)
        }
    }

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