package de.app.ui.service

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.databinding.FragmentAdministrativeServiceResultBinding


class SubmittedResultFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdministrativeServiceResultBinding.inflate(inflater, container, false)
        val url = "https://coden256.github.io/deutschland-app/"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
        return binding.root
    }
}