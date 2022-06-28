package de.app.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class SimpleFragment<B: ViewBinding>: Fragment() {

    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateBinding(inflater, container)
        setup()
        return binding.root
    }

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun setup()
}