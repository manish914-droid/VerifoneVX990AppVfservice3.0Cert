package com.example.verifonevx990app.bankEmiEnquiry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.verifonevx990app.databinding.FragmentIssuerListBinding
import com.example.verifonevx990app.databinding.ItemIssuerListViewholderBinding
import com.example.verifonevx990app.realmtables.IssuerParameterTable
import com.example.verifonevx990app.vxUtils.VFService

class IssuerListFragment : Fragment() {
    private var bindingView: FragmentIssuerListBinding? = null
    private var selectedIssuers: ArrayList<IssuerParameterTable> = arrayListOf()
    private var allIssuers: ArrayList<IssuerParameterTable> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingView = FragmentIssuerListBinding.inflate(inflater, container, false)
        return bindingView?.root
    }

    private fun createSelectedIssuerList(position: Int, operation: EnumAddDeleteIssuer) {
        if (allIssuers.size > 0) {
            when (operation) {
                EnumAddDeleteIssuer.SELECTED -> {
                    selectedIssuers.add(allIssuers[position])
                }
                EnumAddDeleteIssuer.UNSELECTED -> {
                    selectedIssuers.remove(allIssuers[position])
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ipt = IssuerParameterTable.selectFromIssuerParameterTable()
        allIssuers = ipt as ArrayList<IssuerParameterTable>
        bindingView?.issuerRV?.layoutManager = LinearLayoutManager(activity)
        bindingView?.issuerRV?.adapter = IssuerListAdapter(allIssuers, ::createSelectedIssuerList)
        bindingView?.doEnquiryBtn?.setOnClickListener {
            if (selectedIssuers.size > 0) {
                // todo further proceed to next step
                selectedIssuers.forEach {
                    Log.e(
                        "SELECT ISSUERS",
                        "Name --> " + it.issuerName + "  ID--> " + it.issuerId
                    )
                }
            } else {
                VFService.showToast("Select an Issuer \nFor further process")
            }
        }
    }
}

// Below adapter is used to show the Issuer lists available in issuer parameter table.
class IssuerListAdapter(
    var issuerList: ArrayList<IssuerParameterTable>,
    var selectedListCb: (Int, EnumAddDeleteIssuer) -> Unit
) : RecyclerView.Adapter<IssuerListAdapter.IssuerListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssuerListViewHolder {
        val itemBinding = ItemIssuerListViewholderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IssuerListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: IssuerListViewHolder, position: Int) {
        holder.viewBinding.issuerName.text = issuerList[position].issuerName
        holder.viewBinding.issuerCb.isChecked = issuerList[position].isIssuerSelected
    }

    override fun getItemCount(): Int = issuerList.size

    inner class IssuerListViewHolder(var viewBinding: ItemIssuerListViewholderBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        init {
            viewBinding.issuerLayout.setOnClickListener {
                if (issuerList[adapterPosition].isIssuerSelected) {
                    issuerList[adapterPosition].isIssuerSelected = false
                    selectedListCb(adapterPosition, EnumAddDeleteIssuer.UNSELECTED)
                } else {
                    issuerList[adapterPosition].isIssuerSelected = true
                    selectedListCb(adapterPosition, EnumAddDeleteIssuer.SELECTED)
                }
                notifyDataSetChanged()
            }
        }
    }
}

// Enum for selection of Issuers handling
enum class EnumAddDeleteIssuer(var id: Int) {
    SELECTED(1),
    UNSELECTED(2)
}


