package com.example.examplecrudlocal.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    abstract fun setListeners()
    abstract fun setObservers()
    abstract fun cleanFields()
    abstract fun removeObservers()
    abstract fun initViewComponents()
    abstract fun changeToolbarParams()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        initViewComponents()
        changeToolbarParams()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObservers()
    }

    fun goToFragment(fragmentId: Int, bundle: Bundle? = null){
        findNavController().navigate(fragmentId, bundle)
    }
}