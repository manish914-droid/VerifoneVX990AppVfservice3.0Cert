package com.example.verifonevx990app.transactions

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.verifonevx990app.R
import com.example.verifonevx990app.databinding.FragmentNewInputAmountBinding
import com.example.verifonevx990app.main.IFragmentRequest
import com.example.verifonevx990app.realmtables.EDashboardItem
import com.example.verifonevx990app.realmtables.HdfcCdt
import com.example.verifonevx990app.realmtables.TerminalParameterTable
import com.example.verifonevx990app.utils.KeyboardModel
import com.example.verifonevx990app.vxUtils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class NewInputAmountFragment : Fragment() {
    private val keyModelSaleAmount: KeyboardModel by lazy {
        KeyboardModel()
    }
    private val keyModelCashAmount: KeyboardModel by lazy {
        KeyboardModel()
    }
    var inputInSaleAmount = false
    var inputInCashAmount = false
    private lateinit var transactionType: EDashboardItem
    private var iFrReq: IFragmentRequest? = null
    private var subHeaderText: TextView? = null
    private var subHeaderBackButton: ImageView? = null

    /// private var navController: NavController? = null
    private var cashAmount: EditText? = null
    private var iDialog: IDialog? = null
    private var binding: FragmentNewInputAmountBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewInputAmountBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ///   (activity as NavigationActivity).showBottomNavigationBar(isShow = false)
        val hdfcTPTData = getHDFCTptData()
        //todo change below
        val hdfcCDTData = HdfcCdt() ///getHDFCDtData()
        Log.d("HDFCTPTData:- ", hdfcTPTData.toString())
        Log.d("HDFCCDTData:- ", hdfcCDTData.toString())

        cashAmount = view.findViewById(R.id.cashAmount)
        ///  navController = Navigation.findNavController(view)
        transactionType = arguments?.getSerializable("type") as EDashboardItem
        if (transactionType == EDashboardItem.SALE_WITH_CASH ||
            (checkHDFCTPTFieldsBitOnOff(TransactionType.TIP_SALE) && transactionType == EDashboardItem.SALE)
        ) {
            cashAmount?.visibility = View.VISIBLE
            if (checkHDFCTPTFieldsBitOnOff(TransactionType.TIP_SALE)) {
                binding?.enterCashAmountTv?.text =
                    VerifoneApp.appContext.getString(R.string.enter_tip_amount)
            }
            binding?.enterCashAmountTv?.visibility = View.VISIBLE

        } else {
            cashAmount?.visibility = View.GONE
            binding?.enterCashAmountTv?.visibility = View.GONE
        }

        subHeaderText = view.findViewById(R.id.sub_header_text)
        subHeaderBackButton = view.findViewById(R.id.back_image_button)
        setTxnTypeMsg(transactionType.title)
        subHeaderBackButton?.setOnClickListener {
            ///    navController?.popBackStack()
            //todo
        }
        keyModelSaleAmount.view = binding?.saleAmount
        keyModelSaleAmount.callback = ::onOKClicked
        inputInSaleAmount = true
        inputInCashAmount = false
        binding?.saleAmount?.setBackgroundResource(R.drawable.et_bg_selected)
        if (hdfcTPTData != null) {
            binding?.saleAmount?.filters = arrayOf<InputFilter>(
                InputFilter.LengthFilter(
                    hdfcTPTData.transAmountDigit.toInt()
                )
            )
        }
        binding?.enterSaleAmtTv?.setTextColor(
            ContextCompat.getColor(
                VerifoneApp.appContext,
                R.color.colorPrimary
            )
        )
        binding?.saleAmount?.setOnClickListener {
            keyModelSaleAmount.view = it
            keyModelSaleAmount.callback = ::onOKClicked
            inputInSaleAmount = true
            inputInCashAmount = false
            it.setBackgroundResource(R.drawable.et_bg_selected)
            cashAmount?.setBackgroundResource(R.drawable.et_bg_un)
            binding?.enterSaleAmtTv?.setTextColor(
                ContextCompat.getColor(
                    VerifoneApp.appContext,
                    R.color.colorPrimary
                )
            )
            binding?.enterCashAmountTv?.setTextColor(
                ContextCompat.getColor(
                    VerifoneApp.appContext,
                    R.color.black
                )
            )
        }
        cashAmount?.setOnClickListener {
            keyModelCashAmount.view = it
            keyModelCashAmount.callback = ::onOKClicked
            inputInSaleAmount = false
            inputInCashAmount = true
            it.setBackgroundResource(R.drawable.et_bg_selected)
            binding?.saleAmount?.setBackgroundResource(R.drawable.et_bg_un)
            binding?.enterSaleAmtTv?.setTextColor(
                ContextCompat.getColor(
                    VerifoneApp.appContext,
                    R.color.colorPrimary
                )
            )
            binding?.enterSaleAmtTv?.setTextColor(
                ContextCompat.getColor(
                    VerifoneApp.appContext,
                    R.color.black
                )
            )
        }
        onSetKeyBoardButtonClick()

    }

    //region============================by sac - to set txn type on amount entry screen:-
    private fun setTxnTypeMsg(strText: String): Unit? {
        return subHeaderText?.setText(strText.toUpperCase(Locale.ROOT))
    }
    //endregion

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentRequest) {
            iFrReq = context
        }
        if (context is IDialog) iDialog = context
    }

    override fun onDetach() {
        super.onDetach()
        iFrReq = null
        iDialog = null
    }

    private fun onSetKeyBoardButtonClick() {
        binding?.mainKeyBoard?.key0

        binding?.mainKeyBoard?.key0?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("0")
            } else {
                keyModelCashAmount.onKeyClicked("0")
            }
        }
        binding?.mainKeyBoard?.key00?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("00")
            } else {
                keyModelCashAmount.onKeyClicked("00")
            }
        }
        binding?.mainKeyBoard?.key000?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("000")
            } else {
                keyModelCashAmount.onKeyClicked("000")
            }
        }
        binding?.mainKeyBoard?.key1?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("1")
            } else {
                keyModelCashAmount.onKeyClicked("1")
            }
        }
        binding?.mainKeyBoard?.key2?.setOnClickListener {
            if (inputInSaleAmount) {
                Log.e("SALE", "KEY 2")
                keyModelSaleAmount.onKeyClicked("2")
            } else {
                Log.e("CASH", "KEY 2")
                keyModelCashAmount.onKeyClicked("2")
            }
        }
        binding?.mainKeyBoard?.key3?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("3")
            } else {
                keyModelCashAmount.onKeyClicked("3")
            }
        }
        binding?.mainKeyBoard?.key4?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("4")
            } else {
                keyModelCashAmount.onKeyClicked("4")
            }
        }
        binding?.mainKeyBoard?.key5?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("5")
            } else {
                keyModelCashAmount.onKeyClicked("5")
            }
        }
        binding?.mainKeyBoard?.key6?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("6")
            } else {
                keyModelCashAmount.onKeyClicked("6")
            }
        }
        binding?.mainKeyBoard?.key7?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("7")
            } else {
                keyModelCashAmount.onKeyClicked("7")
            }
        }
        binding?.mainKeyBoard?.key8?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("8")
            } else {
                keyModelCashAmount.onKeyClicked("8")
            }
        }
        binding?.mainKeyBoard?.key9?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("9")
            } else {
                keyModelCashAmount.onKeyClicked("9")
            }
        }
        binding?.mainKeyBoard?.keyClr?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("c")
            } else {
                keyModelCashAmount.onKeyClicked("c")
            }
        }
        binding?.mainKeyBoard?.keyDelete?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("d")
            } else {
                keyModelCashAmount.onKeyClicked("d")
            }
        }
        binding?.mainKeyBoard?.keyOK?.setOnClickListener {
            if (inputInSaleAmount) {
                keyModelSaleAmount.onKeyClicked("o")
            } else {
                keyModelCashAmount.onKeyClicked("o")
            }
        }

    }

    private fun onOKClicked(amt: String) {
        Log.e("SALE", "OK CLICKED  ${binding?.saleAmount?.text.toString()}")
        Log.e("CASh", "OK CLICKED  ${cashAmount?.text}")
        Log.e("AMT", "OK CLICKED  $amt")
        if ((binding?.saleAmount?.text.toString()).toDouble() < 1) {
            VFService.showToast("Sale Amount should be greater than Rs 1")
            return
        } else if (transactionType == EDashboardItem.SALE_WITH_CASH && (cashAmount?.text.toString()).toDouble() < 1) {
            VFService.showToast("Cash Amount should be greater than Rs 1")
            return
        } else {
            when (transactionType) {
                EDashboardItem.SALE -> {
                    val saleAmt = binding?.saleAmount?.text.toString().trim().toFloat()
                    val saleTipAmt = cashAmount?.text.toString().trim().toFloat()
                    val trnsAmt = saleAmt + saleTipAmt
                    if (saleTipAmt > 0) {
                        validateTIP(trnsAmt, saleAmt)
                    } else {
                        iFrReq?.onFragmentRequest(
                            UiAction.START_SALE,
                            Pair(trnsAmt.toString().trim(), cashAmount?.text.toString().trim())
                        )
                    }
                }
                EDashboardItem.CASH_ADVANCE -> {
                    iFrReq?.onFragmentRequest(
                        UiAction.CASH_ADVANCE,
                        Pair(
                            binding?.saleAmount?.text.toString().trim(),
                            binding?.saleAmount?.text.toString().trim()
                        )
                    )
                }
                EDashboardItem.SALE_WITH_CASH -> {
                    iFrReq?.onFragmentRequest(
                        UiAction.SALE_WITH_CASH,
                        Pair(
                            binding?.saleAmount?.text.toString().trim(),
                            cashAmount?.text.toString().trim()
                        )
                    )
                }
                EDashboardItem.REFUND -> {
                    iFrReq?.onFragmentRequest(
                        UiAction.REFUND,
                        Pair(binding?.saleAmount?.text.toString().trim(), "0")
                    )
                }
                EDashboardItem.PREAUTH -> {
                    iFrReq?.onFragmentRequest(
                        UiAction.PRE_AUTH,
                        Pair(binding?.saleAmount?.text.toString().trim(), "0")
                    )
                }
                else -> {

                }
            }
        }
    }

    private fun validateTIP(totalTransAmount: Float, saleAmt: Float) {
        val tpt = TerminalParameterTable.selectFromSchemeTable()
        if (tpt != null) {
            val tipAmount = try {
                cashAmount?.text.toString().toFloat()
            } catch (ex: Exception) {
                0f
            }
            val maxTipPercent =
                if (tpt.maxTipPercent.isEmpty()) 0f else (tpt.maxTipPercent.toFloat()).div(
                    100
                )
            val maxTipLimit =
                if (tpt.maxTipLimit.isEmpty()) 0f else (tpt.maxTipLimit.toFloat()).div(
                    100
                )
            if (maxTipLimit != 0f) { // flat tip check is applied
                if (tipAmount <= maxTipLimit) {
                    // iDialog?.showProgress()
                    GlobalScope.launch {
                        iFrReq?.onFragmentRequest(
                            UiAction.START_SALE,
                            Pair(
                                totalTransAmount.toString().trim(),
                                cashAmount?.text.toString().trim()
                            )
                        )
                    }
                } else {
                    val msg =
                        "Maximum tip allowed on this terminal is \u20B9 ${
                            "%.2f".format(
                                maxTipLimit
                            )
                        }."
                    GlobalScope.launch(Dispatchers.Main) {
                        iDialog?.getInfoDialog("Tip Sale Error", msg) {}
                    }
                }
            } else { // percent tip check is applied
                val saleAmount = saleAmt
                val maxAmountTip = (maxTipPercent / 100) * saleAmount
                val formatMaxTipAmount = "%.2f".format(maxAmountTip)
                if (tipAmount <= maxAmountTip) {
                    //   iDialog?.showProgress()
                    GlobalScope.launch {
                        iFrReq?.onFragmentRequest(
                            UiAction.START_SALE,
                            Pair(
                                totalTransAmount.toString().trim(),
                                cashAmount?.text.toString().trim()
                            )
                        )
                    }
                } else {
                    //    val tipAmt = saleAmt * per / 100
                    val msg = "Tip limit for this transaction is \n \u20B9 ${
                        "%.2f".format(
                            formatMaxTipAmount.toDouble()
                        )
                    }"
                    /* "Maximum ${"%.2f".format(
                         maxTipPercent.toDouble()
                     )}% tip allowed on this terminal.\nTip limit for this transaction is \u20B9 ${"%.2f".format(
                         formatMaxTipAmount.toDouble()
                     )}"*/
                    GlobalScope.launch(Dispatchers.Main) {
                        iDialog?.getInfoDialog("Tip Sale Error", msg) {}
                    }
                }
            }
        } else {
            VFService.showToast("TPT not fount")
        }
    }
}