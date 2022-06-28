package de.app.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class SimpleFragment<B: ViewBinding>: Fragment() {

    protected lateinit var binding: B
    protected val root: View
        get() = binding.root

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container)
        setup()
        return root
    }

    abstract fun inflate(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun setup()
}