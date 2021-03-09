package com.example.verifonevx990app.brandemi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.verifonevx990app.R
import com.example.verifonevx990app.databinding.FragmentBrandEmiProductBinding
import com.example.verifonevx990app.vxUtils.IDialog

class BrandEMIProductFragment : Fragment() {
    private var binding: FragmentBrandEmiProductBinding? = null
    private var iDialog: IDialog? = null
    private var isSubCategoryItemPresent: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IDialog) iDialog = context
    }

    override fun onDetach() {
        super.onDetach()
        iDialog = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBrandEmiProductBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isSubCategoryItemPresent = arguments?.getBoolean("isSubCategoryItemPresent") ?: false
        binding?.subHeaderView?.subHeaderText?.text = getString(R.string.select_product)
        binding?.subHeaderView?.backImageButton?.setOnClickListener {
            if (isSubCategoryItemPresent) parentFragmentManager.popBackStackImmediate()
            else {
                parentFragmentManager.popBackStack()
                parentFragmentManager.popBackStack()
            }
        }
    }
}