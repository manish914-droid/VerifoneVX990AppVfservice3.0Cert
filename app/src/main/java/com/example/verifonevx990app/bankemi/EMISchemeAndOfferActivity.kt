package com.example.verifonevx990app.bankemi

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.verifonevx990app.R
import com.example.verifonevx990app.databinding.EmiSchemeOfferViewBinding
import com.example.verifonevx990app.emv.transactionprocess.CardProcessedDataModal
import com.example.verifonevx990app.main.MainActivity
import com.example.verifonevx990app.vxUtils.*
import com.google.android.material.card.MaterialCardView

class EMISchemeAndOfferActivity : BaseActivity() {
    private var binding: EmiSchemeOfferViewBinding? = null
    private var emiSchemeOfferDataList: MutableList<BankEMIDataModal>? = null
    private var emiTAndCDataList: MutableList<BankEMIIssuerTAndCDataModal>? = null
    private var cardProcessedDataModal: CardProcessedDataModal? = null
    private var selectedSchemeUpdatedPosition = -1
    private val emiSchemeAndOfferAdapter: EMISchemeAndOfferAdapter by lazy {
        EMISchemeAndOfferAdapter(
            emiSchemeOfferDataList,
            ::onSchemeClickEvent
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EmiSchemeOfferViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.toolbarTxn?.mainToolbarStart?.setBackgroundResource(R.drawable.ic_back_arrow)
        ROCProviderV2.refreshToolbarLogos(this)
        binding?.toolbarTxn?.mainToolbarStart?.setOnClickListener {
            navigateControlBackToTransaction(
                isTransactionContinue = false
            )
        }
        showProgress()

        //region======================Getting Parcelable Data List of Emi Scheme&Offer , Emi TAndC and CardProcessedData:-
        emiSchemeOfferDataList = intent?.getParcelableArrayListExtra("emiSchemeDataList")
        emiTAndCDataList = intent?.getParcelableArrayListExtra("emiTAndCDataList")
        cardProcessedDataModal =
            intent?.getSerializableExtra("cardProcessedData") as CardProcessedDataModal?
        //endregion

        setUpRecyclerView()

        //region======================Proceed TXN Floating Button OnClick Event:-
        binding?.emiSchemeFloatingButton?.setOnClickListener {
            if (selectedSchemeUpdatedPosition != -1)
                navigateControlBackToTransaction(isTransactionContinue = true)
            else
                VFService.showToast(getString(R.string.please_select_scheme))
        }
        //endregion
    }

    //region==========================onClickEvent==================================================
    private fun onSchemeClickEvent(position: Int) {
        Log.d("Position:- ", emiSchemeOfferDataList?.get(position).toString())
        selectedSchemeUpdatedPosition = position
    }
    //endregion

    override fun onBackPressed() {
        navigateControlBackToTransaction(isTransactionContinue = false)
    }

    //region=========================SetUp RecyclerView Data:-
    private fun setUpRecyclerView() {
        if (emiSchemeOfferDataList != null) {
            binding?.emiSchemeOfferRV?.apply {
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                adapter = emiSchemeAndOfferAdapter
            }
            hideProgress()
        }
        hideProgress()
    }
    //endregion


    //region=========================control back to VFTransactionActivity==========================
    private fun navigateControlBackToTransaction(isTransactionContinue: Boolean) {
        if (isTransactionContinue) {
            val intent = Intent().apply {
                putExtra("cardProcessedData", cardProcessedDataModal)
                putExtra(
                    "emiSchemeDataList",
                    emiSchemeOfferDataList?.get(selectedSchemeUpdatedPosition)
                )
                putExtra("emiTAndCDataList", emiTAndCDataList?.get(0))
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            //Below method to Navigate merchant to MainActivity:-
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }
    //endregion

    override fun onEvents(event: VxEvent) {}
}

internal class EMISchemeAndOfferAdapter(
    private val emiSchemeDataList: MutableList<BankEMIDataModal>?,
    private var schemeSelectCB: (Int) -> Unit
) : RecyclerView.Adapter<EMISchemeAndOfferAdapter.EMISchemeOfferHolder>() {

    private var index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EMISchemeOfferHolder {
        val inflater =
            LayoutInflater.from(p0.context).inflate(R.layout.item_emi_scheme_offer, p0, false)
        return EMISchemeOfferHolder(inflater)
    }

    override fun getItemCount(): Int {
        return emiSchemeDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: EMISchemeOfferHolder, position: Int) {
        val modelData = emiSchemeDataList?.get(position)
        if (modelData != null) {
            holder.transactionAmount.text =
                divideAmountBy100(modelData.transactionAmount.toInt()).toString()
            val tenureDuration = "${modelData.tenure} Months"
            val tenureHeadingDuration = "${modelData.tenure} Months Scheme"
            holder.tenure.text = tenureDuration
            holder.tenureHeadingTV.text = tenureHeadingDuration
            holder.loanAmount.text = divideAmountBy100(modelData.loanAmount.toInt()).toString()
            holder.emiAmount.text = divideAmountBy100(modelData.emiAmount.toInt()).toString()

            //If Discount Amount Available show this else if CashBack Amount show that:-
            if (modelData.discountAmount.toInt() != 0) {
                holder.discountAmount.text =
                    divideAmountBy100(modelData.discountAmount.toInt()).toString()
                holder.discountLinearLayout.visibility = View.VISIBLE
                holder.cashBackLinearLayout.visibility = View.GONE
            }
            if (modelData.cashBackAmount.toInt() != 0) {
                holder.cashBackAmount.text =
                    divideAmountBy100(modelData.cashBackAmount.toInt()).toString()
                holder.cashBackLinearLayout.visibility = View.VISIBLE
                holder.discountLinearLayout.visibility = View.GONE
            }
            holder.totalInterestPay.text =
                divideAmountBy100(modelData.tenureInterestRate.toInt()).toString()
            holder.totalEmiPay.text = divideAmountBy100(modelData.totalEmiPay.toInt()).toString()
        }

        holder.parentEmiLayout.setOnClickListener {
            index = position
            notifyDataSetChanged()
        }

        //region==========================Checked Particular Row of RecyclerView Logic:-
        if (index === position) {
            holder.cardView.strokeColor = Color.parseColor("#13E113")
            holder.schemeCheckIV.visibility = View.VISIBLE
            schemeSelectCB(position)
        } else {
            holder.cardView.strokeColor = Color.parseColor("#FFFFFF")
            holder.schemeCheckIV.visibility = View.GONE
        }
        //endregion
    }


    inner class EMISchemeOfferHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val transactionAmount = view.findViewById<TextView>(R.id.tv_transaction_amount)
        val tenure = view.findViewById<TextView>(R.id.tv_tenure)
        val emiAmount = view.findViewById<TextView>(R.id.tv_emi_amount)
        val loanAmount = view.findViewById<TextView>(R.id.tv_loan_amount)
        val discountAmount = view.findViewById<TextView>(R.id.tv_discount_amount)
        val cashBackAmount = view.findViewById<TextView>(R.id.tv_cashback_amount)
        val totalInterestPay = view.findViewById<TextView>(R.id.tv_total_interest_pay)
        val totalEmiPay = view.findViewById<TextView>(R.id.tv_total_emi_pay)
        val tenureHeadingTV = view.findViewById<TextView>(R.id.tenure_heading_tv)
        val parentEmiLayout = view.findViewById<LinearLayout>(R.id.parent_emi_view_ll)
        val discountLinearLayout = view.findViewById<LinearLayout>(R.id.discountLL)
        val cashBackLinearLayout = view.findViewById<LinearLayout>(R.id.cashBackLL)
        val cardView = view.findViewById<MaterialCardView>(R.id.cardView)
        val schemeCheckIV = view.findViewById<ImageView>(R.id.scheme_check_iv)
    }
}

