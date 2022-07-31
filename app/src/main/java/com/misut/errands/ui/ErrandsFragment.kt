package com.misut.errands.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.misut.errands.R
import com.misut.errands.databinding.FragmentErrandsBinding


class ErrandsFragment : Fragment() {
    private lateinit var binding: FragmentErrandsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentErrandsBinding.inflate(inflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.errandsToolbar.inflateMenu(R.menu.menu_explorer)
    }
}
