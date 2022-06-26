package de.app.ui.signature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.account.CitizenServiceAccountRepository
import de.app.api.account.SecretToken
import de.app.api.signature.SignatureService
import de.app.core.SessionManager
import de.app.core.config.DataGenerator.Companion.generateDocuments
import de.app.data.model.FileHeader
import de.app.databinding.FragmentSignatureBinding
import de.app.ui.components.OpenableFileViewAdapter
import de.app.ui.safe.DataSafePickerFactory
import de.app.ui.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DataSignatureFragment : Fragment(){

    @Inject
    lateinit var service: SignatureService
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var repo: CitizenServiceAccountRepository
    @Inject
    lateinit var dataSafePickerFactory: DataSafePickerFactory
    lateinit var binding: FragmentSignatureBinding

    private val files: MutableList<FileHeader> = ArrayList(generateDocuments(5))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignatureBinding.inflate(inflater, container, false)

        binding.files.adapter = OpenableFileViewAdapter(requireActivity(), files)

        val pickFileLauncher = lifecycle.launcher(FilePickerIntent(requireActivity()) {
            addFile(it)
        })
        val saveFileLauncher = lifecycle.launcher(FileSaverIntent(requireActivity()))

        binding.uploadFileLocal.setOnClickListener {
            pickFileLauncher.launch("application/pdf")
        }

        binding.submitLocal.setOnClickListener {
            files.forEach {
                saveFileLauncher.launch(it)
            }
        }

        binding.uploadFileDataSafe.setOnClickListener {
            sessionManager.currentUser?.let { user ->
                repo.getCitizenAccount(SecretToken( user.accountSecretToken)).onSuccess { accountInfo ->
                    dataSafePickerFactory.showPicker(requireActivity(), accountInfo) {
                        addFile(it)
                    }
                }
            }
        }


        return binding.root
    }

    private fun addFile(file: FileHeader) {
        binding.files.adapter?.apply {
            files.add(0, file)
            notifyItemInserted(0)
        }
        binding.files.post { binding.files.smoothScrollToPosition(0) }
    }

}