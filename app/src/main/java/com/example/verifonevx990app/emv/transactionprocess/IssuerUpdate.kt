package com.example.verifonevx990app.emv.transactionprocess

import android.os.*
import com.example.verifonevx990app.vxUtils.*
import com.vfi.smartpos.deviceservice.aidl.IssuerUpdateHandler

class IssuerUpdate(var cardProcessedDataModal: CardProcessedDataModal,var vissuerCallback: (CardProcessedDataModal) -> Unit) : IssuerUpdateHandler.Stub() {

    override fun onRequestIssuerUpdate() {
        vissuerCallback(cardProcessedDataModal)
        VFService.showToast("Request issuer update in callback")
        println("Request issuer update in callback")
    }

}